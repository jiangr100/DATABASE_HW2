package mytest;

import org.junit.*;
import technify.Solution;
import technify.business.Playlist;
import technify.business.ReturnValue;
import technify.business.Song;
import technify.business.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static technify.business.ReturnValue.*;

public class MyTest {

    @BeforeClass
    public static void testCreateTables() {
        Solution.createTables();
    }

    @AfterClass
    public static void testDropTables() {
        Solution.dropTables();
    }

    @After
    public void testClearTables() {
        Solution.clearTables();
    }

    private static User newUser(int id, String name, String country, boolean premium) {
        User u = new User();
        u.setId(id);
        u.setName(name);
        u.setCountry(country);
        u.setPremium(premium);
        return u;
    }

    private static Song newSong(int id, String name, String genre, String country, int playCount) {
        Song s = new Song();
        s.setId(id);
        s.setName(name);
        s.setGenre(genre);
        s.setCountry(country);
        s.setPlayCount(playCount);
        return s;
    }

    private static Playlist newPlaylist(int id, String genre, String description) {
        Playlist p = new Playlist();
        p.setId(id);
        p.setGenre(genre);
        p.setDescription(description);
        return p;
    }

    private static void insertBigDatabase() {
        List<Song> songsToInsert = Arrays.asList(
            newSong(1, "Cluster One", "Rock", "UK", 0),
            newSong(1, "Cluster One", "Rock", "UK", 0),
            newSong(2, "What Do You Want from Me", "Rock", "UK", 0),
            newSong(3, "Poles Apart", "Rock", "UK", 0),
            newSong(4, "Marooned", "Rock", "UK", 0),
            newSong(5, "A Great Day for Freedom", "Rock", "UK", 0),
            newSong(6, "Wearing the Inside Out", "Rock", "UK", 0),
            newSong(7, "Take It Back", "Rock", "UK", 0),
            newSong(8, "Coming Back to Life", "Rock", "UK", 0),
            newSong(9, "Keep Talking", "Rock", "UK", 0),
            newSong(10, "Lost for Words", "Rock", "UK", 0),
            newSong(11, "High Hopes", "Rock", "UK", 0),

            newSong(12, "Lost in Space (feat. A-WA)", "Rap", "Israel", 0),
            newSong(13, "Galgal Anak", "Rap", "Israel", 0),
            newSong(14, "Cowboy", "Rap", "Israel", 0),
            newSong(15, "Sharhoret", "Rap", "Israel", 0),
            newSong(16, "BeSivuv", "Rap", "Israel", 0),
            newSong(17, "Yud-Alef 2", "Rap", "Israel", 0),
            newSong(18, "Summer Day", "Rap", "Israel", 0),
            newSong(19, "On the Train", "Rap", "Israel", 0),
            newSong(20, "Ahrei HaAhava", "Rap", "Israel", 0),
            newSong(21, "Hakol Ze Batzevet", "Rap", "Israel", 0),
            newSong(22, "Dang", "Rap", "Israel", 0),

            newSong(23, "Bliss on Mushrooms", "Psytrance", "Israel", 0),
            newSong(24, "Guitarmass", "Psytrance", "Israel", 0),
            newSong(25, "Head of NASA", "Psytrance", "Israel", 0),
            newSong(26, "Chenchen Barvaz", "Psytrance", "Israel", 0),
            newSong(27, "Walking on the Moon", "Psytrance", "Israel", 0),
            newSong(28, "Here We Go Go Go", "Psytrance", "Israel", 0),
            newSong(29, "Lost in Space", "Psytrance", "Israel", 0),


            newSong(30, "Let It Happen", "Indie", "Australia", 0),
            newSong(31, "Nangs", "Indie", "Australia", 0),
            newSong(32, "The Moment", "Indie", "Australia", 0),
            newSong(33, "Yes I'm Changing", "Indie", "Australia", 0),
            newSong(34, "Eventually", "Indie", "Australia", 0),
            newSong(35, "Gossip", "Indie", "Australia", 0),
            newSong(36, "The Less I Know the Better", "Indie", "Australia", 0),
            newSong(37, "Past Life", "Indie", "Australia", 0),
            newSong(38, "Disciples", "Indie", "Australia", 0),
            newSong(39, "Reality in Motion", "Indie", "Australia", 0)

        );

        for(Song song : songsToInsert) Solution.addSong(song);

        List<Playlist> playlistsToInsert = Arrays.asList(
                newPlaylist(1, "Rock", "The Division Bell"),
                newPlaylist(2, "Rap", "Tunapark"),
                newPlaylist(3, "Psytrance", "Head of NASA"),
                newPlaylist(4, "Indie", "Currents")
        );

        for(Playlist playlist : playlistsToInsert) {
            for(Song song : songsToInsert) Solution.addSongToPlaylist(song.getId(), playlist.getId());
            Solution.addPlaylist(playlist);
        }

        List<User> usersToInsert = Arrays.asList(
            newUser(1, "User 1", "Israel", false),
                newUser(2, "User 2", "Israel", false),
                newUser(3, "User 3", "Israel", false),
                newUser(4, "User 4", "Israel", false),
                newUser(5, "User 5", "Israel", false),
                newUser(6, "User 6", "Israel", false),
                newUser(7, "User 7", "Israel", true),
                newUser(8, "User 8", "Israel", true),
                newUser(9, "User 9", "Israel", true),
                newUser(10, "User 10", "Israel", true),
                newUser(11, "User 11", "Israel", true),
                newUser(12, "User 12", "Israel", true),

                newUser(13, "User 13", "UK", false),
                newUser(14, "User 14", "UK", false),
                newUser(15, "User 15", "UK", false),
                newUser(16, "User 16", "UK", false),
                newUser(17, "User 17", "UK", false),
                newUser(18, "User 18", "UK", true),
                newUser(19, "User 19", "UK", true),
                newUser(20, "User 20", "UK", true),
                newUser(21, "User 21", "UK", true),
                newUser(22, "User 22", "UK", true),
                newUser(23, "User 23", "UK", true),

                newUser(24, "User 24", "Australia", false),
                newUser(25, "User 25", "Australia", false),
                newUser(26, "User 26", "Australia", false),
                newUser(27, "User 27", "Australia", false),
                newUser(28, "User 28", "Australia", true),
                newUser(29, "User 29", "Australia", true),
                newUser(30, "User 30", "Australia", true),
                newUser(31, "User 31", "Australia", true),
                newUser(32, "User 32", "Australia", true),
                newUser(33, "User 33", "Australia", true)
                );
        for (User u : usersToInsert) Solution.addUser(u);

        for(Song song : songsToInsert) Solution.songPlay(song.getId(), 1000 - song.getId() * 5);
        for(User u : usersToInsert) {
            Solution.followPlaylist(u.getId(), u.getId() % 4);
        }
    }

