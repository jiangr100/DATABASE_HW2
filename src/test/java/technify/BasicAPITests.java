package technify;

import org.junit.Test;
import technify.business.*;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static technify.business.ReturnValue.*;


public class BasicAPITests extends AbstractTest {
    /*@Test
    public void songPlayTest() {

        ReturnValue res;
        Song s = new Song();
        s.setId(1);
        s.setName("Despacito");
        s.setGenre("Latin");
        s.setCountry("Spain");

        res = Solution.addSong(s);
        assertEquals(OK, res);

        res = Solution.songPlay(1, 1);
        assertEquals(OK, res);

        res = Solution.songPlay(1, -3);
        assertEquals(BAD_PARAMS, res);
    }

    @Test
    public void followPlaylistTest() {

        ReturnValue res;
        Playlist p = new Playlist();
        p.setId(10);
        p.setGenre("Pop");
        p.setDescription("Best pop songs of 2018");

        res = Solution.addPlaylist(p);
        assertEquals(OK, res);

        User u = new User();
        u.setId(100);
        u.setName("Nir");
        u.setCountry("Israel");
        u.setPremium(false);

        res = Solution.addUser(u);
        assertEquals(OK, res);

        res = Solution.followPlaylist(100, 10);
        assertEquals(OK , res);

        res = Solution.followPlaylist(100, 10);
        assertEquals(ALREADY_EXISTS , res);

        res = Solution.followPlaylist(101, 10);
        assertEquals(NOT_EXISTS , res);
    }*/

