import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

public class SpellCorrector {
    final private CorpusReader cr;
    final private ConfusionMatrixReader cmr;
    
    final private static double NO_ERROR = 0.90;
    final private static double LAMBDA = 0.5;
    
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
        finalSuggestion = words[0] + " ";
        for (int i = 1; i < words.length; i++) {
            String word = words[i];
            Map<String,Double> candidates = getCandidateWords(word);
            if (cr.inVocabulary(word)) {
                candidates.put(word, NO_ERROR);
            }
            /*
            for(Entry<String, Double> e : candidates.entrySet()) {
                e.setValue(e.getValue() + wordProbability(e.getKey()));
            }
            
            
            if (i > 0) {
                for (Entry<String, Double> e : candidates.entrySet()) {
                    System.out.println(words[i-1] + " " + e.getKey() + ": " + cr.getSmoothedCount(words[i-1] + " " + e.getKey()));
                    e.setValue(
                        cr.getSmoothedCount(words[i-1] + " " + e.getKey()) + 
                            e.getValue()
                    );
                }
            }
            
            words[i] = candidates.entrySet().stream()
                .max((o1, o2) -> o1.getValue().compareTo(o2.getValue()))
                .get().getKey();
            */
            for (Entry<String, Double> e : candidates.entrySet()) {
                String candidate = e.getKey();
                Probability p = new Probability(
                    candidate, 
                    e.getValue(), 
                    wordProbability(candidate), 
                    (i > 0)? words[i-1] : null, 
                    (i > 0)? cr.getSmoothedCount(words[i-1] + " " + candidate) : 1
                );
                System.out.println(p + "Prob: " + p.probability(LAMBDA));
                e.setValue(p.probability(LAMBDA));
            }
            words[i] = candidates.entrySet().stream()
                .max((o1, o2) -> o1.getValue().compareTo(o2.getValue()))
                .get().getKey();
            finalSuggestion += words[i] + " ";
            
            System.out.println("Word: "+word+". Candidates:");
            //candidates.entrySet().forEach((e) -> System.out.println(e));
        }
        return finalSuggestion.trim();
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