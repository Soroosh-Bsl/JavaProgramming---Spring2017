import org.junit.Test;

import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HammamTest {

    Hammam<String> hammam = new Hammam<>();

   @Test
    public void getMin(){
       Hammam<String> hammam = new Hammam<>();
       hammam.add("Ali");
       hammam.add("Hassan");
       hammam.add("Sina");
       hammam.add("Farzane");
       hammam.add("Ghasem");
       hammam.add("Reza");

       assertEquals("Ali", hammam.getMin());
    }
    @Test
    public void getMinEx(){
        Hammam<String> hammam = new Hammam<>();
        boolean catched = false;

        try{
            hammam.getMin();
        }catch (IllegalStateException e){
            assertEquals("", "");
            catched = true;
        }

        if (!catched){
            assertEquals("" ,"A");
        }
    }

    @Test
    public void getLastNoRemove(){
        Hammam<String> hammam = new Hammam<>();
        hammam.add("Ali");
        hammam.add("Hassan");
        hammam.add("Sina");
        hammam.add("Farzane");
        hammam.add("Ghasem");
        hammam.add("Reza");

        assertEquals("Reza", hammam.getLast(false));
    }

    @Test
    public void getLastRemove(){
        Hammam<String> hammam = new Hammam<>();
        hammam.add("Ali");
        hammam.add("Hassan");
        hammam.add("Sina");
        hammam.add("Farzane");
        hammam.add("Ghasem");
        hammam.add("Reza");

        hammam.getLast(true);
        assertEquals("Ghasem", hammam.getLast(false));

    }

    @Test
    public void getLastEx(){
        Hammam<String> hammam = new Hammam<>();
        boolean catched = false;
        try{
            hammam.getLast(false);
        }catch (IllegalStateException e){
            assertEquals("", "");
            catched = true;
        }

        if (!catched){
            assertEquals("" ,"A");
        }
    }

    @Test
    public void getFirstNoRemove(){
        Hammam<String> hammam = new Hammam<>();
        hammam.add("Ali");
        hammam.add("Hassan");
        hammam.add("Sina");
        hammam.add("Farzane");
        hammam.add("Ghasem");
        hammam.add("Reza");

        assertEquals("Ali", hammam.getFirst(false));
    }

    @Test
    public void getFirstRemove(){
        Hammam<String> hammam = new Hammam<>();
        hammam.add("Ali");
        hammam.add("Hassan");
        hammam.add("Sina");
        hammam.add("Farzane");
        hammam.add("Ghasem");
        hammam.add("Reza");

        hammam.getFirst(true);
        assertEquals("Hassan", hammam.getFirst(false));

    }

    @Test
    public void getFirstEx(){
        Hammam<String> hammam = new Hammam<>();
        boolean catched = false;
        try{
            hammam.getFirst(false);
        }catch (IllegalStateException e){
            assertEquals("", "");
            catched = true;
        }
        if (!catched){
            assertEquals("" ,"A");
        }
    }

    @Test
    public void getLess(){
        Hammam<String> hammam = new Hammam<>();

        hammam.add("Ali");
        hammam.add("Hassan");
        hammam.add("Sina");
        hammam.add("Farzane");
        hammam.add("Ghasem");
        hammam.add("Reza");

        Comparable[] results = hammam.getLess("Hassan", false);
        if (results[0].equals("Ali") && results[1].equals("Farzane") && results[2].equals("Ghasem")){
            try{
                results[4] = "";
            }catch (ArrayIndexOutOfBoundsException e){
                assertEquals("", "");
            }
        }

        else {
            assertEquals("" ,"A");

        }

    }

    @Test
    public void getLessEx(){
        Hammam<String> hammam = new Hammam<>();
        hammam.add("Ali");
        Comparable[] results = hammam.getLess("Ali", false);

        if (results.length == 0){
            assertEquals("", "");
        }
        else {
            assertEquals("" ,"A");
        }
    }

    @Test
    public void getLessWithRemove(){
        Hammam<String> hammam = new Hammam<>();

        hammam.add("Ali");
        hammam.add("Hassan");
        hammam.add("Sina");
        hammam.add("Farzane");
        hammam.add("Ghasem");
        hammam.add("Reza");

        hammam.getLess("Hassan", true);
        Comparable[] results = hammam.getLess("Reza", false);


        if (results[0].equals("Hassan") && results[1].equals("Ghasem")){
            try{
                results[2] = "";
            }catch (ArrayIndexOutOfBoundsException e){
                assertEquals("", "");
            }
        }
        else {
            assertEquals("" ,"A");
        }
    }

    @Test
    public void getRecentlyRemoved(){
        Hammam<String> hammam = new Hammam<>();

        hammam.add("Ali");
        hammam.add("Hassan");
        hammam.add("Sina");
        hammam.add("Farzane");
        hammam.add("Ghasem");
        hammam.add("Reza");

        hammam.getLess("Hassan", true);
        Comparable[] results = hammam.getRecentlyRemoved(2);

        if (results[0].equals("Ali") && results[1].equals("Farzane")){
            try {
                results[2] = null;
            }catch (ArrayIndexOutOfBoundsException e){
                assertEquals("", "");
            }
        }
        else {
            assertEquals("", "A");
        }

    }
}