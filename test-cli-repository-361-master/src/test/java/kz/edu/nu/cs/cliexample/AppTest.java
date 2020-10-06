package kz.edu.nu.cs.cliexample;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AppTest {
    
    @Test
    public void reader_test_1() {
        int words = App.wordcount("The dog barked.");
        assertEquals("for The Dog Barked words", 3, words);
    }
    
    @Test
    public void reader_test_2() {
        int words = App.wordcount("The dog did not bark.");
        assertEquals("if The dog did not bark, words", 5, words);
    }
    
    @Test
    public void reader_test_3() {
        int words = App.wordcount(" ");
        assertEquals("if empty words", 0, words);
    }
    
}
