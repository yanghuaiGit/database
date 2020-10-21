/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.demo;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Pattern
 *
 * @author by dujie@dtstack.com
 * @Date 2020/8/17
 */
public class PatternTest {
    //(?<host>[0-9a-zA-Z\.]+) 获取进组 且组名为host
    public static Pattern HIVE_JDBC_PATTERN = Pattern.compile("(?i)jdbc:hive2://(?<host>[^:]+):(?<port>\\d+)/(?<db>[^;]+)(?<param>[\\?;#].*)*");
    public static Pattern HIVE_JDBC_PATTERN1 = Pattern.compile("(?<param>[\\?;#].*)");
    public static Pattern HIVE_JDBC_PATTERN2 = Pattern.compile("(?<param>(?!.*[^\\d+\\-*/\\(\\)]))");
    public static final String HOST_KEY = "host";
    public static final String PORT_KEY = "port";

    public static void main(String[] args) {
        Map<String, Map<String, Object>> o = new Gson().fromJson("{\"key\":{\"key1\":\"23\"}}", Map.class);
        String test = "20190828-1346-687476c4-5efe-4f1a-b698-4e1b27cb5cc0,1981346-004D812,1,PRINT,FinishJob,1,1,null,,,,null,,,332ae088-de57-4ccd-8d4f-8e74cb29f884,134621499,006580,null,null,134601545,东风本田汽车有限公司\\\\二工厂\\\\总装2科\\\\品质系\\\\品质1班,null,,134610,默认成本中心,杨静,MAF-4QG5B92,172.21.57.47,4C-EB-42-CB-74-A1,006580,2019-08-28 08:31:24.0,20190828T003026781,null,,null,,-1,134600011,HUB1,134600013,Radar1,134600015,Node1,134600026,F1-IT-2F,Multi-model Print Driver 2,2019年CIVIC各科VQ单台不良推移 .xlsx,,2019-08-28 08:30:55.0,2019-08-28 08:33:00.0,1,0,1,1,0,1,1,0,0,0,0,A3,297,420,Mono,1,134650,公开,0,,0,TC101294346266,F2-AF2-01,Node1,HUB1,0,0,1346,0,,,,PCL,,,,,Mono,1,1,A3,1,0,1,1,0,1,1,,null,,null,,YES,1,0.0,0.0,0.0,0.0,0.0,0.0,N,null,0.0,,,,,,,,,,,null,null,null,null,null";
        String hah = "";
        Matcher matcher2 = HIVE_JDBC_PATTERN2.matcher(hah);
        while (matcher2.find()) {
            System.out.println(matcher2.group("param"));
        }
        System.out.println(test.split(",").length);
        String text = "";
        System.out.println(text.split(" ").length);
        String data = "\\34\\34c\\3242";

        System.out.println("data->>" + data);
        System.out.println("data->>" + data.replaceAll("\\\\", "\\\\\\\\"));


        if (data.contains("\\")) {
            data.replaceAll("\\\\", "\\\\\\\\");
        }

        String url = "jdbc:hive2://eng-cdh3:10001/defa-ult";
        Matcher matcher = HIVE_JDBC_PATTERN.matcher(url);
        Matcher matcher1 = HIVE_JDBC_PATTERN1.matcher(url);
        if (matcher1.find()) {
            System.out.println(matcher1.group("param"));
        }
        if (matcher.find()) {
            String addr = matcher.group(HOST_KEY) + ":" + matcher.group(PORT_KEY) + ":" + matcher.group("db") + ":" + matcher.group("param");
            System.out.println(addr);
        }
        String addr = url.substring(url.indexOf("//") + 2);
        addr = addr.substring(0, addr.indexOf("/"));
        System.out.println(addr);
    }

    public static void test() {
        String text = "taskcenterdb_00\\.binlog1.1";
        System.out.println(text.split("\\\\.").toString());
        //含有\\. \\*而不是. *
        Pattern.matches("\\\\\\.", "\\.");
        Pattern.matches("\\\\\\*", "\\*");
        String[] split = text.split("/,&&[^\\\\,]/");
        String pattern = "[\\*\\.]&&[^\\\\*]|[^\\.\\*]]";
        System.out.println(Pattern.matches(pattern, text));
        Pattern patPunc = Pattern.compile("[*]$");
        Matcher matcher = patPunc.matcher(text);
        matcher.find(); //false
        Arrays.stream(split).map(i -> (StringEscapeUtils.unescapeJava(i))).forEach(i -> {
            Arrays.stream(i.split("\\.&&[^\\\\.]")).forEach(ii -> System.out.print(ii));
            System.out.println("\n");
            System.out.println(Pattern.matches(pattern, i));
        });
    }
}
