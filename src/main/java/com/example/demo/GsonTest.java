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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.internal.LinkedHashTreeMap;
import com.google.gson.internal.LinkedTreeMap;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * GsonTest
 *
 * @author by dujie@dtstack.com
 * @Date 2020/9/4
 */
public class GsonTest {
    static Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    public static void main(String[] args) {

   
        System.out.println(Date.valueOf("2020-09-07 17:19:05"));
        String job = "{\n" +
                "    \"stime\":\"value:2020-03-23 23:23:21\\\\,nextvalue:${body.etime}\\\\,type:timestamp/datetime/date/long/int\",\n" +
                "    \"etime\":\"value:${body.stime}\\\\+8640000\\\\+\\\\,format:yyyy-mm-dd hh:dd:ss\",\n" +
                "    \"array\":[\"value\",\"2\",\"3\"],\n" +
                "    \"flag\":\"value:\\\\uuid\",\n" +
                "    \"test\":{\n" +
                "        \"test\":\"value:2020-03-23 23:23:21\"\n" +
                "    },\n" +
                "    \"dkucode\":\"huy+weuiri,ertine\"\n" +
                "}" ;

        Map<String, Object> map = gson.fromJson(job, Map.class);
        System.out.println(map.toString());
        Map<String, Object> stringObjectMap = convertToHashMap(map);
        System.out.println(stringObjectMap.toString());
        gson.toJson(map);
    }

    /**
     * convert LinkedTreeMap or LinkedHashTreeMap Map to HashMap,for LinkedTreeMap,LinkedHashTreeMap can not serialize
     *
     * @param target
     * @return
     */
    public static Map<String, Object> convertToHashMap(Map<String, Object> target) {
        for (Map.Entry<String, Object> tmp : target.entrySet()) {
            if (null == tmp.getValue()) {
                continue;
            }

            if (tmp.getValue().getClass().equals(LinkedTreeMap.class) ||
                    tmp.getValue().getClass().equals(LinkedHashTreeMap.class)) {
                Map<String, Object> convert = convertToHashMap((Map) tmp.getValue());
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.putAll(convert);
                tmp.setValue(hashMap);
            }
        }

        return target;
    }
}
