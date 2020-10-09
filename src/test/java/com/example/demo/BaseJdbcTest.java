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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * BaseJdbcTest
 *
 * @author by dujie@dtstack.com
 * @Date 2020/8/21
 */
public class BaseJdbcTest {

    private static final Logger LOG = LoggerFactory.getLogger(BaseJdbcTest.class);

    protected PreparedStatement ps;
    protected ResultSet resultSet;

    //https://www.jianshu.com/p/c7c5dbe63019
    protected void queryBystream(Connection connection, String sql) throws SQLException {
        ps = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ps.setFetchSize(Integer.MIN_VALUE);
        ps.setFetchDirection(ResultSet.FETCH_REVERSE);
        ps.setQueryTimeout(2000);
        resultSet = ps.executeQuery();
        print();
    }

    protected void queryBystream(Connection connection, String sql, List<Object> objects) throws SQLException {
        ps = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        for (int i = 1; i < objects.size() + 1; i++) {
            ps.setObject(i, objects.get(i - 1));
        }
        ps.setFetchSize(Integer.MIN_VALUE);
        ps.setFetchDirection(ResultSet.FETCH_REVERSE);
        ps.setQueryTimeout(2000);
        resultSet = ps.executeQuery();
        print();
    }

    protected void execute(Connection connection, String sql, List<Object> objects) throws SQLException {
        ps = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        for (int i = 1; i < objects.size() + 1; i++) {
            ps.setObject(i, objects.get(i - 1));
        }

        ps.execute();
    }

    protected void print() throws SQLException {
        while (resultSet.next()) {
            int columnCount = resultSet.getMetaData().getColumnCount();
            for (int i = 1; i < columnCount + 1; i++) {
                LOG.info("{columnName->{},columnData->{}},", resultSet.getMetaData().getColumnName(i), resultSet.getObject(i));
            }
        }
    }
}
