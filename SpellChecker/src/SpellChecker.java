import java.io.IOException;
import java.util.Scanner;

public class SpellChecker {

    public static boolean inPeach = false; // set this to true if you submit to peach!!!
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        
        try {
            CorpusReader cr = new CorpusReader();
            ConfusionMatrixReader cmr = new ConfusionMatrixReader();
            SpellCorrector sc = new SpellCorrector(cr, cmr);
            if (inPeach) {
                peachTest(sc);
            } else {
                nonPeachTest(sc);
            }
        } catch (IOException ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
    }
    
    static void nonPeachTest(SpellCorrector sc) throws IOException { 
            String[] sentences = {
                "at the hme locations there were traces of water",
                "this assay allowed us to measure a wide variety of conditions",
                "the development of diabetes is present in mice that carry a transgene"
            };
            
            for(String sentence: sentences) {
                System.out.println("Input : " + sentence);
                String result=sc.correctPhrase(sentence);
                System.out.println("Answer: " +result);
                System.out.println();
            }
    }
    
    static void peachTest(SpellCorrector sc) throws IOException {
            Scanner input = new Scanner(System.in);
            String sentence = input.nextLine();
            System.out.println("Answer: " + sc.correctPhrase(sentence));  
    } 
}