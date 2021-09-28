package com.fihman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TextScan {
    ObjectMapper objectMapper = new ObjectMapper();

    public void getParams(String id) {
        try {
            URL url = new URL("https://api.stats.golem.network/v1/provider/node/" + id);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            String contentType = con.getHeaderField("Content-Type");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            int status = con.getResponseCode();
            if (status == 200) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                //System.out.println(content);
                List provider = objectMapper.readValue(content.toString(), List.class);
                LinkedHashMap p1 = (LinkedHashMap) provider.get(0);

                String nodeId = (String) p1.get("node_id");

                String createdAt = (String) p1.get("created_at");
                String updatedAt = (String) p1.get("updated_at");

                LinkedHashMap data = (LinkedHashMap) p1.get("data");
                String name = (String) data.get("golem.node.id.name");
                name = name.replace("'", "\\\'");

                Integer cores = (Integer) data.get("golem.inf.cpu.cores");
                Integer threads = (Integer) data.get("golem.inf.cpu.threads");
                Double memory = (Double) data.get("golem.inf.mem.gib");

                ArrayList coeffs = (ArrayList) data.get("golem.com.pricing.model.linear.coeffs");
                double c1 = (double) coeffs.get(0);
                double c2 = (double) coeffs.get(1);
                double c3 = (double) coeffs.get(2);

                int online = ((Boolean) p1.get("online")) ? 1 : 0;

                String subnet = (String) data.get("golem.node.debug.subnet");

                String sql = String.format("replace into provider values ('%s', '%s', '%s', '%s', %s ,%s, %s, %s, %s, %s, %s, '%s', null, null);", nodeId, name, createdAt, updatedAt, cores, threads, memory, c1, c2, c3, online, subnet);
                System.out.println(sql);

                in.close();
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void scan(String fileName) {
        File inputFile = new File(fileName);
        if (inputFile.exists()) {
            try {
                File myObj = new File(fileName);
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    if (data.startsWith("offer issuer:")) {
                        String id = data.split(" ")[2];
                        //System.out.println(id);
                        getParams(id);
                    }
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }

    public void scan2(String fileName) {
        File inputFile = new File(fileName);
        ArrayList<String> processed = new ArrayList<>(1000);
        if (inputFile.exists()) {
            try {
                File myObj = new File(fileName);
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    if (processed.contains(data))
                        continue;
                    getParams(data);
                    processed.add(data);
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }
}