    /*@Test
    public void similarUserTest() {

        ReturnValue res;
        Playlist p1 = new Playlist();
        p1.setId(1);
        p1.setGenre("p");
        p1.setDescription("p");

        Playlist p2 = new Playlist();
        p2.setId(2);
        p2.setGenre("p");
        p2.setDescription("p");

        Playlist p3 = new Playlist();
        p3.setId(3);
        p3.setGenre("p");
        p3.setDescription("p");

        Playlist p4 = new Playlist();
        p4.setId(4);
        p4.setGenre("p");
        p4.setDescription("p");

        Playlist p5 = new Playlist();
        p5.setId(5);
        p5.setGenre("p");
        p5.setDescription("p");

        Playlist p6 = new Playlist();
        p6.setId(6);
        p6.setGenre("p");
        p6.setDescription("p");

        Playlist p7 = new Playlist();
        p7.setId(7);
        p7.setGenre("p");
        p7.setDescription("p");

        Playlist p8 = new Playlist();
        p8.setId(8);
        p8.setGenre("p");
        p8.setDescription("p");

        res = Solution.addPlaylist(p1);
        assertEquals(OK, res);

        res = Solution.addPlaylist(p2);
        assertEquals(OK, res);

        res = Solution.addPlaylist(p3);
        assertEquals(OK, res);

        res = Solution.addPlaylist(p4);
        assertEquals(OK, res);

        res = Solution.addPlaylist(p5);
        assertEquals(OK, res);

        res = Solution.addPlaylist(p6);
        assertEquals(OK, res);

        res = Solution.addPlaylist(p7);
        assertEquals(OK, res);

        res = Solution.addPlaylist(p8);
        assertEquals(OK, res);

        User u1 = new User();
        u1.setId(1);
        u1.setName("Nir");
        u1.setCountry("Israel");
        u1.setPremium(false);

        res = Solution.addUser(u1);
        assertEquals(OK, res);

        User u2 = new User();
        u2.setId(2);
        u2.setName("Nir");
        u2.setCountry("Israel");
        u2.setPremium(false);

        res = Solution.addUser(u2);
        assertEquals(OK, res);

        User u3 = new User();
        u3.setId(3);
        u3.setName("Nir");
        u3.setCountry("Israel");
        u3.setPremium(false);

        res = Solution.addUser(u3);
        assertEquals(OK, res);

        User u4 = new User();
        u4.setId(4);
        u4.setName("Nir");
        u4.setCountry("Israel");
        u4.setPremium(false);

        res = Solution.addUser(u4);
        assertEquals(OK, res);

        User u5 = new User();
        u5.setId(5);
        u5.setName("Nir");
        u5.setCountry("Israel");
        u5.setPremium(false);

        res = Solution.addUser(u5);
        assertEquals(OK, res);

        res = Solution.followPlaylist(1, 1);
        res = Solution.followPlaylist(1, 2);
        res = Solution.followPlaylist(1, 3);
        res = Solution.followPlaylist(1, 4);
        res = Solution.followPlaylist(1, 5);
        res = Solution.followPlaylist(1, 6);

        res = Solution.followPlaylist(2, 1);
        res = Solution.followPlaylist(2, 3);
        res = Solution.followPlaylist(2, 4);
        res = Solution.followPlaylist(2, 5);
        res = Solution.followPlaylist(2, 6);
        res = Solution.followPlaylist(2, 7);

        res = Solution.followPlaylist(3, 2);
        res = Solution.followPlaylist(3, 3);
        res = Solution.followPlaylist(3, 4);
        res = Solution.followPlaylist(3, 5);
        res = Solution.followPlaylist(3, 6);
        res = Solution.followPlaylist(3, 7);

        res = Solution.followPlaylist(4, 3);
        res = Solution.followPlaylist(4, 4);
        res = Solution.followPlaylist(4, 5);
        res = Solution.followPlaylist(4, 6);
        res = Solution.followPlaylist(4, 7);
        res = Solution.followPlaylist(4, 8);

        res = Solution.followPlaylist(5, 2);
        res = Solution.followPlaylist(5, 4);
        res = Solution.followPlaylist(5, 5);
        res = Solution.followPlaylist(5, 6);
        res = Solution.followPlaylist(5, 7);
        res = Solution.followPlaylist(5, 8);

        ArrayList<Integer> a1 = new ArrayList<Integer>();
        a1 = Solution.getSimilarUsers(1);
        assertEquals(2, a1.size()); // 2, 3

        ArrayList<Integer> a2 = new ArrayList<Integer>();
        a2 = Solution.getSimilarUsers(2);
        assertEquals(3, a2.size()); // 1, 3, 4

        ArrayList<Integer> a3 = new ArrayList<Integer>();
        a3 = Solution.getSimilarUsers(3);
        assertEquals(4, a3.size()); // 1, 2, 4, 5

        ArrayList<Integer> a4 = new ArrayList<Integer>();
        a4 = Solution.getSimilarUsers(4);
        assertEquals(3, a4.size()); // 2, 3, 5

        ArrayList<Integer> a5 = new ArrayList<Integer>();
        a5 = Solution.getSimilarUsers(5);
        assertEquals(3, a5.get(0).intValue()); // 3, 4
        assertEquals(2, a5.size()); // 3, 4
    }

    @Test
    public void recommandTest() {

        ReturnValue res;
        Playlist p1 = new Playlist();
        p1.setId(1);
        p1.setGenre("p");
        p1.setDescription("p");

        Playlist p2 = new Playlist();
        p2.setId(2);
        p2.setGenre("p");
        p2.setDescription("p");

        Playlist p3 = new Playlist();
        p3.setId(3);
        p3.setGenre("p");
        p3.setDescription("p");

        Playlist p4 = new Playlist();
        p4.setId(4);
        p4.setGenre("p");
        p4.setDescription("p");

        Playlist p5 = new Playlist();
        p5.setId(5);
        p5.setGenre("p");
        p5.setDescription("p");

        Playlist p6 = new Playlist();
        p6.setId(6);
        p6.setGenre("p");
        p6.setDescription("p");

        Playlist p7 = new Playlist();
        p7.setId(7);
        p7.setGenre("p");
        p7.setDescription("p");

        Playlist p8 = new Playlist();
        p8.setId(8);
        p8.setGenre("p");
        p8.setDescription("p");

        res = Solution.addPlaylist(p1);
        assertEquals(OK, res);

        res = Solution.addPlaylist(p2);
        assertEquals(OK, res);

        res = Solution.addPlaylist(p3);
        assertEquals(OK, res);

        res = Solution.addPlaylist(p4);
        assertEquals(OK, res);

        res = Solution.addPlaylist(p5);
        assertEquals(OK, res);

        res = Solution.addPlaylist(p6);
        assertEquals(OK, res);

        res = Solution.addPlaylist(p7);
        assertEquals(OK, res);

        res = Solution.addPlaylist(p8);
        assertEquals(OK, res);

        User u1 = new User();
        u1.setId(1);
        u1.setName("Nir");
        u1.setCountry("Israel");
        u1.setPremium(false);

        res = Solution.addUser(u1);
        assertEquals(OK, res);

        User u2 = new User();
        u2.setId(2);
        u2.setName("Nir");
        u2.setCountry("Israel");
        u2.setPremium(false);

        res = Solution.addUser(u2);
        assertEquals(OK, res);

        User u3 = new User();
        u3.setId(3);
        u3.setName("Nir");
        u3.setCountry("Israel");
        u3.setPremium(false);

        res = Solution.addUser(u3);
        assertEquals(OK, res);

        User u4 = new User();
        u4.setId(4);
        u4.setName("Nir");
        u4.setCountry("Israel");
        u4.setPremium(false);

        res = Solution.addUser(u4);
        assertEquals(OK, res);

        User u5 = new User();
        u5.setId(5);
        u5.setName("Nir");
        u5.setCountry("Israel");
        u5.setPremium(false);

        res = Solution.addUser(u5);
        assertEquals(OK, res);

        res = Solution.followPlaylist(1, 1);
        res = Solution.followPlaylist(1, 2);
        res = Solution.followPlaylist(1, 3);
        res = Solution.followPlaylist(1, 4);
        res = Solution.followPlaylist(1, 5);
        res = Solution.followPlaylist(1, 6);

        res = Solution.followPlaylist(2, 1);
        res = Solution.followPlaylist(2, 3);
        res = Solution.followPlaylist(2, 4);
        res = Solution.followPlaylist(2, 5);
        res = Solution.followPlaylist(2, 6);
        res = Solution.followPlaylist(2, 7);

        res = Solution.followPlaylist(3, 2);
        res = Solution.followPlaylist(3, 3);
        res = Solution.followPlaylist(3, 4);
        res = Solution.followPlaylist(3, 5);
        res = Solution.followPlaylist(3, 6);
        res = Solution.followPlaylist(3, 7);

        res = Solution.followPlaylist(4, 3);
        res = Solution.followPlaylist(4, 4);
        res = Solution.followPlaylist(4, 5);
        res = Solution.followPlaylist(4, 6);
        res = Solution.followPlaylist(4, 7);
        res = Solution.followPlaylist(4, 8);

        res = Solution.followPlaylist(5, 2);
        res = Solution.followPlaylist(5, 4);
        res = Solution.followPlaylist(5, 5);
        res = Solution.followPlaylist(5, 6);
        res = Solution.followPlaylist(5, 7);
        res = Solution.followPlaylist(5, 8);

        ArrayList<Integer> a1 = new ArrayList<Integer>();
        a1 = Solution.getPlaylistRecommendation(1);
        assertEquals(7, a1.get(0).intValue()); // 2, 3

        ArrayList<Integer> a2 = new ArrayList<Integer>();
        a2 = Solution.getPlaylistRecommendation(2);
        assertEquals(2, a2.get(0).intValue()); // 1, 3, 4

        ArrayList<Integer> a3 = new ArrayList<Integer>();
        a3 = Solution.getPlaylistRecommendation(3);
        assertEquals(1, a3.get(0).intValue()); // 1, 2, 4, 5
        assertEquals(8, a3.get(1).intValue()); // 1, 2, 4, 5
        assertEquals(2, a3.size());

        ArrayList<Integer> a4 = new ArrayList<Integer>();
        a4 = Solution.getPlaylistRecommendation(4);
        assertEquals(2, a4.get(0).intValue()); // 2, 3, 5

        ArrayList<Integer> a5 = new ArrayList<Integer>();
        a5 = Solution.getPlaylistRecommendation(5);
        assertEquals(3, a5.get(0).intValue()); // 3, 4
    }*/

