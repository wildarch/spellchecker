
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author daan
 */
public class SpellCorrectorTest {

    public String[] testPhrases1 = new String[]{
        "this assay allowed us to measure a wide variety of conditions",
        "this assay allowed us to measure a wide variety of conitions",
        "this assay allowed us to meassure a wide variety of conditions",
        "this assay allowed us to measure a wide vareity of conditions"
    };
    
    public String[] testPhrases2 = new String[]{
        "at the home locations there were traces of water",
        "at the hme locations there were traces of water",
        "at the hoome locations there were traces of water",
        "at the home locasions there were traces of water"
    };

    public String[] testPhrases3 = new String[] {
        "the development of diabetes is present in mice that carry a transgene",
        "the development of diabetes is present in moce that carry a transgene",
        "the development of idabetes is present in mice that carry a transgene",
        "the development of diabetes us present in mice that harry a transgene"
    };
    
    public String[] testPhrases4 = new String[] {
        "boxing gloves shield the knuckles not the head",
        "boxing loves shield the knuckles nots the head",
        "boing gloves shield the knuckles nut the head",
    };
    
    public String[] testPhrases6 = new String[] {
        "she still refers to me as a friend but i feel i am treated quite badly",
        "she still refers to me as a fiend but i feel i am treated quite badly",
        "she still refers to me has a friend but i fel i am treated quite badly",
        "she still refers to me as a friendd but i feel i am traeted quite badly",
    };
    
    public String[] testPhrases7 = new String[] {
        "a response may be any measurable biological parameter that is correlated with the toxicant",
        "a responses may be any measurable biological parameter that is correlated with the toxicant"
    };
    
    public String[] testPhrases8 = new String[] {
        "essentially there has been no change in japan",
        "essentially here has bien no change in japan",
    };
    
    public String[] testPhrases9 = new String[] {
        "ancient china was one of the longest lasting societies in the history of the world",
        "ancient china was one of the longst lasting societies iin the history of the world"
    };
    
    public String[] testPhrases10 = new String[] {
        "playing in the national football league was my dream",
        "laying in the national footbal league was my dream"
    };
    

    /**
     * Test of correctPhrase method, of class SpellCorrector.
     */
    private void testCorrectPhrase(String[] phrases) {
        try {
            CorpusReader cr = new CorpusReader();
            ConfusionMatrixReader cmr = new ConfusionMatrixReader();
            SpellCorrector sc = new SpellCorrector(cr, cmr);
            for(String s : phrases) {
                assertEquals(phrases[0], sc.correctPhrase(s));
            }
        } catch (IOException ex) {
            Logger.getLogger(SpellCorrectorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void test1() {
        testCorrectPhrase(testPhrases1);
    }
    
    @Test
    public void test2() {
        testCorrectPhrase(testPhrases2);
    }
    
    @Test
    public void test3() {
        testCorrectPhrase(testPhrases3);
    }
    
    @Test
    public void test4() {
        testCorrectPhrase(testPhrases4);
    }
    
    /*
    @Test
    public void test5() {
        testCorrectPhrase(testPhrases5);
    }
    */

    @Test
    public void test6() {
        testCorrectPhrase(testPhrases6);
    }
    
    @Test
    public void test7() {
        testCorrectPhrase(testPhrases7);
    }
    
    @Test
    public void test8() {
        testCorrectPhrase(testPhrases8);
    }
    
    @Test
    public void test9() {
        testCorrectPhrase(testPhrases9);
    }
    
    @Test
    public void test10() {
        testCorrectPhrase(testPhrases10);
    }

}
