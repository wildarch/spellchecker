
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
    public String next;
    public double smoothedCountPrevious;
    public double smoothedCountNext;
    
    public Probability(String w, double tp, double wo, String p, String n, double scp, double scn) {
        this.word = w;
        this.typoProbability = tp;
        this.wordProbability = wo;
        this.previous = p;
        this.next = n;
        this.smoothedCountPrevious = scp;
        this.smoothedCountNext = scn;
    }
    
    @Override
    public String toString() {
        Formatter f = new Formatter();
        return f.format("%s: (typo: %f, word: %f, prev: %s, next: %s, smoothedCountPrevious: %f, smoothedCountNext: %f)", word, (-1.0 / Math.log(typoProbability*0.8)), wordProbability, previous, next, smoothedCountPrevious, smoothedCountNext).toString();
    }
    
    public double probability(double lambda) {
<<<<<<< HEAD
        return (-1.0 / Math.log(typoProbability*0.8)) * Math.pow(wordProbability, lambda) * smoothedCountPrevious * smoothedCountNext;
=======
        //double kneserNey = smoothedCountPrevious
        return typoProbability * Math.pow(wordProbability * smoothedCountPrevious * smoothedCountNext, lambda);
>>>>>>> 77c06be64a30edd18b447f62da5093ef66c0ef8a
    }
}
