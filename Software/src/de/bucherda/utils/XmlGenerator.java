package de.bucherda.utils;

import org.jdom2.Element;

import java.util.HashMap;

import static de.bucherda.fuzzing.*;
import static de.bucherda.utils.RandomUtils.*;
import static de.bucherda.utils.XmlHelper.*;

public class XmlGenerator {

	public static HashMap<String, String> workWithParameters(Element e) {
		HashMap<String, String> paramMap = new HashMap<>();
		for(Element ele : e.getChildren()) {
			if(ele.getName().toLowerCase().equals("param")) {
				paramMap.put(ele.getAttributeValue("name"), ele.getText());
			}
		}
		return paramMap;
	}

	public static String generateData(Element e) {
		//Generate Datasets
		HashMap<String, String> paramMap = workWithParameters(e);
		int length = -1;
		if(paramMap.containsKey("length")) {
			length = Integer.parseInt(paramMap.get("length"));
		}
		if(paramMap.containsKey("totalDigits")) {
			length = Integer.parseInt(paramMap.get("totalDigits"));
		}
		if(paramMap.containsKey("minLength") && paramMap.containsKey("maxLength")) {
			length = generateRandom(Integer.parseInt(paramMap.get("minLength")), Integer.parseInt(paramMap.get("maxLength")));
		}
		if(paramMap.containsKey("pattern")) {
			return generateRandom(paramMap.get("pattern"));
		}
		String value = "";
		//	return "DEMO DATA";
		try {
			switch(getElementAttributeIfExist(e, "type").toLowerCase()) {
				case "string": {
					/*
						length
						minLength
						maxLength
						pattern
					enumeration
						whiteSpace
					 */
					if(length != -1) {
						value = generateRandom(length);
					}else{
						value = generateRandom(10);
					}
					break;
				}
				case "boolean": {
					if(Math.random()>0.5) {
						value = "true";
					}else{
						value = "false";
					}
					break;
				}
				case "decimal": {
					/*
						totalDigits
					fractionDigits
						pattern
						whiteSpace
					enumeration
					maxInclusive
					maxExclusive
					minInclusive
					minExclusive
					 */
					float upper = Float.MAX_VALUE;
					float lower = Float.MIN_VALUE;
					if(paramMap.containsKey("maxInclusive")) {
						upper = Float.parseFloat(paramMap.get("maxInclusive"));
					}
					if(paramMap.containsKey("maxExclusive")) {
						upper = Float.parseFloat(paramMap.get("maxExclusive"))-1;
					}
					if(paramMap.containsKey("minInclusive")) {
						lower = Float.parseFloat(paramMap.get("minInclusive"));
					}
					if(paramMap.containsKey("minExclusive")) {
						lower = Float.parseFloat(paramMap.get("minExclusive"))+1;
					}
					if(paramMap.containsKey("totalDigits")) {
						upper = getFloatWithNDigits(Integer.parseInt(paramMap.get("totalDigits")));
					}
					if(paramMap.containsKey("fractionDigits")) {
						//Kleinste Abweichung
					}

					value = String.valueOf(generateRandom(lower, upper));
					break;
				}
				case "float": {
					/*
						pattern
				    enumeration
				        whiteSpace
				    maxInclusive
				    maxExclusive
				    minInclusive
				    minExclusive
					 */
					float upper = Float.MAX_VALUE;
					float lower = Float.MIN_VALUE;
					if(paramMap.containsKey("maxInclusive")) {
						upper = Float.parseFloat(paramMap.get("maxInclusive"));
					}
					if(paramMap.containsKey("maxExclusive")) {
						upper = Float.parseFloat(paramMap.get("maxExclusive"))-1;
					}
					if(paramMap.containsKey("minInclusive")) {
						lower = Float.parseFloat(paramMap.get("minInclusive"));
					}
					if(paramMap.containsKey("minExclusive")) {
						lower = Float.parseFloat(paramMap.get("minExclusive"))+1;
					}
					if(paramMap.containsKey("totalDigits")) {
						upper = getFloatWithNDigits(Integer.parseInt(paramMap.get("totalDigits")));
					}
					if(paramMap.containsKey("fractionDigits")) {
						//TODO: Kleinste Abweichung
					}

					value = String.valueOf(generateRandom(lower, upper));
					break;
				}
				case "double": {
					//TODO
					/*
						pattern
				    enumeration
				        whiteSpace
				    maxInclusive
				    maxExclusive
				    minInclusive
				    minExclusive
					 */
					double upper = Float.MAX_VALUE;
					double lower = Float.MIN_VALUE;
					if(paramMap.containsKey("maxInclusive")) {
						upper = Double.parseDouble(paramMap.get("maxInclusive"));
					}
					if(paramMap.containsKey("maxExclusive")) {
						upper = Double.parseDouble(paramMap.get("maxExclusive"))-1;
					}
					if(paramMap.containsKey("minInclusive")) {
						lower = Double.parseDouble(paramMap.get("minInclusive"));
					}
					if(paramMap.containsKey("minExclusive")) {
						lower = Double.parseDouble(paramMap.get("minExclusive"))+1;
					}
					if(paramMap.containsKey("totalDigits")) {
						upper = getFloatWithNDigits(Integer.parseInt(paramMap.get("totalDigits")));
					}
					if(paramMap.containsKey("fractionDigits")) {
						//TODO: Kleinste Abweichung
					}

					value = String.valueOf(generateRandom(lower, upper));
					break;
				}
				case "duration": {
					//TODO
					/*
				        pattern
				    enumeration
				        whiteSpace
				    maxInclusive
				    maxExclusive
				    minInclusive
				    minExclusive
					 */
					int years = generateRandom(0, 20);
					int months = generateRandom(0, 12);
					int days = generateRandom(0, 30);
					int hours = generateRandom(0, 24);
					int minutes = generateRandom(0, 60);
					value = "P"+years+"Y"+months+"M"+days+"DT"+hours+"H"+minutes+"M";
					break;
				}
				case "datetime": {
					//TODO
					/*
				        pattern
				    enumeration
				        whiteSpace
				    maxInclusive
				    maxExclusive
				    minInclusive
				    minExclusive
					 */
					int years = generateRandom(1000, 9999);
					int months = generateRandom(0, 12);
					int days = generateRandom(0, 30);
					int hours = generateRandom(0, 24);
					int minutes = generateRandom(0, 60);
					int seconds = generateRandom(0, 60);
					value = years+"-"+months+"-"+days+"T"+hours+":"+minutes+":"+seconds;
					break;
				}
				case "time": {
					//TODO
					/*
				        pattern
				    enumeration
				        whiteSpace
				    maxInclusive
				    maxExclusive
				    minInclusive
				    minExclusive
					 */
					int hours = generateRandom(0, 24);
					int minutes = generateRandom(0, 60);
					int seconds = generateRandom(0, 60);
					int ms = generateRandom(0, 999);
					value = hours+":"+minutes+":"+seconds+":"+ms;
					break;
				}
				case "date": {
					//TODO
					int years = generateRandom(1000, 9999);
					int months = generateRandom(0, 12);
					int days = generateRandom(0, 30);
					value = years+"-"+months+"-"+days;
					break;
				}
				case "gyearmonth": {
					//TODO
					/*
				    pattern
				    enumeration
				    whiteSpace
				    maxInclusive
				    maxExclusive
				    minInclusive
				    minExclusive
					 */
					int years = generateRandom(1000, 9999);
					int months = generateRandom(0, 12);
					value = years+"-"+months;
					break;
				}
				case "gyear": {
					//TODO
					/*
				    pattern
				    enumeration
				    whiteSpace
				    maxInclusive
				    maxExclusive
				    minInclusive
				    minExclusive
					 */
					int years = generateRandom(1000, 9999);
					value = String.valueOf(years);
					break;
				}
				case "gmonthday": {
					//TODO
					/*
				    pattern
				    enumeration
				    whiteSpace
				    maxInclusive
				    maxExclusive
				    minInclusive
				    minExclusive
					 */
					int months = generateRandom(0, 12);
					int days = generateRandom(0, 30);
					value = months+"-"+days;
					break;
				}
				case "gday": {
					//TODO
					/*
				    pattern
				    enumeration
				    whiteSpace
				    maxInclusive
				    maxExclusive
				    minInclusive
				    minExclusive
					 */
					int days = generateRandom(0, 30);
					value = String.valueOf(days);
					break;
				}
				case "gmonth": {
					//TODO
					/*
				    pattern
				    enumeration
				    whiteSpace
				    maxInclusive
				    maxExclusive
				    minInclusive
				    minExclusive
					 */
					int months = generateRandom(0, 12);
					value = String.valueOf(months);
					break;
				}
				case "hexbinary": {
					//TODO
					/*
				    length
				    minLength
				    maxLength
				    pattern
				    enumeration
				    whiteSpace
					 */
					value = generateRandomBin(16);
					break;
				}
				case "base64binary": {
					//TODO
					/*
				    length
				    minLength
				    maxLength
				    pattern
				    enumeration
				    whiteSpace
					 */
					value = generateRandomBin(64);
					break;
				}
				case "anyuri": {
					//TODO
					/*
				    length
				    minLength
				    maxLength
				    pattern
				    enumeration
				    whiteSpace
					 */
					value = "http://hu-berlin.de";
			//		value = generateRandom("[A-Za-z0-9](([_\\.\\-]?[a-zA-Z0-9]+)*)@([A-Za-z0-9]+)(([\\.\\-]?[a-zA-Z0-9]+)*)\\.([A-Za-z]{2,})");
					break;
				}
				case "qname": {
					//TODO
					/*
					length
					minLength
					maxLength
					pattern
					enumeration
					whiteSpace
					 */
					value = generateRandom("((([a-zA-Z] | '_') ([a-zA-Z] | [0-9] | '.' | '-' | '_')*) ':')? (([a-zA-Z] | '_') ([a-zA-Z] | [0-9] | '.' | '-' | '_')*)");
					break;
				}
				case "notation": {
					//TODO
					/*
					length
				    minLength
				    maxLength
				    pattern
				    enumeration
				    whiteSpace
					 */
					break;
				}
				case "normalizedstring": {
					//TODO
					/*
				    length
				    minLength
				    maxLength
				    pattern
				    enumeration
				    whiteSpace
					 */
					if(length!=-1) {
						value = generateRandom(length);
					}else{
						value = generateRandom(generateRandom(0, 100));
					}
					value = value.replaceAll("[\r\t\n]+", "");
					break;
				}
				case "token": {
					//TODO
					/*
				    length
				    minLength
				    maxLength
				    pattern
				    enumeration
				    whiteSpace
					 */
					if(length!=-1) {
						value = generateRandom(length);
					}else{
						value = generateRandom(generateRandom(0, 100));
					}
					value = value.replaceAll("[\r\t\n ]+", "");
					break;
				}
				case "language": {
					//TODO
					/*
				    length
				    minLength
				    maxLength
				    pattern
				    enumeration
				    whiteSpace
					 */
					value = generateRandom("[a-zA-Z]{1,8}(-[a-zA-Z0-9]{1,8})*");
					break;
				}
				case "nmtoken": {
					//TODO
					/*
				    length
				    minLength
				    maxLength
				    pattern
				    enumeration
				    whiteSpace
					 */
					//Letter | Digit | '.' | '-' | '_' | ':' | CombiningChar | Extender
					value = generateRandom("([a-zA-Z])+");
					break;
				}
				case "nmtokens": {
					//TODO
					/*
				    length
				    minLength
				    maxLength
				    enumeration
				    whiteSpace
				    pattern
					 */
					//Letter | Digit | '.' | '-' | '_' | ':' | CombiningChar | Extender (%20)*
					value = generateRandom("([a-zA-Z])+(%20([a-zA-Z])+)*");
					break;
				}
				case "name": {
					//TODO
					/*
				    length
				    minLength
				    maxLength
				    pattern
				    enumeration
				    whiteSpace
					 */
					//(Letter | '_' | ':') ( NameChar)*
					value = generateRandom("([a-zA-Z])+");
					break;
				}
				case "ncname": {
					//TODO
					/*
				    length
				    minLength
				    maxLength
				    pattern
				    enumeration
				    whiteSpace
					 */
					//(Letter | '_') (NCNameChar)*
					//Name ohne :
					value = generateRandom("([a-zA-Z])+");
					break;
				}
				case "id": {
					//TODO
					/*
				    length
				    minLength
				    maxLength
				    pattern
				    enumeration
				    whiteSpace
					 */
					//(Letter | '_' | ':') ( NameChar)*
					value = generateRandom("([a-zA-Z])+");
					break;
				}
				case "idref": {
					//TODO
					/*
					length
				    minLength
				    maxLength
				    pattern
				    enumeration
				    whiteSpace
					 */
					//(Letter | '_' | ':') ( NameChar)*
					value = generateRandom("([a-zA-Z])+");
					break;
				}
				case "idrefs": {
					//TODO
					/*
				    length
				    minLength
				    maxLength
				    enumeration
				    whiteSpace
				    pattern
					 */
					//((Letter | '_' | ':') ( NameChar)*) (%20 (Letter | '_' | ':') ( NameChar)*)
					value = generateRandom("([a-zA-Z])+(%20([a-zA-Z])+)*");
					break;
				}
				case "entity": {
					//TODO
					/*
				    length
				    minLength
				    maxLength
				    pattern
				    enumeration
				    whiteSpace
					 */
					//(Letter | '_' | ':') ( NameChar)*
					value = generateRandom("([a-zA-Z])+");
					break;
				}
				case "entities": {
					//TODO
					/*
				    length
				    minLength
				    maxLength
				    enumeration
				    whiteSpace
				    pattern
					 */
					//((Letter | '_' | ':') ( NameChar)*) (%20 (Letter | '_' | ':') ( NameChar)*)
					value = generateRandom("([a-zA-Z])+(%20([a-zA-Z])+)*");
					break;
				}
				case "integer": {
					//TODO
					/*
				        totalDigits
				    fractionDigits
				        pattern
				        whiteSpace
				    enumeration
					    maxInclusive
					    maxExclusive
					    minInclusive
					    minExclusive
					 */
					int upper = Integer.MAX_VALUE;
					int lower = Integer.MIN_VALUE;
					if(paramMap.containsKey("maxInclusive")) {
						upper = Integer.parseInt(paramMap.get("maxInclusive"));
					}
					if(paramMap.containsKey("maxExclusive")) {
						upper = Integer.parseInt(paramMap.get("maxExclusive"))-1;
					}
					if(paramMap.containsKey("minInclusive")) {
						lower = Integer.parseInt(paramMap.get("minInclusive"));
					}
					if(paramMap.containsKey("minExclusive")) {
						lower = Integer.parseInt(paramMap.get("minExclusive"))+1;
					}
					if(paramMap.containsKey("totalDigits")) {
						upper = getIntWithNDigits(Integer.parseInt(paramMap.get("totalDigits")));
					}
					if(paramMap.containsKey("fractionDigits")) {
						//TODO: Kleinste Abweichung
					}

					value = String.valueOf(generateRandom(lower, upper));
					break;
				}
				case "nonpositiveinteger": {
					//TODO
					/*
				        totalDigits
				    fractionDigits
				        pattern
				        whiteSpace
				    enumeration
					    maxInclusive
					    maxExclusive
					    minInclusive
					    minExclusive
					 */
					int upper = 0;
					int lower = Integer.MIN_VALUE;
					if(paramMap.containsKey("maxInclusive")) {
						upper = Integer.parseInt(paramMap.get("maxInclusive"));
					}
					if(paramMap.containsKey("maxExclusive")) {
						upper = Integer.parseInt(paramMap.get("maxExclusive"))-1;
					}
					if(paramMap.containsKey("minInclusive")) {
						lower = Integer.parseInt(paramMap.get("minInclusive"));
					}
					if(paramMap.containsKey("minExclusive")) {
						lower = Integer.parseInt(paramMap.get("minExclusive"))+1;
					}
					if(paramMap.containsKey("totalDigits")) {
						upper = getIntWithNDigits(Integer.parseInt(paramMap.get("totalDigits")));
					}
					if(paramMap.containsKey("fractionDigits")) {
						//TODO: Kleinste Abweichung
					}

					value = String.valueOf(generateRandom(lower, upper));
					break;
				}
				case "negativinteger": {
					//TODO
					/*
				        totalDigits
				    fractionDigits
					    pattern
					    whiteSpace
				    enumeration
					    maxInclusive
					    maxExclusive
					    minInclusive
					    minExclusive
					 */
					int upper = -1;
					int lower = Integer.MIN_VALUE;
					if(paramMap.containsKey("maxInclusive")) {
						upper = Integer.parseInt(paramMap.get("maxInclusive"));
					}
					if(paramMap.containsKey("maxExclusive")) {
						upper = Integer.parseInt(paramMap.get("maxExclusive"))-1;
					}
					if(paramMap.containsKey("minInclusive")) {
						lower = Integer.parseInt(paramMap.get("minInclusive"));
					}
					if(paramMap.containsKey("minExclusive")) {
						lower = Integer.parseInt(paramMap.get("minExclusive"))+1;
					}
					if(paramMap.containsKey("totalDigits")) {
						upper = getIntWithNDigits(Integer.parseInt(paramMap.get("totalDigits")));
					}
					if(paramMap.containsKey("fractionDigits")) {
						//TODO: Kleinste Abweichung
					}

					value = String.valueOf(generateRandom(lower, upper));
					break;
				}
				case "long": {
					//TODO
					/*
				    totalDigits
				    fractionDigits
				        pattern
				        whiteSpace
				    enumeration
				        maxInclusive
				        maxExclusive
				        minInclusive
				        minExclusive
					 */
					long upper = Long.MAX_VALUE;
					long lower = Long.MIN_VALUE;
					if(paramMap.containsKey("maxInclusive")) {
						upper = Long.parseLong(paramMap.get("maxInclusive"));
					}
					if(paramMap.containsKey("maxExclusive")) {
						upper = Long.parseLong(paramMap.get("maxExclusive"))-1;
					}
					if(paramMap.containsKey("minInclusive")) {
						lower = Long.parseLong(paramMap.get("minInclusive"));
					}
					if(paramMap.containsKey("minExclusive")) {
						lower = Long.parseLong(paramMap.get("minExclusive"))+1;
					}
					if(paramMap.containsKey("totalDigits")) {
						upper = getLongWithNDigits(Long.parseLong(paramMap.get("totalDigits")));
					}
					if(paramMap.containsKey("fractionDigits")) {
						//TODO: Kleinste Abweichung
					}

					value = String.valueOf(generateRandom(lower, upper));
					break;
				}
				case "int": {
					//TODO
					/*
				        totalDigits
				    fractionDigits
				        pattern
				        whiteSpace
				    enumeration
					    maxInclusive
					    maxExclusive
					    minInclusive
					    minExclusive
					 */
					int upper = Integer.MAX_VALUE;
					int lower = Integer.MIN_VALUE;
					if(paramMap.containsKey("maxInclusive")) {
						upper = Integer.parseInt(paramMap.get("maxInclusive"));
					}
					if(paramMap.containsKey("maxExclusive")) {
						upper = Integer.parseInt(paramMap.get("maxExclusive"))-1;
					}
					if(paramMap.containsKey("minInclusive")) {
						lower = Integer.parseInt(paramMap.get("minInclusive"));
					}
					if(paramMap.containsKey("minExclusive")) {
						lower = Integer.parseInt(paramMap.get("minExclusive"))+1;
					}
					if(paramMap.containsKey("totalDigits")) {
						upper = getIntWithNDigits(Integer.parseInt(paramMap.get("totalDigits")));
					}
					if(paramMap.containsKey("fractionDigits")) {
						//TODO: Kleinste Abweichung
					}

					value = String.valueOf(generateRandom(lower, upper));
					break;
				}
				case "short": {
					//TODO
					/*
				        totalDigits
				    fractionDigits
				        pattern
				        whiteSpace
				    enumeration
					    maxInclusive
					    maxExclusive
					    minInclusive
					    minExclusive
					 */
					short upper = Short.MAX_VALUE;
					short lower = Short.MIN_VALUE;
					if(paramMap.containsKey("maxInclusive")) {
						upper = Short.parseShort(paramMap.get("maxInclusive"));
					}
					if(paramMap.containsKey("maxExclusive")) {
						upper = (short) (Short.parseShort(paramMap.get("maxExclusive")) - 1);
					}
					if(paramMap.containsKey("minInclusive")) {
						lower = Short.parseShort(paramMap.get("minInclusive"));
					}
					if(paramMap.containsKey("minExclusive")) {
						lower = (short) (Short.parseShort(paramMap.get("maxExclusive")) + 1);
					}
					if(paramMap.containsKey("totalDigits")) {
						upper = getByteWithNDigits(Short.parseShort(paramMap.get("totalDigits")));
					}
					if(paramMap.containsKey("fractionDigits")) {
						//TODO: Kleinste Abweichung
					}

					value = String.valueOf(generateRandom(lower, upper));
					break;
				}
				case "byte": {
					//TODO
					/*
				        totalDigits
				    fractionDigits
				        pattern
				        whiteSpace
				    enumeration
					    maxInclusive
					    maxExclusive
					    minInclusive
					    minExclusive
					 */
					byte upper = Byte.MAX_VALUE;
					byte lower = Byte.MIN_VALUE;
					if(paramMap.containsKey("maxInclusive")) {
						upper = Byte.parseByte(paramMap.get("maxInclusive"));
					}
					if(paramMap.containsKey("maxExclusive")) {
						upper = (byte) (Byte.parseByte(paramMap.get("maxExclusive")) - 1);
					}
					if(paramMap.containsKey("minInclusive")) {
						lower = Byte.parseByte(paramMap.get("minInclusive"));
					}
					if(paramMap.containsKey("minExclusive")) {
						lower = (byte) (Byte.parseByte(paramMap.get("maxExclusive")) + 1);
					}
					if(paramMap.containsKey("totalDigits")) {
						upper = getByteWithNDigits(Byte.parseByte(paramMap.get("totalDigits")));
					}
					if(paramMap.containsKey("fractionDigits")) {
						//TODO: Kleinste Abweichung
					}

					value = String.valueOf(generateRandom(lower, upper));
					break;
				}
				case "nonnegativeinteger": {
					//TODO
					/*
				        totalDigits
				    fractionDigits
				        pattern
				        whiteSpace
				    enumeration
				        maxInclusive
				        maxExclusive
				        minInclusive
				        minExclusive
					 */
					int upper = Integer.MAX_VALUE;
					int lower = 0;
					if(paramMap.containsKey("maxInclusive")) {
						upper = Integer.parseInt(paramMap.get("maxInclusive"));
					}
					if(paramMap.containsKey("maxExclusive")) {
						upper = Integer.parseInt(paramMap.get("maxExclusive"))-1;
					}
					if(paramMap.containsKey("minInclusive")) {
						lower = Integer.parseInt(paramMap.get("minInclusive"));
					}
					if(paramMap.containsKey("minExclusive")) {
						lower = Integer.parseInt(paramMap.get("minExclusive"))+1;
					}
					if(paramMap.containsKey("totalDigits")) {
						upper = getIntWithNDigits(Integer.parseInt(paramMap.get("totalDigits")));
					}
					if(paramMap.containsKey("fractionDigits")) {
						//TODO: Kleinste Abweichung
					}

					value = String.valueOf(generateRandom(lower, upper));
					break;
				}
				case "unsignedlong": {
					//TODO: Einen Weg für UnsignedLong finden
					/*
				        totalDigits
				    fractionDigits
				        pattern
				        whiteSpace
				    enumeration
				        maxInclusive
				        maxExclusive
				        minInclusive
					 */
					long upper = Long.parseUnsignedLong("4,294,967,295");
					long lower = 0;
					if(paramMap.containsKey("maxInclusive")) {
						upper = Long.parseUnsignedLong(paramMap.get("maxInclusive"));
					}
					if(paramMap.containsKey("maxExclusive")) {
						upper = Long.parseUnsignedLong(paramMap.get("maxExclusive"))-1;
					}
					if(paramMap.containsKey("minInclusive")) {
						lower = Long.parseUnsignedLong(paramMap.get("minInclusive"));
					}
					if(paramMap.containsKey("minExclusive")) {
						lower = Long.parseUnsignedLong(paramMap.get("minExclusive"))+1;
					}

					if(paramMap.containsKey("totalDigits")) {
						upper = getULongWithNDigits(Long.parseUnsignedLong(paramMap.get("totalDigits")));
					}
					if(paramMap.containsKey("fractionDigits")) {
						//TODO: Kleinste Abweichung
					}

					value = generateRandomUnsigned(lower, upper)+"";
					break;
				}
				case "unsignedint": {
					//TODO
					/*
				        totalDigits
				    fractionDigits
				        pattern
				        whiteSpace
				    enumeration
					    maxInclusive
					    maxExclusive
					    minInclusive
					    minExclusive
					 */
					long upper = Long.MAX_VALUE;
					long lower = 0;
					if(paramMap.containsKey("maxInclusive")) {
						upper = Long.parseLong(paramMap.get("maxInclusive"));
					}
					if(paramMap.containsKey("maxExclusive")) {
						upper = Long.parseLong(paramMap.get("maxExclusive")) - 1;
					}
					if(paramMap.containsKey("minInclusive")) {
						lower = Long.parseLong(paramMap.get("minInclusive"));
					}
					if(paramMap.containsKey("minExclusive")) {
						lower = Long.parseLong(paramMap.get("minExclusive")) + 1;
					}
					if(paramMap.containsKey("totalDigits")) {
						upper = getLongWithNDigits(Long.parseLong(paramMap.get("totalDigits")));
					}
					if(paramMap.containsKey("fractionDigits")) {
						//TODO: Kleinste Abweichung
					}

					value = String.valueOf(generateRandom(lower, upper));
					break;
				}
				case "unsignedshort": {
					//TODO
					/*
				        totalDigits
				    fractionDigits
					    pattern
					    whiteSpace
				    enumeration
					    maxInclusive
					    maxExclusive
					    minInclusive
					    minExclusive
					 */
					int upper = Integer.MAX_VALUE;
					int lower = 0;
					if(paramMap.containsKey("maxInclusive")) {
						upper = Integer.parseInt(paramMap.get("maxInclusive"));
					}
					if(paramMap.containsKey("maxExclusive")) {
						upper = Integer.parseInt(paramMap.get("maxExclusive")) - 1;
					}
					if(paramMap.containsKey("minInclusive")) {
						lower = Integer.parseInt(paramMap.get("minInclusive"));
					}
					if(paramMap.containsKey("minExclusive")) {
						lower = Integer.parseInt(paramMap.get("minExclusive")) + 1;
					}
					if(paramMap.containsKey("totalDigits")) {
						upper = getIntWithNDigits(Integer.parseInt(paramMap.get("totalDigits")));
					}
					if(paramMap.containsKey("fractionDigits")) {
						//TODO: Kleinste Abweichung
					}

					value = String.valueOf(generateRandom(lower, upper));
					break;
				}
				case "unsignedbyte": {
					//TODO
					/*
				        totalDigits
				    fractionDigits
				        pattern
				        whiteSpace
				    enumeration
				        maxInclusive
				        maxExclusive
				        minInclusive
				        minExclusive
					 */
					short upper = Short.MAX_VALUE;
					short lower = 0;
					if(paramMap.containsKey("maxInclusive")) {
						upper = Short.parseShort(paramMap.get("maxInclusive"));
					}
					if(paramMap.containsKey("maxExclusive")) {
						upper = (short) (Short.parseShort(paramMap.get("maxExclusive")) - 1);
					}
					if(paramMap.containsKey("minInclusive")) {
						lower = Short.parseShort(paramMap.get("minInclusive"));
					}
					if(paramMap.containsKey("minExclusive")) {
						lower = (short) (Short.parseShort(paramMap.get("minExclusive")) + 1);
					}
					if(paramMap.containsKey("totalDigits")) {
						upper = (short) (getIntWithNDigits(Integer.parseInt(paramMap.get("totalDigits"))));
					}
					if(paramMap.containsKey("fractionDigits")) {
						//TODO: Kleinste Abweichung
					}

					value = String.valueOf(generateRandom(lower, upper));
					break;
				}
				case "positiveinteger": {
					//TODO
					/*
				        totalDigits
				    fractionDigits
					    pattern
					    whiteSpace
				    enumeration
					    maxInclusive
					    maxExclusive
					    minInclusive
					    minExclusive
					 */
					double upper = Integer.MAX_VALUE;
					double lower = 1;
					if(paramMap.containsKey("maxInclusive")) {
						upper = Double.parseDouble(paramMap.get("maxInclusive"));
					}
					if(paramMap.containsKey("maxExclusive")) {
						upper = Double.parseDouble(paramMap.get("maxExclusive"))-(Double.MIN_VALUE);
					}
					if(paramMap.containsKey("minInclusive")) {
						lower = Double.parseDouble(paramMap.get("minInclusive"));
					}
					if(paramMap.containsKey("minExclusive")) {
						lower = Double.parseDouble(paramMap.get("minExclusive"))+(Double.MIN_VALUE);
					}
					break;
				}
				default: {
					System.err.println("ERROR: Dieses Element sollte hier nicht sein. (generateData)");
					System.exit(-1);
				}
			}

			if(paramMap.containsKey("whiteSpace")) {
				switch(paramMap.get("whiteSpace")) {
					case "preserve": {
						//Nothing to do
						break;
					}
					case "replace": {
						value = value.replace("#x9", "#x20");
						value = value.replace("#xA", "#x20");
						value = value.replace("#xD", "#x20");
					}
					case "collapse": {
						value = value.replaceAll("(#x20)+", "#x20");
						break;
					}
				}
			}

			return value;
		}catch(Exception ex) {
			System.err.println("ERROR: Das Data Element hat keinen Typ.");
			System.exit(-1);
		}
		return null;
	}

