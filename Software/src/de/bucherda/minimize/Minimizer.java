package de.bucherda.minimize;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

import static de.bucherda.LibreOfficeFuzzer.*;

public class Minimizer {

	public static int count = 0;

	public static Element minimize(Element ele, int lastReturnCcode, String pathName) {
		if(acceptedReturnCodes.contains(lastReturnCcode)) {
			//Dokument wird bereits akzeptiert
			return ele;
		}

		int newReturnCode = -1;

		ArrayList<Integer> permanentRemove = new ArrayList<>();

		int eChilds = ele.getChildren().size();

		// Über die Kinder iterieren um zu prüfen, welche entfernt werden können
		for(int i = 0; i < eChilds;i++) {
//			System.out.println("Childs: " + ele.getChildren().size());
			Element retEle = ele.getChildren().get(0).detach();
//			System.out.println("Node Name: "+ retEle.getName() + " iterator id:  "+ i);

			Document cDoc = ele.getDocument();
			XMLOutputter xmlOutput = new XMLOutputter();
			xmlOutput.setFormat(Format.getPrettyFormat());
			try {
				xmlOutput.output(cDoc, new FileWriter(pathName+"content_tmp_" + count + ".xml"));
			}catch(IOException ex) {
				ex.printStackTrace();
			}

			newReturnCode = changeTester(pathName, "content_tmp_" + count + ".xml");
//			System.out.println("Aktuelle Iteration: "+ count);
			count++;

			ele.addContent(retEle);
			//Temporäre Datei löschen
			new File(pathName+"content_tmp_" + count + ".xml").delete();

			if(acceptedReturnCodes.contains(newReturnCode) && lastReturnCcode == newReturnCode) {
				//Datei wird jetzt akzeptiert, wurde es aber vorher nicht akzeptiert
				//Knoten enthält einen Fehler.
			}else{
				//Datei wird jetzt NICHT akzeptiert, wurde es vorher auch nicht
				//Entfernen zulässig, next child
				permanentRemove.add(0);
			}
		}

		Collections.reverse(permanentRemove);

//		System.out.println("Childs 2: " + eChilds);
		// Zur entfernung vorgesehene Kinder entfernen
		for(int k : permanentRemove) {
			ele.getChildren().remove(k);
		}

		for(Element child : ele.getChildren()) {
			child = minimize(child, newReturnCode, pathName);
		}
		return ele;
	}


	public static int changeTester(String pathName, String docName) {
		int scriptExitCode;
		try {
			System.out.println("Log: Für Datei " + docName + " wurde das Orakel gestartet.");
			ProcessBuilder p = new ProcessBuilder(packScript, pathName, docName, templatePath, String.valueOf(officeTimeout));

			Process q = p.start();

			BufferedReader in =
					new BufferedReader(new InputStreamReader(q.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
	//			System.out.println(inputLine);
			}
			in.close();
			q.waitFor();
			scriptExitCode = q.exitValue();
			System.out.println("Log: Für Datei " + docName + " ist das Orakel beendet. Returncode: "+scriptExitCode);

		} catch (Exception e) {
			System.out.println(e);
			scriptExitCode = 0;
			System.exit(1);
		}
		return scriptExitCode;
	}
}
