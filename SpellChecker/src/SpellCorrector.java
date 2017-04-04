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
        //words = doPass(words, false);
        for (String w : words) {
            finalSuggestion += w + " ";
        }
        
        return finalSuggestion.trim();
    }

    public String[] doPass(String[] words) {
        
        for (int i = 0; i < words.length; i++) {
            boolean hasPrevious = i > 0;
            boolean hasNext = i < (words.length - 1);
            
            String previous = null;
            String next = null;
            String word = words[i];
            if (hasPrevious) {
                previous = words[i-1];
            }
            if (hasNext) {
                next = words[i+1];
                if (!cr.inVocabulary(next)) { //2 consecutive errors not allowed
                    words[i] = word;
                    continue;
                }
            }

            Map<String,Double> candidates = getCandidateWords(word);
            if (cr.inVocabulary(word)) {
                candidates.put(word, NO_ERROR);
            }
            for (Entry<String, Double> e : candidates.entrySet()) {
                if (e.getValue() == 0.0) {
                    e.setValue(0.00001);
                }
                String candidate = e.getKey();
                String comboPrevious = null;
                String comboNext = null;
                if (hasPrevious) {
                    comboPrevious = previous + " " + candidate;
                }
                if (hasNext) {
                    comboNext = candidate + " " + next;
                }
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
