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

import org.apache.flink.types.Row;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.exec.vector.VectorizedRowBatch;
import org.apache.orc.OrcFile;
import org.apache.orc.Reader;
import org.apache.orc.RecordReader;
import org.apache.orc.TypeDescription;

import java.util.List;

public class OrcTest {


    public static void main(String[] args) throws Exception {

        Reader reader = OrcFile.createReader(new Path("/tmp/orc/test1.orc"),
                OrcFile.readerOptions(new Configuration()));
        TypeDescription readSchema =
                TypeDescription.fromString(reader.getSchema().toString());

        // Read the row data
        VectorizedRowBatch batch = readSchema.createRowBatch();

        int[] fields = new int[reader.getSchema().getFieldNames().size()];
        List<TypeDescription> fieldTypes = reader.getSchema().getChildren();
        Row[] rows = new Row[1024];
        for (int i = 0; i < fieldTypes.size(); i++) {
            fields[i] = i;
        }
        OrcBatchReader.fillRows(rows, reader.getSchema(), batch, fields);
        for (Row row : rows) {
            System.out.println(row);
        }

    }
}
