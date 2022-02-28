package com.varunirani.utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BinaryUtils {
	public static String stringToBinary(byte[] input) {
		StringBuilder result = new StringBuilder();
		for (byte b : input) {
			result.append(bigIntToNBitBinary(BigInteger.valueOf((int) b), 8));
		}
		return result.toString();
	}

	public static String left(String binary, int size) {
		return binary.substring(0, size);
	}

	public static String right(String binary, int size) {
		return binary.substring(size);
	}

	public static String bigIntToNBitBinary(BigInteger decimal, int size) {
		String result = decimal.toString(2);
		if (result.length() < size) {
			int remaining = size - result.length();
			result = "0".repeat(remaining) + result;
		}
		return result;
	}

	public static String prettyBinary(String binary, int blockSize, String separator) {
		ArrayList<String> result = new ArrayList<>();
		int index = 0;
		while (index < binary.length()) {
			result.add(binary.substring(index, Math.min(index + blockSize, binary.length())));
			index += blockSize;
		}
		return String.join(separator, result);
	}

	public static ArrayList<String> binaryStringToArrayList(String binary, int blockSize) {
		return new ArrayList<>(List.of(prettyBinary(binary, blockSize, " ").split(" ")));
	}

	public static String binaryToString(String binary) {
		ArrayList<String> blocks = binaryStringToArrayList(binary, 8);
		int plainTextLength = blocks.size() - Integer.parseInt(blocks.get(blocks.size() - 1), 2);
		plainTextLength = plainTextLength == 0 ? 8 : plainTextLength;
		List<String> plainTextBlocks = blocks.subList(0, plainTextLength);
		return plainTextBlocks
				.stream()
				.map(b -> Integer.parseInt(b, 2))
				.map(Character::toString)
				.collect(Collectors.joining());
	}
}
