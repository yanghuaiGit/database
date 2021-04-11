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

import util.DbUtil;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Logminer {

    static {
        String mysqlDriven = "oracle.jdbc.driver.OracleDriver";

        try {
            Class.forName(mysqlDriven);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        HashMap<String,String> scns = new HashMap<>(4096);
        HashMap<String,String> scns1 = new HashMap<>(4096);
        test("/data/oracle/archivelog/1_1019_934817032.dbf", scns);
        System.out.println("--------************------------");
//        test("+DATA/orcl/arch/1_140_1063406722.dbf", scns1);

        Set<String> key1 = scns.keySet();
        Set<String> key2 = scns1.keySet();


        key1.forEach(i -> {
            if (key2.contains(i)) {
                System.out.println("重复scn " + i+" value" +scns.get(i));
            }
        });

        key2.forEach(i -> {
            if (key1.contains(i)) {
                System.out.println("重复scn " + i+" value" +scns1.get(i));
            }
        });


    }


    public static void test(String filePath, Map<String, String> scns) throws SQLException, ClassNotFoundException {
        // 获取源数据库连接
//        Connection connection = DbUtil.getConnection("jdbc:oracle:thin:@//10.168.4.36:1521/ctmsprd", "roma_logminer", "Logminer#2020");
        Connection connection = DbUtil.getConnection("jdbc:oracle:thin:@//172.16.100.220:1521/xe", "tudou", "abc123");

        Statement statement = connection.createStatement();

//        INSERT INTO "TUDOU"."cdc"("id", "name") VALUES ('36', '1');
        // 添加所有日志文件，本代码仅分析联机日志
        StringBuilder sbSQL = new StringBuilder(1024);
        sbSQL.append(" BEGIN");
        sbSQL.append(" SYS.dbms_logmnr.add_logfile(logfilename=>'").append(filePath).append("', options=>SYS.dbms_logmnr.NEW);");
        ///u01/app/oracle/product/11.2.0/db_1/dbs/arch2_7_1063406722.dbf 1003858 insert into "TUDOU"."cdc"("id","name") values (2,'1'
        sbSQL.append(" END;");
        CallableStatement callableStatement = connection.prepareCall(sbSQL.toString());
        callableStatement.execute();
        callableStatement = connection.prepareCall(" BEGIN sys.dbms_logmnr.start_logmnr(options => SYS.DBMS_LOGMNR.dict_from_online_catalog );END;");
        callableStatement.execute();
        System.out.println("完成分析日志文件");

        // 查询获取分析结果
        System.out.println("查询分析结果");
        ResultSet rs;

//        while (true) {
        rs = statement.executeQuery("SELECT scn,timestamp,table_name,sql_redo,operation FROM v$logmnr_contents  ");
        //        rs = statement.executeQuery("SELECT count(*) FROM v$logmnr_contents WHERE scn > 5541273 and OPERATION in ('DELETE','INSERT','UPDATE')  and ((SEG_OWNER='HOSTDB' and TABLE_NAME='ACT_INVENTORY') or (SEG_OWNER='HOSTDB' and TABLE_NAME='ACT_INVENTORY_BL') or (SEG_OWNER='HOSTDB' and TABLE_NAME='ACT_INVENTORY_HZ') or (SEG_OWNER='HOSTDB' and TABLE_NAME='ACT_INVENTORY_OD') or (SEG_OWNER='HOSTDB' and TABLE_NAME='ACT_INVENTORY_RF') or (SEG_OWNER='HOSTDB' and TABLE_NAME='ACT_CTN_MOVEMENT') or (SEG_OWNER='HOSTDB' and TABLE_NAME='ACT_DOCIN_CTN') or (SEG_OWNER='HOSTDB' and TABLE_NAME='ACT_DOCIN_BL') or (SEG_OWNER='HOSTDB' and TABLE_NAME='VSP_VOYAGE_PLAN') or (SEG_OWNER='HOSTDB' and TABLE_NAME='COD_SERVICE_OPERATOR') or (SEG_OWNER='HOSTDB' and TABLE_NAME='COD_YARD_LIST') or (SEG_OWNER='HOSTDB' and TABLE_NAME='BOK_INTERNAL') or (SEG_OWNER='HOSTDB' and TABLE_NAME='HIS_INVENTORY') or (SEG_OWNER='HOSTDB' and TABLE_NAME='HIS_CTN_MOVEMENT') or (SEG_OWNER='HOSTDB' and TABLE_NAME='HIS_TRUCK_DYNAMIC') or (SEG_OWNER='HOSTDB' and TABLE_NAME='ACT_TRUCK_DYNAMIC') or (SEG_OWNER='HOSTDB' and TABLE_NAME='QGAWORKINFO'))");
        int count = rs.getMetaData().getColumnCount();
        List<String> nameList = new ArrayList<>(count);
        for (int i = 1; i <= count; i++) {
            nameList.add(rs.getMetaData().getColumnName(i));
        }


        while (rs.next()) {
            StringBuilder sb = new StringBuilder(128);
            for (int i = 1; i <= count; i++) {
                sb.append("name = {").append(nameList.get(i - 1)).append("},").append(" value = {").append(rs.getObject(i)).append(" }");
            }


            if (sb.length() > 0) {
                String scn = rs.getString("SCN");

                scns.put(scn, sb.toString());
                System.out.println(sb.toString());
                System.out.println("----------------------------------------------------------------");
            }
        }



        System.out.println("**************************************");
    }
}
