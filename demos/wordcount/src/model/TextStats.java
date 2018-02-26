package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;

import dataclay.DataClayObject;

@SuppressWarnings("serial")
public class TextStats extends DataClayObject implements Serializable {
	HashMap<String, Integer> currentWordCount;
	boolean debug;

	public TextStats() {
		currentWordCount = new HashMap<String, Integer>();
		debug = false;
		// System.out.println("[ TextStats ] Call to empty constructor (for COMPSs, to fill results later)");
	}

	public TextStats(HashMap<String, Integer> newWordCount, final boolean doDebug) {
		currentWordCount = new HashMap<String, Integer>();
		currentWordCount.putAll(newWordCount);
		debug = doDebug;
		// System.out.println("[ TextStats ] Call to constructor for wordcount");
	}

	public void setCurrentWordCount(HashMap<String, Integer> newWordCount) {
		currentWordCount.putAll(newWordCount);
	}

	public HashMap<String, Integer> getCurrentWordCount() {
		return currentWordCount;
	}

	public int getSize() {
		return currentWordCount.size();
	}
	
	public boolean getDebug() {
		return debug;
	}
	
	public void setDebug() {
		debug = true;
	}

	public void mergeWordCounts(final TextStats newWordCount) {
		long start = 0, end;
		if (debug) {
			start = System.currentTimeMillis();
		}
		HashMap<String, Integer> wordCountToMerge = newWordCount.getCurrentWordCount();
		for (Entry<String, Integer> entry : wordCountToMerge.entrySet()) {
			String word = entry.getKey();
			Integer count = entry.getValue();
			Integer curCount = currentWordCount.get(word);
			if (curCount == null) {
				currentWordCount.put(word, count);
			} else {
				currentWordCount.put(word, curCount + count);
			}
		}
		if (debug) {
			end = System.currentTimeMillis();
			System.out.println("Merged result in " + (end - start) + " millis");
		}
	}

	public HashMap<String, Integer> getSummary(int maxEntries) {
		int i = 0;
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		for (Entry<String, Integer> curEntry : currentWordCount.entrySet()) {
			result.put(curEntry.getKey(), curEntry.getValue());
			i++;
			if (i == maxEntries) {
				break;
			}
		}
		return result;
	}

}
