package de.bucherda.utils;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.StAXEventBuilder;
import org.w3c.dom.Node;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static de.bucherda.utils.RandomUtils.generateRandom;

public class XmlHelper {

	public static Document getStAXParsedDocument(final String fileName) {
		Document document = null;
		try {
			XMLInputFactory factory = XMLInputFactory.newFactory();
			XMLEventReader reader = factory.createXMLEventReader(new FileReader(fileName));
			StAXEventBuilder builder = new StAXEventBuilder();
			document = builder.build(reader);
		}
		catch (JDOMException | IOException | XMLStreamException e) {
			e.printStackTrace();
		}
		return document;
	}

	/**
	 * Prüft ob das gegebene Element eine passendes Attribut besitzt
	 * @param e Zu prüfendes Element
	 * @param attr Gesuchter Attribut Name
	 * @return Gibt wahr oder falsch zurück, ob das Attribut gefunden wurde
	 */
	public static boolean hasElementThisAttribute(Element e, String attr) {
		return e.getAttributeValue(attr) != null;
	}

	public static String getElementName(Element node) {
		if(hasElementThisAttribute(node, "name")) {
			return node.getAttributeValue("name");
		}
		return node.getName();
	}

	public static String getElementAttributeIfExist(Element node, String attr) throws Exception {
		if(hasElementThisAttribute(node, attr)) {
			return node.getAttributeValue(attr);
		}
		throw new Exception("ERROR: Attribute "+ attr +" could not be read.");
	}

	public static String getElementAttributeIfExistLast(Element node, String attr) throws Exception {
		String s;
		if(hasElementThisAttribute(node, attr)) {
			s = node.getAttributeValue(attr).split(":")[node.getAttributeValue(attr).split(":").length -1];
			return s;
		}
		throw new Exception("ERROR: Attribute "+ attr +" could not be read.");
	}

	/**
	 * Sucht unter dem root Element, ob das Element den gesuchten Name trägt
	 * @param root Wurzelelement von dem aus gesucht wird
	 * @param name Gesuchter Name des Elements
	 * @return Gibt das erste vorkommen des gesuchten Elements, oder null zurück, wenn keins gefunden wurde
	 */
	public static Element findElement(Element root, String name) {
		Element ret;
		if(root.getName().equals(name)) {
			return root;
		}
		for(Element e : root.getChildren()) {
			ret = findElement(e, name);
			if(ret!=null) {
				return ret;
			}
		}
		return null;
	}

	public static HashMap<String, Namespace> prepareNameSpaces(Element root) {
		HashMap nsp = new HashMap();

		for(Namespace nc : root.getNamespacesIntroduced()) {
			nsp.put(nc.getPrefix(), nc);
		}

		for(Element e : root.getChildren()) {
			nsp.putAll(prepareNameSpaces(e));
		}
		return nsp;
	}

	public static HashMap<String, Element> prepareRules(Element root) {
		HashMap<String, Element> defs = new HashMap<>();
		if(root.getName().toLowerCase().equals("define")) {
			//TODO: Parent is defined
			System.err.println("ERROR: Parent ist schon eine definition.");
			System.exit(1);
		}else{
			defs = prepareRuleRecurse(root);
		}

		for(Map.Entry<String, Element> entry : defs.entrySet()) {
			String name = entry.getKey();
			Element ele = entry.getValue();
			if(hasElementThisAttribute(ele, "combine")) {
				if(ele.getAttributeValue("combine").toLowerCase().equals("interleave")) {
					ArrayList<Element> children = new ArrayList<>();

					ele.removeContent();

					Collections.shuffle(children);
					ele.addContent(children);
				}else if(ele.getAttributeValue("combine").toLowerCase().equals("choice")) {
					ArrayList<Element> children = new ArrayList<>();
					for(Element chld : ele.getChildren()) {
						children.add(chld.detach());
					}
					int chosenChild = generateRandom(0, children.size());
					ele.addContent(children.get(chosenChild));
				}
				ele.removeAttribute("combine");
			}
		}

		return defs;
	}

	/**
	 * Methode sucht alle Definitionen und legt diese in einer Hashmap ab.
	 * @param root Wurzelelement von dem aus gesucht wird
	 * @return HashMap mit allen Definitionseinträgen
	 */
	private static HashMap<String, Element> prepareRuleRecurse(Element root) {
		HashMap<String, Element> rules = new HashMap<>();

		Iterator<Element> it = root.getChildren().listIterator();
		while(it.hasNext()) {
			Element e = it.next();
			if(e.getName().toLowerCase().equals("define") && hasElementThisAttribute(e, "name")) {
				rules.put(e.getAttributeValue("name"), e);
			}
		}

/*		if(root.getName().toLowerCase().equals("define") && hasElementThisAttribute(root, "name")) {
			rules.put(root.getAttributeValue("name"), root);
		}

		for(Element e : root.getChildren()) {
			for(Map.Entry<String, Element> entry : prepareRules(e).entrySet()) {
				if(rules.containsKey(entry.getKey())) {
					//Die Regel gab es schon und es werden nur Kinder übertragen
					System.out.println(rules.get(entry.getKey()).getName());
					System.out.println(entry.getValue().getAttributeValue("name"));
					rules.get(entry.getKey())
							.addContent(entry.getValue());
				}else{
					//Die Regel ist neu und wird hinzugefügt
					rules.put(entry.getValue().getAttributeValue("name"), entry.getValue());
				}
			}
		}*/
		return rules;
	}

	/**
	 * Durchsucht alle Kinder des Root Elements und sucht nach der Definition mit dem passenden Namen
	 * @param root Wurzelelement, von dem aus gesucht wird
	 * @param moreRoots Weitere Wurzelelemente, aus anderen Eingebundenen Dateien
	 * @param name Name der Definition, die gesucht werden soll
	 * @return Gibt de erste vorkommende Definition mit dem passenden Namen zurück
	 */
	public static ArrayList<Element> findDefinition(Element root, ArrayList<Element> moreRoots, String name) {
		ArrayList<Element> ret = new ArrayList<>();

		if(root.getName().toLowerCase().equals("define")) {
			if(hasElementThisAttribute(root, "name")) {
				if(root.getAttributeValue("name").toLowerCase().equals(name.toLowerCase())) {
					ret.addAll(root.getChildren());
				}
			}
		}
//Andere Eingebundene Datein durchlaufen
		for(Element otherRoot : moreRoots) {
			ret.addAll(findDefinition(otherRoot, new ArrayList<>(), name));
		}

		for(Element e : root.getChildren()) {
			ret.addAll(findDefinition(e, new ArrayList<>(), name));
		}
		return ret;
	}

	/**
	 * Durchsucht vom gegebenen Wurzelelement an alle Kinder, ob sie ein Attribut mit dem Wert val haben
	 * @param root Wurzelelement, von dem aus gesucht wird
	 * @param attr Name des gesuchten Attributs
	 * @param val Wert des gesuchten Attributs
	 * @return Gibt das erste vorkommen mit der passenden Kombination zurück, oder null wenn keins gefunden wurde
	 */
	public static Element findElementByAttrName(Element root, String attr, String val) {
		Element ret;
		if(hasElementThisAttribute(root, attr)) {
			if(root.getAttributeValue(attr).equals(val)) {
				return root;
			}
		}
		for(Element e : root.getChildren()) {
			ret = findElementByAttrName(e, attr, val);
			if(ret!=null) {
				return ret;
			}
		}
		return null;
	}

}
