package org.example.utils;

import java.io.FileWriter;
import java.io.IOException;

public class FileReader {

    public static String readFromFile(String filePath) throws IOException {
        java.io.FileReader fileReader = new java.io.FileReader(filePath);
        StringBuffer sb = new StringBuffer();
        int i;
        while((i=fileReader.read()) != -1) {
            sb.append((char) i);
        }
        return sb.toString();
    }

    public static void writeToFile(String filePath, String fileContent) throws IOException {
        FileWriter myWriter = new FileWriter(filePath);
        myWriter.write(fileContent);
        myWriter.close();
    }
}
