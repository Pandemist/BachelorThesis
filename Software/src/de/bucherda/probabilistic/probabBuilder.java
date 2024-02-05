package de.bucherda.probabilistic;

import de.bucherda.utils.Tripple;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.filter.ElementFilter;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static de.bucherda.LibreOfficeFuzzer.*;
import static de.bucherda.utils.XmlHelper.*;

public class probabBuilder {

	static Element grammarRootNode;
	static HashMap<String, Integer> tokenMap;
	static HashMap<String, Integer> attributeKeyMap;
	static ArrayList<Tripple<String, String, Integer>> attributeMap;

	public static void probabBuilding() {
		File folder = new File(probablisticPath);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			// Alle Datein mit Pfad durchlaufen
			if (listOfFiles[i].isFile()) {
				System.out.println(listOfFiles[i].getName() + ": verarbeitung begonnen!");
				addProbabs(probablisticPath+listOfFiles[i].getName());
				System.out.println(listOfFiles[i].getName() + ": verarbeitung abgeschlossen!");
			}
		}
	}

	private static void addProbabs(String path) {
		Document document = getStAXParsedDocument(path);
		Element docRootNode = document.getRootElement();

		Document document2 = getStAXParsedDocument(pathToRng);
		grammarRootNode = document2.getRootElement();

		ElementFilter filter = new org.jdom2.filter.ElementFilter("start");
		Element startRule = grammarRootNode.getDescendants(filter).next();

		tokenMap = new HashMap<>();
		attributeKeyMap = new HashMap<>();
		attributeMap = new ArrayList<>();

		getProbabsRecursive(docRootNode, startRule);

		Comparator<Tripple<String, String , Integer>> keyComparator =
				Comparator.comparing(Tripple::getX);

		Collections.sort(attributeMap, keyComparator);

		//Alle Regeln und Knoten durchlaufen und anotationen aus den Listen hinzufügen
		anotateRecursive(grammarRootNode);

		//Eine Schleife über alle weiteren Regeln
		filter = new org.jdom2.filter.ElementFilter("define");
		for(Element ruleDef : grammarRootNode.getDescendants(filter)) {
			anotateRecursive(ruleDef);
		}

		// Alle Regeln durchlaufen und bei Entscheidungen Wahrscheinlichkeiten anotieren

		// Mit der Startregel beginnen
		addProbabsRecursive(grammarRootNode);

		//Eine Schleife über alle weiteren Regeln
		filter = new org.jdom2.filter.ElementFilter("define");
		for(Element ruleDef : grammarRootNode.getDescendants(filter)) {
			addProbabsRecursive(ruleDef);
		}

		Document cDoc = grammarRootNode.getDocument();
		XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());
		try {
			xmlOutput.output(cDoc, new FileWriter(pathToRng+"_Probab.rng"));
		}catch(IOException ex) {
			ex.printStackTrace();
		}

	}

	private static void anotateOnNodes(Element grammar) {
		if(hasElementThisAttribute(grammar, "name")) {
			try {
				switch(grammar.getName()) {
					case "element": {
						if(tokenMap.containsKey(getElementAttributeIfExistLast(grammar, "name"))) {
							anotate(grammar, "prob_amount",
									tokenMap.get(getElementAttributeIfExistLast(grammar, "name")));
						}
						break;
					}
					case "attribute": {
						if(attributeKeyMap.containsKey(getElementAttributeIfExistLast(grammar, "name"))) {
							anotate(grammar, "prob_amount",
									attributeKeyMap.get(getElementAttributeIfExistLast(grammar, "name")));
						}
						break;
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void anotate(Element e, String name, int value) {
		e.setAttribute(name, String.valueOf(value));
	}

	private static void anotateRecursive(Element grammar) {
		if(hasElementThisAttribute(grammar, "name")) {
			anotateOnNodes(grammar);
		}
		for(Element chld : grammar.getChildren()) {
			anotateRecursive(chld);
		}
		// Rekursiv dorch eine Regel laufen und an den Elementen und Attributen anotieren
	}

	private static void anotateChoices(Element currentSelector) {
		int counter = 0;
		int total = 0;
		for(Element chld : currentSelector.getChildren()) {
			int childVals = getNextAnotation(chld);
			total += childVals;
			anotate(currentSelector, "prob_nr"+counter, childVals);
			counter++;
		}
		anotate(currentSelector, "prob_total", total);
	}

	private static void anotateRepeats(Element currentSelector) {
		int max = 0;
		int min = 0;
		boolean first = true;
		for(Element chld : currentSelector.getChildren()) {
			int childVals = getNextAnotation(chld);
			if(first) {
				min = childVals;
				first = false;
			}
			max = Math.max(max, childVals);
		}
		int av = (max - min) / 2;
		anotate(currentSelector, "prob_min", min);
		anotate(currentSelector, "prob_max", max);
		anotate(currentSelector, "prob_av", av);
	}

	private static void anotateOptional(Element currentSelector) {
		int min = 0;
		int total = 0;
		boolean first = true;
		for(Element chld : currentSelector.getChildren()) {
			int childVals = getNextAnotation(chld);
			if(first) {
				min = childVals;
				first = false;
			}
			total = Math.max(total, childVals);
		}
		anotate(currentSelector, "prob_chosen", min);
		anotate(currentSelector, "prob_total", total);
	}

	private static int getNextAnotation(Element current) {
		if(hasElementThisAttribute(current, "prob_amount")) {
		//	System.out.println(Integer.parseInt(current.getAttribute("prob_amount").getValue()));
			return Integer.parseInt(current.getAttribute("prob_amount").getValue());
		}else{
			int childAnotation = 0;
			for(Element chld : current.getChildren()) {
				childAnotation += getNextAnotation(chld);
			}
			return childAnotation;
		}
	}

	private static void addProbabsRecursive(Element grammar) {
//		if(hasElementThisAttribute(grammar, "name")) {
			try {
				switch(grammar.getName().toLowerCase()) {
					case "choice": {
						//TODO: Choice Element behandeln
		//				System.out.println("Choice Anotation");
						anotateChoices(grammar);
						break;
					}
					case "oneormore":
					case "zeroormore": {
						//TODO: zeroOrMore Element behandeln
		//				System.out.println("OneOrMore Anotation");
						anotateRepeats(grammar);
						break;
					}
					case "optional": {
						//TODO: oneOrMore Element behandeln
		//				System.out.println("Optional Anotation");
						anotateOptional(grammar);
						break;
					}
					default: {
		//				System.out.println(grammar.getName().toLowerCase());
					}
				}
			}catch(Exception e) {
				e.getStackTrace();
			}
			for(Element chld : grammar.getChildren()) {
				addProbabsRecursive(chld);
			}
//		}
		// Rekursiv dorch eine Regel laufen und anotieren
	}

	private static void getProbabsRecursive(Element doc, Element grammar) {
		// Handle node
		if(tokenMap.containsKey(doc.getName())) {
			tokenMap.replace(doc.getName(), tokenMap.get(doc.getName())+1);
		}else{
			tokenMap.put(doc.getName(), 1);
		}

		// Handle Attributes
		for(Attribute attr : doc.getAttributes()) {
			if(attributeKeyMap.containsKey(attr.getName())) {
				attributeKeyMap.replace(attr.getName(), attributeKeyMap.get(attr.getName())+1);
			}else{
				attributeKeyMap.put(attr.getName(), 1);
			}

			if(attributeMapContainsKeyValue(attr.getName(), attr.getValue())) {
				attributeMapUpdate(attr.getName(), attr.getValue(), 1);
			}else{
				attributeMap.add(new Tripple<>(attr.getName(), attr.getValue(), 1));
			}
		}

		// Handle Namespaces
		if(doc.getNamespacesIntroduced().size() > 0) {
			for(Namespace namespace : doc.getNamespacesIntroduced()) {
			}
		}

		if(doc.getChildren().size() > 0) {
			for(Element child : doc.getChildren()) {
				getProbabsRecursive(child, grammar);
			}
		}
	}

	private static boolean attributeMapContainsKey(String s) {
		for(Tripple<String, String, Integer> entry : attributeMap) {
			if(entry.getX().equals(s)) {
				return true;
			}
		}
		return false;
	}

	private static boolean attributeMapContainsKeyValue(String s, String v) {
		for(Tripple<String, String, Integer> entry : attributeMap) {
			if(entry.getX().equals(s) && entry.getY().equals(v)) {
				return true;
			}
		}
		return false;
	}

	private static void attributeMapUpdate(String s, int modifyer) {
		for(Tripple<String, String, Integer> entry : attributeMap) {
			if(entry.getX().equals(s)) {
				entry.setZ(entry.getZ()+modifyer);
			}
		}
	}

	private static void attributeMapUpdate(String s, String v, int modifyer) {
		for(Tripple<String, String, Integer> entry : attributeMap) {
			if(entry.getX().equals(s) && entry.getY().equals(v)) {
				entry.setZ(entry.getZ()+modifyer);
			}
		}
	}
}
