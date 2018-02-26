package producer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// import dataclay.collections.DataClayArrayList;
import model.Text;
import model.TextCollection;
import model.TextCollectionIndex;
import storage.StorageItf;
import dataclay.api.BackendID;
import dataclay.api.DataClay;

public class TextCollectionGen {
    public static String configPropertiesFile = "./cfgfiles/session.properties";
    static int timesPerFile = 1;
    public static boolean doDebug = false;

    public static void main(String[] args) throws Exception {
	if (args.length < 2) {
	    printErrorUsage();
	    return;
	}
	if (!setOptionalArguments(args)) {
	    return;
	}
	final String textColAlias = args[0];
	final String remoteFilePath = args[1];

	StorageItf.init(configPropertiesFile);

	// Try to retrieve a previously created text collection
	TextCollectionIndex textCollectionIndex = null;
	try {
	    textCollectionIndex = TextCollectionIndex.getByAlias(textColAlias);
	    System.out.println("[LOG] Found collection index with " + textCollectionIndex.getSize() + " files");
	} catch (Exception ex) {
	    System.out.println("[LOG] No previous collection index found.");
	    ArrayList<TextCollection> tcs = new ArrayList<TextCollection>();
	    int id = 1;

	    // Distributed index among N collections, one per Backend.
	    for (BackendID locID : DataClay.getJBackends().keySet()) {
		String prefixForTexts = textColAlias + id;
		TextCollection tc = new TextCollection(prefixForTexts, doDebug);

		tc.makePersistent(prefixForTexts, locID);
		System.out.println("[LOG] Collection created at " + tc.getLocation());

		tcs.add(tc);
		id++;
	    }
	    textCollectionIndex = new TextCollectionIndex(tcs);
	    textCollectionIndex.makePersistent(textColAlias);
	    System.out.println("[LOG] Created new collection index");
	}
	System.out.println("[LOG] Collection index located at " + textCollectionIndex.getLocation());

	// Add text files to the index.
	// Internally, text objects are created from text files, and stored in a round
	// robin fashion among the distributed index.
	List<String> textTitles = new ArrayList<String>();
	for (int i = 0; i < timesPerFile; i++) {
	    try {
		textTitles = textCollectionIndex.addTextsFromPath(remoteFilePath);
	    } catch (Exception ex) {
		System.err.println("[ERROR] Could not add texts from path " + remoteFilePath
			+ ". Is it a valid path in the backend?");
	    }

	    System.out.println("[LOG] Updated collection. Now it has " + textCollectionIndex.getSize() + " files.");
	    for (String textTitle : textTitles) {
		Text t = Text.getByAlias(textTitle);
		System.out.println("[LOG] New text " + textTitle + " is located at " + t.getLocation());
	    }
	}
	StorageItf.finish();

    }

    /**
     * Checks arguments and set properties properly.
     * 
     * @param args
     *            arguments of the application
     * @return false if arguments are wrong. true otherwise.
     */
    private static boolean setOptionalArguments(String[] args) {
	for (int argIndex = 3; argIndex < args.length;) {
	    String arg = args[argIndex++];
	    if (arg.equals("-c")) {
		configPropertiesFile = args[argIndex++];
		File f = new File(configPropertiesFile);
		if (!f.exists() || f.isDirectory()) {
		    System.err
			    .println("Bad argument. Configuration file: " + configPropertiesFile + " does not exist.");
		    return false;
		}
	    } else if (arg.equals("-t")) {
		timesPerFile = new Integer(args[argIndex++]);
		if (timesPerFile <= 0) {
		    System.err.println("Bad argument. TimesPerFile must be greater than zero");
		    return false;
		}
	    } else if (arg.equals("-debug")) {
		doDebug = true;
	    } else {
		printErrorUsage();
		return false;
	    }
	}

	return true;
    }

    /**
     * @brief print application usage
     */
    private static void printErrorUsage() {
	System.err.println("Bad arguments. Usage: \n\n" + TextCollectionGen.class.getName()
		+ " <text_col_alias> <remote_path> [-c <config_properties> ] [-t <times_file>] [-debug] " + " \n");
    }

}