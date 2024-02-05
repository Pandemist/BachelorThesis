package de.bucherda.rules;

import java.util.ArrayList;
import java.util.Collections;

public class Operation extends RightRule {

	public enum OperationType {
		optional, group, interleave, choice, zeroOrMore, oneOrMore, list, mixed, div
	}

	private OperationType type;

	public Operation(OperationType typ) {
		this.type = typ;
	}

	OperationType getType() {
		return type;
	}

	public ArrayList<String> printRncFormat() {
		ArrayList<String> rules = new ArrayList<>();
		switch(type) {
			case optional: {
				return new ArrayList<>(Collections.singletonList("("+ this.printRncChildren() + ")?"));
			}
			case group: {
				return new ArrayList<>(Collections.singletonList("("+ this.printRncChildren() + ")"));
			}
			case interleave: {
				if(this.hasChildren()) {
					for(RightRule r : this.getChildren()) {
						for(String prf : r.printRncFormat()) {
							StringBuilder b = new StringBuilder();
							if(this.getChildren().indexOf(r) != 0) {
								b.append(" & ");
							}
							b.append(prf);
							rules.add(b.toString());
						}
					}
				}
				break;
			}
			case choice: {
				if(this.hasChildren()) {
					for(RightRule r : this.getChildren()) {
						for(String prf : r.printRncFormat()) {
							StringBuilder b = new StringBuilder();
							if(this.getChildren().indexOf(r) != 0) {
								b.append("| ");
							}
							b.append(prf);
							rules.add(b.toString());
						}
					}
				}
				break;
			}
			case zeroOrMore: {
				return new ArrayList<>(Collections.singletonList("("+ this.printRncChildren() + ")*"));
			}
			case oneOrMore: {
				return new ArrayList<>(Collections.singletonList("("+ this.printRncChildren() + ")+"));
			}
			case list: {
				StringBuilder b = new StringBuilder();
				b.append("list").append(" { ");
				b.append(this.printRncChildren());
				b.append(" }");
				return new ArrayList<>(Collections.singletonList(b.toString()));
			}
			case mixed: {
				StringBuilder b = new StringBuilder();
				b.append("mixed").append(" { ");
				b.append(this.printRncChildren());
				b.append(" }");
				return new ArrayList<>(Collections.singletonList(b.toString()));
			}
			case div: {
				StringBuilder b = new StringBuilder();
				b.append(this.printRncChildren());
				return new ArrayList<>(Collections.singletonList(b.toString()));
			}
			default: {
				System.err.println("ERROR: Operationstyp wurde nicht festgelegt.");
				System.exit(1);
			}
		}
		return rules;
	}
}
