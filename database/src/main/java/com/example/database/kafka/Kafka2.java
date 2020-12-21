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

package com.example.database.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Objects;
import java.util.Properties;

public class Kafka2 {


    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 86400000);
        props.put(ProducerConfig.RETRIES_CONFIG, 1000000);
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
        props.put("bootstrap.servers", "flinkx1:9092");

        KafkaProducer producer = new KafkaProducer<>(props);

        String s = "{\"database\":{\"as\":\"as\",\"we\":\"22\",\"list\":[1,2,3]},\"data\":{\"key1\":1,\"k2\":\"23\",\"ke\":[{\"ke1\":[{\"ke\":\"23\"},{\"23\":\"23\"}]},{\"k2\":[{\"ke12\":\"23\"},{\"2211\":\"23\"}]}]}}";
        String b = "{\"database\":\"1\",\"data\":{\"key1\":1,\"k2\":\"23\",\"ke\":[{\"ke1\":[{\"ke\":\"23\"},{\"23\":\"23\"}]},{\"k2\":[{\"ke12\":\"23\"},{\"2211\":\"23\"}]}]}}";
        String c = "{\"data\":\"this is a data\",\"database\":\"dhaha\"}";
        String d = "{\"data\":\"this is a data\",\"database\":{\"k1\":\"v1\"}}";
        for (int i = 0; i < 10; i++) {
            producer.send(new ProducerRecord<>("test", s, s), (metadata, exception) -> {
                if (Objects.nonNull(exception)) {
                    System.out.println("---");
                }
            });
            producer.send(new ProducerRecord<>("test", b, b));

            producer.send(new ProducerRecord<>("test", c, c));

            producer.send(new ProducerRecord<>("test", d, d));
        }
        while (true) {
        }
    }
}
