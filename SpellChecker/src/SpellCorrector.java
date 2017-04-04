import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

public class SpellCorrector {
    final private CorpusReader cr;
    final private ConfusionMatrixReader cmr;
    
    final private static double NO_ERROR = 0.9;
    final private static double LAMBDA = 2.5;
    
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
        
        words = doPass(words, true);
        //words = doPass(words, false);
        for (String w : words) {
            finalSuggestion += w + " ";
        }
        
        return finalSuggestion.trim();
    }

    public String[] doPass(String[] words, boolean ltr) {
        
        int i = ltr ? 0 : words.length -1;
        for (; i >= 0 && i < words.length; i+= ltr ? 1 : -1) {
            boolean hasOther = ((i > 0 && ltr) || ((i < words.length-1) && !ltr));
            String other = null;
            String word = words[i];
            if (hasOther) {
                other = ltr ? words[i-1] : words[i+1];
            }

            Map<String,Double> candidates = getCandidateWords(word);
            if (cr.inVocabulary(word)) {
                candidates.put(word, NO_ERROR);
            }
            for (Entry<String, Double> e : candidates.entrySet()) {
                String candidate = e.getKey();
                String combo = null;
                if (hasOther) {
                    combo = ltr ? other + " " + candidate : candidate + " " + other;
                }
                Probability p = new Probability(
                    candidate, 
                    e.getValue(), 
                    wordProbability(candidate), 
                    hasOther ? other : null, 
                    hasOther ? cr.getSmoothedCount(combo) : 1
                );
                if (!SpellChecker.inPeach) System.out.println(p + "Prob: " + p.probability(LAMBDA));
                e.setValue(p.probability(LAMBDA));
            }
            words[i] = candidates.entrySet().stream()
                .max((o1, o2) -> o1.getValue().compareTo(o2.getValue()))
                .get().getKey();
        }
        return words;
    }
      
    /** returns a map with candidate words and their noisy channel probability. **/
    public Map<String,Double> getCandidateWords(String typo)
    {
        return new WordGenerator(cr,cmr).getCandidateCorrections(typo);
    }
    
    private double wordProbability(String word) {
        return (cr.getNGramCount(word)+1) * LAMBDA;
    }
}
