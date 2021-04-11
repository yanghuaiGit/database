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

package com.example.database.hadoop;

import org.apache.kudu.ColumnSchema;
import org.apache.kudu.Schema;
import org.apache.kudu.Type;
import org.apache.kudu.client.CreateTableOptions;
import org.apache.kudu.client.Insert;
import org.apache.kudu.client.KuduClient;
import org.apache.kudu.client.KuduException;
import org.apache.kudu.client.KuduScanner;
import org.apache.kudu.client.KuduSession;
import org.apache.kudu.client.KuduTable;
import org.apache.kudu.client.PartialRow;
import org.apache.kudu.client.RowResult;
import org.apache.kudu.client.RowResultIterator;
import org.apache.kudu.client.SessionConfiguration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class KuduReader {
    public static KuduClient kuduClient;

    public static void main(String[] args) throws Exception {
        String kuduMaster = "172.16.101.134:7051";
        String tableName = "student";
        init(kuduMaster);
        createTable(tableName);
//        insertTable(tableName);
//        queryData(tableName);


    }

    public static void init(String kuduMaster) {
        try {
            System.out.println("initing........................");
            //初始化操作

            //指定表名
            String tableName = "student";
            KuduClient.KuduClientBuilder kuduClientBuilder = new KuduClient.KuduClientBuilder(kuduMaster);
            kuduClientBuilder.defaultOperationTimeoutMs(1800000);
            kuduClient = kuduClientBuilder.build();
            System.out.println("服务器地址" + kuduMaster + ":客户端" + kuduClient + "初始化成功...");
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public static void createTable(String tableName) throws KuduException {

        // 设置表的schema
        List<ColumnSchema> columns = new LinkedList<ColumnSchema>();
        columns.add(newColumn("id", Type.STRING, true));
        columns.add(newColumn("name", Type.STRING, false));
        columns.add(newColumn("age", Type.INT32, false));
        columns.add(newColumn("sex", Type.INT32, false));
        Schema schema = new Schema(columns);

        // 设置表的replica备份和分区规则
        List<String> parcols = new LinkedList<String>();
        parcols.add("id");

        //创建表时提供的所有选项
        CreateTableOptions options = new CreateTableOptions();
        options.setNumReplicas(1);  //设置表的备份数
        options.setRangePartitionColumns(parcols);  //设置range分区
        options.addHashPartitions(parcols, 3);  //设置hash分区和数量
        try {
            kuduClient.createTable(tableName, schema, options);
        } catch (KuduException e) {
            e.printStackTrace();
        }
    }

    private static ColumnSchema newColumn(String name, Type type, boolean iskey) {
        ColumnSchema.ColumnSchemaBuilder column = new ColumnSchema.ColumnSchemaBuilder(name, type);
        column.key(iskey);
        return column.build();
    }


    public static void insertTable(String tableName) throws KuduException {
        //向表加载数据需要一个 kuduSession 对象
        KuduSession kuduSession = kuduClient.newSession();
        //        kuduSession.set
        kuduSession.setTimeoutMillis(100000);
        kuduSession.setFlushMode(SessionConfiguration.FlushMode.AUTO_FLUSH_SYNC);
        //需要使用 kuduTable 来构建 Operation 的子类实例对象
        KuduTable kuduTable = kuduClient.openTable(tableName);
        for (int i = 1; i <= 10; i++) {
            Insert insert = kuduTable.newInsert();
            PartialRow row = insert.getRow();
            row.addString("id", i + "");
            row.addString("name", "zhangsan-" + i);
            row.addInt("age", 20 + i);
            row.addInt("sex", i % 2);
            //最后实现执行数据的加载操作
            kuduSession.apply(insert);
        }
    }


    /**
     * 查询表的数据结果
     */
    public static void queryData(String tableName) throws KuduException {

        //构建一个查询的扫描器
        KuduScanner.KuduScannerBuilder kuduScannerBuilder =
                kuduClient.newScannerBuilder(kuduClient.openTable(tableName));
        ArrayList<String> columnsList = new ArrayList<String>();
        columnsList.add("id");
        columnsList.add("name");
        columnsList.add("age");
        columnsList.add("sex");
        kuduScannerBuilder.setProjectedColumnNames(columnsList);
        //返回结果集
        KuduScanner kuduScanner = kuduScannerBuilder.build();
        //遍历
        while (kuduScanner.hasMoreRows()) {
            RowResultIterator rowResults = kuduScanner.nextRows();
            while (rowResults.hasNext()) {
                RowResult row = rowResults.next();
                String id = row.getString("id");
                String name = row.getString("name");
                int age = row.getInt("age");
                int sex = row.getInt("sex");
                System.out.println(">>>>>>>>>>  id=" + id + " name=" + name + " age=" + age + "sex = " + sex);
            }
        }
    }
}
