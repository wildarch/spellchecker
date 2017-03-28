
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

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

    /**
     * Test of correctPhrase method, of class SpellCorrector.
     */
    @Test
    public void testCorrectPhrase1() {
        try {
            CorpusReader cr = new CorpusReader();
            ConfusionMatrixReader cmr = new ConfusionMatrixReader();
            SpellCorrector sc = new SpellCorrector(cr, cmr);
            for(String s : testPhrases1) {
                assertEquals(testPhrases1[0], sc.correctPhrase(s));
            }
        } catch (IOException ex) {
            Logger.getLogger(SpellCorrectorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testCorrectPhrase2() {
        try {
            CorpusReader cr = new CorpusReader();
            ConfusionMatrixReader cmr = new ConfusionMatrixReader();
            SpellCorrector sc = new SpellCorrector(cr, cmr);
            for(String s : testPhrases2) {
                assertEquals(sc.correctPhrase(s), testPhrases2[0]);
            }
        } catch (IOException ex) {
            Logger.getLogger(SpellCorrectorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testCorrectPhrase3() {
        try {
            CorpusReader cr = new CorpusReader();
            ConfusionMatrixReader cmr = new ConfusionMatrixReader();
            SpellCorrector sc = new SpellCorrector(cr, cmr);
            for(String s : testPhrases3) {
                assertEquals(testPhrases3[0], sc.correctPhrase(s));
            }
        } catch (IOException ex) {
            Logger.getLogger(SpellCorrectorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
