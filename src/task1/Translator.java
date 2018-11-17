package task1;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Translator {
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static void main(String[] args) throws IOException {
		// read the binary file into byte
		// however paths.get doesn't work on my labtop,couldn't find the right path..
		Path path = Paths.get("Users/qinlong/eclipse-workspace/RISC-V/src/test1/addlarge.bin");
		byte[] data = Files.readAllBytes(path);
		System.out.println(data);
	}
	// method used for translate the binary to hex
	//https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
}