	public static String generateStringValue(Element e) {
		StringBuilder value = new StringBuilder();
		switch(e.getName().toLowerCase()) {
			case "start": {
				for(Element ch : e.getChildren()) {
					value.append(generateStringValue(ch));
				}
				break;
			}
			case "element": {
				for(Element ch : e.getChildren()) {
					value.append(generateStringValue(ch));
				}
				break;
			}
			case "attribute": {
				for(Element ch : e.getChildren()) {
					value.append(generateStringValue(ch));
				}
				break;
			}
			case "choice": {
				for(Element ch : rp.chooseElement(e, 0)) {
					value.append(generateStringValue(ch));
				}
				break;
			}
			case "optional": {
				for(Element ch : rp.chooseElement(e, 0)) {
					value.append(generateStringValue(ch));
				}
				break;
			}
			case "zeroormore": {
				for(Element ch : rp.chooseElement(e, 0)) {
					value.append(generateStringValue(ch));
				}
				break;
			}
			case "oneormore": {
				for(Element ch : rp.chooseElement(e, 0)) {
					value.append(generateStringValue(ch));
				}
				break;
			}
			case "ref": {
				if(!hasElementThisAttribute(e, "name")) {
					System.err.println("ERROR: Ref has no name Attribute.");
					System.exit(-1);
				}
				Element ref = ruleList.get(e.getAttributeValue("name"));
				for(Element ch : ref.getChildren()) {
					value.append(generateStringValue(ch));
				}
				break;
			}
			case "interleave": {
				for(Element ch : e.getChildren()) {
					value.append(generateStringValue(ch));
				}
				break;
			}
			case "group": {
				for(Element ch : e.getChildren()) {
					value.append(generateStringValue(ch));
				}
				break;
			}
			case "mixed": {
				for(Element ch : e.getChildren()) {
					value.append(generateStringValue(ch));
				}
				break;
			}
			case "list": {
				for(Element ch : e.getChildren()) {
					value.append(generateStringValue(ch));
				}
				break;
			}
			case "empty": {
				for(Element ch : e.getChildren()) {
					value.append(generateStringValue(ch));
				}
				break;
			}
			case "except": {
				for(Element ch : e.getChildren()) {
					value.append(generateStringValue(ch));
				}
				break;
			}
			case "value": {
				for(Element ch : e.getChildren()) {
					value.append(generateStringValue(ch));
				}
				break;
			}
			case "nsname": {
				for(Element ch : e.getChildren()) {
					value.append(generateStringValue(ch));
				}
				break;
			}
			case "name": {
				for(Element ch : e.getChildren()) {
					value.append(generateStringValue(ch));
				}
				break;
			}
			case "anyname": {
				for(Element ch : e.getChildren()) {
					value.append(generateStringValue(ch));
				}
				break;
			}
			case "text": {
				String rndStrg = generateRandom(generateRandom(0, 10));
				rndStrg = rndStrg.replaceAll("[\r\n\t]+", "_");
				value.append(rndStrg);
				for(Element ch : e.getChildren()) {
					value.append(generateStringValue(ch));
				}
				break;
			}
			case "data": {
				value.append(generateData(e));
				for(Element ch : e.getChildren()) {
					value.append(generateStringValue(ch));
				}
				break;
			}
			case "param": {
				for(Element ch : e.getChildren()) {
					value.append(generateStringValue(ch));
				}
				break;
			}
			case "parentref": {
				for(Element ch : e.getChildren()) {
					value.append(generateStringValue(ch));
				}
				break;
			}
			case "externalref": {
				for(Element ch : e.getChildren()) {
					value.append(generateStringValue(ch));
				}
				break;
			}
			case "include": {
				for(Element ch : e.getChildren()) {
					value.append(generateStringValue(ch));
				}
				break;
			}
			case "div": {
				for(Element ch : e.getChildren()) {
					value.append(generateStringValue(ch));
				}
				break;
			}
			default: {
				System.err.println("ERROR: Fuzzer stoped at Token "+ e.getName() + ". Could not resolve handling.");
				System.exit(-1);
			}
		}
		return value.toString();
	}

