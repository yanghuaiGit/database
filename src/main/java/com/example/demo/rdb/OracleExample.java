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
import org.apache.commons.lang3.RandomUtils;

import java.math.BigDecimal;
import java.sql.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * OracleExample
 *
 * @author by dujie@dtstack.com
 * @Date 2020/9/8
 */
public class OracleExample {
//    private static volatile AtomicLong longAdder = new AtomicLong(0L);
    private static volatile LongAdder longAdder = new LongAdder();

    //index 25 稳定在2700
    //index 10 稳定在？
    // index 15 稳定在 2428.000
    //index 10 batchsizee 11 100  310 3000 102 101
    public static void main(String[] args) throws ClassNotFoundException, InterruptedException {
        String mysqlDriven = "oracle.jdbc.driver.OracleDriver";

        Class.forName(mysqlDriven);


        long start = System.currentTimeMillis();
        System.out.println("开始时间" + new Timestamp(start));
        for (int index = 0; index < 10; index++) {
            insertBatch(0, 0L,105);
        }
        long prevSum = 0;
//        select();
        //开始
        //结束

        while (true) {
            long l = System.currentTimeMillis();
            Thread.sleep(5000);
            long l1 = (System.currentTimeMillis() - l) / 1000;
            long sum = longAdder.sum();
            System.out.println("qps:" + new BigDecimal(sum - prevSum + "").divide(new BigDecimal(l1), 3, BigDecimal.ROUND_HALF_UP));
            System.out.println("输出sum->" + sum);
            prevSum = sum;
            if (System.currentTimeMillis() - start > (10 * 60 * 1000)+(15*1000)) {
                System.out.println("开始时间："+new Timestamp(start)  + "--结束时间" + new Timestamp(System.currentTimeMillis()) + " 总数" +sum+ " qps->" + new BigDecimal(sum + "").divide(new BigDecimal(10 * 60), 3, BigDecimal.ROUND_HALF_UP));
                System.exit(0);
            }
        }
    }

    public static void test() {
        PreparedStatement ps = null;
        try (Connection connection = DbUtil.getConnection("jdbc:oracle:thin:@kudu5:1521:helowin", "tudou", "abc123")) {
            String sql = "INSERT INTO \"TUDOU\".\"CDC\" (\"ID\",\"USER_ID\",\"NAME\") values (?,?,?)  ";
            ps = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

            ps.setObject(1, 1);
            ps.setObject(2, 1);
            ps.setObject(3, "test");
            ps.execute();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        }

    }

