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
package com.example.database.rdb;

import com.example.database.BaseJdbc;
import util.DbUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * JdbcExample
 *
 * @author by dujie@dtstack.com
 * @Date 2020/9/8
 */
public class MysqlExample extends BaseJdbc {

    static {
        String mysqlDriven = "com.mysql.jdbc.Driver";
        try {
            Class.forName(mysqlDriven);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException, InterruptedException {
//        new JdbcExample().select();
//        DriverManager.getConnection("jdbc:mysql://172.16.10.251:3308/tudou", "root", "abc123");
//        for (int i = 10; i < Long.MAX_VALUE; i++) {
//            new MysqlExample().testtimestamp(i);
//            Thread.sleep(300L);
//        }
        new MysqlExample().select();
    }


    public void testtimestamp(long id) throws SQLException, ClassNotFoundException {
        try (Connection connection = DbUtil.getConnection("jdbc:mysql://localhost:3306/dujie?serverTimezone=Asia/Shanghai", "root", "rootroot")) {
            String sql = "INSERT INTO dujie.test1 (id, user_id, name) VALUES (?, ?, ?)";
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(id);
            objects.add(1);
            objects.add("21w3");
            execute(connection, sql, objects);
        }
    }

    public void select() throws SQLException, ClassNotFoundException {
        try (Connection connection = DbUtil.getConnection("jdbc:mysql://127.0.0.1:3306/dujie?useSSL=false&serverTimezone=GMT%2B8", "root", "rootroot")) {
            String sql = "SELECT * FROM `temp`";
            queryBystream(connection, sql);
        }
    }

}
