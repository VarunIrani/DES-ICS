package com.varunirani;

import com.varunirani.utils.BinaryUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;

public class DESMain {
	public static void main(String[] args) throws IOException {
		DES.initializeSBoxes();
		DES.initializeKeySchedule();

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter Plain Text: ");
		String plainText = reader.readLine();
		String binaryString = BinaryUtils.stringToBinary(plainText.getBytes(StandardCharsets.UTF_8));
		ArrayList<String> plainTextBlocks = DES.get64BitBlocks(binaryString);

		System.out.print("Enter Key (8 characters only): ");
		String inputKey = BinaryUtils.stringToBinary(
				reader.readLine().getBytes(StandardCharsets.UTF_8));

		String key = DES.get56BitPermutedKey(inputKey);

		StringBuilder cipherText = new StringBuilder();

		for (String plainTextBlock : plainTextBlocks)
			cipherText.append(DES.encryptBlock(plainTextBlock, key));

		byte[] byteCipherText = new BigInteger(cipherText.toString(), 2).toByteArray();
		if (byteCipherText[0] == 0) {
			byte[] tmp = new byte[byteCipherText.length - 1];
			System.arraycopy(byteCipherText, 1, tmp, 0, tmp.length);
			byteCipherText = tmp;
		}
		System.out.println("Cipher Text: " + Base64.getEncoder().encodeToString(byteCipherText));

		ArrayList<String> cipherTextBlocks = DES.get64BitBlocks(cipherText.toString());

		StringBuilder decryptedPlainText = new StringBuilder();

		for (String cipherTextBlock : cipherTextBlocks)
			decryptedPlainText.append(DES.decryptBlock(cipherTextBlock));

		System.out.println("Decrypted Plain Text: "
				+ BinaryUtils.binaryToString(decryptedPlainText.substring(0, decryptedPlainText.length() - 64)));
	}
}
