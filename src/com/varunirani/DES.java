package com.varunirani;

import com.varunirani.utils.BinaryUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class DES {
	private static final HashMap<Integer, Integer[][]> s_boxes = new HashMap<>();
	private static final ArrayList<Integer> leftShiftSchedule = new ArrayList<>();
	private static final ArrayList<String> keys = new ArrayList<>();

	public static ArrayList<String> get64BitBlocks(String binary) {
		ArrayList<String> stringArrayList = BinaryUtils.binaryStringToArrayList(binary, 8);
		ArrayList<String> binary64BitBlocks = new ArrayList<>();
		int i = 0, remaining;
		boolean moreThanEight = false;
		boolean exactlyEight = stringArrayList.size() % 8 == 0;
		if (stringArrayList.size() < 8) {
			remaining = 8 - stringArrayList.size();
		} else {
			moreThanEight = true;
			remaining = stringArrayList.size() % 8;
		}
		// Make 8 groups of 8 bits
		if (moreThanEight || exactlyEight) {
			for (; i < stringArrayList.size() - remaining; i += 8) {
				String p = "";
				for (int j = 0; j < 8; j++) {
					String character = stringArrayList.get(i + j);
					p = String.join("", p, character);
				}
				binary64BitBlocks.add(p);
			}
		}
		// Pad additional 8 bit zero groups for remaining characters (Electronic Code Book) with CMS Padding
		if (remaining > 0) {
			int additional = moreThanEight ? 8 - remaining : remaining;
			String remainingString = "";
			for (; i < stringArrayList.size() + additional; i++) {
				if (i < stringArrayList.size()) {
					String character = stringArrayList.get(i);
					remainingString = String.join("", remainingString, character);
				} else {
					remainingString = String.join("", remainingString,
							BinaryUtils.bigIntToNBitBinary(BigInteger.valueOf(additional), 8));
				}
			}
			binary64BitBlocks.add(remainingString);
		}
		if (exactlyEight)
			binary64BitBlocks.add("00001000".repeat(8));
		return binary64BitBlocks;
	}

	public static void initializeKeySchedule() {
		// 1122222212222221
		leftShiftSchedule.add(1);
		for (int i = 0; i < 2; i++) {
			leftShiftSchedule.add(1);
			for (int j = 0; j < 6; j++)
				leftShiftSchedule.add(2);
		}
		leftShiftSchedule.add(1);
	}

	public static void initializeSBoxes() {
		s_boxes.put(0, new Integer[][]{
				{14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
				{0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
				{4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
				{15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
		});
		s_boxes.put(1, new Integer[][]{
				{15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
				{3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
				{0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
				{13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9},
		});
		s_boxes.put(2, new Integer[][]{
				{10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
				{13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
				{13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
				{1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
		});
		s_boxes.put(3, new Integer[][]{
				{7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
				{13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
				{10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
				{3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
		});
		s_boxes.put(4, new Integer[][]{
				{2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
				{14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
				{4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
				{11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
		});
		s_boxes.put(5, new Integer[][]{
				{12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
				{10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
				{9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
				{4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
		});
		s_boxes.put(6, new Integer[][]{
				{4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
				{13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
				{1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
				{6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
		});
		s_boxes.put(7, new Integer[][]{
				{13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
				{1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
				{7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
				{2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
		});
	}

	private static String substitution(String binary) {
		ArrayList<String> sixBitBlocks = BinaryUtils.binaryStringToArrayList(binary, 6);
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < sixBitBlocks.size(); i++) {
			String sixBitBlock = sixBitBlocks.get(i);
			Integer[][] s_box = s_boxes.get(i);
			int row = Integer.parseInt(String.valueOf(sixBitBlock.charAt(0))
					.concat(String.valueOf(sixBitBlock.charAt(5))), 2);
			int column = Integer.parseInt(sixBitBlock.substring(1, 5), 2);
			result.append(BinaryUtils.bigIntToNBitBinary(
					BigInteger.valueOf(s_box[row][column]), 4));
		}
		return result.toString();
	}

	private static String permutation32Bit(String binary) {
		int[] indices = {
				16, 7, 20, 21, 29, 12, 28, 17,
				1, 15, 23, 26, 5, 18, 31, 10,
				2, 8, 24, 14, 32, 27, 3, 9,
				19, 13, 30, 6, 22, 11, 4, 25
		};
		StringBuilder result = new StringBuilder();
		for (int index : indices) {
			result.append(binary.charAt(index - 1));
		}
		return result.toString();
	}

	private static String inverseInitialPermutation(String binary) {
		StringBuilder result = new StringBuilder();
		int[] rowStartingNumbers = {40, 39, 38, 37, 36, 35, 34, 33};
		ArrayList<Integer> indices = new ArrayList<>();
		for (int rowStart : rowStartingNumbers) {
			indices.add(rowStart);
			for (int i = 0; i < 7; i += 2) {
				indices.add(indices.get(indices.size() - 1) - 32);
				if (i < 6)
					indices.add(indices.get(indices.size() - 1) + 40);
			}
		}
		for (int index : indices) {
			result.append(binary.charAt(index - 1));
		}
		return result.toString();
	}

	private static String initialPermutation(String block) {
		StringBuilder result = new StringBuilder();
		int[] rowStartingNumbers = {58, 60, 62, 64, 57, 59, 61, 63};
		ArrayList<Integer> indices = new ArrayList<>();
		for (int rowStart : rowStartingNumbers) {
			int x = rowStart;
			indices.add(x);
			while (x - 8 > 0) {
				indices.add(x - 8);
				x -= 8;
			}
		}
		for (int index : indices) {
			result.append(block.charAt(index - 1));
		}
		return result.toString();
	}

	private static String expansion(String binary) {
		StringBuilder result = new StringBuilder();
		int[] rowStartingNumbers = {32, 4, 8, 12, 16, 20, 24, 28};
		int[] rowEndingNumbers = {5, 9, 13, 17, 21, 25, 29, 1};
		ArrayList<Integer> indices = new ArrayList<>();
		for (int i = 0; i < 32; i++)
			indices.add(i + 1);
		for (int i = 0; i < rowStartingNumbers.length; i++)
			indices.add(i * 5, rowStartingNumbers[i]);
		for (int i = 0; i < rowEndingNumbers.length; i++)
			indices.add((i + 1) * 5 + i, rowEndingNumbers[i]);

		for (int index : indices)
			result.append(binary.charAt(index - 1));
		return result.toString();
	}

	private static String get48BitPermutedKey(String _56BitKey) {
		int[] indices = {
				14, 17, 11, 24, 1, 5, 3, 28,
				15, 6, 21, 10, 23, 19, 12, 4,
				26, 8, 16, 7, 27, 20, 13, 2,
				41, 52, 31, 37, 47, 55, 30, 40,
				51, 45, 33, 48, 44, 49, 39, 56,
				34, 53, 46, 42, 50, 36, 29, 32
		};
		StringBuilder result = new StringBuilder();
		for (int index : indices) {
			result.append(_56BitKey.charAt(index - 1));
		}
		return result.toString();
	}

	public static String get56BitPermutedKey(String _64BitKey) {
		StringBuilder keyStringBuilder = new StringBuilder();
		int[] first28BitNumbers = {57, 58, 59, 60};
		int[] last28BitNumbers = {63, 62, 61, 28};
		ArrayList<Integer> final56BitIndices = new ArrayList<>();
		ArrayList<Integer> last28BitIndices = new ArrayList<>();
		create28Indices(first28BitNumbers, final56BitIndices);
		create28Indices(last28BitNumbers, last28BitIndices);
		final56BitIndices.addAll(last28BitIndices);
		for (int index : final56BitIndices)
			keyStringBuilder.append(_64BitKey.charAt(index - 1));
		return keyStringBuilder.toString();
	}

	private static BigInteger circularLeftShift(BigInteger number, int shiftAmount, int size) {
		BigInteger result = number;
		for (int i = 0; i < shiftAmount; i++) {
			result = result.shiftLeft(1);
			if (result.testBit(size)) {
				result = result.clearBit(size).setBit(0);
			}
		}
		return result;
	}

	private static void create28Indices(int[] numberList, ArrayList<Integer> indices) {
		for (int i = 0; i < numberList.length; i++) {
			int last28BitNumber = numberList[i];
			indices.add(last28BitNumber);
			boolean lastNumber = i == numberList.length - 1;
			for (int j = 0; j < (lastNumber ? 3 : 7); j++) {
				indices.add(indices.get((i * 8) + j) - 8);
			}
		}
	}

	public static String encryptBlock(String plainTextBlock, String key) {
		String initialPermutation = DES.initialPermutation(plainTextBlock);
		String left, right, leftKey, rightKey;
		leftKey = BinaryUtils.left(key, 28);
		rightKey = BinaryUtils.right(key, 28);
		left = BinaryUtils.left(initialPermutation, 32);
		right = BinaryUtils.right(initialPermutation, 32);
		for (int i = 0; i < 16; i++) {
			// ROUND START
			// Key Generation START
			leftKey = BinaryUtils.bigIntToNBitBinary(
					DES.circularLeftShift(
							new BigInteger(leftKey, 2), leftShiftSchedule.get(i), 28),
					28);
			rightKey = BinaryUtils.bigIntToNBitBinary(
					DES.circularLeftShift(
							new BigInteger(rightKey, 2), leftShiftSchedule.get(i), 28),
					28);
			String newKey = DES.get48BitPermutedKey(leftKey + rightKey);
			keys.add(newKey);
			// ! Key Generation END
			// Round Function F START
			String leftXORRight = roundFunction(left, right, newKey);
			// ! ROUND END
			// ? Update values for next ROUND
			left = right;
			right = leftXORRight;
		}
		// 32 bit swap and inverse IP
		return inverseInitialPermutation(right + left);
	}

	public static String decryptBlock(String cipherTextBlock) {
		String initialPermutation = DES.initialPermutation(cipherTextBlock);
		String left, right;
		left = BinaryUtils.left(initialPermutation, 32);
		right = BinaryUtils.right(initialPermutation, 32);
		for (int i = 0; i < 16; i++) {
			// ROUND START
			// Get Keys in Reverse Order
			String newKey = keys.get(keys.size() - i - 1);
			// Round Function F START
			String leftXORRight = roundFunction(left, right, newKey);
			// ! Round Function F END
			// ! ROUND END
			// ? Update values for next ROUND
			left = right;
			right = leftXORRight;
		}
		// 32 bit swap and inverse IP
		return inverseInitialPermutation(right + left);
	}

	private static String roundFunction(String left, String right, String newKey) {
		String right48Bit = DES.expansion(right);
		String keyXORRight = BinaryUtils.bigIntToNBitBinary(new BigInteger(newKey, 2)
				.xor(new BigInteger(right48Bit, 2)), 48);
		String substitution = DES.substitution(keyXORRight);
		String permuted32BitRight = DES.permutation32Bit(substitution);
		return BinaryUtils.bigIntToNBitBinary(new BigInteger(left, 2)
				.xor(new BigInteger(permuted32BitRight, 2)), 32);
	}
}
