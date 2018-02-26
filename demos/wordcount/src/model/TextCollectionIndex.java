package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dataclay.DataClayObject;

@SuppressWarnings("serial")
public class TextCollectionIndex extends DataClayObject{
	ArrayList<TextCollection> textCollections;
	int nextCollection;
	
	public TextCollectionIndex(ArrayList<TextCollection> newTextCollections) {
		textCollections = new ArrayList<TextCollection>();
		textCollections.addAll(newTextCollections);
		nextCollection = 0;
	}
	
	public ArrayList<String> getTextTitles() {
		ArrayList<String> result = new ArrayList<String>();
		for (TextCollection tc : textCollections) {
			result.addAll(tc.getTextTitles());
		}
		return result;
	}
	
	public int getSize() {
		int result = 0;
		for (TextCollection tc : textCollections) {
			result += tc.getSize();
		}
		return result;
	}
	
	public List<String> addTextsFromPath(final String filePath) throws IOException {
		List<String> result;
		File f = new File(filePath);
		if (f.isDirectory()) {
			result = addTextsFromDir(filePath);
		} else {
			result = new ArrayList<String>();
			String newTitle = addTextFromFile(filePath);
			result.add(newTitle);
		}
		return result;
	}

	public String addTextFromFile(final String filePath) throws IOException {
		if (nextCollection == textCollections.size()) {
			nextCollection = 0;
		}
		TextCollection tc = textCollections.get(nextCollection);
		nextCollection++;
		String textTitle = tc.addTextFromFile(filePath);
		return textTitle;
	}

	public List<String> addTextsFromDir(final String dirPath) throws IOException {
		File dir = new File(dirPath);
		List<String> result = new ArrayList<String>();
		for (File f : dir.listFiles()) {
			String addedText = addTextFromFile(f.getAbsolutePath());
			result.add(addedText);
		}
		return result;
	}
}