    @Test
    public void testUser() {
        User user1 = newUser(1, "User1", "Israel", false);
        assertEquals(Solution.addUser(user1), OK);
        assertEquals(Solution.addUser(user1), ALREADY_EXISTS);
        User user2 = newUser(1, "User2", "USA", true);
        assertEquals(Solution.addUser(user2), ALREADY_EXISTS);
        User user3 = newUser(2, "User2", "USA", true);
        assertEquals(Solution.addUser(user3), OK);

        // Parameter Checks
        assertEquals(Solution.addUser(User.badUser()), BAD_PARAMS);
        assertEquals(Solution.addUser(newUser(-1, "Name", "Country", false)), BAD_PARAMS);
        assertEquals(Solution.addUser(newUser(0, "Name", "Country", false)), BAD_PARAMS);
        assertEquals(Solution.addUser(newUser(10, null, "Country", false)), BAD_PARAMS);
        assertEquals(Solution.addUser(newUser(10, "Name", null, false)), BAD_PARAMS);

        // Get user profile
        User user1queried = Solution.getUserProfile(1);
        assertEquals(user1queried.getId(), 1);
        assertEquals(user1queried.getName(), "User1");
        assertEquals(user1queried.getCountry(), "Israel");
        assertFalse(user1queried.getPremium());
        assertEquals(Solution.getUserProfile(-1), User.badUser());

        User user2queried = Solution.getUserProfile(2);
        assertEquals(user2queried.getId(), 2);
        assertEquals(user2queried.getName(), "User2");
        assertEquals(user2queried.getCountry(), "USA");
        assertTrue(user2queried.getPremium());

        assertEquals(Solution.updateUserPremium(user2queried.getId()), ALREADY_EXISTS);
        assertEquals(Solution.updateUserNotPremium(user2queried.getId()), OK);
        assertFalse(Solution.getUserProfile(user2queried.getId()).getPremium());
        assertEquals(Solution.updateUserNotPremium(user2queried.getId()), ALREADY_EXISTS);
        assertEquals(Solution.updateUserPremium(user2queried.getId()), OK);
        assertTrue(Solution.getUserProfile(user2queried.getId()).getPremium());

        assertEquals(Solution.getUserProfile(3), User.badUser());

        // Delete User
        assertEquals(Solution.deleteUser(User.badUser()), NOT_EXISTS);
        assertEquals(Solution.deleteUser(user1queried), OK);
        assertEquals(Solution.deleteUser(user1queried), NOT_EXISTS);
        assertEquals(Solution.deleteUser(user2queried), OK);
        assertEquals(Solution.deleteUser(user3), NOT_EXISTS);
    }

