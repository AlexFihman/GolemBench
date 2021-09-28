package com.fihman;

public class Main {

    public static void main(String[] args) {
        //String dataDir = "N:\\yapapi\\original\\yapapi\\examples\\hello-bench\\";
        String dataDir = "C:\\data\\y\\bench\\";

        //TextScan textScan = new TextScan();
        //textScan.scan(dataDir + "proposal.txt");

        TextScan textScan = new TextScan();
        textScan.scan2(dataDir + "missing.txt");

//        ResultsScan resultsScan = new ResultsScan();
//        resultsScan.scan(dataDir + "geek.txt");
//
//        //didn't finish
//        DidNotFinish didNotFinish = new DidNotFinish();
//        didNotFinish.scan(dataDir + "start.txt",dataDir + "finish.txt");
    }
}
