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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.io.orc.OrcSerde;
import org.apache.hadoop.hive.serde2.io.DateWritable;
import org.apache.hadoop.hive.serde2.io.HiveDecimalWritable;
import org.apache.hadoop.hive.serde2.io.TimestampWritable;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.ByteWritable;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordWriter;
import org.apache.hadoop.mapred.Reporter;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * HDfs
 *
 * @author by dujie@dtstack.com
 * @Date 2020/8/27
 */
public class HDfs {

    public static void main(String[] args) throws Exception {
        List<String> fullColumnNames = new ArrayList<>();
        fullColumnNames.add("colum1");
        fullColumnNames.add("colum2");
        fullColumnNames.add("colum3");
        fullColumnNames.add("colum4");
        fullColumnNames.add("colum5");

        write(fullColumnNames);
    }


    private static void write(List<String> fullColumnNames) throws Exception {
        String HADOOP_URL = "hdfs://flinkx1:9000";
        Configuration conf = new Configuration();
        conf.set(" fs.default.name", HADOOP_URL);

        List<ObjectInspector> fullColTypeList = new ArrayList<>();


        ColumnType type = ColumnType.INT;
        fullColTypeList.add(columnTypeToObjectInspetor(type));
        type = ColumnType.TIMESTAMP;
        fullColTypeList.add(columnTypeToObjectInspetor(type));
        type = ColumnType.TIMESTAMP;
        fullColTypeList.add(columnTypeToObjectInspetor(type));
        type = ColumnType.STRING;
        fullColTypeList.add(columnTypeToObjectInspetor(type));
        type = ColumnType.DATE;
        fullColTypeList.add(columnTypeToObjectInspetor(type));

        RecordWriter recordWriter;
        OrcSerde orcSerde = new OrcSerde();
        ;
        StructObjectInspector inspector = ObjectInspectorFactory
                .getStandardStructObjectInspector(fullColumnNames, fullColTypeList);
        ;
        JobConf jobConf = new JobConf(conf);
        FileOutputFormat outputFormat = new org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat();
        ;

        FileSystem fs = FileSystem.get(new URI(HADOOP_URL), new Configuration(), "root");
        recordWriter = outputFormat.getRecordWriter(fs, jobConf, "/haha/hh1.txt", Reporter.NULL);


        List<Object> recordList = new ArrayList<>();
        getData(recordList, ColumnType.INT, "12233");
        getData(recordList, ColumnType.TIMESTAMP, "2020-08-27 20:15:08.000000");
        getData(recordList, ColumnType.TIMESTAMP, "2020-08-27 20:15:08.000000");
        getData(recordList, ColumnType.STRING, "test2");
        getData(recordList, ColumnType.DATE, new Timestamp(System.currentTimeMillis()).toString());
        recordWriter.write(NullWritable.get(), orcSerde.serialize(recordList, inspector));
        recordWriter.close(Reporter.NULL);
        // 实现上传文件,首先是读取本地的文件
        System.out.println(fs.exists(new Path("/test1")));
        System.out.println(fs.exists(new Path("/test")));
        System.out.println(fs.exists(new Path("/12")));

        // 关闭流
        fs.close();
    }

    public static Object getWritableValue(Object writable) {
        Class<?> clz = writable.getClass();
        Object ret = null;

        if (clz == IntWritable.class) {
            ret = ((IntWritable) writable).get();
        } else if (clz == Text.class) {
            ret = ((Text) writable).toString();
        } else if (clz == LongWritable.class) {
            ret = ((LongWritable) writable).get();
        } else if (clz == ByteWritable.class) {
            ret = ((ByteWritable) writable).get();
        } else if (clz == DateWritable.class) {
            ret = ((DateWritable) writable).get();
        } else if (writable instanceof DoubleWritable) {
            ret = ((DoubleWritable) writable).get();
        } else if (writable instanceof TimestampWritable) {
            ret = ((TimestampWritable) writable).getTimestamp();
        } else if (writable instanceof DateWritable) {
            ret = ((DateWritable) writable).get();
        } else if (writable instanceof FloatWritable) {
            ret = ((FloatWritable) writable).get();
        } else if (writable instanceof BooleanWritable) {
            ret = ((BooleanWritable) writable).get();
        } else {
            ret = writable.toString();
        }
        return ret;
    }

