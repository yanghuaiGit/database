///*
// * Licensed to the Apache Software Foundation (ASF) under one
// * or more contributor license agreements.  See the NOTICE file
// * distributed with this work for additional information
// * regarding copyright ownership.  The ASF licenses this file
// * to you under the Apache License, Version 2.0 (the
// * "License"); you may not use this file except in compliance
// * with the License.  You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.example.hdfs.orc;
//
//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.fs.FileSystem;
//import org.apache.hadoop.fs.Path;
//import org.apache.parquet.column.ColumnDescriptor;
//import org.apache.parquet.column.ParquetProperties;
//import org.apache.parquet.column.page.Page;
//import org.apache.parquet.column.page.PageReadStore;
//import org.apache.parquet.column.page.PageReader;
//import org.apache.parquet.example.data.Group;
//import org.apache.parquet.example.data.simple.SimpleGroupFactory;
//import org.apache.parquet.hadoop.ParquetFileReader;
//import org.apache.parquet.hadoop.ParquetReader;
//import org.apache.parquet.hadoop.ParquetWriter;
//import org.apache.parquet.hadoop.example.GroupReadSupport;
//import org.apache.parquet.hadoop.example.GroupWriteSupport;
//import org.apache.parquet.hadoop.metadata.CompressionCodecName;
//import org.apache.parquet.hadoop.metadata.ParquetMetadata;
//import org.apache.parquet.schema.MessageType;
//
//
//import java.io.IOException;
//
//import static org.apache.parquet.schema.MessageTypeParser.parseMessageType;
//
//
//public class ParquetTest1 {
////    public static final MessageType FILE_SCHEMA = Types.buildMessage()
////            .required(PrimitiveType.PrimitiveTypeName.BINARY).named("user_name")
////            .required(PrimitiveType.PrimitiveTypeName.INT64).named("bookid")
////            .required(PrimitiveType.PrimitiveTypeName.INT32).named("bookscore")
////            .named("douban");
//
//    public static final MessageType schema = parseMessageType(
//            "message test { "
//                    + "required binary user_name; "
//                    + "required binary bookid; "
//                    + "required binary bookscore; "
//                    + "} ");
//
//
//    public static  void write() throws IOException {
//        Configuration conf = new Configuration(true);
//        conf.set("fs.defaultFS","hdfs://flink:9000");
//        conf.set("parquet.example.schema", "message test { "
//                + "required binary user_name; "
//                + "required binary bookid; "
//                + "required binary bookscore; "
//                + "} ");
//
//        String file = "/user/yangthuai/parquet/test1.parquet";
//        Path path = new Path(file);
//        FileSystem fs = path.getFileSystem(conf);
//        if (fs.exists(path)) {
//            fs.delete(path, true);
//        }
//        GroupWriteSupport.setSchema(schema, conf);
//        SimpleGroupFactory f = new SimpleGroupFactory(schema);
//
//
//        ParquetWriter<Group> writer = new ParquetWriter<Group>(path, new GroupWriteSupport(),
//                CompressionCodecName.GZIP, 1024, 1024,1024, false,true, ParquetProperties.WriterVersion.PARQUET_2_0,conf);
//
//        for (int i = 0; i < 100; i++) {
//
//            writer.write(
//                    f.newGroup()
//                            .append("user_name", String.valueOf(i))
//                            .append("bookid",  String.valueOf(i))
//                            .append("bookscore", String.valueOf(i)));
//        }
//        writer.close();
//    }
//
//
//    public static  void read() throws IOException {
//        Configuration conf = new Configuration(true);
//        conf.set("fs.defaultFS","hdfs://flink:9000");
//        Path parquetFilePath = new Path("/user/yangthuai/parquet/test1.parquet");
//
//        ParquetReader<Group> groupParquetReader = new ParquetReader<>(conf, parquetFilePath, new GroupReadSupport());
//
//        Group read = groupParquetReader.read();
//
//        String c = read.getString(1,0);
//
//        ParquetMetadata readFooter = ParquetFileReader.readFooter(conf, parquetFilePath);
//        MessageType schema =readFooter.getFileMetaData().getSchema();
//
//        ParquetFileReader parquetFileReader = new ParquetFileReader(conf, parquetFilePath, readFooter.getBlocks(), readFooter.getFileMetaData().getSchema().getColumns());
//
//        PageReadStore pageReadStore = parquetFileReader.readNextRowGroup();
//        for( ColumnDescriptor columnDescriptor : readFooter.getFileMetaData().getSchema().getColumns()){
//            PageReader pageReader = pageReadStore.getPageReader(columnDescriptor);
//            Page page = pageReader.readPage();
//        }
//
//
//    }
//    public  static  void testGetSchema() throws IOException {
//        Configuration conf = new Configuration(true);
//        conf.set("fs.defaultFS","hdfs://flink:9000");
//
//
//
//
//        Path parquetFilePath = new Path("/user/yangthuai/parquet/test1.parquet");
//        ParquetMetadata  readFooter = ParquetFileReader.readFooter(conf, parquetFilePath);
//        MessageType schema =readFooter.getFileMetaData().getSchema();
//        System.out.println(schema.toString());
//    }
//
//
//    public static void main(String[] args) throws IOException {
//        write();
//        testGetSchema();
//        read();
//    }
//
//}
