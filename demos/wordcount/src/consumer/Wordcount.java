package consumer;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import es.bsc.compss.api.COMPSs;
import model.Text;
import model.TextCollectionIndex;
import model.TextStats;
import storage.StorageItf;

public class Wordcount {
    /** Default session properties file. */
    public static String configPropertiesFile = "./cfgfiles/session.properties";
    /** Default execution times (to see cache effects). */
    public static int execTimes = 3;
    /** Variable for partial results. */
    public static TextStats[] partialResult;
    /** Default num of times that every text must be counted. */
    public static int timesPerText = 1;
    /** Whether to debug or not. */
    public static boolean doDebug = false;

    public static void main(String args[]) throws Exception {
	if (!checkArguments(args)) {
	    return;
	}

	String textColAlias = args[0];
	if (configPropertiesFile != null) {
	    StorageItf.init(configPropertiesFile);
	}

	// Init texts to parse
	TextCollectionIndex tc = TextCollectionIndex.getByAlias(textColAlias);
	Text[] textsToCount = totalTexts(tc, timesPerText);
	System.out.println(
		"[LOG] Obtained TextCollection: " + textColAlias + " with " + textsToCount.length + " texts to count.");
	partialResult = new TextStats[textsToCount.length];

	// Run wordcount N times
	for (int i = 0; i < execTimes; i++) {
	    System.out.println("[LOG] Computing result");
	    long startTime = System.currentTimeMillis();
	    TextStats finalResult = run(textsToCount);
	    COMPSs.barrier();
	    long endTime = System.currentTimeMillis();
	    if (finalResult == null) {
		System.err.println("[ERROR] Null result");
		break;
	    }
	    System.out.println("[TIMER] Execution " + i + " time: " + (endTime - startTime) + " ms");
	    printResult(finalResult);
	    endTime = System.currentTimeMillis();
	    System.out.println("[TIMER] Execution " + i + " time + result check: " + (endTime - startTime) + " ms");
	}

	if (configPropertiesFile != null) {
	    StorageItf.finish();
	}
    }

    /**
     * @brief Main run method
     * @param texts
     *            text object reference to be wordcounted
     * @return final stats
     * @throws Exception
     *             if an error occurs
     */
    public static TextStats run(final Text[] texts) throws Exception {
	// MAP-WORDCOUNT
	for (int i = 0; i < texts.length; i++) {
	    partialResult[i] = wordCountNewStats(texts[i]);
	}

	// REDUCE-MERGE
	LinkedList<Integer> q = new LinkedList<Integer>();
	for (int i = 0; i < texts.length; i++) {
	    q.add(i);
	}
	int x = 0;
	while (!q.isEmpty()) {
	    x = q.poll();
	    int y;
	    if (!q.isEmpty()) {
		y = q.poll();
		partialResult[x] = reduceTaskIN(partialResult[x], partialResult[y]);
		q.add(x);
	    }
	}
	return partialResult[x];
    }

    /**
     * @brief Wordcount given text
     * @param text
     *            a reference to persistent text object
     * @return resulting stats of wordcount
     */
    public static TextStats wordCountNewStats(Text text) {
	return text.wordCountMakingPersistent();
    }

    /**
     * @brief Reduce Task
     * @param m1
     *            a set of stats
     * @param m2
     *            a secondary set of stats
     * @return merged stats
     */
    public static TextStats reduceTaskIN(TextStats m1, TextStats m2) {
	m1.mergeWordCounts(m2);
	return m1;
    }

    /**
     * @brief print result of the wordcount
     * @param finalResult
     *            final stats
     */
    private static void printResult(final TextStats finalResult) {
	System.out.println("[LOG] Final result contains " + finalResult.getSize() + " unique words.");
	System.out.println("[LOG] Result summary: ");
	System.out.println(finalResult.getSummary(10));
    }

    /**
     * @brief Return an array of Text object references to be 'wordcounted'
     * @param tc
     *            index of texts
     * @param timesPerText
     *            repetitions per text
     * @return array of text references
     * @throws Exception
     */
    private static Text[] totalTexts(TextCollectionIndex tc, int timesPerText) throws Exception {
	ArrayList<String> textTitles = tc.getTextTitles();
	int actualTexts = textTitles.size();
	int totalTexts = actualTexts * timesPerText;
	Text[] result = new Text[totalTexts];
	int index = 0;
	for (String textTitle : textTitles) {
	    for (int j = 0; j < timesPerText; j++) {
		result[index] = (Text) Text.getByAlias(textTitle);
		index++;
	    }
	}
	return result;
    }

    /**
     * Check app arguments and initialize properties properly
     * 
     * @param args
     *            arguments of the application
     * @return false if arguments are wrong. true otherwise.
     */
    private static boolean checkArguments(String[] args) {
	if (args.length < 1) {
	    System.err.println("[ERROR] Bad arguments. " + getUsage());
	    return false;
	}
	for (int argIndex = 1; argIndex < args.length;) {
	    String arg = args[argIndex++];
	    if (arg.equals("-c")) {
		configPropertiesFile = args[argIndex++];
		File f = new File(configPropertiesFile);
		if (!f.exists() || f.isDirectory()) {
		    System.err.println("Bad argument. Config file: " + configPropertiesFile + " does not exist.");
		    return false;
		}
	    } else if (arg.equals("-t")) {
		timesPerText = new Integer(args[argIndex++]);
	    } else if (arg.equals("-h")) {
		System.out.println("[HELP] " + getUsage());
		return false;
	    } else if (arg.equals("-debug")) {
		doDebug = true;
	    } else if (arg.equals("-exectimes")) {
		execTimes = new Integer(args[argIndex++]);
	    } else {
		System.err.println("[ERROR] Bad arguments. " + getUsage());
		return false;
	    }
	}
	return true;
    }

    /**
     * Retrieves app's usage.
     * 
     * @return app's usage.
     */
    private static String getUsage() {
	return "Usage \n\n" + Wordcount.class.getName() + " <text_col_alias> [ -c <config_properties> ] "
		+ "[-t <times_per_text>] [-exectimes <run_n_times>] " + "[-debug (extra info)] [-h (this help)] \n";
    }
}
