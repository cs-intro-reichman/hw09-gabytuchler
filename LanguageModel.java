import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    HashMap<String, List> CharDataMap;
    int windowLength;
    private Random randomGenerator;

    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        this.randomGenerator = new Random(seed);
        this.CharDataMap = new HashMap<>();
    }

    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        this.randomGenerator = new Random();
        this.CharDataMap = new HashMap<>();
    }

    /** Builds a language model from the text in the given file (circular training). */
    public void train(String fileName) {
    In in = new In(fileName);
    String corpus = in.readAll();

    int n = corpus.length();

    corpus = corpus + corpus.substring(0, windowLength);

    for (int i = 0; i < n; i++) {
        String window = corpus.substring(i, i + windowLength);
        char nextChar = corpus.charAt(i + windowLength);

        List probs = CharDataMap.get(window);
        if (probs == null) {
            probs = new List();
            CharDataMap.put(window, probs);
        }
        probs.update(nextChar);
    }
}


    /** Computes probabilities (p and cp) */
    public void calculateProbabilities(List probs) {
        int total = 0;
        ListIterator it = probs.listIterator(0);
        while (it.hasNext()) {
            total += it.next().count;
        }

        double cumulative = 0.0;
        it = probs.listIterator(0);
        while (it.hasNext()) {
            CharData cd = it.next();
            cd.p = (double) cd.count / total;
            cumulative += cd.p;
            cd.cp = cumulative;
        }
    }

    /** Returns random char according to probabilities */
    public char getRandomChar(List probs) {
        double r = randomGenerator.nextDouble();
        ListIterator it = probs.listIterator(0);

        while (it.hasNext()) {
            CharData cd = it.next();
            if (cd.cp >= r) {
                return cd.chr;
            }
        }
        return probs.get(probs.getSize() - 1).chr;
    }

    /** Generates random text */
    public String generate(String initialText, int textLength) {
        if (initialText.length() < windowLength) {
            return initialText;
        }

        String generatedText = initialText;

        while (generatedText.length() < textLength) {
            String window = generatedText.substring(
                    generatedText.length() - windowLength
            );

            List probs = CharDataMap.get(window);
            if (probs == null) {
                break;
            }

            calculateProbabilities(probs);
            char nextChar = getRandomChar(probs);
            generatedText += nextChar;
        }

        return generatedText;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String key : CharDataMap.keySet()) {
            sb.append(key)
              .append(" : ")
              .append(CharDataMap.get(key))
              .append("\n");
        }
        return sb.toString();
    }


    public static void main(String[] args) {
    int windowLength = 2;
    String fileName = "tinyshakespeare.txt"; 
    
    LanguageModel lm = new LanguageModel(windowLength, 20);
    
    lm.train(fileName);
    

    System.out.println("--- Model Statistics ---");
    System.out.println(lm);
    
    System.out.println("--- Generated Text ---");
    String initialText = "th"; 
    int generatedTextLength = 50;
    
    String result = lm.generate(initialText, generatedTextLength);
    System.out.println(result);
}    }

