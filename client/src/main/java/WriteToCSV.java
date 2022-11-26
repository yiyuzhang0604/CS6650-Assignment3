import javax.swing.*;
import java.io.*;

public class WriteToCSV {
    private File file;
    private FileWriter fw;

    public WriteToCSV(File file) throws IOException {
        this.file = file;
        this.fw = new FileWriter(file);
    }


    public void saveRecord(String[] records) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (String str : records) {
            sb.append("\"");
            sb.append(str.replaceAll("\"","\"\""));
            sb.append("\"");
            sb.append(",");
        }
        sb.append("\n");
        fw.write(sb.toString());
    }

}
