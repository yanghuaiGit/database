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

package com.example.hdfs.orc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws Exception {


        FileSystem fs = FileSystem.get(getConfiguration());
        Path data = new Path("/temp/kudu1/test/.data");
        FileStatus[] fileStatuses = fs.listStatus(data);
        System.out.println(fileStatuses.length);

       for(int i = 0;i<10;i++){
           new Thread(()->{
               for(FileStatus dataFile : fileStatuses) {
                   try {
                       fs.rename(dataFile.getPath(), new Path("/temp/kudu1/test"));
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
           }).start();
       }
       fs.delete(data,true);
       while(true){
           fs.toString();
       }
    }

    private static Configuration getConfiguration() {
        //非kerberos环境
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://ns1");
        conf.set("dfs.namenode.http-address.ns1.nn2", "kudu2:50070");
        conf.set("dfs.namenode.http-address.ns1.nn1", "kudu1:50070");
        conf.set("dfs.nameservices", "ns1");
        conf.set("hadoop.user.name", "admin");
        conf.set("dfs.namenode.rpc-address.ns1.nn2", "kudu2:9000");
        conf.set("dfs.namenode.rpc-address.ns1.nn1", "kudu1:9000");
        conf.set("dfs.client.failover.proxy.provider.ns1", "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
        conf.set("dfs.ha.namenodes.ns1", "nn1,nn2");
        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        return conf;

        //kerberos环境
//        Configuration conf = new Configuration();
//        conf.set("fs.defaultFS", "hdfs://172.16.100.170:8020");
//        return conf;
    }
}