    @Test
    public void topCountryTest() {

        ReturnValue res;
        Playlist p1 = new Playlist();
        p1.setId(1);
        p1.setGenre("p");
        p1.setDescription("p");

        Playlist p2 = new Playlist();
        p2.setId(2);
        p2.setGenre("p");
        p2.setDescription("p");

        Playlist p3 = new Playlist();
        p3.setId(3);
        p3.setGenre("p");
        p3.setDescription("p");

        Playlist p4 = new Playlist();
        p4.setId(4);
        p4.setGenre("p");
        p4.setDescription("p");

        Playlist p5 = new Playlist();
        p5.setId(5);
        p5.setGenre("p");
        p5.setDescription("p");

        res = Solution.addPlaylist(p1);
        assertEquals(OK, res);

        res = Solution.addPlaylist(p2);
        assertEquals(OK, res);

        res = Solution.addPlaylist(p3);
        assertEquals(OK, res);

        res = Solution.addPlaylist(p4);
        assertEquals(OK, res);

        res = Solution.addPlaylist(p5);
        assertEquals(OK, res);

        Song s1 = new Song();
        s1.setId(1);
        s1.setName("s");
        s1.setGenre("p");
        s1.setCountry("1");
        s1.setPlayCount(1);

        Song s2 = new Song();
        s2.setId(2);
        s2.setName("s");
        s2.setGenre("p");
        s2.setCountry("1");
        s2.setPlayCount(2);

        Song s3 = new Song();
        s3.setId(3);
        s3.setName("s");
        s3.setGenre("p");
        s3.setCountry("2");
        s3.setPlayCount(3);

        Song s4 = new Song();
        s4.setId(4);
        s4.setName("s");
        s4.setGenre("p");
        s4.setCountry("2");
        s4.setPlayCount(2);

        Song s5 = new Song();
        s5.setId(5);
        s5.setName("s");
        s5.setGenre("p");
        s5.setCountry("3");
        s5.setPlayCount(3);

        Song s6 = new Song();
        s6.setId(6);
        s6.setName("s");
        s6.setGenre("p");
        s6.setCountry("4");
        s6.setPlayCount(2);

        res = Solution.addSong(s1); assertEquals(OK, res);
        res = Solution.addSong(s2); assertEquals(OK, res);
        res = Solution.addSong(s3); assertEquals(OK, res);
        res = Solution.addSong(s4); assertEquals(OK, res);
        res = Solution.addSong(s5); assertEquals(OK, res);
        res = Solution.addSong(s6); assertEquals(OK, res);

        res = Solution.songPlay(1, 1); assertEquals(OK, res);
        res = Solution.songPlay(2, 2); assertEquals(OK, res);
        res = Solution.songPlay(3, 3); assertEquals(OK, res);
        res = Solution.songPlay(4, 2); assertEquals(OK, res);
        res = Solution.songPlay(5, 3); assertEquals(OK, res);
        res = Solution.songPlay(6, 2); assertEquals(OK, res);

        //1, 2, 3, 2, 3, 2
        res = Solution.addSongToPlaylist(1, 1); assertEquals(OK, res); // 6
        res = Solution.addSongToPlaylist(2, 1); assertEquals(OK, res);
        res = Solution.addSongToPlaylist(3, 1); assertEquals(OK, res);

        res = Solution.addSongToPlaylist(1, 2); assertEquals(OK, res); // 9
        res = Solution.addSongToPlaylist(3, 2); assertEquals(OK, res);
        res = Solution.addSongToPlaylist(4, 2); assertEquals(OK, res);
        res = Solution.addSongToPlaylist(5, 2); assertEquals(OK, res);

        res = Solution.addSongToPlaylist(2, 3); assertEquals(OK, res); // 7
        res = Solution.addSongToPlaylist(4, 3); assertEquals(OK, res);
        res = Solution.addSongToPlaylist(5, 3); assertEquals(OK, res);

        res = Solution.addSongToPlaylist(3, 4); assertEquals(OK, res); // 8
        res = Solution.addSongToPlaylist(5, 4); assertEquals(OK, res);
        res = Solution.addSongToPlaylist(6, 4); assertEquals(OK, res);

        res = Solution.addSongToPlaylist(2, 5); assertEquals(OK, res); // 4
        res = Solution.addSongToPlaylist(6, 5); assertEquals(OK, res);

        User u1 = new User();
        u1.setId(1);
        u1.setName("Nir");
        u1.setCountry("1");
        u1.setPremium(true);

        res = Solution.addUser(u1);
        assertEquals(OK, res);

        User u2 = new User();
        u2.setId(2);
        u2.setName("Nir");
        u2.setCountry("2");
        u2.setPremium(true);

        res = Solution.addUser(u2);
        assertEquals(OK, res);

        User u3 = new User();
        u3.setId(3);
        u3.setName("Nir");
        u3.setCountry("3");
        u3.setPremium(true);

        res = Solution.addUser(u3);
        assertEquals(OK, res);

        User u4 = new User();
        u4.setId(4);
        u4.setName("Nir");
        u4.setCountry("4");
        u4.setPremium(false);

        res = Solution.addUser(u4);
        assertEquals(OK, res);

        ArrayList<Integer> a1 = new ArrayList<Integer>();
        a1 = Solution.getTopCountryPlaylists(1);
        assertEquals(4, a1.size()); // 2, 3, 1, 5
        assertEquals(4, a1.get(0).intValue()); // 2, 3, 1, 5
        assertEquals(4, a1.get(4).intValue()); // 2, 3, 1, 5



        ArrayList<Integer> a2 = new ArrayList<Integer>();
        a2 = Solution.getTopCountryPlaylists(2);
        assertEquals(2, a2.get(0).intValue()); // 2, 3, 1

        ArrayList<Integer> a3 = new ArrayList<Integer>();
        a3 = Solution.getTopCountryPlaylists(3);
        assertEquals(2, a3.get(0).intValue()); // 2, 4, 3

        ArrayList<Integer> a4 = new ArrayList<Integer>();
        a4 = Solution.getTopCountryPlaylists(4);
        assertEquals(0, a4.size()); // nothing, user 4 is not premium
    }

