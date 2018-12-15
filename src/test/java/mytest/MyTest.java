package mytest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import technify.Solution;
import technify.business.ReturnValue;
import technify.business.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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

//    @Test
//    public void testClearTables() {
//        Solution.clearTables();
//    }

    private static User newUser(int id, String name, String country, boolean premium) {
        User u = new User();
        u.setId(id);
        u.setName(name);
        u.setCountry(country);
        u.setPremium(premium);
        return u;
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
}
