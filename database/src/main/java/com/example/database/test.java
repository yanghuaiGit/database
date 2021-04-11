/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.database;


import com.google.gson.Gson;
import util.GsonUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {


    public static Pattern valueExpression =
            Pattern.compile("(?<variable>(\\$\\{((?<innerName>(uuid|currentTime|intervalTime))|((?<paramType>(param|response|body))\\.(?<name>(.*?([^${]*|(\\$\\{(.*?)})*)))))}))");
    public static Pattern valueExpressio1n =   Pattern.compile("(?<variable>(\\$\\{.*?\\}){1,})");


    public static void main(String[] args) throws ParseException {

        Date parse = new SimpleDateFormat("yyyy&MM#dd HH@mm!ss").parse("2020&12#13 12@12!12");

//
//        test();
        pattern();


    }

    public static void test() {


        HashMap<String, Object> map = new HashMap<>();

        HashMap<String, Object> test1 = new HashMap<>();
        test1.put("key1", "1");
        test1.put("key2", "2");
        test1.put("key3", "3");

        map.put("key1", test1);
        HashMap<String, Object> test2 = new HashMap<>();
        HashMap<String, Object> test3 = new HashMap<>();
        HashMap<String, Object> test4 = new HashMap<>();
        test4.put("key1", "213");
        test4.put("key2", "12");
        test3.put("key1", test4);
        test3.put("key2", test4);
        test2.put("key1", test3);
//        test2.put("key1", "213");
        map.put("key3", test2);
        map.put("key4", 213);
        ArrayList<String> strings = new ArrayList<>();
//        strings.add("key2.key2");
//        strings.add("key1.key2");

        strings.add("msg.key1");
        strings.add("msg.key2");
        strings.add("msg.key3.key4");

//        strings.add("key4");

        System.out.println(GsonUtil.GSON.toJson(map));
        System.out.println("--------------------");

        Map map1 = GsonUtil.GSON.fromJson("{\n" +
                "  \"msg\": {\n" +
                "    \"key1\": \"value1\",\n" +
                "    \"key2\": \"value2\",\n" +
                "    \"key3\": {\n" +
                "      \"key4\": \"value4\",\n" +
                "      \"key5\": \"value5\"\n" +
                "    },\n" +
                "    \"key6\": 2\n" +
                "  }\n" +
                "}", Map.class);

//        System.out.println(test(map, strings));
        System.out.println(new Gson().toJson(test2(map1, strings)));
    }
    //todo startegy的修改   https的引入

//    public static Map test(Map<String, Object> map, List<String> keys) {
//        HashMap<String, Object> value = new HashMap<>();
//
//        for (String key : keys) {
//            Object o;
//            String[] split = key.split("\\.");
//            Map<String, Object> stringObjectHashMap = map;
//            Map<String, Object> tempMap = value;
//            for (int i = 0; i < split.length; i++) {
//                o = getValue(stringObjectHashMap, split[i]);
//
//                if (o == null) {
//                    for (int ii = i; ii < split.length; ii++) {
//                        if (ii == split.length - 1) {
//                            tempMap.put(split[ii], null);
//                        }
//                        if (tempMap.containsKey(split[ii])) {
//                            break;
//                        } else {
//                            HashMap<String, Object> stringObjectHashMap1 = new HashMap<>();
//                            tempMap.put(split[ii], stringObjectHashMap1);
//                            tempMap = stringObjectHashMap1;
//                        }
//                    }
//                } else if (i == split.length - 1) {
//                    tempMap.put(split[i], o);
//                } else {
//                    if (!(o instanceof Map)) {
//                        throw new RuntimeException("key " + key + "in " + map + " is not a json");
//                    }
//                    stringObjectHashMap = (Map<String, Object>) o;
//                    if (tempMap.containsKey(split[i])) {
//                        tempMap = (Map<String, Object>) tempMap.get(split[i]);
//                    } else {
//                        HashMap<String, Object> stringObjectHashMap1 = new HashMap<>();
//                        tempMap.put(split[i], stringObjectHashMap1);
//                        tempMap = stringObjectHashMap1;
//                    }
//                }
//            }
//        }
//        return value;
//    }


    public static Map test2(Map<String, Object> map, List<String> keys) {
        HashMap<String, Object> value = new HashMap<>();

        for (String key : keys) {
            Object o = null;
            String[] split = key.split("\\.");
            Map<String, Object> stringObjectHashMap = map;
            for (int i = 0; i < split.length; i++) {
                o = getValue(stringObjectHashMap, split[i]);
                if (o == null) {
                    break;
                }
                if (i != split.length - 1) {
                    if (!(o instanceof Map)) {
                        throw new RuntimeException("key " + key + " in " + map + " is not a json");
                    }
                    stringObjectHashMap = (Map<String, Object>) o;
                }
            }
            value.put(key, o);
        }


        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        value.forEach((k, v) -> {
            String[] split = k.split("\\.");
            if (split.length == 1) {
                stringObjectHashMap.put(split[0], v);
            } else {
                HashMap<String, Object> temp = stringObjectHashMap;

                for (int i = 0; i < split.length - 1; i++) {

                    if (temp.containsKey(split[i])) {
                        temp = (HashMap<String, Object>) temp.get(split[i]);
                    } else {
                        HashMap hashMap = new HashMap();
                        temp.put(split[i], hashMap);
                        temp = hashMap;
                    }

                    if (i == split.length - 2) {
                        temp.put(split[split.length - 1], v);
                    }
                }
            }
        });
        return stringObjectHashMap;
    }

    public static Object getValue(Map<String, Object> map, String key) {
        return map.get(key);
    }


//        System.out.println(matcher.find());
//
//        ArrayList<Integer> numbers = new ArrayList<>(32);
//
//        for (int i = 0; i < 10; i++) {
//            numbers.add(i);
//        }
//
//        numbers.stream().filter(i -> i < 5).forEach(System.out::println);


    public static void pattern() {
        Matcher matcher = valueExpression.matcher("123+${param.${uuid}${ab}${ac}}+${response.v}+${uuid}");
        while (matcher.find()) {
            String variableName = matcher.group("variable");
            String name = matcher.group("name");
            Matcher matcher1 = valueExpressio1n.matcher(name);

            while (matcher1.find()){
              System.out.println("---"+matcher1.group("variable"));
            }

            String name1 = matcher.group("innerName");
            System.out.println("variableName: " + variableName);
            System.out.println("name: " + name);
            System.out.println("innerName: " + name1);
        }
    }

}