    @Test
    public void testSong() {
        assertEquals(BAD_PARAMS, Solution.addSong(Song.badSong()));
        Song song1 = newSong(1, "Song 1", "Rock", "Israel", 0);
        assertEquals(OK, Solution.addSong(song1));
        assertEquals(ALREADY_EXISTS, Solution.addSong(song1));
        Song song1copy = newSong(song1.getId(), song1.getName(), song1.getGenre(), song1.getCountry(), 1);
        assertEquals(ALREADY_EXISTS, Solution.addSong(song1copy));
        song1copy.setId(2);
        assertEquals(OK, Solution.addSong(song1copy));

        // Parameter Checks
        assertEquals(BAD_PARAMS, Solution.addSong(newSong(0, "Song", "Rock", "Israel", 0)));
        assertEquals(BAD_PARAMS, Solution.addSong(newSong(3, null, "Rock", "Israel", 0)));
        assertEquals(BAD_PARAMS, Solution.addSong(newSong(3, "Song", null, "Israel", 0)));
        assertEquals(OK, Solution.addSong(newSong(3, "Song", "Rock", null, -1)));

        Song song1queried = Solution.getSong(song1.getId());
        assertEquals(song1queried, song1);
        Song song2queried = Solution.getSong(song1copy.getId());
        assertNotEquals(song2queried, song1copy);
        assertEquals(song2queried.getId(), song1copy.getId());
        assertEquals(song2queried.getName(), song1copy.getName());
        assertEquals(song2queried.getCountry(), song1copy.getCountry());
        assertEquals(song2queried.getGenre(), song1copy.getGenre());

        assertEquals(NOT_EXISTS, Solution.deleteSong(Song.badSong()));
        assertEquals(OK, Solution.deleteSong(song1queried));
        assertEquals(NOT_EXISTS, Solution.deleteSong(song1queried));
        assertEquals(OK, Solution.addSong(song1queried));
        assertEquals(OK, Solution.deleteSong(song1));
    }

    @Test
    public void testSongPlay() {
        Song song1 = newSong(1, "Song 1", "Rock", "Israel", 100);
        assertEquals(OK, Solution.addSong(song1));
        Song song2 = newSong(2, "Song 2", "Metal", "USA", -100);
        assertEquals(OK, Solution.addSong(song2));

        assertEquals(NOT_EXISTS, Solution.songPlay(-1, 1));
        assertEquals(BAD_PARAMS, Solution.songPlay(song1.getId(), -1));
    }

    @Test
    public void testUpdateSongName() {
        Song song1 = newSong(1, "Song 1", "Rock", "Israel", 100);
        assertEquals(OK, Solution.addSong(song1));
        Song song2 = newSong(2, "Song 2", "Metal", "USA", -100);

        assertEquals("Song 1", Solution.getSong(song1.getId()).getName());
        song1.setName("Song 1 Updated");
        assertEquals(OK, Solution.updateSongName(song1));
        assertEquals("Song 1 Updated", Solution.getSong(song1.getId()).getName());

        assertEquals(NOT_EXISTS, Solution.updateSongName(Song.badSong()));
        assertEquals(NOT_EXISTS, Solution.updateSongName(song2));

        assertEquals(OK, Solution.addSong(song2));
        song2.setName(null);
        assertEquals(BAD_PARAMS, Solution.updateSongName(song2));
    }

