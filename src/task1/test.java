package task1;

import java.io.File;

public class test {
	public static void main(String[] args) {
	File file = new File("addlarge.bin");
	for(String fileNames : file.list())System.out.println(fileNames);
	}
}
