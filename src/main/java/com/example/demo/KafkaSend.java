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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * KafkaSend
 *
 * @author by dujie@dtstack.com
 * @Date 2020/8/19
 */
@Component
public class KafkaSend {


    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void test() {
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> stringStringHashMap = new HashMap<>(4);
//            stringStringHashMap.put("message","{\n" +
//                    "  \"scn\": 123,\n" +
//                    "  \"type\": \"update\",\n" +
//                    "  \"schema\": \"dujie\",\n" +
//                    "  \"table\": \"VSP_VOYAGE_TEST\",\n" +
//                    "  \"ts\": 123213233243232,\n" +
//                    "  \"opTime\": 12321323,\n" +
//                    "  \"after_AFTER_ID\":9,\n" +
//                    "  \"after_USER_ID\": 212323,\n" +
//                    "  \"after_name\": \"hahah\"\n" +
//                    "}");

            this.kafkaTemplate.send("test", "{\"ID\":1,\"NAME\":\"wangwu\",\"MESSAGE\":\"mesaagesssss\"}" );
        }


    }


}