    @Test
    public void testPlaylist() {
        Playlist p1 = newPlaylist(1, "Genre", "Desc");
        assertEquals(OK, Solution.addPlaylist(p1));
        assertEquals(ALREADY_EXISTS, Solution.addPlaylist(p1));
        p1.setGenre("New Genre");
        p1.setDescription("New Desc");
        assertEquals(ALREADY_EXISTS, Solution.addPlaylist(p1));
        assertEquals(BAD_PARAMS, Solution.addPlaylist(Playlist.badPlaylist()));

        Playlist p2 = newPlaylist(-1, "Genre", "Desc");
        assertEquals(BAD_PARAMS, Solution.addPlaylist(p2));
        p2.setId(0);
        assertEquals(BAD_PARAMS, Solution.addPlaylist(p2));
        p2.setId(1);
        assertEquals(ALREADY_EXISTS, Solution.addPlaylist(p2));
        p2.setId(2);
        p2.setDescription(null);
        assertEquals(BAD_PARAMS, Solution.addPlaylist(p2));
        p2.setDescription("Desc");
        p2.setGenre(null);
        assertEquals(BAD_PARAMS, Solution.addPlaylist(p2));
        p2.setGenre("Genre");
        assertEquals(OK, Solution.addPlaylist(p2));

        assertEquals(Playlist.badPlaylist(), Solution.getPlaylist(-1));
        assertEquals(Playlist.badPlaylist(), Solution.getPlaylist(0));
        assertEquals(Playlist.badPlaylist(), Solution.getPlaylist(3));
        Playlist p1queried = Solution.getPlaylist(p1.getId());
        assertEquals(p1queried.getId(), p1.getId());
        assertEquals(p2, Solution.getPlaylist(p2.getId()));

        assertEquals(NOT_EXISTS, Solution.deletePlaylist(Playlist.badPlaylist()));
        assertEquals(OK, Solution.deletePlaylist(p1queried));
        assertEquals(Playlist.badPlaylist(), Solution.getPlaylist(p1.getId()));
        assertEquals(OK, Solution.addPlaylist(p1queried));

        String desc1 = "This is the first description";
        String desc2 = "This is the second description";
        Playlist p3 = newPlaylist(3, "Genre", desc1);
        assertEquals(NOT_EXISTS, Solution.updatePlaylist(p3));
        assertEquals(OK, Solution.addPlaylist(p3));
        p3.setDescription(null);
        assertEquals(BAD_PARAMS, Solution.updatePlaylist(p3));
        assertEquals(desc1, Solution.getPlaylist(p3.getId()).getDescription());
        p3.setDescription(desc2);
        assertEquals(OK, Solution.updatePlaylist(p3));
        assertEquals(desc2, Solution.getPlaylist(p3.getId()).getDescription());

    }

    @Test
    public void testAddRemoveSongPlaylist() {
        Playlist p1 = newPlaylist(1, "Funk", "Desc");
        assertEquals(OK, Solution.addPlaylist(p1));
        Playlist p2 = newPlaylist(2, "Metal", "Desc 2");
        assertEquals(OK, Solution.addPlaylist(p2));

        Song theGateway = newSong(1, "The Gateway", "Funk", "U.S", 0);
        Song ironMan = newSong(2, "Iron Man", "Metal", "U.K", 0);
        Song babyBaby = newSong(3, "Baby", "Pop", "U.S", 0);

        Solution.addSong(theGateway);

        assertEquals(NOT_EXISTS, Solution.addSongToPlaylist(10, p1.getId()));
        assertEquals(NOT_EXISTS, Solution.addSongToPlaylist(theGateway.getId(), -1));
        assertEquals(NOT_EXISTS, Solution.addSongToPlaylist(theGateway.getId(), 0));
        assertEquals(NOT_EXISTS, Solution.addSongToPlaylist(ironMan.getId(), p2.getId()));

        Solution.addSong(babyBaby);
        Solution.addSong(ironMan);

        assertEquals(BAD_PARAMS, Solution.addSongToPlaylist(babyBaby.getId(), p1.getId()));
        assertEquals(BAD_PARAMS, Solution.addSongToPlaylist(babyBaby.getId(), p2.getId()));
        assertEquals(OK, Solution.addSongToPlaylist(theGateway.getId(), p1.getId()));
        assertEquals(OK, Solution.addSongToPlaylist(ironMan.getId(), p2.getId()));
        Song s4 = newSong(4, "Dark Necessities", "Funk", "U.S", 0);
        Solution.addSong(s4);
        assertEquals(OK, Solution.addSongToPlaylist(s4.getId(), p1.getId()));
        assertEquals(ALREADY_EXISTS, Solution.addSongToPlaylist(ironMan.getId(), p2.getId()));

        assertEquals(NOT_EXISTS, Solution.removeSongFromPlaylist(-1, -1));
        assertEquals(NOT_EXISTS, Solution.removeSongFromPlaylist(ironMan.getId(), -1));
        assertEquals(NOT_EXISTS, Solution.removeSongFromPlaylist(-1, p2.getId()));
        assertEquals(OK, Solution.removeSongFromPlaylist(ironMan.getId(), p2.getId()));
        assertEquals(NOT_EXISTS, Solution.removeSongFromPlaylist(ironMan.getId(), p2.getId()));
        assertEquals(OK, Solution.addSongToPlaylist(ironMan.getId(), p2.getId()));
    }

