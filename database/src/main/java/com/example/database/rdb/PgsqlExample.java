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
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * SqlserverExample
 *
 * @author by dujie@dtstack.com
 * @Date 2020/9/8
 */
public class PgsqlExample extends BaseJdbc {

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        new PgsqlExample().testtimestamp();
    }


    public void testtimestamp() throws SQLException, ClassNotFoundException {
        try (Connection connection = DbUtil.getConnection("jdbc:postgresql://172.16.8.193:5432/shitou_test", "root", "postgresql")) {

            String sql = "SELECT a.attname AS name,t.typname AS type, a.attlen AS length, a.atttypmod AS lengthvar \n" +
                    ", a.attnotnull AS notnull , b.description AS comment\n" +
                    "FROM pg_class c, pg_attribute a LEFT JOIN  pg_description b ON a.attrelid = b.objoid\n" +
                    "AND a.attnum = b.objsubid, pg_type t WHERE c.relname = 'age' AND a.attnum > 0\n" +
                    "AND a.attrelid = c.oid AND a.atttypid = t.oid ORDER BY  a.attnum";
//            ArrayList<Object> objects = new ArrayList<>();
//            objects.add(7);
//            objects.add(new Timestamp(System.currentTimeMillis()));
//            objects.add(new Timestamp(System.currentTimeMillis()));
            queryBystream(connection, sql);
        }
    }
}
