package model;

import java.io.IOException;
import java.util.ArrayList;

import dataclay.DataClayObject;

@SuppressWarnings("serial")
public class TextCollection extends DataClayObject {
	String textPrefix;
	ArrayList<String> textTitles;
	boolean debug;

	public TextCollection(String prefixForTextsInCollection, boolean doDebug) {
		textTitles = new ArrayList<String>();
		textPrefix = prefixForTextsInCollection;
		debug = doDebug;
	}

	public String getTextPrefix() {
		return textPrefix;
	}

	public ArrayList<String> getTextTitles() {
		return textTitles;
	}

	public int getSize() {
		return textTitles.size();
	}

	public String addTextFromFile(final String filePath) throws IOException {
		String textTitle = textPrefix + ".file" + (textTitles.size() + 1);
		Text t = new Text(textTitle, debug);
		t.makePersistent(textTitle);
		textTitles.add(textTitle);
		t.addWords(filePath);
		return textTitle;
	}
}