    public static void select() {
        PreparedStatement ps;
        ResultSet resultSet;
        try (Connection connection = DbUtil.getConnection("jdbc:oracle:thin:@172.16.8.193:1521:xe", "system", "oracle")) {
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


    public static void insertBatch(int i, Long limit, int batchSize) {
        new Thread(() -> {
            PreparedStatement ps;
            try (Connection connection = DbUtil.getConnection("jdbc:oracle:thin:@172.16.100.42:1521:ORCL", "roma_logminer", "abc123")) {
                String sql = "INSERT INTO ROMA_LOGMINER.B2CTDB_ACT_INVENTORY (CTN_NO, VESSEL_IE, CTN_OPERATOR_CODE, CTN_TYPE, CTN_SIZE, CTN_STATUS, CTN_CATEGORY, TARE_WEIGHT, CTN_WEIGHT, CTN_HEIGHT, SEAL_NO1, SEAL_NO2, SEAL_NO3, DOOR_DIRECTION, FORWARDER_CODE, OD_FLAG, HAZARD_FLAG, REEFER_FLAG, DAMAGE_FLAG, CARGO_CODE, TRANSFER_PORT_CODE, GROUP_ID, SPARCS_HOLDS, DESTINATION_PORT_CODE, OPT_DIS_PORT_CODE, LOAD_PORT_CODE, REMARKS, TRADE_NW, ACTUAL_POSITION_QUALIFER, ACTUAL_LOCATION, ACTUAL_POSITION_BLOCK, ACTUAL_SLOT_POSITION, CTN_WEIGHT_UP, HISTORY_FLAG, RELEASE_FLAG, RELEASE_METHOD, CTN_WEIGHT_CRANE, RELEASE_USERID, TRUCK_NO_IN, RELEASE_TIME, TRUCK_NO_OUT, OUTER_DIRECTION, POSITION_ON_TRUCK, CTN_NO1, CTN_NO2, PLAN_POSITION_BLOCK, CTN_NO3, SPECIAL_STOW_CODE, INBOUND_CARRIER, SG_FLAG, STOW_REQUIREMENT_FLAG, FIRST_INQUAY_USERID, CTN_NO4, FIRST_QUARY_POSITION, FP_FLAG, RETURN_TRANSHIP_FLAG, INBOUND_RELEASE, PLAN_POSITION_QUALIFER, EXIT_CUSTOM, OUTBOUND_CARRIER, PLAN_LOCATION, LAST_OUTQUAY_USERID, PLAN_SLOT_POSITION, LAST_OUTQUAY_POSITION, DISCHARGE_PORT_CODE, INBOUND_CATEGORY, OUTBOUND_CATEGORY, FIRST_INQUAY_TIME, FIRST_INYARD_TIME, INGATE_RECEIVE_TIME, INGATE_DELIVERY_TIME, TRANSFER_FLAG, LAST_OUTYARD_TIME, LAST_OUTQUAY_TIME, CTOS_HOLD1, CTOS_HOLD2, CTOS_HOLD3, RELEASE_LOCATION, CUSTOM_RELEASE_FLAG, CTOS_HOLD4, CTOS_HOLD5, EQUIPMENT_RECEIPT_NO, CFS_FLAG, TRADEFREE_FLAG, CHANGE_CTN_DIRECTION, PROCESS_STATUS, RF_TO_GP, BILLING_STATUS, BOOKING_NUMBER, CTN_MATERIAL, DIRECT_FLAG, STUFFING_NO, RECEIVE_BOOKING_NO, RECEIVE_BOK_METHOD, DELIVERY_BOOKING_NO, DELIVERY_BOK_METHOD, RECEIVE_DELIVERY_FLAG, ACTUAL_INBOUND_POSITION, ACTUAL_OUTBOUND_POSITION, BOOKING_NO, INSPECTION_ID, INSPECTION_FLAG, ASSIGN_EMPTY_VESSEL, FROM_SOURCE, CFS_YARD, JOB_TYPE, SPARCS_FLAG, DELIVERY_INUSERID, LOAD_VESSEL_REFERENCE, LOAD_INSTCO, LOAD_DTINCO, LOAD_TMINCO, LOAD_DTGOCO, LOAD_TMGOCO, CTN_TRIM_FLAG, SENDER_ID, CTN_WEIGHT_UP_CTN, CTN_WEIGHT_UP_CTN_VESSEL, TT_FLAG, FROM_TERMINAL, TO_TERMINAL, CHECK_USERID, CHECK_TIME, DMS_CTN_RETURN, BUFFER_FLAG, IN_BUFFER_FLAG, SEAL_CUST, SEAL_CIQ, IN_YARD_GATENO, OUT_YARD_GATENO, FB_FLAG, VESSEL_UN_CODE_YJ, INBOUND_VOYAGE_YJ, CTN_WEIGHT_VGM, HT_FLAG, INSERT_TIME, UPDATE_TIME, DATA_SOURCE, STATUS) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                ps = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

                while (true) {

                    long start = System.currentTimeMillis();
                    for (int size = 0; size < batchSize; size++) {
                        ps.setString(1, i + "");
                        ps.setObject(2, "N");
                        ps.setObject(3, RandomUtils.nextInt(0, 99) + "");
                        ps.setString(4, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(5, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(6, "N");
                        ps.setString(7, "N");
                        ps.setObject(8, i);
                        ps.setObject(9, i);
                        ps.setObject(10, i);
                        ps.setObject(11, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(12, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(13, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(14, "N");
                        ps.setObject(15, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(16, "N");
                        ps.setObject(17, "N");
                        ps.setObject(18, "N");
                        ps.setObject(19, "N");
                        ps.setObject(20, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(21, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(22, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(23, "2");
                        ps.setObject(24, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(25, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(26, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(27, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(28, "N");
                        ps.setObject(29, "Y");
                        ps.setObject(30, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(31, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(32, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(33, i);
                        ps.setObject(34, "N");
                        ps.setObject(35, "y");
                        ps.setObject(36, "N");
                        ps.setObject(37, i);
                        ps.setObject(38, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(39, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(40, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(41, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(42, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(43, "N");
                        ps.setObject(44, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(45, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(46, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(47, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(48, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(49, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(50, "N");
                        ps.setObject(51, "N");
                        ps.setObject(52, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(53, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(54, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(55, "N");
                        ps.setObject(56, "N");
                        ps.setObject(57, "N");
                        ps.setObject(58, "N");
                        ps.setObject(59, "N");
                        ps.setObject(60, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(61, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(62, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(63, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(64, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(65, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(66, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(67, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(68, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(69, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(70, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(71, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(72, "N");
                        ps.setObject(73, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(74, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(75, "N");
                        ps.setObject(76, "N");
                        ps.setObject(77, "N");
                        ps.setObject(78, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(79, "N");
                        ps.setObject(80, "N");
                        ps.setObject(81, "N");
                        ps.setObject(82, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(83, "N");
                        ps.setObject(84, "N");
                        ps.setObject(85, "N");
                        ps.setObject(86, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(87, "N");
                        ps.setObject(88, "Y");
                        ps.setObject(89, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(90, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(91, "N");
                        ps.setObject(92, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(93, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(94, "N");
                        ps.setObject(95, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(96, "N");
                        ps.setObject(97, "N");
                        ps.setObject(98, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(99, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(100, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(101, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(102, "N");
                        ps.setObject(103, "N");
                        ps.setObject(104, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(105, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(106, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(107, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(108, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(109, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(110, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(111, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(112, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(113, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(114, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(115, "N");
                        ps.setObject(116, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(117, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(118, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(119, "N");
                        ps.setObject(120, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(121, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(122, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(123, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(124, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(125, "N");
                        ps.setObject(126, "N");
                        ps.setObject(127, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(128, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(129, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(130, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(131, "N");
                        ps.setObject(132, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(133, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(134, i);
                        ps.setObject(135, "*");
                        ps.setObject(136, new Date(System.currentTimeMillis()));
                        ps.setObject(137, new Date(System.currentTimeMillis()));
                        ps.setObject(138, RandomUtils.nextInt(0, 99) + "");
                        ps.setObject(139, RandomUtils.nextInt(0, 99) + "");
                        ps.addBatch();
                    }
                    ps.executeBatch();
                    longAdder.add(batchSize);
                    ps.clearBatch();
                    long l = System.currentTimeMillis() - start;
                    if (l <1000) {
                        Thread.sleep(1000-l);
                    }

                }

            } catch (Exception sqlException) {
                sqlException.printStackTrace();
            }
        }).start();


    }
}
