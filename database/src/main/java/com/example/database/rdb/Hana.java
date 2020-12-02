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

package com.example.database.rdb;

import com.example.database.util.DbUtil;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Hana {
//    jdbc:sap://localhost:30013/?databaseName=tdb1&user=SYSTEM&password=manager

    private static final String[] TYPES = {"TABLE", "VIEW"};

    static {
        String mysqlDriven = "com.sap.db.jdbc.Driver";

        try {
            Class.forName(mysqlDriven);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

        try (Connection connection = DbUtil.getConnection("jdbc:sap://172.16.10.251:39017", "SYSTEM", "Abc12345678")) {

            ArrayList<String> listofTable = new ArrayList<String>();

            DatabaseMetaData md = connection.getMetaData();

            ResultSet rs = md.getTables(null, null, "%", null);

            while (rs.next()) {
                if (rs.getString(4).equalsIgnoreCase("TABLE")) {
                    listofTable.add(rs.getString(3));
                }
            }


            String catalog = connection.getCatalog();
            ResultSet catalogs = connection.getMetaData().getCatalogs();
            String schema = connection.getSchema();

            String sql ="select SCHEMA_NAME,TABLE_NAME,TABLE_TYPE from TABLES";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<String> objects = new ArrayList<>();
            while (resultSet.next()){
                objects.add(resultSet.getString(1));
                System.out.println(resultSet.getString(1));
                System.out.println(resultSet.getString(2));
                System.out.println(resultSet.getString(3));
            }

          System.out.println("1");

        }


    }


    public static void getTableMetadata(Connection jdbcConnection, String tableNamePattern, String schema, String catalog, boolean isQuoted) throws Exception {
        try {
            DatabaseMetaData meta = jdbcConnection.getMetaData();
            ResultSet rs = null;
            try {
                if ((isQuoted && meta.storesMixedCaseQuotedIdentifiers())) {
                    rs = meta.getTables(catalog, schema, tableNamePattern, TYPES);
                } else if ((isQuoted && meta.storesUpperCaseQuotedIdentifiers())) {
                    rs = meta.getTables(
                            catalog.toUpperCase(),
                            schema.toUpperCase(),
                            tableNamePattern.toUpperCase(),
                            TYPES
                    );
                } else if ((isQuoted && meta.storesLowerCaseQuotedIdentifiers())
                        || (!isQuoted && meta.storesLowerCaseIdentifiers())) {
                    rs = meta.getTables(
                            catalog.toLowerCase(),
                            schema.toLowerCase(),
                            tableNamePattern.toLowerCase(),
                            TYPES
                    );
                } else {
                    rs = meta.getTables(catalog, schema, tableNamePattern, TYPES);
                }

                while (rs.next()) {
                    String tableName = rs.getString("TABLE_NAME");
                    System.out.println("table = " + tableName);
                }


            } finally {
                if (rs != null) rs.close();
            }
        } catch (SQLException sqlException) {
            // TODO
            sqlException.printStackTrace();
        }


    }

}
