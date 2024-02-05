package de.bucherda.rules;

import java.util.ArrayList;
import java.util.Collections;

public class NonTerminal extends RightRule {

	public enum NonTerminalType {
		element, attribute
	}

	private NonTerminalType type;
	private String name;

	public NonTerminal(NonTerminalType type, String nam) {
		this.type = type;
		this.name = nam;
	}

	public NonTerminalType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public ArrayList<String> printRncFormat() {
		StringBuilder b = new StringBuilder();
		switch(type) {
			case element: {
				b.append("element ").append(this.name);
				break;
			}
			case attribute: {
				b.append("attribute ").append(this.name);
				break;
			}
			default: {
				b.append("default ").append(this.name);
			}
		}

		if(this.hasChildren()) {
			b.append(" { ");
			b.append(this.printRncChildren());
			b.append(" }");
		}

		return new ArrayList<>(Collections.singletonList(b.toString()));
	}
}
