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

package com.example.hadoop.resourcemanager;

import java.util.HashSet;

import static util.YarnWebUtil.killJobByLikeName;
import static util.YarnWebUtil.killUnContainsJob;

public class ResourceManagerWeb {

    public static void main(String[] args) throws Exception {
        String host = "kudu1";
        String port = "8088";
        HashSet<String> strings = new HashSet<>(8);
        strings.add("Flink session_default_a");
        strings.add("flinksession_4_dev_default_a");
        strings.add("flinksession_3_1_x_default_a");
        strings.add("flinksession_4_1_x_default_a");
        strings.add("run_sync_task_mysql_kudu_1607392664589");


        while (true) {
            killUnContainsJob(host, port, "running", strings);
            killUnContainsJob(host, port, "accepted", strings);
            killJobByLikeName(host, port, "accepted", "cronJob");
            killJobByLikeName(host, port, "running", "cronJob");
            Thread.sleep(3000L);
        }
//        killJobByName(host,port,"running","zabbix_load1_avg");
    }


}
