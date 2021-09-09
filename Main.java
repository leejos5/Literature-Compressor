/**
 * The main class to run the encoder, encoding and compressing the given file.
 * 
 * @author leejos5
 * @version TCSS 342C Spr2021
 */

public class Main {
    
    /**
     * The main function to run the program.
     */
    public static void main(String[]theArgs) {
		String inputFileName = "./WarAndPeace.txt";
		String codesFileName = "./codes.txt";
		String outputFileName = "./output.txt";
		Encoder myEncoder = new Encoder(inputFileName, outputFileName, codesFileName);
		System.out.println(myEncoder.toString());

	}
}
