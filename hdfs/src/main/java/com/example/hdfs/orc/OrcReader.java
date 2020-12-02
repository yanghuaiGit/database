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

package com.example.hdfs.orc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.exec.vector.BytesColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.LongColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.MapColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.StructColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.TimestampColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.VectorizedRowBatch;
import org.apache.orc.OrcFile;
import org.apache.orc.Reader;
import org.apache.orc.RecordReader;
import org.apache.orc.TypeDescription;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

public class OrcReader {
    public static void main(Configuration conf, String[] args) throws IOException {


        // Get the information from the file footer
        Reader reader = OrcFile.createReader(new Path("/tmp/orc/test1.orc"),
                OrcFile.readerOptions(conf));
//        byte[] bytes = new byte[3];
//        ByteBuffer byteBuffer = reader.getSerializedFileFooter().get(bytes);
//        System.out.println(bytes);


        System.out.println("File schema: " + reader.getSchema());
        List<TypeDescription> children = reader.getSchema().getChildren();
        int numFields = children.size();
//        readField(structs, i, childrenTypes.get(i), structVector.fields[i],

        List<String> fieldNames = reader.getSchema().getFieldNames();

        System.out.println("Row count: " + reader.getNumberOfRows());
//        System.out.println(reader.getStripes());
        System.out.println(reader.getFileTail());
        // Pick the schema we want to read using schema evolution
        //看下orc支持的类型  如何进行转换
        TypeDescription readSchema =
                TypeDescription.fromString(reader.getSchema().toString());



        RecordReader rowIterator = reader.rows(reader.options()
                .schema(readSchema));

        // Read the row data
        VectorizedRowBatch batch = readSchema.createRowBatch();
//        VectorizedRowBatch batch = reader.getSchema().createRowBatch();

        MapColumnVector first1 = (MapColumnVector) batch.cols[0];
        LongColumnVector second = (LongColumnVector) batch.cols[1];
        StructColumnVector structColumnVector = (StructColumnVector) batch.cols[2];

        TimestampColumnVector forty = (TimestampColumnVector) batch.cols[3];
        while (rowIterator.nextBatch(batch)) {
            for (int row = 0; row < batch.size; ++row) {
                BytesColumnVector keys = (BytesColumnVector) first1.keys;
                LongColumnVector values = (LongColumnVector) first1.values;

                String key = new String(keys.vector[row], keys.start[row], keys.length[row]);

                System.out.println("first1: " + key + "--->" + values.vector[row]);

//                System.out.println("second: " + second.vector[row]);
//
//                System.out.println("forty: " + forty.getTimestampAsLong(row));
//
//                System.out.println(  "structColumnVector-->"+ ((LongColumnVector) structColumnVector.fields[0]).vector[row]);
            }
        }
        rowIterator.close();
    }

    public static void main(String[] args) throws IOException {
        main(new Configuration(), args);
    }
}