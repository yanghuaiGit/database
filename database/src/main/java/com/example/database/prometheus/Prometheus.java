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

package com.example.database.prometheus;


import io.prometheus.client.exporter.PushGateway;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import util.UrlUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Prometheus {

    public static void main(String[] args) throws Exception {
        PushGateway pushGateway = new PushGateway("kudu5" + ':' + "9091");

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String monitors = String.format("http://kudu5:9091/metrics");

        String response = UrlUtil.get(httpClient, monitors);
        String pattern = "job=\".*\"";

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(response);
        while (m.find()) {
            String group = m.group(0);
            System.out.println("Found value: " + group);
            pushGateway.delete(group.substring(5,group.length()-1));
        }
    }
}
