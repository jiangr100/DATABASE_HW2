package mytest;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import technify.Solution;
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
}
