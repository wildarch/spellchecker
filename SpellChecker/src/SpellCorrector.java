import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

public class SpellCorrector {
    final private CorpusReader cr;
    final private ConfusionMatrixReader cmr;
    
    final private static double NO_ERROR = 0.8;
    final private static double LAMBDA = 0.3;
    
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
        
        words = doPass(words);
        for (String w : words) {
            finalSuggestion += w + " ";
        }
        
        return finalSuggestion.trim();
    }

    public String[] doPass(String[] words) {
        
        for (int i = 0; i < words.length; i++) {
            // The current word is not the first in the phrase
            boolean hasPrevious = i > 0;
            // The current word is not the last in the phrase
            boolean hasNext = i < (words.length - 1);
            
            String previous = null;
            String next = null;
            String word = words[i];
            if (hasPrevious) {
                previous = words[i-1];
            }
            if (hasNext) {
                next = words[i+1];
                if (!cr.inVocabulary(next)) { 
                    // 2 consecutive errors not allowed, so this word is correct
                    words[i] = word;
                    continue;
                }
            }

            Map<String,Double> candidates = getCandidateWords(word);
            // If the word is in the vocabulary, it is also an option to consider
            if (cr.inVocabulary(word)) {
                candidates.put(word, NO_ERROR);
            }
            for (Entry<String, Double> e : candidates.entrySet()) {
                if (e.getValue() == 0.0) {
                    //if the typo probability is 0, set it to an extremely small value
                    //since it can still be the correct word (0 makes the total probability 0)
                    ///having a typo probability of 0 does not ensure it cannot be correct
                    e.setValue(0.00001);
                }
                String candidate = e.getKey();
                // Holds the combination of this word with either 
                // the previous or next word
                String comboPrevious = null;
                String comboNext = null;
                if (hasPrevious) {
                    comboPrevious = previous + " " + candidate;
                }
                if (hasNext) {
                    comboNext = candidate + " " + next;
                }
                
                // Construct a probability record class to keep track of all relevant data
                // This makes debugging much simpler
                Probability p = new Probability(
                    candidate, 
                    e.getValue(), 
                    cr.uniGramProbability(candidate), 
                    hasPrevious ? previous : null, 
                    hasNext ? next : null, 
                    hasPrevious ? cr.getSmoothedCount(comboPrevious) : 1,
                    hasNext ? cr.getSmoothedCount(comboNext) : 1
                );
                if (!SpellChecker.inPeach) System.out.println(p + "Prob: " + p.probability(LAMBDA));
                e.setValue(p.probability(LAMBDA));
            }
            
            // Get the word with the highest total probability
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
}
