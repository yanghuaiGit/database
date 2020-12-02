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
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.exec.vector.BytesColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.TimestampColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.VectorizedRowBatch;
import org.apache.orc.CompressionKind;
import org.apache.orc.OrcFile;
import org.apache.orc.Reader;
import org.apache.orc.RecordReader;
import org.apache.orc.TypeDescription;
import org.apache.orc.Writer;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class OrcStringWriter {
    public static void main(String[] args) throws Exception {
        String path = "/tmp/orc/test.orc";
        writeString(path);
        readString(path);
    }


    public static void writeString(String path) throws Exception {
        Path testFilePath = new Path(path);
        Configuration conf = new Configuration();

        TypeDescription schema = TypeDescription.createStruct()
                .addField("field1", TypeDescription.createString());

        Writer writer = OrcFile.createWriter(testFilePath, OrcFile.writerOptions(conf).setSchema(schema).compress(CompressionKind.SNAPPY));
        VectorizedRowBatch batch = schema.createRowBatch();

        final int BATCH_SIZE = batch.getMaxSize();
//        BytesColumnVector key1 = new BytesColumnVector();

        BytesColumnVector key1 = (BytesColumnVector) batch.cols[0];

        // add 1500 rows to file
        for (int r = 0; r < 10; ++r) {
            int row = batch.size++;
            key1.noNulls = true;

            key1.setVal(row, ("key" + r).getBytes(StandardCharsets.UTF_8));

            if (row == BATCH_SIZE - 1) {
                writer.addRowBatch(batch);
                batch.reset();
            }
        }
        if (batch.size != 0) {
            writer.addRowBatch(batch);
            batch.reset();
        }
        writer.close();

    }

    public static void readString(String path) throws Exception {

        Reader reader = OrcFile.createReader(new Path(path),
                OrcFile.readerOptions(new Configuration()));
        List<TypeDescription> children = reader.getSchema().getChildren();

        // Pick the schema we want to read using schema evolution
        TypeDescription readSchema =
                TypeDescription.fromString(reader.getSchema().toString());

        RecordReader rowIterator = reader.rows(reader.options()
                .schema(readSchema));

        // Read the row data
        VectorizedRowBatch batch = readSchema.createRowBatch();

        BytesColumnVector values = (BytesColumnVector) batch.cols[0];

        while (rowIterator.nextBatch(batch)) {
            for (int row = 0; row < batch.size; ++row) {

                String val = new String(values.vector[row], values.start[row], values.length[row]);

                System.out.println("val--> " + val);
            }
        }
        rowIterator.close();
    }
}