    public static ObjectInspector columnTypeToObjectInspetor(ColumnType columnType) {
        ObjectInspector objectInspector = null;
        switch (columnType) {
            case TINYINT:
                objectInspector = ObjectInspectorFactory.getReflectionObjectInspector(Byte.class, ObjectInspectorFactory.ObjectInspectorOptions.JAVA);
                break;
            case SMALLINT:
                objectInspector = ObjectInspectorFactory.getReflectionObjectInspector(Short.class, ObjectInspectorFactory.ObjectInspectorOptions.JAVA);
                break;
            case INT:
                objectInspector = ObjectInspectorFactory.getReflectionObjectInspector(Integer.class, ObjectInspectorFactory.ObjectInspectorOptions.JAVA);
                break;
            case BIGINT:
                objectInspector = ObjectInspectorFactory.getReflectionObjectInspector(Long.class, ObjectInspectorFactory.ObjectInspectorOptions.JAVA);
                break;
            case FLOAT:
                objectInspector = ObjectInspectorFactory.getReflectionObjectInspector(Float.class, ObjectInspectorFactory.ObjectInspectorOptions.JAVA);
                break;
            case DOUBLE:
                objectInspector = ObjectInspectorFactory.getReflectionObjectInspector(Double.class, ObjectInspectorFactory.ObjectInspectorOptions.JAVA);
                break;
            case DECIMAL:
                objectInspector = ObjectInspectorFactory.getReflectionObjectInspector(HiveDecimalWritable.class, ObjectInspectorFactory.ObjectInspectorOptions.JAVA);
                break;
            case TIMESTAMP:
                objectInspector = ObjectInspectorFactory.getReflectionObjectInspector(java.sql.Timestamp.class, ObjectInspectorFactory.ObjectInspectorOptions.JAVA);
                break;
            case DATE:
                objectInspector = ObjectInspectorFactory.getReflectionObjectInspector(java.sql.Date.class, ObjectInspectorFactory.ObjectInspectorOptions.JAVA);
                break;
            case STRING:
            case VARCHAR:
            case CHAR:
                objectInspector = ObjectInspectorFactory.getReflectionObjectInspector(String.class, ObjectInspectorFactory.ObjectInspectorOptions.JAVA);
                break;
            case BOOLEAN:
                objectInspector = ObjectInspectorFactory.getReflectionObjectInspector(Boolean.class, ObjectInspectorFactory.ObjectInspectorOptions.JAVA);
                break;
            case BINARY:
                objectInspector = ObjectInspectorFactory.getReflectionObjectInspector(BytesWritable.class, ObjectInspectorFactory.ObjectInspectorOptions.JAVA);
                break;
            default:
                throw new IllegalArgumentException("You should not be here");
        }
        return objectInspector;
    }

    public static void getData(List<Object> recordList, ColumnType columnType, String rowData) {


        switch (columnType) {
            case TINYINT:
                recordList.add(Byte.valueOf(rowData));
                break;
            case SMALLINT:
                recordList.add(Short.valueOf(rowData));
                break;
            case INT:
                recordList.add(Integer.valueOf(rowData));
                break;

            case FLOAT:
                recordList.add(Float.valueOf(rowData));
                break;
            case DOUBLE:
                recordList.add(Double.valueOf(rowData));
                break;
//            case DECIMAL:
//                recordList.add(getDecimalWritable(index, rowData));
//                break;
            case STRING:
                recordList.add((rowData));
                break;
            case VARCHAR:
            case CHAR:
                SimpleDateFormat fm = DateUtil.getDateTimeFormatter();
                recordList.add(fm.format(rowData));
                break;
            case BOOLEAN:
                recordList.add(Boolean.valueOf(rowData));
                break;
            case DATE:
                recordList.add(DateUtil.columnToDate(rowData, null));
                break;
            case TIMESTAMP:
                recordList.add(DateUtil.columnToTimestamp(rowData, null));
                break;
            case BINARY:
                recordList.add(new BytesWritable(rowData.getBytes(StandardCharsets.UTF_8)));
                break;
            default:
                throw new IllegalArgumentException();
        }
    }
}
