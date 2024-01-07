package fileProcessing;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileProcessing {

    /**
     * Reads the content of a file and returns it as a string.
     *
     * @param filePath The path of the file to be read.
     * @return The content of the file as a string.
    */
    public static String readFromFile(String filePath) {
        StringBuilder content = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();
    }
}
