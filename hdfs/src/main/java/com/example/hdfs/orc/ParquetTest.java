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

package com.example.hdfs.orc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import parquet.column.ColumnDescriptor;
import parquet.column.page.Page;
import parquet.column.page.PageReadStore;
import parquet.column.page.PageReader;
import parquet.example.data.Group;
import parquet.example.data.simple.SimpleGroup;
import parquet.example.data.simple.SimpleGroupFactory;
import parquet.format.converter.ParquetMetadataConverter;
import parquet.hadoop.ParquetFileReader;
import parquet.hadoop.ParquetReader;
import parquet.hadoop.ParquetWriter;
import parquet.hadoop.example.GroupReadSupport;
import parquet.hadoop.example.GroupWriteSupport;
import parquet.hadoop.metadata.CompressionCodecName;
import parquet.hadoop.metadata.ParquetMetadata;
import parquet.schema.MessageType;
import parquet.schema.PrimitiveType;

import java.io.IOException;

import static parquet.schema.MessageTypeParser.parseMessageType;

public class ParquetTest {
//    public static final MessageType FILE_SCHEMA = Types.buildMessage()
//            .required(PrimitiveType.PrimitiveTypeName.BINARY).named("user_name")
//            .required(PrimitiveType.PrimitiveTypeName.INT64).named("bookid")
//            .required(PrimitiveType.PrimitiveTypeName.INT32).named("bookscore")
//            .named("douban");

    public static final   MessageType schema = parseMessageType(
            "message test { "
                    + "required binary user_name; "
                    + "required binary bookid; "
                    + "required binary bookscore; "
                    + "} ");


    public static  void write() throws IOException {
        Configuration conf = new Configuration(true);
        conf.set("fs.defaultFS","hdfs://flink:9000");
        conf.set("parquet.example.schema", "message test { "
                + "required binary user_name; "
                + "required binary bookid; "
                + "required binary bookscore; "
                + "} ");

        String file = "/user/yangthuai/parquet/test1.parquet";
        Path path = new Path(file);
        FileSystem fs = path.getFileSystem(conf);
        if (fs.exists(path)) {
            fs.delete(path, true);
        }
        GroupWriteSupport.setSchema(schema, conf);
        SimpleGroupFactory f = new SimpleGroupFactory(schema);


        ParquetWriter<Group> writer = new ParquetWriter<Group>(path, new GroupWriteSupport(),
                CompressionCodecName.GZIP, 1024, 1024,true, false);

        for (int i = 0; i < 1000; i++) {

            writer.write(
                    f.newGroup()
                            .append("user_name", String.valueOf(i))
                            .append("bookid",  String.valueOf(i))
                            .append("bookscore", String.valueOf(i)));
        }
        writer.close();
    }


    public static  void read() throws IOException {
        Configuration conf = new Configuration(true);
        conf.set("fs.defaultFS","hdfs://flink:9000");
        Path parquetFilePath = new Path("/user/yangthuai/parquet/4f9eb10c-11f6-46ed-8d1e-a9aacbd8f836-0.parquet.snappy");

        ParquetReader<Group> groupParquetReader = new ParquetReader<>(conf, parquetFilePath, new GroupReadSupport());

        Group read = groupParquetReader.read();

        String c = read.getString(1,0);

        ParquetMetadata  readFooter = ParquetFileReader.readFooter(conf, parquetFilePath);
        MessageType schema =readFooter.getFileMetaData().getSchema();

        ParquetFileReader parquetFileReader = new ParquetFileReader(conf, parquetFilePath, readFooter.getBlocks(), readFooter.getFileMetaData().getSchema().getColumns());

        PageReadStore pageReadStore = parquetFileReader.readNextRowGroup();
        for( ColumnDescriptor columnDescriptor : readFooter.getFileMetaData().getSchema().getColumns()){
            PageReader pageReader = pageReadStore.getPageReader(columnDescriptor);
            Page page = pageReader.readPage();
        }


    }
    public  static  void testGetSchema() throws IOException {
        Configuration conf = new Configuration(true);
        conf.set("fs.defaultFS","hdfs://flink:9000");




        Path parquetFilePath = new Path("/user/yangthuai/parquet/4f9eb10c-11f6-46ed-8d1e-a9aacbd8f836-0.parquet.snappy");
        ParquetMetadata  readFooter = ParquetFileReader.readFooter(conf, parquetFilePath);
        MessageType schema =readFooter.getFileMetaData().getSchema();
        System.out.println(schema.toString());
    }


    public static void main(String[] args) throws IOException {
//        write();
        testGetSchema();
        read();
    }

}
