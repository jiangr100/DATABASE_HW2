package technify;

import technify.business.*;

import technify.data.DBConnector;
import technify.data.PostgreSQLErrorCodes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Solution {

    private static final String TABLE_USER = "technifyuser";
    private static final String TABLE_SONG = "song";
    private static final String TABLE_PLAYLIST = "playlist";

    private static final String TABLE_REL_FOLLOWS = "follows";
    private static final String TABLE_REL_IN_PLAYLIST = "inplaylist";

    private static PreparedStatement prepareStatement(Connection c, String sql) {
        PreparedStatement pstmt;
        try {
            pstmt = c.prepareStatement(sql);
            return pstmt;
        } catch (SQLException e) {
            return null;
        }
    }

    private static void closeConnection(Connection c) {   //todo we should return error if there's an exception
        try {
            c.close();
        } catch (SQLException e) {
            //
        }
    }

    private static void finish(PreparedStatement s) { //todo error
        if(s == null) return;
        try {
            s.close();
        } catch (SQLException e) {
            //
        }
    }

    private static PostgreSQLErrorCodes errorCode(SQLException ex) {
        int code = Integer.valueOf(ex.getSQLState());
        for(PostgreSQLErrorCodes c : PostgreSQLErrorCodes.values()) {
            if(c.getValue() == code) return c;
        }
        return null;
    }

    /**
     * Create all the tables needed for the database
     * This functions creates the following tables:
     * - user (User Entity)
     * - song (Song Entity)
     * - playlist (Playlist Entity)
     * - follow (Relation, User - Playlist)
     * - inPlaylist (Relation, Song - Playlist)
     *
     * And views:
     * - CountPlaylistSongs(count)
     * - SumPlaylistPlayCount(sum, playlistId)
     * - SimilarUsers(id1, id2)
     * - PlaylistFollowers(playlistId, count)
     * - CountriesInPlaylist(pid, country)
     */
    public static void createTables() {
        Connection connection = DBConnector.getConnection();
        // Create User table
        PreparedStatement userCreateStatement = prepareStatement(connection,"CREATE TABLE " + TABLE_USER + "\n" +
                "(\n" +
                "    id         INTEGER,\n" +
                "    username   TEXT NOT NULL,\n" +
                "    country    TEXT NOT NULL,\n" +
                "    premium    BOOLEAN NOT NULL,\n" +
                "               PRIMARY KEY (id),\n" +
                "               CHECK (id > 0)\n" +
                ")");

        // Create Song table
        PreparedStatement songCreateStatement = prepareStatement(connection,"CREATE TABLE " + TABLE_SONG + "\n" +
                "(\n" +
                "    id         INTEGER,\n" +
                "    name       TEXT NOT NULL,\n" +
                "    genre      TEXT NOT NULL,\n" +
                "    country    TEXT,\n" +
                "    playCount  INTEGER DEFAULT(0),\n" +
                "               PRIMARY KEY (id),\n" +
                "               CHECK (id > 0),\n" +
                "               CHECK (playCount >= 0)\n" +
                ")");

        // Create Playlist table
        PreparedStatement playlistCreateStatement = prepareStatement(connection,"CREATE TABLE " + TABLE_PLAYLIST + "\n" +
                "(\n" +
                "    id             INTEGER,\n" +
                "    genre          TEXT NOT NULL,\n" +
                "    description    TEXT NOT NULL,\n" +
                "                   PRIMARY KEY (id),\n" +
                "                   CHECK (id > 0)\n" +
                ")");

        PreparedStatement followsCreateStatement = prepareStatement(connection, "CREATE TABLE " + TABLE_REL_FOLLOWS +"\n" +
                "(\n" +
                "   userId INTEGER,\n" +
                "   playlistId INTEGER,\n" +
                "   UNIQUE(userId, playlistId),\n" +
                "   FOREIGN KEY(userId) REFERENCES " + TABLE_USER + "(id) ON DELETE CASCADE,\n" +
                "   FOREIGN KEY(playlistId) REFERENCES " + TABLE_PLAYLIST + "(id) ON DELETE CASCADE\n" +
                ")");

        PreparedStatement inPlaylistCreateStatement = prepareStatement(connection, "CREATE TABLE " + TABLE_REL_IN_PLAYLIST +"\n" +
                "(\n" +
                "   playlistId INTEGER,\n" +
                "   songId INTEGER,\n" +
                "   UNIQUE(playlistId, songId),\n" +
                "   FOREIGN KEY(playlistId) REFERENCES " + TABLE_PLAYLIST + "(id) ON DELETE CASCADE,\n" +
                "   FOREIGN KEY(songId) REFERENCES " + TABLE_SONG + "(id) ON DELETE CASCADE\n" +
                ")");

        PreparedStatement countPlaylistSongsView = prepareStatement(connection,
                "CREATE VIEW CountPlaylistSongs AS (SELECT COUNT(playlistId) AS count FROM " + TABLE_REL_IN_PLAYLIST + " GROUP BY songId)");

        PreparedStatement sumPlaylistPlayCountView = prepareStatement(connection,
                "CREATE VIEW SumPlaylistPlayCount AS (" +
                            "SELECT SUM(S.playCount) AS sum, I.playlistId FROM " + TABLE_REL_IN_PLAYLIST + " I, " + TABLE_SONG + " S\n" +
                            "WHERE I.songId = S.id\n" +
                            "GROUP BY I.playlistId\n" +
                        ")");

        PreparedStatement similarUsersView = prepareStatement(connection,
                "CREATE VIEW SimilarUsers AS (\n" +
                        "SELECT U1.id AS id1, U2.id AS id2\n" +
                        "FROM " + TABLE_USER + " U1, " + TABLE_USER + " U2, " + TABLE_REL_FOLLOWS + " Follows1, " + TABLE_REL_FOLLOWS + " Follows2\n" +
                        "WHERE U1.id != U2.id AND Follows1.playlistId = Follows2.playlistId AND Follows1.userId = u1.id AND Follows2.userId = u2.id\n" +
                        "GROUP BY (U1.id, U2.id) " +
                        "HAVING 4 * COUNT(U1.id) >= 3 * (SELECT COUNT(*) FROM " + TABLE_REL_FOLLOWS +" WHERE userId = u2.id)" +
                        ")"
                );

        PreparedStatement countPlaylistFollowers = prepareStatement(connection,
                "CREATE VIEW PlaylistFollowers AS (\n" +
                        "SELECT playlistId, COUNT(userId) FROM " + TABLE_REL_FOLLOWS + "\n" +
                        "GROUP BY playlistId\n" +
                        ")"
                );

        PreparedStatement countriesInPlaylist = prepareStatement(connection,
                "CREATE VIEW CountriesInPlaylist AS (\n" +
                        "SELECT DISTINCT P.playlistId AS pid, S.country AS country FROM " + TABLE_SONG + " S, " + TABLE_REL_IN_PLAYLIST + " P \n" +
                        "WHERE S.id IN (" + "SELECT songId FROM " + TABLE_REL_IN_PLAYLIST + " WHERE playlistId = P.playlistId)\n" +
                        ")"
        );

        PreparedStatement songsOfUserFollows = prepareStatement(connection,
                "CREATE VIEW SongsOfUsers AS (\n" +
                        "SELECT U.id AS uid, S.id AS sid FROM " + TABLE_SONG + " S, " + TABLE_USER + " U, " +
                            TABLE_REL_IN_PLAYLIST + " IP, " + TABLE_REL_FOLLOWS + " F "+
                        "WHERE U.id = F.userId " +
                            "AND S.id = IP.songId " +
                            "AND F.playlistId = IP.playlistId " +
                        ")"
        );

        try {
            userCreateStatement.execute();
            songCreateStatement.execute();
            playlistCreateStatement.execute();
            followsCreateStatement.execute();
            inPlaylistCreateStatement.execute();

            // Create Views
            countPlaylistSongsView.execute();
            sumPlaylistPlayCountView.execute();
            similarUsersView.execute();
            countPlaylistFollowers.execute();
            countriesInPlaylist.execute();
            songsOfUserFollows.execute();

        } catch (SQLException ex) {
            ex.printStackTrace(); // TODO Remove
            // Handle Exception
        } finally {
            finish(userCreateStatement);
            finish(songCreateStatement);
            finish(playlistCreateStatement);
            finish(followsCreateStatement);
            finish(inPlaylistCreateStatement);
            closeConnection(connection);
        }
    }

    /**
     * Clears all the database tables (removes all entries, leaving an empty table)
     */
    public static void clearTables() {
        Connection connection = DBConnector.getConnection();
        // Create User table
        PreparedStatement userClearStatement = prepareStatement(connection,"DELETE FROM " + TABLE_USER);
        PreparedStatement songClearStatement = prepareStatement(connection,"DELETE FROM " + TABLE_SONG);
        PreparedStatement playlistClearStatement = prepareStatement(connection,"DELETE FROM " + TABLE_PLAYLIST);
        PreparedStatement followsClearStatement = prepareStatement(connection,"DELETE FROM " + TABLE_REL_FOLLOWS);
        PreparedStatement inPlaylistClearStatement = prepareStatement(connection,"DELETE FROM " + TABLE_REL_IN_PLAYLIST);

        try {
            userClearStatement.execute();
            songClearStatement.execute();
            playlistClearStatement.execute();
            followsClearStatement.execute();
            inPlaylistClearStatement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();

            finish(userClearStatement);
            finish(songClearStatement);
            finish(playlistClearStatement);
            finish(followsClearStatement);
            finish(inPlaylistClearStatement);
            closeConnection(connection);
        }
    }

    /**
     * Drops all the tables in the database
     */
    public static void dropTables() {
        Connection connection = DBConnector.getConnection();
        // Create User table
        PreparedStatement userDropStatement = prepareStatement(connection,"DROP TABLE " + TABLE_USER);
        PreparedStatement songDropStatement = prepareStatement(connection,"DROP TABLE " + TABLE_SONG);
        PreparedStatement playlistDropStatement = prepareStatement(connection,"DROP TABLE " + TABLE_PLAYLIST);
        PreparedStatement followsDropStatement = prepareStatement(connection,"DROP TABLE " + TABLE_REL_FOLLOWS);
        PreparedStatement inPlaylistDropStatement = prepareStatement(connection,"DROP TABLE " + TABLE_REL_IN_PLAYLIST);

        PreparedStatement countPlaylistSongsDropView = prepareStatement(connection, "DROP VIEW CountPlaylistSongs");
        PreparedStatement sumPlaylistsPlayCountDropView = prepareStatement(connection, "DROP VIEW SumPlaylistPlayCount");
        PreparedStatement similarUsersDropView = prepareStatement(connection, "DROP VIEW SimilarUsers");
        PreparedStatement playlistFollowersDropView = prepareStatement(connection, "DROP VIEW PlaylistFollowers");
        PreparedStatement countriesInPlaylistDropView = prepareStatement(connection, "DROP VIEW CountriesInPlaylist");
        PreparedStatement songsOfUsersDropView = prepareStatement(connection, "DROP VIEW SongsOfUsers");

        try {
            // Drop Views first, because other tables depend on them
            countPlaylistSongsDropView.execute();
            sumPlaylistsPlayCountDropView.execute();
            similarUsersDropView.execute();
            playlistFollowersDropView.execute();
            countriesInPlaylistDropView.execute();
            songsOfUsersDropView.execute();

            followsDropStatement.execute();
            inPlaylistDropStatement.execute();
            userDropStatement.execute();
            songDropStatement.execute();
            playlistDropStatement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            finish(userDropStatement);
            finish(songDropStatement);
            finish(playlistDropStatement);
            finish(followsDropStatement);
            finish(inPlaylistDropStatement);
            closeConnection(connection);
        }
    }

    public static ReturnValue addUser(User user) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement statement = prepareStatement(connection,
                "INSERT INTO " + TABLE_USER + " VALUES(?, ?, ?, ?)");
        try {
            statement.setInt(1, user.getId());
            statement.setString(2, user.getName());
            statement.setString(3, user.getCountry());
            statement.setBoolean(4, user.getPremium());
            return statement.executeUpdate() == 1 ? ReturnValue.OK : ReturnValue.ALREADY_EXISTS;
        } catch (SQLException ex) {
            switch (errorCode(ex)) {
                case UNIQUE_VIOLATION:
                    return ReturnValue.ALREADY_EXISTS;
                case NOT_NULL_VIOLATION:
                case CHECK_VIOLATION:
                    return ReturnValue.BAD_PARAMS;
            }
            return ReturnValue.ERROR;
        } finally {
            finish(statement);
            closeConnection(connection);
        }
    }

    public static User getUserProfile(Integer userId) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement statement = prepareStatement(connection,
                "SELECT * FROM " + TABLE_USER + " WHERE id = ?");
        User user = User.badUser();
        try {
            statement.setInt(1, userId);
            ResultSet set = statement.executeQuery();
            if(set.next()) {
                User newUser = new User();
                newUser.setId(set.getInt(1));
                newUser.setName(set.getString(2));
                newUser.setCountry(set.getString(3));
                newUser.setPremium(set.getBoolean(4));
                user = newUser;
            }
        } catch (SQLException ex) {

        } finally {
            finish(statement);
            closeConnection(connection);
        }
        return user;
    }

    public static ReturnValue deleteUser(User user) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement statement = prepareStatement(connection,
                "DELETE FROM " + TABLE_USER + " WHERE id = ?");
        try {
            statement.setInt(1, user.getId());
            return statement.executeUpdate() == 1 ? ReturnValue.OK : ReturnValue.NOT_EXISTS;
        } catch (SQLException ex) {
            return ReturnValue.ERROR;
        } finally {
            finish(statement);
            closeConnection(connection);
        }
    }

    private static ReturnValue updateUserStatus(Integer userId, boolean premium) {
        if(getUserProfile(userId).getPremium() == premium) {
            return ReturnValue.ALREADY_EXISTS;
        }
        Connection connection = DBConnector.getConnection();
        PreparedStatement statement = prepareStatement(connection,
                "UPDATE " + TABLE_USER + " SET premium = ? WHERE id = ?");
        try {
            statement.setBoolean(1, premium);
            statement.setInt(2, userId);
            return statement.executeUpdate() == 1 ? ReturnValue.OK: ReturnValue.NOT_EXISTS;
        } catch (SQLException ex) {
            switch (errorCode(ex)) {
                case UNIQUE_VIOLATION:
                    return ReturnValue.NOT_EXISTS;
            }
            return ReturnValue.ERROR;
        } finally {
            finish(statement);
            closeConnection(connection);
        }
    }
    public static ReturnValue updateUserPremium(Integer userId) {
        return updateUserStatus(userId, true);
    }

    public static ReturnValue updateUserNotPremium(Integer userId) {
        return updateUserStatus(userId, false);
    }

    public static ReturnValue addSong(Song song) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement statement = prepareStatement(connection,
                "INSERT INTO " + TABLE_SONG + " VALUES(?, ?, ?, ?)");
        try {
            statement.setInt(1, song.getId());
            statement.setString(2, song.getName());
            statement.setString(3, song.getGenre());
            statement.setString(4, song.getCountry());
            return statement.executeUpdate() == 1 ? ReturnValue.OK : ReturnValue.ALREADY_EXISTS;
        } catch (SQLException ex) {
            switch (errorCode(ex)) {
                case UNIQUE_VIOLATION:
                    return ReturnValue.ALREADY_EXISTS;
                case NOT_NULL_VIOLATION:
                case CHECK_VIOLATION:
                    return ReturnValue.BAD_PARAMS;
            }
            return ReturnValue.ERROR;
        } finally {
            finish(statement);
            closeConnection(connection);
        }
    }

    public static Song getSong(Integer songId) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement statement = prepareStatement(connection,
                "SELECT * FROM " + TABLE_SONG + " WHERE id = ?");
        Song song = Song.badSong();
        try {
            statement.setInt(1, songId);
            ResultSet set = statement.executeQuery();
            if(set.next()) {
                Song newSong = new Song();
                newSong.setId(set.getInt(1));
                newSong.setName(set.getString(2));
                newSong.setGenre(set.getString(3));
                newSong.setCountry(set.getString(4));
                newSong.setPlayCount(set.getInt(5));
                song = newSong;
            }
        } catch (SQLException ex) {

        } finally {
            finish(statement);
            closeConnection(connection);
        }
        return song;
    }

    public static ReturnValue deleteSong(Song song) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement statement = prepareStatement(connection,
                "DELETE FROM " + TABLE_SONG + " WHERE id = ?");
        try {
            statement.setInt(1, song.getId());
            return statement.executeUpdate() == 1 ? ReturnValue.OK : ReturnValue.NOT_EXISTS;
        } catch (SQLException ex) {
            return ReturnValue.ERROR;
        } finally {
            finish(statement);
            closeConnection(connection);
        }
    }

    public static ReturnValue updateSongName(Song song) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement statement = prepareStatement(connection,
                "UPDATE " + TABLE_SONG + " SET name = ? WHERE id = ?");
        try {
            statement.setString(1, song.getName());
            statement.setInt(2, song.getId());
            return statement.executeUpdate() == 1 ? ReturnValue.OK: ReturnValue.NOT_EXISTS;
        } catch (SQLException ex) {
            switch (errorCode(ex)) {
                case NOT_NULL_VIOLATION:
                case CHECK_VIOLATION:
                    return ReturnValue.BAD_PARAMS;
            }
            return ReturnValue.ERROR;
        } finally {
            finish(statement);
            closeConnection(connection);
        }
    }

    public static ReturnValue addPlaylist(Playlist playlist) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement statement = prepareStatement(connection,
                "INSERT INTO " + TABLE_PLAYLIST + " VALUES(?, ?, ?)");
        try {
            statement.setInt(1, playlist.getId());
            statement.setString(2, playlist.getGenre());
            statement.setString(3, playlist.getDescription());
            statement.execute();
            return ReturnValue.OK;
        } catch (SQLException ex) {
            switch (errorCode(ex)) {
                case UNIQUE_VIOLATION:
                    return ReturnValue.ALREADY_EXISTS;
                case NOT_NULL_VIOLATION:
                case CHECK_VIOLATION:
                    return ReturnValue.BAD_PARAMS;
            }
            return ReturnValue.ERROR;
        } finally {
            finish(statement);
            closeConnection(connection);
        }
    }

    public static Playlist getPlaylist(Integer playlistId) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement statement = prepareStatement(connection,
                "SELECT * FROM " + TABLE_PLAYLIST + " WHERE id = ?");
        Playlist playlist = Playlist.badPlaylist();
        try {
            statement.setInt(1, playlistId);
            ResultSet set = statement.executeQuery();
            if(set.next()) {
                Playlist newPlaylist = new Playlist();
                newPlaylist.setId(set.getInt(1));
                newPlaylist.setGenre(set.getString(2));
                newPlaylist.setDescription(set.getString(3));
                playlist = newPlaylist;
            }
        } catch (SQLException ex) {

        } finally {
            finish(statement);
            closeConnection(connection);
        }
        return playlist;
    }

    public static ReturnValue deletePlaylist(Playlist playlist) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement statement = prepareStatement(connection,
                "DELETE FROM " + TABLE_PLAYLIST + " WHERE id = ?");
        try {
            statement.setInt(1, playlist.getId());
            return statement.executeUpdate() == 1 ? ReturnValue.OK : ReturnValue.NOT_EXISTS;
        } catch (SQLException ex) {
            return ReturnValue.ERROR;
        } finally {
            finish(statement);
            closeConnection(connection);
        }
    }

    public static ReturnValue updatePlaylist(Playlist playlist) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement statement = prepareStatement(connection,
                "UPDATE " + TABLE_PLAYLIST + " SET description = ? WHERE id = ?");
        try {
            statement.setString(1, playlist.getDescription());
            statement.setInt(2, playlist.getId());
            return statement.executeUpdate() == 1 ? ReturnValue.OK: ReturnValue.NOT_EXISTS;
        } catch (SQLException ex) {
            switch (errorCode(ex)) {
                case UNIQUE_VIOLATION:
                    return ReturnValue.NOT_EXISTS;
                case NOT_NULL_VIOLATION:
                case CHECK_VIOLATION:
                    return ReturnValue.BAD_PARAMS;
            }
            return ReturnValue.ERROR;
        } finally {
            finish(statement);
            closeConnection(connection);
        }
    }

    public static ReturnValue songPlay(Integer songId, Integer times){
        Connection connection = DBConnector.getConnection();
        PreparedStatement statement = prepareStatement(connection,
                "UPDATE " + TABLE_SONG + " SET playCount = playCount + ? WHERE id = ?");
        try {
            statement.setInt(1, times);
            statement.setInt(2, songId);
            return statement.executeUpdate() == 1 ? ReturnValue.OK: ReturnValue.NOT_EXISTS;
        } catch (SQLException ex) {
            switch (errorCode(ex)) {
                case CHECK_VIOLATION: return ReturnValue.BAD_PARAMS;
                case UNIQUE_VIOLATION: return ReturnValue.NOT_EXISTS;
                default: return ReturnValue.ERROR;
            }
        } finally {
            finish(statement);
            closeConnection(connection);
        }
    }

    public static ReturnValue addSongToPlaylist(Integer songid, Integer playlistId){
        if(getSong(songid).getGenre() != null && getPlaylist(playlistId).getGenre() != null && !getSong(songid).getGenre().equals(getPlaylist(playlistId).getGenre())) {
            return ReturnValue.BAD_PARAMS;
        }
        Connection connection = DBConnector.getConnection();
        PreparedStatement statement = prepareStatement(connection, "INSERT INTO " + TABLE_REL_IN_PLAYLIST + "\n" +
                "(\n" +
                    "SELECT P.id, S.id FROM\n" +
                    TABLE_SONG + " S, " + TABLE_PLAYLIST + " P\n" +
                    "WHERE P.id = ? AND S.id = ? AND P.genre = S.genre\n" +
                ")"
        );

        try {
            statement.setInt(1, playlistId);
            statement.setInt(2, songid);
            return statement.executeUpdate() == 1 ? ReturnValue.OK : ReturnValue.NOT_EXISTS;
        } catch (SQLException ex) {
            switch (errorCode(ex)) {
                case FOREIGN_KEY_VIOLATION:
                case UNIQUE_VIOLATION:
                    return ReturnValue.ALREADY_EXISTS;
            }
            return ReturnValue.ERROR;
        } finally {
            finish(statement);
            closeConnection(connection);
        }
    }

    public static ReturnValue removeSongFromPlaylist(Integer songid, Integer playlistId){
        Connection connection = DBConnector.getConnection();
        PreparedStatement statement = prepareStatement(connection,
                "DELETE FROM " + TABLE_REL_IN_PLAYLIST + " WHERE playlistId = ? AND songId = ?"
        );

        try {
            statement.setInt(1, playlistId);
            statement.setInt(2, songid);
            return statement.executeUpdate() == 1 ? ReturnValue.OK : ReturnValue.NOT_EXISTS;
        } catch (SQLException ex) {
            return ReturnValue.ERROR;
        } finally {
            finish(statement);
            closeConnection(connection);
        }
    }

    public static ReturnValue followPlaylist(Integer userId, Integer playlistId){
        Connection connection = DBConnector.getConnection();
        PreparedStatement statement = prepareStatement(connection, "INSERT INTO " + TABLE_REL_FOLLOWS + "\n" +
                "(\n" +
                    "SELECT U.id, P.id FROM\n" +
                    TABLE_USER + " U, " + TABLE_PLAYLIST + " P\n" +
                    "WHERE U.id = ? AND P.id = ?\n" +
                ")"
        );

        try {
            statement.setInt(1, userId);
            statement.setInt(2, playlistId);
            return statement.executeUpdate() == 1 ? ReturnValue.OK : ReturnValue.NOT_EXISTS;
        } catch (SQLException ex) {
            switch (errorCode(ex)) {
                case FOREIGN_KEY_VIOLATION:
                case UNIQUE_VIOLATION:
                    return ReturnValue.ALREADY_EXISTS;
            }
            return ReturnValue.ERROR;
        } finally {
            finish(statement);
            closeConnection(connection);
        }
    }

    public static ReturnValue stopFollowPlaylist(Integer userId, Integer playlistId){
        Connection connection = DBConnector.getConnection();
        PreparedStatement statement = prepareStatement(connection,
                "DELETE FROM " + TABLE_REL_FOLLOWS + " WHERE userId = ? AND playlistId = ?"
        );

        try {
            statement.setInt(1, userId);
            statement.setInt(2, playlistId);
            return statement.executeUpdate() == 1 ? ReturnValue.OK : ReturnValue.NOT_EXISTS;
        } catch (SQLException ex) {
            return ReturnValue.ERROR;
        } finally {
            finish(statement);
            closeConnection(connection);
        }
    }

    public static Integer getPlaylistTotalPlayCount(Integer playlistId){
        Connection connection = DBConnector.getConnection();
        PreparedStatement statement = prepareStatement(connection,
                "SELECT * FROM SumPlaylistPlayCount WHERE playlistId = ?");
        try {
            statement.setInt(1, playlistId);
            ResultSet results = statement.executeQuery();
            return results.next() ? results.getInt(1) : 0;
        } catch (SQLException ex) {
            return 0;
        } finally {
            finish(statement);
            closeConnection(connection);
        }
    }

    public static Integer getPlaylistFollowersCount(Integer playlistId){
        Connection connection = DBConnector.getConnection();
        PreparedStatement statement = prepareStatement(connection,
                "SELECT COUNT(userId) FROM " + TABLE_REL_FOLLOWS + " WHERE playlistId = ?");
        try {
            statement.setInt(1, playlistId);
            ResultSet results = statement.executeQuery();
            return results.next() ? results.getInt(1) : 0;
        } catch (SQLException ex) {
            return 0;
        } finally {
            finish(statement);
            closeConnection(connection);
        }
    }

    public static String getMostPopularSong() {
        Connection connection = DBConnector.getConnection();
        PreparedStatement statement = prepareStatement(connection,
                "SELECT name FROM " + TABLE_SONG + " WHERE id = (\n" +
                        "SELECT MAX(songId) FROM (\n" +
                            "SELECT songId FROM " + TABLE_REL_IN_PLAYLIST + "\n" +
                            "GROUP BY songId\n" +
                            "HAVING COUNT(playlistId) = (SELECT MAX(count) FROM CountPlaylistSongs)\n" +
                        ") AS SongPlaylistCount\n" +
                    ")");
        try {
            ResultSet results = statement.executeQuery();
            return results.next() ? results.getString(1) : "No songs";
        } catch (SQLException ex) {
            return null;
        } finally {
            finish(statement);
            closeConnection(connection);
        }
    }

    public static Integer getMostPopularPlaylist(){
        Connection connection = DBConnector.getConnection();
        PreparedStatement statement = prepareStatement(connection,

                "SELECT MAX(IP.playlistId), IP.playlistId FROM " + TABLE_SONG + " S, " + TABLE_REL_IN_PLAYLIST + " IP\n" +
                        "WHERE S.id = IP.songId\n" +
                        "GROUP BY IP.playlistId\n" +
                        "HAVING SUM(S.playCount) = (SELECT MAX(sum) FROM SumPlaylistPlayCount)\n" +
                        "ORDER BY IP.playlistId DESC");
        try {
            ResultSet results = statement.executeQuery();
            return results.next() ? results.getInt(1) : 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        } finally {
            finish(statement);
            closeConnection(connection);
        }
    }

    public static ArrayList<Integer> hottestPlaylistsOnTechnify(){
        Connection connection = DBConnector.getConnection();
        PreparedStatement statement = prepareStatement(connection,
                "SELECT I.playlistId, (S.sum / COUNT(I.songId)) AS rating FROM SumPlaylistPlayCount S, " + TABLE_REL_IN_PLAYLIST + " I\n" +
                        "WHERE S.playlistId = I.playlistId\n" +
                        "GROUP BY I.playlistId, S.sum\n" +
                        "ORDER BY rating DESC, I.playlistId ASC LIMIT 10");

        try {
            ResultSet results = statement.executeQuery();
            ArrayList<Integer> resultList = new ArrayList<>();
            while (results.next()) resultList.add(results.getInt(1));
            return resultList;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        } finally {
            finish(statement);
            closeConnection(connection);
        }
    }

    public static ArrayList<Integer> getSimilarUsers(Integer userId){
        Connection connection = DBConnector.getConnection();
        PreparedStatement statement = prepareStatement(connection,
                "SELECT id2 FROM SimilarUsers WHERE id1 = ? ORDER BY id2 ASC");
        try {
            statement.setInt(1, userId);
            ResultSet results = statement.executeQuery();
            ArrayList<Integer> resultList = new ArrayList<>();
            while (results.next()) resultList.add(results.getInt(1));
            return resultList;
        } catch (SQLException ex) {
            return null;
        } finally {
            finish(statement);
            closeConnection(connection);
        }
    }

    public static ArrayList<Integer> getPlaylistRecommendation(Integer userId) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement statement = prepareStatement(connection,
                "SELECT DISTINCT Follows.playlistId, PF.count FROM " + TABLE_USER + " U, " + TABLE_REL_FOLLOWS + " Follows, SimilarUsers SU, PlaylistFollowers PF\n" +
                        "WHERE SU.id1 = ? AND SU.id2 = Follows.userId\n" +
                            "AND SU.id1 != Follows.userId\n" +
                            "AND Follows.playlistId NOT IN (SELECT playlistId FROM " + TABLE_REL_FOLLOWS + " WHERE userId = ?)\n" +
                            "AND PF.playlistId = Follows.playlistId\n" +
                        "ORDER BY PF.count DESC, Follows.playlistId ASC LIMIT 5\n");
        try {
            statement.setInt(1, userId);
            statement.setInt(2, userId);
            ResultSet results = statement.executeQuery();
            ArrayList<Integer> resultList = new ArrayList<>();
            while (results.next()) resultList.add(results.getInt(1));
            return resultList;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        } finally {
            finish(statement);
            closeConnection(connection);
        }
    }

    // TODO Test
    public static ArrayList<Integer> getTopCountryPlaylists(Integer userId) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement statement = prepareStatement(connection,
                "SELECT P.id FROM " + TABLE_PLAYLIST + " P, SumPlaylistPlayCount PC, " + TABLE_USER + " U\n" +
                        "WHERE PC.playlistId = P.id " +
                            "AND U.country IN (SELECT country FROM CountriesInPlaylist WHERE pid = P.id) " +
                            "AND U.id = ? AND U.premium = true " +
                        "ORDER BY PC.sum DESC LIMIT 10");
        try {
            statement.setInt(1, userId);
            ResultSet results = statement.executeQuery();
            ArrayList<Integer> resultList = new ArrayList<>();
            while (results.next()) resultList.add(results.getInt(1));
            return resultList;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        } finally {
            finish(statement);
            closeConnection(connection);
        }
    }

    public static ArrayList<Integer> getSongsRecommendationByGenre(Integer userId, String genre) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement statement = prepareStatement(connection,
                "SELECT S.id FROM " + TABLE_SONG + " S, " + TABLE_USER + " U\n" +
                        "WHERE S.genre = ? " +
                            "AND U.id = ? " +
                            "AND S.id NOT IN (SELECT sid FROM SongsOfUsers WHERE uid = U.id) \n" +
                        "ORDER BY S.playCount DESC LIMIT 10");
        try {
            statement.setString(1, genre);
            statement.setInt(2, userId);
            ResultSet results = statement.executeQuery();
            ArrayList<Integer> resultList = new ArrayList<>();
            while (results.next()) resultList.add(results.getInt(1));
            return resultList;
        } catch (SQLException ex) {
            return new ArrayList<>();
        } finally {
            finish(statement);
            closeConnection(connection);
        }
    }


}

