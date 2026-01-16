import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    // The map of this model.
    // Maps windows to lists of charachter data objects.
    HashMap<String, List> CharDataMap;
    
    // The window length used in this model.
    int windowLength;
    
    // The random number generator used by this model. 
	private Random randomGenerator;

    /** Constructs a language model with the given window length and a given
     *  seed value. Generating texts from this model multiple times with the 
     *  same seed value will produce the same random texts. Good for debugging. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
	public void train(String fileName) {
    In in = new In(fileName);
    String corpus = in.readAll();
    for (int i = 0; i <= corpus.length() - windowLength - 1; i++) {
        // גזירת החלון (למשל "he")
        String window = corpus.substring(i, i + windowLength);
        // האות שבאה מיד אחריו (למשל 'l')
        char nextChar = corpus.charAt(i + windowLength);

        // שלב 3: בדיקה במפה
        List probs = CharDataMap.get(window);
        if (probs == null) {
            probs = new List();
            CharDataMap.put(window, probs);
        }
                probs.update(nextChar);
    }	}

    // Computes and sets the probabilities (p and cp fields) of all the
	// characters in the given list. */
	void calculateProbabilities(List probs) {	
        int count = 0;
    ListIterator it = probs.listIterator(0);
    while (it.hasNext()) {
        count += it.next().count;
    }
    double newcp = 0.0;
    it = probs.listIterator(0);
    while (it.hasNext()) {
        CharData current = it.next();
        current.p = (double) current.count / count;
        newcp += current.p;
        current.cp = newcp;
    }        

	}

    // Returns a random character from the given probabilities list.
	char getRandomChar(List probs) {
        double r = randomGenerator.nextDouble();
        ListIterator it = probs.listIterator(0);
    while (it.hasNext()) {
        CharData current = it.next();
            if (current.cp > r) {
            return current.chr;
        }
    }
    return probs.get(probs.getSize() - 1).chr;
	}

    /**
	 * Generates a random text, based on the probabilities that were learned during training. 
	 * @param initialText - text to start with. If initialText's last substring of size numberOfLetters
	 * doesn't appear as a key in Map, we generate no text and return only the initial text. 
	 * @param numberOfLetters - the size of text to generate
	 * @return the generated text
	 */
	public String generate(String initialText, int textLength) {
    if (initialText.length() < windowLength) {
        return initialText;
    }
    String generatedText = initialText;
        while (generatedText.length() < textLength) {
        String currentWindow = generatedText.substring(generatedText.length() - textLength);
        List probs = CharDataMap.get(currentWindow);
                if (probs == null) {
            break;
        }
        calculateProbabilities(probs);
        char nextChar = getRandomChar(probs);
        generatedText = generatedText + nextChar;
    }
    
    return generatedText.toString();
}
	

    /** Returns a string representing the map of this language model. */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (String key : CharDataMap.keySet()) {
			List keyProbs = CharDataMap.get(key);
			str.append(key + " : " + keyProbs + "\n");
		}
		return str.toString();
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

