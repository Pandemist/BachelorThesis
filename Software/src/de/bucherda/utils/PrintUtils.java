package de.bucherda.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class PrintUtils {

	public static void printHelp() {
		System.out.println("Falsche Parametern, benutze:");
		System.out.println("LibreOfficeFuzzer [0]");
		System.out.println("[0] - kann sein:");
		System.out.println("   g (Erstellt die Grammatik)");
		System.out.println("   f [1]");
		System.out.println("   h");
		System.out.println("[1] - Anzahl der zu fuzzenden Dokumente. (-1 = unendlich viele)");
		System.exit(1);
	}

	public static void printToFile(String path, StringBuilder stringBuilder) {
		File file = new File(path);
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			writer.write(stringBuilder.toString());
		}catch(Exception e) {
			System.err.println("ERROR: Die Grammatik konnte nicht geschrieben werden.");
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static String calcIndent(int s) {
		StringBuilder b = new StringBuilder();
		for(int i = 0; i < s; i++) {
			b.append(" ");
		}
		return b.toString();
	}
}
