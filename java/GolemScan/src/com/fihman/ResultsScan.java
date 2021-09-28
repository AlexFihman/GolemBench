package com.fihman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ResultsScan {
    public void scan(String fileName) {
        File inputFile = new File(fileName);
        int lineNo = 0;
        double bench1 = 0.0;
        double bench2 = 0.0;
        String host = "";
        if (inputFile.exists()) {
            try {
                File myObj = new File(fileName);
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    if (lineNo % 3 == 0) {
                        host = data;
                    }
                    if (lineNo % 3 == 1) {
                        String[] split = data.split(" ");
                        bench1 = Double.parseDouble(split[split.length-1]);
                    }
                    if (lineNo % 3 == 2) {
                        String[] split = data.split(" ");
                        bench2 = Double.parseDouble(split[split.length-1]);
                        System.out.println(String.format("insert into results values ('%s', %s, %s);", host, bench1, bench2));
                    }
                    lineNo++;
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

    }
}
