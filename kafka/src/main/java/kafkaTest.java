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

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Collection;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class kafkaTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        Properties props = new Properties();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 86400000);
        props.put(ProducerConfig.RETRIES_CONFIG, 1000000);
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
        props.put("delivery.timeout.ms", 1000);
        props.put("bootstrap.servers", "flinkx1:9092");

        KafkaProducer producer = new KafkaProducer<>(props);


//        try (AdminClient client = KafkaAdminClient.create(props)) {
//            while (true) {
//                try {
////                ListTopicsResult topics = client.listTopics();
//                    KafkaFuture<Collection<Node>> nodes = client.describeCluster().nodes();
////                Set<String> names = topics.names().get();
//                    System.out.println("connect to kafka cluster success");
//                    if (nodes.get(60000, TimeUnit.MILLISECONDS).isEmpty()) {
//                        // case: if no topic found.
//                        System.out.println("--------error=------");
//                    }
//                } catch (Exception e) {
//                    System.out.println("--------error=------");
//                }
//            }
//        } catch (Exception e) {
//
//        }
        int i = 1;
        boolean flag = true;
        while (true) {
            if (flag) {
                flag = i++ < 100;
                int finalI = i;
                Object test = producer.send(new ProducerRecord<>("test", i + "", i + "")).get(60000, TimeUnit.MILLISECONDS);
                System.out.println(test);
                producer.send(new ProducerRecord<>("test", i + "", i + ""), (metadata, exception) -> {
                    if (Objects.nonNull(exception)) {
                        String errorMessage = String.format("send data failed,data 【%s】 ,error info  %s", finalI, exception.getCause());
                        System.out.println(errorMessage);
                    } else {
                        System.out.println("success" + finalI);
                    }
                });
            } else {
                flag = false;
            }
        }
    }
}
