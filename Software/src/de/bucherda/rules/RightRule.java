package de.bucherda.rules;

import java.util.ArrayList;

public class RightRule {

	private ArrayList<RightRule> children;

	public RightRule() {
		children = new ArrayList<>();
	}

	public void addChild(RightRule r) {
		children.add(r);
	}

	public boolean hasChildren() {
		return children.size() > 0;
	}

	public ArrayList<RightRule> getChildren() {
		return children;
	}

	public void debugPrint() {
	//	System.out.println("RightRule");
	}

	public ArrayList<String> printInForm(String ruleFormat) {
		ArrayList<String> s = new ArrayList<>();
		if(ruleFormat.toLowerCase().equals("rnc")) {
			s.addAll(this.printRncFormat());
		}
		return s;
	}

	protected ArrayList<String> printRncFormat() {
		System.out.println("default");
		System.exit(1);
		return null;
	}

	protected String printRncChildren() {
		StringBuilder b = new StringBuilder();
		if(this.hasChildren()) {
			if(this instanceof Operation && ((Operation) this).getType() == Operation.OperationType.choice) {
				for(RightRule r : this.getChildren()) {
					if(0 < this.getChildren().indexOf(r)) {
						b.append("| ");
					}
					for(String s : r.printRncFormat()) {
						b.append(s);
					}
				}
			}else{
				for(RightRule r : this.getChildren()) {
					for(String s : r.printRncFormat()) {
						b.append(s);
					}
					if((this.getChildren().size() -1) > this.getChildren().indexOf(r)) {
						b.append(", ");
					}
				}
			}
		}
		return b.toString();
	}

}
