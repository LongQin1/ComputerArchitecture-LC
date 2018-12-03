package task1;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class MemoryPrinter {


    public void CreateFile(String data) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("memory"));

        writer.append(data);
        writer.close();

    }
}
