package de.bucherda.utils;

import de.bucherda.fuzzing;
import org.jdom2.Document;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static de.bucherda.LibreOfficeFuzzer.fMode;
import static de.bucherda.LibreOfficeFuzzer.settingsPath;
import static de.bucherda.utils.RandomUtils.generateRandom;
import static de.bucherda.utils.XmlHelper.*;
import static de.bucherda.utils.XmlHelper.getElementAttributeIfExist;

public class RandomPicker {

	private static double ch_optional;
	private static int oneOrMore_Min;
	private static int oneOrMore_Max;
	private static int oneOrMore_Av;
	private static int zeroOrMore_Min;
	private static int zeroOrMore_Max;
	private static int zeroOrMore_Av;
	private static int max_Rec_Depth;
	private static int max_Usage;

	private static HashMap<Element, Integer> maxUsages;

	public RandomPicker() {
		maxUsages = new HashMap<>();
		loadFuzzingParameter();
	}

	public List<Element> chooseElement(Element e, int recDepth) {

		switch(e.getName().toLowerCase()) {
			case "choice": {
				//Würfel für jedes Kindelement die Wahrscheinlichkeit und gibt die Liste mit ausgewählten Kindern zurück
				int chosenOne = getChChance(e);
				ArrayList<Element> chosen = new ArrayList<>();
				chosen.add(e.getChildren().get(chosenOne));
				return chosen;
			}
			case "optional": {
				//Würfel die Chance für die Ausführung des Elements und gib zurück, ob das Element ausgewählt wurde
				double chance = getOptChance(e);
				if(Math.random() < chance) {
					return e.getChildren();
				}else{
					return new ArrayList<>();
				}
			}
			case "zeroormore": {
				if(!maxUsages.containsKey(e)) {
					maxUsages.put(e, 0);
				}else{
					maxUsages.replace(e, maxUsages.get(e)+1);
				}

				ArrayList<Element> repeating = new ArrayList<>();
				if(maxUsages.get(e) < max_Usage) {
					Tripple<Integer, Integer, Integer> chance = getZOMChance(e);
					int repeats = (int) Math.round(generateRandom(chance.getX(), chance.getY()) *
							Math.max(0.0, 1.0-((double) recDepth/ (double) max_Rec_Depth)));
					for(int i = 0; i < repeats; i++) {
						repeating.addAll(e.getChildren());
					}
				}
				return repeating;
			}
			case "oneormore": {
				if(!maxUsages.containsKey(e)) {
					maxUsages.put(e, 0);
				}else{
					maxUsages.replace(e, maxUsages.get(e)+1);
				}

				ArrayList<Element> repeating = new ArrayList<>();
				if(maxUsages.get(e) < max_Usage) {
					Tripple<Integer, Integer, Integer> chance = getZOMChance(e);
					int repeats = (int) Math.round(generateRandom(chance.getX(), chance.getY()) *
							(Math.max(0.0, 1.0-((double) recDepth/ (double) max_Rec_Depth)))+1);
					for(int i = 0; i < repeats; i++) {
						repeating.addAll(e.getChildren());
					}
				}else{
					repeating.addAll(e.getChildren());
				}
				return repeating;
			}
			default: {
				return e.getChildren();
			}
		}
	}

	private double getOptChance(Element e) {
		if(fMode == fuzzing.fuzzingMode.probab) {
			if(hasElementThisAttribute(e, "prob_total") && hasElementThisAttribute(e, "prob_chosen")) {
				return (Double.parseDouble(e.getAttributeValue("prob_chosen")))
						/(Double.parseDouble(e.getAttributeValue("prob_total")));
			}
		}else{
			return ch_optional;
		}
		return ch_optional;
	}

	private int getChChance(Element e) {
		int childs = e.getChildren().size();
		int chosen;
		int allWeight = 0;
		int total = -1;
		ArrayList<Integer> chances = new ArrayList<>();
		if(hasElementThisAttribute(e, "prob_total")) {
			total = Integer.parseInt(e.getAttributeValue("prob_total"));
		}
		for(int i = 1; i <= childs; i++) {
			if(hasElementThisAttribute(e, "prob_nr"+i) && total > 0) {
				chances.add(Integer.parseInt(e.getAttributeValue("prob_nr"+i)));
				allWeight += Integer.parseInt(e.getAttributeValue("prob_nr"+i));
			}else{
				chances.add(0);
			}
		}
		if(fMode == fuzzing.fuzzingMode.probab) {
			chosen = -1;
			double ch = Math.random() * allWeight;
			double countWeight = 0;
			for(int i = 0; i < childs; i++) {
				countWeight += chances.get(i);

				if (countWeight >= ch) {
					return i;
				}
				i++;
			}
			return childs;
		}else if (fMode == fuzzing.fuzzingMode.random) {
			return generateRandom(0, childs);
		}else{
			System.err.println("ERROR: Fuzzingmodus wurde nicht korrekt definiert.");
			System.exit(-1);
			return -1;
		}
	}

	private Tripple<Integer, Integer, Integer> getZOMChance(Element e) {
		int a = oneOrMore_Min;
		int b = oneOrMore_Max;
		int c = oneOrMore_Av;

		if("zeroormore".equals(e.getName().toLowerCase())) {
			a = zeroOrMore_Min;
			b = zeroOrMore_Max;
			c = zeroOrMore_Av;
		}

		if(hasElementThisAttribute(e, "prob_min")) {
			a = Integer.parseInt(e.getAttributeValue("prob_min"));
		}
		if(hasElementThisAttribute(e, "prob_max")) {
			b = Integer.parseInt(e.getAttributeValue("prob_max"));
		}
		if(hasElementThisAttribute(e, "prob_av")) {
			c = Integer.parseInt(e.getAttributeValue("prob_av"));
		}
		return new Tripple<>(a, b, c);
	}

	private void loadFuzzingParameter() {
		Document document = getStAXParsedDocument(settingsPath);
		Element rootNode = document.getRootElement();
		try {
			ch_optional = Double.parseDouble(getElementAttributeIfExist(rootNode, "CHANCE_OPTIONAL"));
			oneOrMore_Min = Integer.parseInt(getElementAttributeIfExist(rootNode, "ONEORMORE_MIN"));
			oneOrMore_Max = Integer.parseInt(getElementAttributeIfExist(rootNode, "ONEORMORE_MAX"));
			oneOrMore_Av = Integer.parseInt(getElementAttributeIfExist(rootNode, "ONEORMORE_AV"));
			zeroOrMore_Min = Integer.parseInt(getElementAttributeIfExist(rootNode, "ZEROORMORE_MIN"));
			zeroOrMore_Max = Integer.parseInt(getElementAttributeIfExist(rootNode, "ZEROORMORE_MAX"));
			zeroOrMore_Av = Integer.parseInt(getElementAttributeIfExist(rootNode, "ZEROORMORE_AV"));
			max_Rec_Depth = Integer.parseInt(getElementAttributeIfExist(rootNode, "MAX_REC_DEPTH"));
			max_Usage = Integer.parseInt(getElementAttributeIfExist(rootNode, "MAX_USAGE"));
		}catch(Exception e) {
			System.err.println("ERROR: Fehler beim einlesen der Fuzzing Parameter");
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
