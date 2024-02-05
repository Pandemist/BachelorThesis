package de.bucherda.rules;

import java.util.ArrayList;
import java.util.Collections;

public class TerminalBody extends RightRule {

	public enum TerminalType {
		name, param, value, include, except
	}

	private TerminalType type;
	private String dataBody;

	public TerminalBody(TerminalType t, String dataBod) {
		this.type = t;
		this.dataBody = dataBod;
	}

	public ArrayList<String> printRncFormat() {
		switch(type) {
			case value: {
				return new ArrayList<>(Collections.singletonList("\"" + dataBody + "\""));
			}
			case param: {
				return new ArrayList<>(Collections.singletonList("pattern = \"" + dataBody + "\""));
			}
			case name: {
				return new ArrayList<>(Collections.singletonList(dataBody));
			}
			case include: {
				return new ArrayList<>(Collections.singletonList(dataBody));
			}
			case except: {
				return new ArrayList<>(Collections.singletonList(" - "+dataBody));
			}
			default: {
				System.err.println("ERROR: TerminalType wurde nicht zugeordnet.");
				System.exit(1);
				return null;
			}
		}
	}
}
