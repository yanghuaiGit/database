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
package com.example.demo.rdb;

import com.example.demo.BaseJdbc;
import com.example.demo.DbUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * db2Example
 *
 * @author by dujie@dtstack.com
 * @Date 2020/9/8
 */
public class db2Example extends BaseJdbc {

    static {
        try {
            Class.forName("com.ibm.db2.jcc.DB2Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new db2Example().test();
    }

    public void test() {
        try (Connection connection = DbUtil.getConnection("jdbc:db2://172.16.10.168:50000/AUDIT", "DB2INST1", "db2root-pwd")) {
            String sql = "INSERT INTO DUJIE.TEST (ID, DATE1, TIMESTAMP1) values (?,?,?)  ";
            ps = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ps.setObject(1, 2);
            ps.setObject(2, new Timestamp(System.currentTimeMillis()));
            ps.setObject(3, new Timestamp(System.currentTimeMillis()));
            ps.execute();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
