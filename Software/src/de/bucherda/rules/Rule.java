package de.bucherda.rules;

import java.util.ArrayList;

import static de.bucherda.utils.PrintUtils.calcIndent;

public class Rule {

	private String leftSite;
	private ArrayList<RightRule> rightSite;

	public Rule(String ls) {
		this.leftSite = ls;
		rightSite = new ArrayList<>();
	}

	public void addRightSite(RightRule rs) {
		this.rightSite.add(rs);
	}

	public void addAllRules(ArrayList<RightRule> rs) {
		this.rightSite.addAll(rs);
	}

	public int getLeftLength() {
		return leftSite.length();
	}

	public String getLeft() {
		return leftSite;
	}

	public ArrayList<RightRule> getRightSite() {
		return rightSite;
	}

	public String printRule(int indent) {
		int diff = indent - leftSite.length();
		StringBuilder print = new StringBuilder(leftSite);
		print.append(calcIndent(diff));
		print.append(" = ");
		boolean firstRule = true;
		for(RightRule rr : rightSite) {
			if(rr instanceof Operation && ((Operation) rr).getType() == Operation.OperationType.choice) {
				for(String s : rr.printInForm("rnc")) {
					if(firstRule) {
						firstRule = false;
					}else{
						print.append(calcIndent(indent));
						print.append(" ");
					}
					print.append(s);
					if((rr.printInForm("rnc").size() -1) > rr.printInForm("rnc").indexOf(s)) {
						print.append("\n");
					}
				}
			}else{
				for(String s : rr.printInForm("rnc")) {
					print.append(s);
					if((rr.printInForm("rnc").size() -1) > rr.printInForm("rnc").indexOf(s)) {
						print.append(", ");
					}
				}
				if((rightSite.size() -1) > rightSite.indexOf(rr)) {
					print.append(", ");
				}
			}
		}
		print.append("\n");
		return print.toString();
	}
}