    @Test
    public void testUserFollowUnfollowPlaylist() {
        Playlist p1 = newPlaylist(1, "Funk", "Desc");
        assertEquals(OK, Solution.addPlaylist(p1));
        Playlist p2 = newPlaylist(2, "Metal", "Desc 2");
        assertEquals(OK, Solution.addPlaylist(p2));

        User u1 = newUser(1, "Miki", "Israel", false);
        User u2 = newUser(2, "Rui", "Israel", true);

        assertEquals(NOT_EXISTS, Solution.followPlaylist(-1, -1));
        assertEquals(NOT_EXISTS, Solution.followPlaylist(u1.getId(), p1.getId()));

        Solution.addUser(u1);
        Solution.addUser(u2);
        Solution.addPlaylist(p1);
        Solution.addPlaylist(p2);

        assertEquals(OK, Solution.followPlaylist(u1.getId(), p1.getId()));
        assertEquals(ALREADY_EXISTS, Solution.followPlaylist(u1.getId(), p1.getId()));
        assertEquals(OK, Solution.followPlaylist(u1.getId(), p2.getId()));
        assertEquals(ALREADY_EXISTS, Solution.followPlaylist(u1.getId(), p2.getId()));
        assertEquals(OK, Solution.followPlaylist(u2.getId(), p2.getId()));

        assertEquals(NOT_EXISTS, Solution.followPlaylist(u1.getId(), 3));

        assertEquals(NOT_EXISTS, Solution.stopFollowPlaylist(-1, -1));
        assertEquals(NOT_EXISTS, Solution.stopFollowPlaylist(u1.getId(), -1));
        assertEquals(NOT_EXISTS, Solution.stopFollowPlaylist(0, p1.getId()));
        assertEquals(OK, Solution.stopFollowPlaylist(u1.getId(), p1.getId()));
        assertEquals(NOT_EXISTS, Solution.stopFollowPlaylist(u1.getId(), p1.getId()));
        assertEquals(NOT_EXISTS, Solution.stopFollowPlaylist(u2.getId(), p1.getId()));
        assertEquals(OK, Solution.stopFollowPlaylist(u2.getId(), p2.getId()));
        assertEquals(OK, Solution.followPlaylist(u2.getId(), p2.getId()));
    }

    @Test
    public void testGetPlaylistTotalCount() {
        Playlist p1 = newPlaylist(1, "Funk", "Desc");
        Solution.addPlaylist(p1);

        assertEquals(Integer.valueOf(0), Solution.getPlaylistTotalPlayCount(-1));
        assertEquals(Integer.valueOf(0), Solution.getPlaylistTotalPlayCount(0));

        Song theGateway = newSong(1, "The Gateway", "Funk", "U.S", 0);
        Song darkNecessities = newSong(2, "Dark Necessities", "Funk", "U.S", 0);
        Song babyBaby = newSong(3, "Baby", "Pop", "U.S", 0);
        Solution.addSong(theGateway);
        Solution.addSong(darkNecessities);
        Solution.addSong(babyBaby);

        Solution.addSongToPlaylist(theGateway.getId(), p1.getId());
        assertEquals(Integer.valueOf(0), Solution.getPlaylistTotalPlayCount(p1.getId()));

        assertEquals(OK, Solution.songPlay(theGateway.getId(), 100));
        assertEquals(OK, Solution.songPlay(darkNecessities.getId(), 100));

        assertEquals(Integer.valueOf(100), Solution.getPlaylistTotalPlayCount(p1.getId()));
        Solution.addSongToPlaylist(darkNecessities.getId(), p1.getId());
        assertEquals(Integer.valueOf(200), Solution.getPlaylistTotalPlayCount(p1.getId()));
    }

