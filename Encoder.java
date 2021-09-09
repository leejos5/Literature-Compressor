/**
 * This is the encoder class for TCSS 342C Assignment 2, Compressed Literature.
 */

import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Set;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

/**
 * The Encoder class, that takes an input text and compresses it to an output using the Huffman Algorithm.
 * 
 * @author leejos5
 * @version TCSS 342C Spr2021
 */
public class Encoder {
    
    /**
     * The path of the input file.
     */
	private String inputFileName;

	/**
	 * The path of the output file.
	 */
	private String outputFileName;

	/**
	 * The path of the file to store codes.
	 */
	private String codesFileName;

    /**
     * The string to hold the text of the input file.
     */
	private String text;

    /**
     * Map containing the characters and their frequencies in the text.
     */
	private Map<Character, Integer> frequencies;

	/**
	 * The Huffman Tree organizing the characters and their weight.
	 */
	private HuffmanNode huffmanTree;

	/**
	 * Map containing the characters and their corresponding codes.
	 */
	private Map<Character, String> codes;

	/**
	 * The array holding the string of codes converted into bytes.
	 */
	private byte[] encodedText;
	
	/**
	 * The starting runtime.
	 */
	private long startTime;

	/**
	 * Constructor for the Encoder to encode and compress the given files to the given output files.
	 * 
	 * @param theInputFile the path of the input file.
	 * @param theOutputFile the path of the output file.
	 * @param theCodesFile the path of the codes file.
	 */
	public Encoder(String theInputFile, String theOutputFile, String theCodesFile) {
	    this.startTime = System.currentTimeMillis();
		this.inputFileName = theInputFile;
		this.outputFileName = theOutputFile;
		this.codesFileName = theCodesFile;
		frequencies = new HashMap<Character, Integer>();
		codes = new HashMap<Character, String>();
		readInputFile();
		countFrequency();
		buildTree();
		assignCodes(huffmanTree, "");
		encode();
		writeOutputFile();
		writeCodesFile();
		
	}

