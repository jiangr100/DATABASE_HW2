package technify;

import technify.business.*;

import technify.data.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Solution {

    private static final String TABLE_USER = "technifyUser";
    private static final String TABLE_SONG = "song";
    private static final String TABLE_PLAYLIST = "playlist";

    private static PreparedStatement prepareStatement(Connection c, String sql) {
        PreparedStatement pstmt;
        try {
            pstmt = c.prepareStatement(sql);
            return pstmt;
        } catch (SQLException e) {
            return null;
        }
    }

    private static void closeConnection(Connection c) {
        try {
            c.close();
        } catch (SQLException e) {
            //
        }
    }

    private static void finish(PreparedStatement s) {
        if(s == null) return;
        try {
            s.close();
        } catch (SQLException e) {
            //
        }
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
            ex.printStackTrace();
        }

        finish(userCreateStatement);
        finish(songCreateStatement);
        finish(playlistCreateStatement);
        closeConnection(connection);
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
        }

        finish(userClearStatement);
        finish(songClearStatement);
        finish(playlistClearStatement);
        closeConnection(connection);
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
        }

        finish(userDropStatement);
        finish(songDropStatement);
        finish(playlistDropStatement);
        closeConnection(connection);
    }


    public static ReturnValue addUser(User user) {
        if (user.getId() <= 0 || user.getName() == null ||
            user.getCountry() == null) {
            return ReturnValue.BAD_PARAMS;
        }
        if (getUserProfile(user.getId()) != User.badUser()) {
            return ReturnValue.BAD_PARAMS;
        }
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("INSERT INTO Users" +
                    " VALUES (?, ?, ?, ?)" );
            pstmt.setInt(1,user.getId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3,user.getCountry());
            pstmt.setBoolean(4,user.getPremium());


            pstmt.execute();
            return ReturnValue.OK;

        } catch (SQLException e) {
            return ReturnValue.ERROR;
        }
        finally {
            try {
                pstmt.close();
            } catch (SQLException e) {
                return ReturnValue.ERROR;
            }
            try {
                connection.close();
            } catch (SQLException e) {
                return ReturnValue.ERROR;
            }
        }
    }

    public static User getUserProfile(Integer userId) {
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(
                    "SELECT * FROM Users " +
                        "WHERE id = ?");
            pstmt.setInt(1, userId);

            ResultSet results = pstmt.executeQuery();
            DBConnector.printResults(results);
            if (!results.next()) {
                results.close();
                return User.badUser();
            }
            User cur(results.getInt(0),
                    results.getString(1),
                    results.getString(2),
                    results.getBoolean(3));
            results.close();
            return cur;

        } catch (SQLException e) {
            return User.badUser();
        }
        finally {
            try {
                pstmt.close();
            } catch (SQLException e) {
                return User.badUser();
            }
            try {
                connection.close();
            } catch (SQLException e) {
                return User.badUser();
            }
        }
        return null;
    }

    public static ReturnValue deleteUser(User user)
    {
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstst = null;
        try {
            pstst = connection.prepareStatement(
                    "DELETE FROM Users " +

                            "where id = ?");
            pstst.setInt(1, user.getId());

            int affectedRows = pstst.executeUpdate();
            System.out.println("deleted " + affectedRows + " rows");
        } catch (SQLException e) {

        }
        finally {
            try {
                pstst.close();
            } catch (SQLException e) {
                //e.printStackTrace()();
            }
            try {
                connection.close();
            } catch (SQLException e) {
                //e.printStackTrace()();
            }
        }
        return null;
    }

    public static ReturnValue updateUserPremium(Integer userId)
    {
        User cur = getUserProfile(userId);
        if (cur.getPremium()) {
            return ReturnValue.ALREADY_EXISTS;
        }
        if (cur == User.badUser()) {
            return ReturnValue.NOT_EXISTS;
        }
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(
                    "UPDATE Users " +
                            "SET premium = ? " +
                            "where id = ?");
            pstmt.setBoolean(1,1);
            pstmt.setInt(1, userId);
            int affectedRows = pstmt.executeUpdate();
            System.out.println("changed " + affectedRows + " rows");
        } catch (SQLException e) {
            return ReturnValue.ERROR;
        }
        finally {
            try {
                pstmt.close();
            } catch (SQLException e) {
                return ReturnValue.ERROR
            }
            try {
                connection.close();
            } catch (SQLException e) {
                return ReturnValue.ERROR;
            }
        }
    }

    public static ReturnValue updateUserNotPremium(Integer userId)
    {
        User cur = getUserProfile(userId);
        if (!cur.getPremium()) {
            return ReturnValue.ALREADY_EXISTS;
        }
        if (cur == User.badUser()) {
            return ReturnValue.NOT_EXISTS;
        }
        Connection connection = DBConnector.getConnection();
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(
                    "UPDATE Users " +
                            "SET premium = ? " +
                            "where id = ?");
            pstmt.setBoolean(1,0);
            pstmt.setInt(1, userId);
            int affectedRows = pstmt.executeUpdate();
            System.out.println("changed " + affectedRows + " rows");
        } catch (SQLException e) {
            return ReturnValue.ERROR;
        }
        finally {
            try {
                pstmt.close();
            } catch (SQLException e) {
                return ReturnValue.ERROR
            }
            try {
                connection.close();
            } catch (SQLException e) {
                return ReturnValue.ERROR;
            }
        }
    }

    public static ReturnValue addSong(Song song)
    {
        return null;
    }

    public static Song getSong(Integer songId)
    {
        return null;
    }

    public static ReturnValue deleteSong(Song song)
    {
        return null;
    }

    public static ReturnValue updateSongName(Song song)
    {
        return null;
    }

    public static ReturnValue addPlaylist(Playlist playlist)
    {
        return null;
    }

    public static Playlist getPlaylist(Integer playlistId)
    {
        return null;
    }

    public static ReturnValue deletePlaylist(Playlist playlist)
    {
        return null;
    }

    public static ReturnValue updatePlaylist(Playlist playlist)
    {
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
        return null;
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

