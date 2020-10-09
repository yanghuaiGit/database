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
package com.example.demo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * jdbcTest
 *
 * @author by dujie@dtstack.com
 * @Date 2020/8/21
 */
@RunWith(PowerMockRunner.class) // 告诉JUnit使用PowerMockRunner进行测试
@PrepareForTest({})
public class jdbcTest extends BaseJdbcTest {
    String mysqlDriven = "com.mysql.jdbc.Driver";


    @Before
    public void before() throws ClassNotFoundException {
        Class.forName(mysqlDriven);
    }

    @Test
    public void test() throws SQLException, ClassNotFoundException {
        try (Connection connection = DbUtil.getConnection("jdbc:mysql://172.16.8.193:3306/taskcenterdb_00?useSSL=false", "binglog3", "abc123")) {
            String sql = "SELECT `id` FROM `t_task_content_config` WHERE 1=1  ";
            queryBystream(connection, sql);
        }
    }

    @Test
    public void testtimestamp() throws SQLException, ClassNotFoundException {
        try (Connection connection = DbUtil.getConnection("jdbc:mysql://127.0.0.1:3306/dujie?useSSL=false", "root", "rootroot")) {
            String sql = "SELECT * FROM `kudu` WHERE 1=1  ";
            queryBystream(connection, sql);
        }
    }


    @Test
    public void number() {

        Integer.parseInt("1526370627531");
    }


}
