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

package util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

//https://zhuanlan.zhihu.com/p/100662052
public class YarnWebUtil {
    public static Logger LOG = LoggerFactory.getLogger(YarnWebUtil.class);

    private static String getAllJobUrl = "http://%s:%s/ws/v1/cluster/apps";
    private static String killJobUrl = "http://%s:%s/ws/v1/cluster/apps/%s/state";



    public static List getAllJobByState(String host, String port, String state) throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String url = String.format(getAllJobUrl, host, port);

        if (StringUtils.isNotBlank(state)) {
            url += "?state=" + state;
        }
        String response = UrlUtil.get(httpClient, url);

        Map map = GsonUtil.GSON.fromJson(response, Map.class);
        Map apps = (Map) map.get("apps");
        if(null == apps){
            return  Collections.emptyList();
        }
        return (List) apps.get("app");
    }


    public static void killJob(String host, String port, String applicationId) throws Exception {

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String url = String.format(killJobUrl, host, port, applicationId);
        UrlUtil.put(httpClient, url, Collections.singletonMap("state", "KILLED"));

        LOG.info(applicationId + "has killed");
    }

    public static void killJobByName(String host, String port, String state, String name) throws Exception {
        List allJobByState = getAllJobByState(host, port, state);
        allJobByState.forEach(i -> {
            Map i1 = (Map) i;
            try {
                if (((String) i1.get("name")).equals(name)) {
                    killJob(host, port, (String) i1.get("id"));
                }

            } catch (Exception e) {

            }
        });
    }

    public static void killJobByLikeName(String host, String port, String state, String name) throws Exception {
        List allJobByState = getAllJobByState(host, port, state);
        allJobByState.forEach(i -> {
            Map i1 = (Map) i;
            try {
                if (((String) i1.get("name")).contains(name)) {
                    killJob(host, port, (String) i1.get("id"));
                }

            } catch (Exception e) {

            }
        });
    }

    public static void killUnContainsJob(String host, String port, String state, HashSet<String> names) throws Exception {
        List allJobByState = getAllJobByState(host, port, state);
        allJobByState.forEach(i -> {
            Map i1 = (Map) i;
            try {
                if (!names.contains((String) i1.get("name"))) {
                    killJob(host, port, (String) i1.get("id"));
                }

            } catch (Exception e) {

            }
        });
    }


    public static void killJobByName(String host, String port, String name) throws Exception {
        List allJobByState = getAllJobByState(host, port, null);
        allJobByState.forEach(i -> {
            Map i1 = (Map) i;
            try {
                if (((String) i1.get("name")).equals(name)) {
                    killJob(host, port, (String) i1.get("id"));
                }

            } catch (Exception e) {

            }
        });
    }

    public static void killJobByLikeName(String host, String port, String name) throws Exception {
        List allJobByState = getAllJobByState(host, port, null);
        allJobByState.forEach(i -> {
            Map i1 = (Map) i;
            try {
                if (((String) i1.get("name")).contains(name)) {
                    killJob(host, port, (String) i1.get("id"));
                }

            } catch (Exception e) {

            }
        });
    }

}
