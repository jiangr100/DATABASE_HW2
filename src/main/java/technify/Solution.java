package technify;

import technify.business.*;

import technify.data.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
        Connection connection = DBConnector.getConnection();
        PreparedStatement statement =
        return null;

    }

    public static User getUserProfile(Integer userId) {

        return null;
    }

    public static ReturnValue deleteUser(User user)
    {
        return null;
    }

    public static ReturnValue updateUserPremium(Integer userId)
    {
        return null;
    }

    public static ReturnValue updateUserNotPremium(Integer userId)
    {
        return null;
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
