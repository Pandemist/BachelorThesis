package de.bucherda.rules;

import java.util.ArrayList;
import java.util.Collections;

public class Terminal extends RightRule {

	public enum TerminalType {
		ref, parentRef, externalRef, data, empty, text, notAllowed, anyName, nsName
	}

	private TerminalType type;
	private String dataType;

	public Terminal(TerminalType typ) {
		this.type = typ;
	}

	public Terminal(TerminalType typ, String dataTyp) {
		this.type = typ;
		this.dataType = dataTyp;
	}

	public ArrayList<String> printRncFormat() {
		StringBuilder b = new StringBuilder();

		switch(type) {
			case ref: {
				return new ArrayList<>(Collections.singletonList(dataType));
			}
			case parentRef: {
				return new ArrayList<>(Collections.singletonList(dataType));
			}
			case data: {
				b.append("xsd:").append(dataType);
				break;
			}
			case empty: {
				return new ArrayList<>(Collections.singletonList("empty"));
			}
			case text: {
				return new ArrayList<>(Collections.singletonList("text"));
			}
			case notAllowed: {
				return new ArrayList<>(Collections.singletonList("notAllowed"));
			}
			case anyName: {
				return new ArrayList<>(Collections.singletonList("anyName"));
			}
			case externalRef: {
				return new ArrayList<>(Collections.singletonList(dataType));
			}
			case nsName: {
				return new ArrayList<>(Collections.singletonList("nsName"));
			}
			default: {
				System.err.println("ERROR: DataType dose not match!");
				System.exit(1);
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
