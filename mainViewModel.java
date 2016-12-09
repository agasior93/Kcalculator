package sample;

import java.io.*;
import java.sql.Connection;

/**
 * Created by Maciek on 01.12.2016.
 */
public class mainViewModel {

    Connection conn;

    public mainViewModel(){
        conn = SqlConnection.Connector();
        if(conn == null){
            System.exit(0);
        }
    }
    public void writeFile(String filePath, String[] text) throws IOException {
        FileWriter fileWriter= new FileWriter(filePath);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        try {
            for (String line : text) {
                bufferedWriter.write(line);
                bufferedWriter.close();
            }
        } finally {
            bufferedWriter.close();
        }
    }






}
