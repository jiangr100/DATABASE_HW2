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


    private static PreparedStatement prepareStatement(Connection c, String sql) { //todo error to be returned
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

        try {
            userCreateStatement.execute();
            songCreateStatement.execute();
            playlistCreateStatement.execute();
        } catch (SQLException ex) {
            finish(userCreateStatement);
            finish(songCreateStatement);
            finish(playlistCreateStatement);
            closeConnection(connection);
        }
    }

    public static void clearTables() {
        Connection connection = DBConnector.getConnection();
        // Create User table
        PreparedStatement userClearStatement = prepareStatement(connection,"DELETE FROM " + TABLE_USER);
        PreparedStatement songClearStatement = prepareStatement(connection,"DELETE FROM " + TABLE_SONG);
        PreparedStatement playlistClearStatement = prepareStatement(connection,"DELETE FROM " + TABLE_PLAYLIST);

        try {
            userClearStatement.execute();
            songClearStatement.execute();
            playlistClearStatement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();

            finish(userClearStatement);
            finish(songClearStatement);
            finish(playlistClearStatement);
            closeConnection(connection);
        }
    }

    public static void dropTables() {
        Connection connection = DBConnector.getConnection();
        // Create User table
        PreparedStatement userDropStatement = prepareStatement(connection,"DROP TABLE " + TABLE_USER);
        PreparedStatement songDropStatement = prepareStatement(connection,"DROP TABLE " + TABLE_SONG);
        PreparedStatement playlistDropStatement = prepareStatement(connection,"DROP TABLE " + TABLE_PLAYLIST);

        try {
            userDropStatement.execute();
            songDropStatement.execute();
            playlistDropStatement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            finish(userDropStatement);
            finish(songDropStatement);
            finish(playlistDropStatement);
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

    public static ReturnValue updateSongName(Song song)
    {
        Connection connection = DBConnector.getConnection();
        PreparedStatement statement = prepareStatement(connection,
                "UPDATE " + TABLE_SONG + " SET name = ? WHERE id = ?");
        try {
            statement.setString(1, song.getName());
            statement.setInt(2, song.getId());
            return statement.executeUpdate() == 1 ? ReturnValue.OK: ReturnValue.NOT_EXISTS;
        } catch (SQLException ex) {
            switch (errorCode(ex)) {
                case UNIQUE_VIOLATION:
                    return ReturnValue.NOT_EXISTS;
                case CHECK_VIOLATION:
                    return ReturnValue.BAD_PARAMS;
            }
            return ReturnValue.ERROR;
        } finally {
            finish(statement);
            closeConnection(connection);
        }
        return null;
    }

    public static ReturnValue addPlaylist(Playlist playlist)
    {
        Connection connection = DBConnector.getConnection();
        PreparedStatement statement = prepareStatement(connection,
                "INSERT INTO " + TABLE_PLAYLIST + " VALUES(?, ?, ?, ?)");
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

    public static Playlist getPlaylist(Integer playlistId)
    {
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

    public static ReturnValue deletePlaylist(Playlist playlist)
    {
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

    public static ReturnValue updatePlaylist(Playlist playlist)
    {
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
                case CHECK_VIOLATION:
                    return ReturnValue.BAD_PARAMS;
            }
            return ReturnValue.ERROR;
        } finally {
            finish(statement);
            closeConnection(connection);
        }
        return null;
    }

    public static ReturnValue addSongToPlaylist(Integer songid, Integer playlistId){
        return null;
    }

    public static ReturnValue removeSongFromPlaylist(Integer songid, Integer playlistId){
        return null;
    }

    public static ReturnValue followPlaylist(Integer userId, Integer playlistId){
        return null;
    }

    public static ReturnValue stopFollowPlaylist(Integer userId, Integer playlistId){
        return null;
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

    public static Integer getPlaylistTotalPlayCount(Integer playlistId){
        return null;
    }

    public static Integer getPlaylistFollowersCount(Integer playlistId){
        return null;
    }

    public static String getMostPopularSong(){
        return null;
    }

    public static Integer getMostPopularPlaylist(){
        return null;
    }

    public static ArrayList<Integer> hottestPlaylistsOnTechnify(){
        return null;
    }

    public static ArrayList<Integer> getSimilarUsers(Integer userId){
        return null;
    }

    public static ArrayList<Integer> getTopCountryPlaylists(Integer userId) {
        return null;
    }

    public static ArrayList<Integer> getPlaylistRecommendation (Integer userId){
        return null;
    }

    public static ArrayList<Integer> getSongsRecommendationByGenre(Integer userId, String genre){
        return null;
    }


}

