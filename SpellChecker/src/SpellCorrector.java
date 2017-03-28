import java.util.Map;
import java.util.Map.Entry;

public class SpellCorrector {
    final private CorpusReader cr;
    final private ConfusionMatrixReader cmr;
    
    public SpellCorrector(CorpusReader cr, ConfusionMatrixReader cmr) 
    {
        this.cr = cr;
        this.cmr = cmr;
    }
    
    public String correctPhrase(String phrase)
    {
        if(phrase == null || phrase.length() == 0)
        {
            throw new IllegalArgumentException("phrase must be non-empty.");
        }
            
        String[] words = phrase.split(" ");
        String finalSuggestion = "";
        
        // TODO
        
        
        return finalSuggestion.trim();
    }    
      
    /** returns a map with candidate words and their noisy channel probability. **/
    public Map<String,Double> getCandidateWords(String typo)
    {
        return new WordGenerator(cr,cmr).getCandidateCorrections(typo);
    }
    
    /** Returns the best candidate word from getCandidateWords. **/
    public String getBestCandidateWord(String word) {
        // TODO what happens if there are no candidate words? 
        // return word itself?
        Entry<String, Double> best = null;
        for (Entry<String, Double> e : getCandidateWords(word).entrySet()) {
            if (best == null || e.getValue() > best.getValue()) {
                best = e;
            }
        }
        if (best == null) {
            return word;
        }
        else {
            return best.getKey();
        }
    }
}