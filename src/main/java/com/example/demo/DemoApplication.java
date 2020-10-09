package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

@SpringBootApplication
public class DemoApplication {

//    public static void main(String[] args) {
//        HashMap<String,String> hashMap = new HashMap<>(8);
//        hashMap.putIfAbsent("key","value1");
//        hashMap.putIfAbsent("key","value2");
//        System.out.println(hashMap);
//    }

    public static void main(String[] args) throws ClassNotFoundException {
//        String mysqlDriven = "oracle.jdbc.driver.OracleDriver";
//
//        Class.forName(mysqlDriven);
//
//        PreparedStatement ps = null;
//        ResultSet resultSet = null;
//        try (Connection connection = DbUtil.getConnection("jdbc:oracle:thin:@172.16.8.193:1521:xe", "system", "oracle")) {
//            String sql = "INSERT INTO \"HR\".\"TEST\" (\"ID\",\"ETL_TIME\",\"MAIL_TIME\",\"MAIL_TO\",\"DATE1\") values (?,?,?,?,?)  ";
//            System.out.println(sql);
//            ps = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
//            ps.setObject(1, "13223");
//            ps.setObject(2, new Timestamp(System.currentTimeMillis()));
//            ps.setObject(3, new Timestamp(System.currentTimeMillis()));
//            ps.setObject(4, "223");
//            ps.setObject(5,new Timestamp(System.currentTimeMillis()) );
//
//            ps.execute();
//        } catch (SQLException sqlException) {
//            sqlException.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(new BigInteger("20200727095857001467").toString());
//        Long.parseLong("1294701817204");
//        System.out.println(Long.MAX_VALUE);
//        System.out.println(Integer.MAX_VALUE);
//        Long.parseLong("20200727095857001467");
//
//
//        Integer.parseInt("1294701817204");
        ConfigurableApplicationContext run = SpringApplication.run(DemoApplication.class, args);
        KafkaSend kafkaSend = run.getBean("kafkaSend", KafkaSend.class);
        kafkaSend.test();

    }

//    public static void main(String[] args) {
//        System.out.println(StringUtils.isNoneBlank("schema", "tabletable"));
//        System.out.println(StringUtils.isNoneBlank("schema", ""));
//        System.out.println(StringUtils.isNoneBlank("schema", null));
//    }


}
