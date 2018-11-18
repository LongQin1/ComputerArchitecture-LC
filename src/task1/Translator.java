package task1;


import java.util.Arrays;

public class Translator {

	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();


	// method used for translate the binary to hex
	//https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
		hexChars = Arrays.copyOfRange(hexChars, 0,hexChars.length);
		return new String(hexChars);
	}

	public Translator() {

	}

	public static int[] getArray(String ins) {

		int insCount = ins.length()/7;
		int[] res = new int[insCount];
		int decimal;


		String[] instructionsString;

		if (ins.length() == 0){
			System.out.println("Error");
		}
		else {
			instructionsString = ins.split("(?<=\\G........)");
			for (int i = 0; i < insCount; i++){
				System.out.println(instructionsString[i]);

				decimal = (int) Long.parseLong(instructionsString[i],16);

				//int result = (Integer.parseInt(Integer.toString(Integer.parseInt(instructionsString[i], 16), 2).replace('0', 'X').replace('1', '0').replace('X', '1'), 2) + 1) * -1;

				res[i] = decimal;
			}
		}
		return res;


	}
}