    @Test
    public void testGetPlaylistFollowersCount() {
        Playlist p1 = newPlaylist(1, "Funk", "Desc");
        assertEquals(Integer.valueOf(0), Solution.getPlaylistFollowersCount(p1.getId()));
        Solution.addPlaylist(p1);

        User u1 = newUser(1, "Name", "Country", true);
        User u2 = newUser(2, "Name", "Country", true);
        User u3 = newUser(3, "Name", "Country", true);
        User u4 = newUser(4, "Name", "Country", true);

        Solution.addUser(u1);
        Solution.addUser(u2);
        Solution.addUser(u3);
        Solution.addUser(u4);

        assertEquals(Integer.valueOf(0), Solution.getPlaylistFollowersCount(-1));
        assertEquals(Integer.valueOf(0), Solution.getPlaylistFollowersCount(p1.getId()));

        assertEquals(OK, Solution.followPlaylist(u1.getId(), p1.getId()));
        assertEquals(OK, Solution.followPlaylist(u2.getId(), p1.getId()));
        assertEquals(OK, Solution.followPlaylist(u3.getId(), p1.getId()));
        assertEquals(OK, Solution.followPlaylist(u4.getId(), p1.getId()));

        assertEquals(Integer.valueOf(4), Solution.getPlaylistFollowersCount(p1.getId()));
        Solution.stopFollowPlaylist(u1.getId(), p1.getId());
        assertEquals(Integer.valueOf(3), Solution.getPlaylistFollowersCount(p1.getId()));
        Solution.followPlaylist(u2.getId(), p1.getId());
        assertEquals(Integer.valueOf(3), Solution.getPlaylistFollowersCount(p1.getId()));
    }

    // @Test
    public void testGetMostPopularSongAndPlaylist() {
        Playlist p1 = newPlaylist(1, "General", "Desc1");
        Playlist p2 = newPlaylist(2, "General", "Desc2");
        Playlist p3 = newPlaylist(3, "General", "Desc3");
        Playlist p4 = newPlaylist(4, "General", "Desc4");
        Solution.addPlaylist(p1);
        Solution.addPlaylist(p2);
        Solution.addPlaylist(p3);
        Solution.addPlaylist(p4);

        assertEquals("No songs", Solution.getMostPopularSong());

        int size = 100;
        ArrayList<Song> songs = new ArrayList<>();
        for(int i = 1; i <= size; i++) {
            Song s = newSong(i, "Song " + i, "General", "Israel", 0);
            songs.add(i - 1, s);
            Solution.addSong(s);
            if(i < 25) {
                Solution.addSongToPlaylist(i, 1);
            } else if(i < 50) {
                Solution.addSongToPlaylist(i, 2);
            } else if(i < 75) {
                Solution.addSongToPlaylist(i, 3);
            } else {
                Solution.addSongToPlaylist(i, 4);
            }
        }
        assertEquals("Song 100", Solution.getMostPopularSong());
        Solution.addSongToPlaylist(99, 1);
        Solution.addSongToPlaylist(99, 2);
        Solution.addSongToPlaylist(99, 3);
        assertEquals("Song 99", Solution.getMostPopularSong());
        Solution.deleteSong(songs.get(98));
        assertEquals("Song 100", Solution.getMostPopularSong());
        assertEquals(Integer.valueOf(4), Solution.getMostPopularPlaylist());
        Solution.songPlay(100, 100);
        assertEquals(Integer.valueOf(4), Solution.getMostPopularPlaylist());
        Solution.songPlay(1, 1000);
        assertEquals(Integer.valueOf(1), Solution.getMostPopularPlaylist());
        Solution.songPlay(25, 1001);
        assertEquals(Integer.valueOf(2), Solution.getMostPopularPlaylist());
        Solution.songPlay(25, -2);
        assertEquals(Integer.valueOf(1), Solution.getMostPopularPlaylist());
    }

