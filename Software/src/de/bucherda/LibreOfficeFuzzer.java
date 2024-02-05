package de.bucherda;

import org.jdom2.Document;
import org.jdom2.Element;

import java.util.ArrayList;

import static de.bucherda.fuzzing.fuzz;
import static de.bucherda.grammar.RngToGrammar.createGrammar;
import static de.bucherda.probabilistic.probabBuilder.probabBuilding;
import static de.bucherda.utils.PrintUtils.printHelp;
import static de.bucherda.utils.XmlHelper.*;

public class LibreOfficeFuzzer {

	public static final String settingsPath = "res/settings.xml";
	public static boolean debug;
	public static String pathToRng;
	public static String packScript;
	public static String templatePath;
	public static String logPath;
	public static String fuzzingPath;
	public static String probablisticPath;
	public static int officeTimeout;
	public static fuzzing.fuzzingMode fMode;
	public static ArrayList<Integer> acceptedReturnCodes;

	public static void main(String[] args) {
		loadSettings(); //!IMPORTANT! Do this at very fist
		//Check if Parameter are correct
		if(!debug) {
			if(args.length < 1) {
				printHelp();
			}
			switch(args[0]) {
				case "g": {
					createGrammar();
				}
				case "p": {
					probabBuilding();
				}
				case "f": {
					if(args.length < 2) {
						printHelp();
					}
					try {
						int amount = Integer.parseInt(args[1]);
						fuzz(amount);
					}catch(Exception e) {
						System.err.println("ERROR: Zweites Argument ist kein Integer.");
						printHelp();
					}
				}
				default: {
					printHelp();
				}
			}
		}else{
			System.out.println("Debug Modus gestartet.");
// Test grammar creation
//			createGrammar();
// Fuzzing a Document
	//		fuzz(30);
// Baue probabilisitsche Inforamtionen in die Grammatik
	//		probabBuilding();
		}
		System.out.println("Fin - Programm exited with Code 0");
	}

	private static void loadSettings() {
		acceptedReturnCodes = new ArrayList<>();
		acceptedReturnCodes.add(0);
		acceptedReturnCodes.add(124);
		acceptedReturnCodes.add(125);
		acceptedReturnCodes.add(126);
		acceptedReturnCodes.add(127);
		acceptedReturnCodes.add(137);
		acceptedReturnCodes.add(255);

		Document document = getStAXParsedDocument(settingsPath);
		Element rootNode = document.getRootElement();
		try {
			debug = getElementAttributeIfExist(rootNode, "DEBUG").toLowerCase().equals("true");
			pathToRng = getElementAttributeIfExist(rootNode, "RNG_PATH");
			packScript = getElementAttributeIfExist(rootNode, "PACK_SCRIPT");
			templatePath = getElementAttributeIfExist(rootNode, "TEMPLATE_PATH");
			logPath = getElementAttributeIfExist(rootNode, "LOGFILE_PATH");
			fuzzingPath = getElementAttributeIfExist(rootNode, "FUZZING_PATH");
			probablisticPath = getElementAttributeIfExist(rootNode, "PROBABLISTIC_PATH");
			officeTimeout = Integer.parseInt(getElementAttributeIfExist(rootNode,"LIBREOFFICE_TIMEOUT"));
			if(!getElementAttributeIfExist(rootNode, "FUZZING_MODE").equals("")) {
				switch(getElementAttributeIfExist(rootNode, "FUZZING_MODE").toLowerCase().charAt(0)) {
					case 'r': {
						fMode = fuzzing.fuzzingMode.random;
						break;
					}
					case 'p': {
						fMode = fuzzing.fuzzingMode.probab;
						break;
					}
					default: {
						fMode = fuzzing.fuzzingMode.random;
					}
				}
			}else{
				fMode = fuzzing.fuzzingMode.random;
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}