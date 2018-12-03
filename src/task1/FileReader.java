package task1;
import java.util.ArrayList;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.EOFException;
import java.util.Iterator;



public class FileReader {

    String path;
    ArrayList<Integer> progr=new ArrayList<>();
    int[] intergers;

    public void addProg(int ins){
        this.progr.add(ins);
    }

    public void readBinFile(String path) throws IOException{
        DataInputStream binFile = new DataInputStream(new FileInputStream(path));
        while(binFile.available() > 0) {
            try
            {
                int instruction = binFile.readInt();
                instruction = Integer.reverseBytes(instruction);
                addProg(instruction);
            }
            catch(EOFException e) {
            }
        }
        binFile.close();
    }


    public int[] getIntergers() {
        return intergers;
    }

    public void setIntergers(int[] intergers) {
        this.intergers = intergers;
    }

    public void convertIntegers()
    {
        ArrayList<Integer> integers = getProgr();
        int[] ret = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = iterator.next().intValue();
        }
        setIntergers(ret);
    }

    public FileReader(String path)throws IOException{
        setPath(path);
        readBinFile(path);
        convertIntegers();
    }

    public ArrayList<Integer> getProgr() {
        return progr;
    }



    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }



}
