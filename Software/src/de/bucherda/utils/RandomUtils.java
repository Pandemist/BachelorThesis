package de.bucherda.utils;

import de.svenjacobs.loremipsum.LoremIpsum;
import nl.flotsam.xeger.Xeger;

public class RandomUtils {

	public static int generateRandom(int lower, int upper) {
		return (int) ((Math.random() * (upper - lower)) + lower);
	}

	public static double generateRandom(double lower, double upper) {
		return (Math.random() * (upper - lower)) + lower;
	}

	public static long generateRandom(long lower, long upper) {
		return (Math.round(Math.random() * (upper - lower)) + lower);
	}

	public static long generateRandomUnsigned(long lower, long upper) {
		return (Math.round(Math.random() * (upper - lower)) + lower);
	}

	public static String generateRandomBin(int length) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < length; i++) {
			if(Math.random()<0.5) {
				sb.append("0");
			}else{
				sb.append("1");
			}
		}
		return sb.toString();
	}

	public static float generateRandom(float lower, float upper) {
		return (float) (Math.random() * (upper - lower) + lower);
	}

	public static byte generateRandom(byte lower, byte upper) {
		return (byte) ((int) (Math.random() * (upper - lower)) + lower);
	}

	public static String generateRandom(int length) {
		LoremIpsum ls = new LoremIpsum();
		return ls.getWords(length);
	}

	public static String generateRandom(String regex) {
	//	return "REGEXED";
	//	System.out.println("Regex: "+regex);
		Xeger generator = new Xeger(regex);
		return generator.generate();
		//	Generex generex = new Generex(regex);
	//	return generex.random();
	}

}
