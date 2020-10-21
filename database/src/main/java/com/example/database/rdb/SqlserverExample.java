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
import com.example.database.util.DbUtil;

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
public class SqlserverExample extends BaseJdbc {

    static {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        new SqlserverExample().testtimestamp();
    }


    public void testtimestamp() throws SQLException, ClassNotFoundException {
        try (Connection connection = DbUtil.getConnection("jdbc:jtds:sqlserver://172.16.8.149:1433", "sa", "Dtstack2018")) {
            String sql = "INSERT INTO [91test].dbo.abc_test (id, name, date1, datetime1) VALUES  (?, ?, ?,? )";
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(7);
            objects.add("213");
            objects.add(new Timestamp(System.currentTimeMillis()));
            objects.add(new Timestamp(System.currentTimeMillis()));
            execute(connection, sql, objects);
        }
    }
}
