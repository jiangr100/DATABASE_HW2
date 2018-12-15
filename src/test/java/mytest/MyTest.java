package mytest;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import technify.Solution;
import technify.business.Playlist;
import technify.business.ReturnValue;
import technify.business.Song;
import technify.business.User;

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

    @Before
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
}
