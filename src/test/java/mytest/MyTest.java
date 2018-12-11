package mytest;

import org.junit.Test;
import technify.Solution;

public class MyTest {

    @Test
    public void testCreateTables() {
        Solution.createTables();
    }

    @Test
    public void testDropTables() {
        Solution.dropTables();
    }

    @Test
    public void testClearTables() {
        Solution.clearTables();
    }

}
