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

import java.util.*;

/**
 * StringTest
 *
 * @author by dujie@dtstack.com
 * @Date 2020/8/18
 */
public class StringTest {
    protected final static String GET_INDEX_SQL = "SELECT " +
            "t.INDEX_NAME," +
            "t.COLUMN_NAME " +
            "FROM " +
            "user_ind_columns t," +
            "user_indexes i " +
            "WHERE " +
            "t.index_name = i.index_name " +
            "AND i.uniqueness = 'UNIQUE' " +
            "AND t.table_name = '%s'";


    public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString());
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("msg.key1", "v1");
        hashMap.put("msg.key2", "v2");
        hashMap.put("msg.key1", "v3");
        json(hashMap);
        hashMap.clear();
        HashMap<String, Object> data1 = new HashMap<>();
        data1.put("key1", "v1");
        data1.put("key2", "v2");
        HashMap<String, Object> data2 = new HashMap<>();
        data2.put("k4", "v4");
        data1.put("key3", data2);
        hashMap.put("msg", data1);
        json(hashMap);
    }

    public static void json(Map map) {
        Gson gson = new Gson();
        System.out.println(gson.toJson(map));

    }


    public static void test1() {
        System.out.println(camelize("hdfsreader", "reader"));
        String s = "\001";
        System.out.println(s);

        String path = "/opt/dtstack/DTBatch/Batch/kerberosConf/RDOS_45/hbase.keytab";
        path = path.substring(path.lastIndexOf("/") + 1);

        System.out.println(path);
//        System.out.print(String.format(GET_INDEX_SQL, "test1"));
//        List<String> strings = splitIgnoreQuota("[abc].[13]", '.');
//        System.out.print(strings.stream().map(i -> {
//            StringBuffer stringBuffer = new StringBuffer(64);
//            return stringBuffer.append("\"").append(i).append("\"").toString();
//        }).collect(Collectors.joining(".")));
    }

    public static List<String> splitIgnoreQuota(String str, char delimiter) {
        List<String> tokensList = new ArrayList<>();
        boolean inQuotes = false;
        boolean inSingleQuotes = false;
        int bracketLeftNum = 0;
        StringBuilder b = new StringBuilder(64);
        char[] chars = str.toCharArray();
        int idx = 0;
        for (char c : chars) {
            char flag = 0;
            if (idx > 0) {
                flag = chars[idx - 1];
            }
            if (c == delimiter) {
                if (inQuotes) {
                    b.append(c);
                } else if (inSingleQuotes) {
                    b.append(c);
                } else if (bracketLeftNum > 0) {
                    b.append(c);
                } else {
                    tokensList.add(b.toString());
                    b = new StringBuilder();
                }
            } else if (c == '\"' && '\\' != flag && !inSingleQuotes) {
                inQuotes = !inQuotes;
                //b.append(c);
            } else if (c == '\'' && '\\' != flag && !inQuotes) {
                inSingleQuotes = !inSingleQuotes;
                //b.append(c);
            } else if (c == '[' && !inSingleQuotes && !inQuotes) {
                bracketLeftNum++;
                //b.append(c);
            } else if (c == ']' && !inSingleQuotes && !inQuotes) {
                bracketLeftNum--;
                //b.append(c);
            } else {
                b.append(c);
            }
            idx++;
        }

        tokensList.add(b.toString());

        return tokensList;
    }

    private static String camelize(String pluginName, String suffix) {
        int pos = pluginName.indexOf(suffix);
        String left = pluginName.substring(0, pos);
        left = left.toLowerCase(Locale.ENGLISH);
        suffix = suffix.toLowerCase(Locale.ENGLISH);
        StringBuffer sb = new StringBuffer();
        sb.append(left + "." + suffix + ".");
        sb.append(left.substring(0, 1).toUpperCase() + left.substring(1));
        sb.append(suffix.substring(0, 1).toUpperCase() + suffix.substring(1));
        return sb.toString();
    }
}