	/**
	 * Reads the put file character by character and records it in the text field.
	 */
	private void readInputFile() {
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
			int theChar = reader.read();
			while ((Integer) theChar > -1) {
					result.append((char) theChar);
					theChar = reader.read();
			}
			reader.close();
		} catch (Exception e) {
			System.out.println("An error has occured.");
		}
		text = result.toString();
        
	}

	/**
	 * Counts the frequency of each character and records it into the map.
	 */
	private void countFrequency() {
		for (int i = 0; i < text.length(); i++) {
			char thisChar = text.charAt(i);
			if (frequencies.containsKey(thisChar)) {
				frequencies.put(thisChar, (frequencies.get(thisChar) + 1));
			} else {
				frequencies.put(thisChar, 1);
			}
		}
	}

	/**
	 * Uses the frequencies to build a Huffman Tree of the characters and weight.
	 */
	private void buildTree() {
		Set<Character> keys = frequencies.keySet();
		MyPriorityQueue<HuffmanNode> queue = new MyPriorityQueue<HuffmanNode>();
		Iterator<Character> it = keys.iterator();
		while (it.hasNext()) {
			char key = (char) it.next();
			HuffmanNode newNode = new HuffmanNode(frequencies.get(key), key);
			queue.offer(newNode);
		}
		while (queue.size() > 1) {
			HuffmanNode mergedNode = new HuffmanNode(queue.poll(), queue.poll());
			queue.offer(mergedNode);
		}
		huffmanTree = queue.poll();
	}

	/**
	 * Records the Huffman Algorithm code for each character in the Huffman Tree.
	 * 
	 * @param root The current node to be traversed/recorded.
	 * @param code The code of character so far.
	 */
	private void assignCodes(HuffmanNode root, String code) {
		if (root.left == null && root.right == null) {
			codes.put(root.c, code);
		} else {
			if (root.left != null) {
				assignCodes(root.left, code + "0");
			}
			if (root.right != null) {
				assignCodes(root.right, code + "1");
			}
		}
	}

	/**
	 * A Huffman Node object used to create a Huffman Tree.
	 * 
	 * @author leejos5
	 * @version TCSS 342C Spr2021
	 */
	private class HuffmanNode implements Comparable<HuffmanNode> {

	    /**
	     * The weight of the node.
	     */
		public int weight;

		/**
		 * The character of the node.
		 */
		public char c;

		/**
		 * The HuffmanNode to the left of the node in the tree.
		 */
		public HuffmanNode left;

		/**
		 * The HuffmanNode to the right of the node in the tree.
		 */
		public HuffmanNode right;

		/**
		 * Constructs a Huffman Node with the given weight and character.
		 * 
		 * @param The weight the weight of the Huffman Node.
		 * @param The character of the Huffman Node.
		 */
		public HuffmanNode(int theWeight, char theChar) {
			weight = theWeight;
			c = theChar;
			left = null;
			right = null;
		}

		/**
		 * Constructor for the HuffmanNode with the given left and right nodes.
		 * 
		 * @param theLeft The node to the left of this node.
		 * @param theRight The node ot the right of this node.
		 */
		public HuffmanNode(HuffmanNode theLeft, HuffmanNode theRight) {
			left = theLeft;
			right = theRight;
			weight = 0;
			if (left != null) {
			    weight += theLeft.weight;
			}
			if (right != null) {
			    weight += theRight.weight;
			}

		}

		/**
		 * Compares the weight to weight of another node.
		 * 
		 * @param theOther the other node to be compared to.
		 */
		public int compareTo(HuffmanNode theOther) {
			return this.weight - theOther.weight;
		}

		/**
		 * Returns a string representation of the node.
		 * 
		 * @return The string representation.
		 */
		public String toString() {
			return c + ": " + weight;
		}

	}

	/**
	 * Encodes the text, converting it into its code and then into a byte array.
	 */
	private void encode() {
		StringBuilder textInCode = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			char character = text.charAt(i);
			textInCode.append(codes.get(character));
		}
		String stringOfCodes = textInCode.toString();
		System.out.println(stringOfCodes.length());
		BitSet bs = new BitSet();
		for (int i = 0; i < stringOfCodes.length(); i++) {
		    if (stringOfCodes.charAt(i) == '1') {
		        bs.set(i);
		    }
		}
		encodedText = bs.toByteArray();
	}

	/**
	 * Writes the encoded text to the output file.
	 */
	private void writeOutputFile() {
		try {
			FileWriter writer = new FileWriter(outputFileName);
			for(int i = 0; i < encodedText.length; i++) {
			    writer.write(encodedText[i]);
			}
			writer.close();
		} catch (IOException e) {
		 System.out.println("An error has occurred.");
		}
	}

	/**
	 * Writes the character and corresponding text to the codes file.
	 */
	private void writeCodesFile() {
		try {
			FileWriter writer = new FileWriter(codesFileName);
			writer.write(codes.toString());
			writer.close();
		} catch (IOException e) {
			System.out.println("An error has occurred.");
		}
	}

	/**
	 * Returns a string representation of the encoder, recording the compression statistics and runtime.
	 * 
	 * @return The string representation of the encoder.
	 */
	public String toString() {
		StringBuilder result = new StringBuilder();
		long inputSizeBytes = new File(inputFileName).length();
		long inputSizeKb = inputSizeBytes / 1024;
		long outputSizeBytes = new File(outputFileName).length();
		long outputSizeKb = outputSizeBytes / 1024;
		result.append("Uncompressed file size: " + inputSizeKb + " Kb (" + inputSizeBytes + " bytes)" + System.lineSeparator());
		result.append("Compressed file size: " + outputSizeKb + " Kb (" + outputSizeBytes + " bytes)" + System.lineSeparator());
		result.append("Compression ratio: " +Math.round((double) outputSizeKb / (double) inputSizeKb * 100) + "%" + System.lineSeparator());
		result.append("Running Time: " + (System.currentTimeMillis() - startTime) + " millseconds");
		return result.toString();
	}
}