    // TODO Fix
     @Test
    public void testHottestPlaylists() {
        Playlist p1 = newPlaylist(1, "General", "Desc1");
        Playlist p2 = newPlaylist(2, "General", "Desc2");
        Playlist p3 = newPlaylist(3, "General", "Desc3");
        Playlist p4 = newPlaylist(4, "General", "Desc4");

        Solution.addPlaylist(p1);
        Solution.addPlaylist(p2);
        Solution.addPlaylist(p3);
        Solution.addPlaylist(p4);

        Solution.addSong(newSong(1, "Song 1", "General", "Israel", 0));
        Solution.addSong(newSong(2, "Song 2", "General", "Israel", 0));
        Solution.addSong(newSong(3, "Song 3", "General", "Israel", 0));
        Solution.addSong(newSong(4, "Song 4", "General", "Israel", 0));

        assertEquals(0, Solution.hottestPlaylistsOnTechnify().size());

        Solution.addSongToPlaylist(1,1);
        Solution.addSongToPlaylist(2,2);
        Solution.addSongToPlaylist(3,3);
        Solution.addSongToPlaylist(4,4);

        Solution.songPlay(4, 100);
        Solution.songPlay(2, 50);
        Solution.songPlay(3, 25);
        Solution.songPlay(1, 1);

        List<Integer> expected1 = Arrays.asList(4,2,3,1);
        ArrayList<Integer> result1 = Solution.hottestPlaylistsOnTechnify();
        assertNotEquals(null, result1);
        assertEquals(4, result1.size());
        for(int i = 0; i < expected1.size(); i++) {
            assertEquals(expected1.get(i), result1.get(i));
        }

        for (int i = 5; i <= 11; i++) {
            Solution.addPlaylist(newPlaylist(i, "General", "Desc" + i));
            Solution.addSongToPlaylist(4, i);
        }
         List<Integer> expected2 = Arrays.asList(4, 5, 6, 7, 8, 9, 10, 11, 2, 3);
         ArrayList<Integer> result2 = Solution.hottestPlaylistsOnTechnify();
         assertEquals(10, result2.size());
         for(int i = 0; i < expected2.size(); i++) {
             assertEquals(expected2.get(i), result2.get(i));
         }
    }

    @Test
    public void testGetSimilarUsers() {
        Solution.addPlaylist(newPlaylist(1, "General", "Desc1"));
        Solution.addPlaylist(newPlaylist(2, "General", "Desc2"));
        Solution.addPlaylist(newPlaylist(3, "General", "Desc3"));
        Solution.addPlaylist(newPlaylist(4, "General", "Desc4"));
        Solution.addPlaylist(newPlaylist(5, "General", "Desc5"));

        Solution.addUser(newUser(1, "User 1", "Israel", false));
        Solution.addUser(newUser(2, "User 2", "Israel", false));
        Solution.addUser(newUser(3, "User 3", "Israel", false));
        Solution.addUser(newUser(4, "User 4", "Israel", false));
        Solution.addUser(newUser(5, "User 5", "Israel", false));
        Solution.addUser(newUser(6, "User 6", "Israel", false));
        Solution.addUser(newUser(7, "User 7", "Israel", false));
        Solution.addUser(newUser(8, "User 8", "Israel", false));
        Solution.addUser(newUser(9, "User 9", "Israel", false));
        Solution.addUser(newUser(10, "User 10", "Israel", false));

        Solution.followPlaylist(1, 1);
        Solution.followPlaylist(1, 2);
        Solution.followPlaylist(1, 3);
        Solution.followPlaylist(1, 4);

        Solution.followPlaylist(5, 2);
        Solution.followPlaylist(5, 3);
        Solution.followPlaylist(5, 1);
        Solution.followPlaylist(5, 5);

        Solution.followPlaylist(6, 2);
        Solution.followPlaylist(6, 4);
        Solution.followPlaylist(6, 1);
        Solution.followPlaylist(6, 5);

        Solution.followPlaylist(7, 3);
        Solution.followPlaylist(7, 4);
        Solution.followPlaylist(7, 1);
        Solution.followPlaylist(7, 5);

        Solution.followPlaylist(4, 1);
        Solution.followPlaylist(4, 2);
        Solution.followPlaylist(4, 3);
        Solution.followPlaylist(4, 5);

        List<Integer> expected1 = Arrays.asList(4,5,6,7);
        ArrayList<Integer> result1 = Solution.getSimilarUsers(1);
        for(int i = 0; i < expected1.size(); i++) assertEquals(expected1.get(i), result1.get(i));

        List<Integer> expected2 = Arrays.asList(5);
        ArrayList<Integer> result2 = Solution.getPlaylistRecommendation(1);
        assertEquals(expected2.size(), result2.size());
        for(int i = 0; i < expected2.size(); i++) assertEquals(expected2.get(i), result2.get(i));

    }
}
