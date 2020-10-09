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

import com.example.demo.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * OracleExample
 *
 * @author by dujie@dtstack.com
 * @Date 2020/9/8
 */
public class OracleExample {
    private static volatile Long id = 116200L;

    public static void main(String[] args) throws ClassNotFoundException {
        String mysqlDriven = "oracle.jdbc.driver.OracleDriver";

        Class.forName(mysqlDriven);

        test();
//        select();
    }

    public static void test() {
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try (Connection connection = DbUtil.getConnection("jdbc:oracle:thin:@kudu5:1521:helowin", "tudou", "abc123")) {
            String sql = "INSERT INTO \"TUDOU\".\"CDC\" (\"ID\",\"USER_ID\",\"NAME\") values (?,?,?)  ";
            ps = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

            for (int i = 0; i < 10000; i++) {
                ps.setObject(1, id++);
                ps.setObject(2, i);
                ps.setObject(3, "test");
                ps.execute();
                Thread.sleep(1000);
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void select() {
        PreparedStatement ps;
        ResultSet resultSet;
        try (
                Connection connection = DbUtil.getConnection("jdbc:oracle:thin:@172.16.8.193:1521:xe", "system", "oracle")) {
            String sql = "select * from HR.TEST  where ID =12 ";
            System.out.println(sql);
            ps = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                Timestamp timestamp = resultSet.getTimestamp(4);
                System.out.println(timestamp);
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
