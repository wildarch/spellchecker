
import java.util.Formatter;


/**
 *
 * @author daan
 */
public class Probability {
    public String word;
    public double typoProbability;
    public double wordProbability;
    public String previous;
    public double smoothedCount;
    
    public Probability(String w, double tp, double wo, String p, double sc) {
        this.word = w;
        this.typoProbability = tp;
        this.wordProbability = wo;
        this.previous = p;
        this.smoothedCount = sc;
    }
    
    @Override
    public String toString() {
        Formatter f = new Formatter();
        return f.format("%s: (typo: %f, word: %f, prev: %s, smoothedCount: %f)", word, typoProbability, wordProbability, previous, smoothedCount).toString();
    }
    
    public double probability(double lambda) {
        return typoProbability * Math.pow(wordProbability, lambda) * smoothedCount;
    }
}
