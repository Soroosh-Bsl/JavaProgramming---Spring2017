import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class HammamTestKianoosh {

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExeptionGetMin() {
        Hammam<Comparable> hammam = new Hammam();
        hammam.getMin();
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExeptionGetLast() {
        Hammam<Comparable> hammam = new Hammam();
        hammam.getLast(true);
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExeptionGetFirst() {
        Hammam<Comparable> hammam = new Hammam();
        hammam.getFirst(false);
    }

    @Test
    public void getMin() throws Exception {
        //Double
        Hammam<String> hammam = new Hammam<>();
        hammam.add("abbas");
        hammam.add("melika");
        hammam.add("narges");
        hammam.add("helia");
        hammam.add("pnane");
        assertEquals("abbas", hammam.getMin());
        assertEquals("helia", hammam.getMin());

    }

    @Test
    public void getLast() throws Exception {
        Hammam<Double> hammam = new Hammam<>();
        hammam.add(new Double(2.17));
        hammam.add(new Double(2.26));
        hammam.add(new Double(3.1));
        hammam.add(new Double(4.12));
        assertEquals(new Double(4.12), hammam.getLast(true));
        assertEquals(new Double(3.1), hammam.getLast(true));
    }

    @Test
    public void getFirst() throws Exception {
        Hammam<Integer> hammam = new Hammam<>();
        hammam.add(new Integer(1));
        hammam.add(new Integer(2));
        hammam.add(new Integer(3));
        hammam.add(new Integer(4));
        assertEquals(new Integer(4), hammam.getLast(true));
        assertEquals(new Integer(3), hammam.getLast(false));
        assertEquals(new Integer(3), hammam.getLast(false));
    }

    @Test
    public void getLess() throws Exception {
        Double russians = new Double(6.3);
        Double independenceDay = new Double(7.04);
        Double twinTowers = new Double(9.11);
        Double vForVendetta = new Double(11.5);

        Hammam<Double> hammam = new Hammam<>();
        hammam.add(twinTowers);
        hammam.add(independenceDay);
        hammam.add(russians);
        hammam.add(vForVendetta);
        Comparable[] answer = hammam.getLess(new Double(8.0), true);
        assertTrue(Arrays.asList(answer).contains(independenceDay));
        assertEquals(twinTowers, hammam.getMin());
    }

    @Test
    public void getRecentlyRemoved() throws Exception {

        Hammam<String> hammam = new Hammam<>();
        hammam.add("Joey");
        hammam.add("Pheobe");
        hammam.add("Eragon");
        hammam.add("Lily");
        hammam.add("Swirly");
        hammam.getMin();
        assertEquals("Eragon", hammam.getRecentlyRemoved(1)[0]);

        hammam.getMin();
        assertEquals("Eragon", hammam.getRecentlyRemoved(2)[1]);

        hammam.getMin();
        assertEquals("Eragon", hammam.getRecentlyRemoved(7)[2]);
    }


}
