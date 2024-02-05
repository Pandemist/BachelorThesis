package de.bucherda.grammar;

import de.bucherda.rules.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

import java.util.ArrayList;

import static de.bucherda.LibreOfficeFuzzer.*;
import static de.bucherda.rules.Operation.OperationType.*;
import static de.bucherda.rules.Terminal.TerminalType.*;
import static de.bucherda.rules.NonTerminal.NonTerminalType.*;
import static de.bucherda.rules.TerminalBody.TerminalType.*;
import static de.bucherda.utils.PrintUtils.printToFile;
import static de.bucherda.utils.XmlHelper.*;

public class RngToGrammar {

	static ArrayList<Rule> rules = new ArrayList<>();

	public static void createGrammar() {
		Document document = getStAXParsedDocument(pathToRng);
		Element rootNode = document.getRootElement();

		Element start = findElement(rootNode, "start");

		createRules(start);

		for(Element defs : rootNode.getChildren()) {
			if(defs.getAttributeValue("name")!=null) {
				createRules(defs);
			}
		}

		//Regeln zusammenführen, die vom selben nicht Terminal ableiten

		ArrayList<Integer> deleteFlag = new ArrayList<>();
		for(Rule gr : rules) {
			for(Rule gr2 : rules) {
				if(deleteFlag.contains(rules.indexOf(gr2))) {
					continue;
				}
				if(gr.getLeft().equals(gr2.getLeft()) && rules.indexOf(gr) != rules.indexOf(gr2)) {
					deleteFlag.add(rules.indexOf(gr2));
					gr.addAllRules(gr2.getRightSite());
				}
			}
		}
		if(deleteFlag.size() >= 1) {
			rules.subList(1, deleteFlag.size() + 1).clear();
		}

		int maxIndent = 0;
		for(Rule gr : rules) {
			if(gr.getLeftLength() > maxIndent) {
				maxIndent = gr.getLeftLength();
			}
		}

		StringBuilder grammar = new StringBuilder();
//Add Prefix
		grammar.append("# OASIS OpenDocument v1.0\n");
		grammar.append("# OASIS Standard, 1 May 2005\n");
		grammar.append("# Relax-NG Schema\n");
		grammar.append("#\n");
		grammar.append("# $Id$\n");
		grammar.append("#\n");
		grammar.append("# © 2002-2005 OASIS Open\n");
		grammar.append("# © 2002-2005 OASIS Open\n");
		grammar.append("\n");

//Add namespaces
		for(Namespace a : rootNode.getNamespacesIntroduced()) {
			if(!a.getPrefix().equals("")) {
				grammar.append("namespace " + a.getPrefix());
				for(int i = 0; i < (maxIndent - ("namespace " + a.getPrefix()).length());i++) {
					grammar.append(" ");
				}
				grammar.append("= \"").append(a.getURI()).append("\"\n");
			}
		}
		grammar.append("\n");

		for(Rule gr : rules) {
			grammar.append(gr.printRule(maxIndent));
		}
	//	System.out.println(grammar.toString());
	}

	private static void createRules(Element e) {
		Rule rg = new Rule(getElementName(e));

		for(Element ch : e.getChildren()) {
			rg.addRightSite(createRightRules(ch));
		}

		rules.add(rg);
	}

	private static RightRule createRightRules(Element e) {
		//Herausfinden, was der Regel hinzugefügt werden muss
		RightRule rp = null;
		switch(e.getName().toLowerCase()) {
			case "element": {
				rp = new NonTerminal(element, getElementName(e));
				break;
			}
			case "attribute": {
				rp = new NonTerminal(attribute, getElementName(e));
				break;
			}
			case "choice": {
				rp = new Operation(choice);
				break;
			}
			case "optional": {
				rp = new Operation(optional);
				break;
			}
			case "ref": {
				rp = new Terminal(ref, getElementName(e));
				break;
			}
			case "zeroormore": {
				rp = new Operation(zeroOrMore);
				break;
			}
			case "oneormore": {
				rp = new Operation(oneOrMore);
				break;
			}
			case "interleave": {
				rp = new Operation(interleave);
				break;
			}
			case "group": {
				rp = new Operation(group);
				break;
			}
			case "mixed": {
				rp = new Operation(mixed);
				break;
			}
			case "list": {
				rp = new Operation(list);
				break;
			}
			case "empty": {
				rp = new Terminal(empty);
				break;
			}
			case "except": {
				rp = new TerminalBody(except, e.getText());
				break;
			}
			case "value": {
				rp = new TerminalBody(value, e.getText());
				break;
			}
			case "anyname": {
				rp = new Terminal(anyName);
				break;
			}
			case "nsname": {
				rp = new Terminal(nsName);
				break;
			}
			case "name": {
				rp = new TerminalBody(name, e.getText());
				break;
			}
			case "text": {
				rp = new Terminal(text);
				break;
			}
			case "data": {
				rp = new Terminal(data, e.getAttributeValue("type"));
				break;
			}
			case "param": {
				rp = new TerminalBody(param, e.getText());
				break;
			}
			case "parentref": {
				rp = new Terminal(parentRef, e.getText());
				break;
			}
			case "externalref": {
				rp = new Terminal(externalRef, e.getText());
				break;
			}
			case "div": {
				rp = new Operation(div);
				break;
			}
			case "include": {
				rp = new TerminalBody(include, e.getText());
				break;
			}
			default: {
				System.err.println("ERROR: "+getElementName(e)+" could not be matched!");
				System.exit(1);
			}
		}

		for(Element ch : e.getChildren()) {
			rp.addChild(createRightRules(ch));
		}
		return rp;
	}
}
