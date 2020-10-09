package com.example.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.stream.Collectors;

public class Curl {
    public static void main(String[] args) throws InterruptedException {


        System.out.println("database".substring(1, "database".length() - 1));
        List<String> strings = new ArrayList<>(2);
        strings.add("database");
        strings.add("[schema]");
        strings.add("[table]");
        System.out.println(strings.stream().map(i -> {
            StringBuffer stringBuffer = new StringBuffer(16);
            return stringBuffer.append("\"").append(i).append("\"").toString();
        }).collect(Collectors.joining(".")));


        System.out.println(System.currentTimeMillis());
//        Long.parseLong("1294701817204");
//        Integer.parseInt("1294701817204");
        System.out.println(Double.valueOf("20200727095857001467"));
        Double aDouble = new Double("20200727095857001467");
        System.out.println(new BigDecimal("20200727095857001467").doubleValue());


        System.out.println(Double.MAX_VALUE + "--" + aDouble);
        System.out.println(new BigInteger("20200727095857001467").doubleValue() + 2e-15 + "--" + aDouble);
        System.out.println(new BigDecimal(Double.MAX_VALUE + "").doubleValue() + 2e-15 + "--" + aDouble);
        System.out.println(Double.MAX_VALUE - 90000000 + "--" + aDouble);
        System.out.println(Double.MAX_VALUE - 100000 + "--" + aDouble);
        //  String[] cmds = {"curl","-k","-u","elastic:abc123","-XGET","http://172.16.10.251:9200/_cluster/health?pretty","-k"};
        String curl = "curl -k -u elastic:abc123 -XGET http://172.16.10.251:9200/_cluster/health?pretty -k";

        curl = "curl -k -u elastic:abc123 -XPUT  http://172.16.10.251:9200/bigdata_p1";
        String[] cmds = curl
                .split(" ");

        System.out.println(curl(cmds));

        curl = "curl -k -u elastic:abc123 http://172.16.10.251:9200/_cat/indices?v";
        cmds = curl
                .split(" ");

        System.out.println(curl(cmds));

        curl = "curl -k -u elastic:abc123 http://172.16.10.251:9200/bigdata_p1/_search?pretty";
        cmds = curl
                .split(" ");

        System.out.println(curl(cmds));


        BlockingQueue<String> queue = new SynchronousQueue<>(false);
        queue.put("2w");
        System.out.println(queue.take());
    }

    public static String curl(String[] cmds) {
        ProcessBuilder process = new ProcessBuilder(cmds);
        Process p;
        try {
            p = process.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
            return builder.toString();

        } catch (IOException e) {
            System.out.print("error");
            e.printStackTrace();
        }
        return null;
    }
}
