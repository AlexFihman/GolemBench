# GolemBench
Pre-requisites: yagna requestor client, mysql, IntelliJ

How to test providers for their benchmarks

1. create mysql database, use script in mysql folder.

2. run proposal.py to get providers

3. open java/GolemScan in intellij, fix dataDir variable in Main.java, 
uncomment following lines:
        //TextScan textScan = new TextScan();
        //textScan.scan(dataDir + "proposal.txt");

Run main, update database with sql statements in output.

4. run test.py which is in PythonRequestor folder

5. uncomment in intellij following lines, run Main
//        ResultsScan resultsScan = new ResultsScan();
//        resultsScan.scan(dataDir + "geek.txt");
//
//        //didn't finish
//        DidNotFinish didNotFinish = new DidNotFinish();
//        didNotFinish.scan(dataDir + "start.txt",dataDir + "finish.txt");

6. update database with resulting sql lines

7. update providers with following sql statement:

SET SQL_SAFE_UPDATES = 0;

update provider
set bench1 = (select avg(bench1) from results r where r.provider = provider.id),
   bench2 = (select avg(bench2) from results r where r.provider = provider.id)
where provider.id in (select provider from results);