    /*@Test
    public void topGenreTest() {

        ReturnValue res;
        Playlist p1 = new Playlist();
        p1.setId(1);
        p1.setGenre("g1");
        p1.setDescription("p");

        Playlist p2 = new Playlist();
        p2.setId(2);
        p2.setGenre("g1");
        p2.setDescription("p");

        Playlist p3 = new Playlist();
        p3.setId(3);
        p3.setGenre("g1");
        p3.setDescription("p");

        Playlist p4 = new Playlist();
        p4.setId(4);
        p4.setGenre("g2");
        p4.setDescription("p");

        Playlist p5 = new Playlist();
        p5.setId(5);
        p5.setGenre("g2");
        p5.setDescription("p");

        Playlist p6 = new Playlist();
        p6.setId(6);
        p6.setGenre("g3");
        p6.setDescription("p");

        res = Solution.addPlaylist(p1);
        assertEquals(OK, res);

        res = Solution.addPlaylist(p2);
        assertEquals(OK, res);

        res = Solution.addPlaylist(p3);
        assertEquals(OK, res);

        res = Solution.addPlaylist(p4);
        assertEquals(OK, res);

        res = Solution.addPlaylist(p5);
        assertEquals(OK, res);

        res = Solution.addPlaylist(p6);
        assertEquals(OK, res);

        Song s1 = new Song();
        s1.setId(1);
        s1.setName("s");
        s1.setGenre("g1");
        s1.setCountry("1");
        s1.setPlayCount(1);

        Song s2 = new Song();
        s2.setId(2);
        s2.setName("s");
        s2.setGenre("g1");
        s2.setCountry("1");
        s2.setPlayCount(2);

        Song s3 = new Song();
        s3.setId(3);
        s3.setName("s");
        s3.setGenre("g2");
        s3.setCountry("2");
        s3.setPlayCount(3);

        Song s4 = new Song();
        s4.setId(4);
        s4.setName("s");
        s4.setGenre("g2");
        s4.setCountry("2");
        s4.setPlayCount(2);

        Song s5 = new Song();
        s5.setId(5);
        s5.setName("s");
        s5.setGenre("g3");
        s5.setCountry("3");
        s5.setPlayCount(3);

        Song s6 = new Song();
        s6.setId(6);
        s6.setName("s");
        s6.setGenre("g3");
        s6.setCountry("4");
        s6.setPlayCount(2);

        res = Solution.addSong(s1); assertEquals(OK, res);
        res = Solution.addSong(s2); assertEquals(OK, res);
        res = Solution.addSong(s3); assertEquals(OK, res);
        res = Solution.addSong(s4); assertEquals(OK, res);
        res = Solution.addSong(s5); assertEquals(OK, res);
        res = Solution.addSong(s6); assertEquals(OK, res);

        res = Solution.songPlay(1, 1); assertEquals(OK, res);
        res = Solution.songPlay(2, 2); assertEquals(OK, res);
        res = Solution.songPlay(3, 3); assertEquals(OK, res);
        res = Solution.songPlay(4, 2); assertEquals(OK, res);
        res = Solution.songPlay(5, 3); assertEquals(OK, res);
        res = Solution.songPlay(6, 2); assertEquals(OK, res);

        res = Solution.addSongToPlaylist(1, 1); assertEquals(OK, res); // 6
        res = Solution.addSongToPlaylist(2, 1); assertEquals(OK, res);

        res = Solution.addSongToPlaylist(1, 2); assertEquals(OK, res); // 9

        res = Solution.addSongToPlaylist(2, 3); assertEquals(OK, res); // 7

        res = Solution.addSongToPlaylist(3, 4); assertEquals(OK, res); // 8
        res = Solution.addSongToPlaylist(4, 4); assertEquals(OK, res);

        res = Solution.addSongToPlaylist(4, 5); assertEquals(OK, res); // 4

        res = Solution.addSongToPlaylist(5, 6); assertEquals(OK, res); // 4

        User u1 = new User();
        u1.setId(1);
        u1.setName("Nir");
        u1.setCountry("1");
        u1.setPremium(true);

        res = Solution.addUser(u1);
        assertEquals(OK, res);

        User u2 = new User();
        u2.setId(2);
        u2.setName("Nir");
        u2.setCountry("2");
        u2.setPremium(true);

        res = Solution.addUser(u2);
        assertEquals(OK, res);

        User u3 = new User();
        u3.setId(3);
        u3.setName("Nir");
        u3.setCountry("3");
        u3.setPremium(true);

        res = Solution.addUser(u3);
        assertEquals(OK, res);

        res = Solution.followPlaylist(1, 2); assertEquals(OK, res);
        res = Solution.followPlaylist(2, 1); assertEquals(OK, res);
        res = Solution.followPlaylist(3, 5); assertEquals(OK, res);

        ArrayList<Integer> a1 = new ArrayList<Integer>();
        a1 = Solution.getSongsRecommendationByGenre(1, "g1");
        assertEquals(2, a1.get(0).intValue()); // 2
        assertEquals(1, a1.size()); // 2

        ArrayList<Integer> a2 = new ArrayList<Integer>();
        a2 = Solution.getSongsRecommendationByGenre(2, "g1");
        assertEquals(0, a2.size()); // nothing, user 2 followed both 1 and 2

        ArrayList<Integer> a3 = new ArrayList<Integer>();
        a3 = Solution.getSongsRecommendationByGenre(3, "g2");
        assertEquals(3, a3.get(0).intValue());

    }*/

}