	public static int getIntWithNDigits(int n) {
		StringBuilder s = new StringBuilder();
		for(int i = 0; i < n; i++) {
			s.append("9");
		}
		return Integer.parseInt(s.toString());
	}

	public static long getLongWithNDigits(long n) {
		StringBuilder s = new StringBuilder();
		for(int i = 0; i < n; i++) {
			s.append("9");
		}
		return Integer.parseInt(s.toString());
	}

	public static long getULongWithNDigits(long n) {
		StringBuilder s = new StringBuilder();
		for(int i = 0; i < n; i++) {
			s.append("9");
		}
		return Long.parseUnsignedLong(s.toString());
	}

	public static byte getByteWithNDigits(long n) {
		StringBuilder s = new StringBuilder();
		for(int i = 0; i < n; i++) {
			s.append("9");
		}
		return Byte.parseByte(s.toString());
	}

	public static float getFloatWithNDigits(int n) {
		StringBuilder s = new StringBuilder();
		for(int i = 0; i < (n-1); i++) {
			s.append("9");
		}
		s.append(".9");
		return Integer.parseInt(s.toString());
	}

	public static double getDoubleWithNDigits(int n) {
		StringBuilder s = new StringBuilder();
		for(int i = 0; i < (n-1); i++) {
			s.append("9");
		}
		s.append(".9");
		return Integer.parseInt(s.toString());
	}
}
