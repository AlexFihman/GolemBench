package com.fihman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class DidNotFinish {
    public void scan(String fileName1, String fileName2) {
        ArrayList<String> started = new ArrayList<>();
        ArrayList<String> finished = new ArrayList<>();
        File inputFile = new File(fileName1);

        if (inputFile.exists()) {
            try {
                File myObj = new File(fileName1);
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    data = myReader.nextLine();
                    started.add(data);
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        inputFile = new File(fileName2);
        if (inputFile.exists()) {
            try {
                File myObj = new File(fileName2);
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    data = myReader.nextLine();
                    finished.add(data);
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        for (String startedProvider: started) {
            if (!finished.contains(startedProvider))
                System.out.println(String.format("insert into results values ('%s',-1,-1);", startedProvider));
        }
    }
}
