package task1;





public class Translator {

	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	String hexChars;
	int[] finalInstructions;


	public Translator(byte[] bytes) {
		hexChars = bytesToHex(reverse(bytes));
		reverseArray(getArray(hexChars));
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

	// Because we want to prepare for converting from little edian to big edian
	public byte[] reverse(byte[] array) {
		int i = 0;
		int j = array.length - 1;
		byte tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
		return array;
	}

	public void reverseArray(int[] array){
		for(int i = 0; i < array.length / 2; i++)
		{
			int temp = array[i];
			array[i] = array[array.length - i - 1];
			array[array.length - i - 1] = temp;
		}
		this.finalInstructions = array;

	}



	public static int[] getArray(String ins) {

		int insCount = ins.length()/8;
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

	public int[] getFinalInstructions() {
		return finalInstructions;
	}
}
