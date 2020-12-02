package com.example.hdfs.orc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.exec.vector.BytesColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.LongColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.MapColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.StructColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.TimestampColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.VectorizedRowBatch;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.security.Credentials;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.security.token.Token;
import org.apache.orc.CompressionKind;
import org.apache.orc.OrcFile;
import org.apache.orc.TypeDescription;
import org.apache.orc.Writer;
import sun.security.krb5.Config;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.PrivilegedExceptionAction;
import java.sql.Timestamp;

/**
 * https://www.codota.com/code/java/classes/org.apache.hadoop.hive.ql.exec.vector.StructColumnVector
 *
 */
public class OrcWriter {
    public static void main(String[] args) throws Exception {


        Path testFilePath = new Path("/tmp/orc/test1.orc");
        Configuration conf = new Configuration();
        TypeDescription struct1 = TypeDescription.createStruct()
                .addField("key1", TypeDescription.createLong());
        TypeDescription schema = TypeDescription.createStruct()
                .addField("field1", TypeDescription.createMap(TypeDescription.createString(), TypeDescription.createLong()))
                .addField("field2", TypeDescription.createLong())
                .addField("field3", struct1)
                .addField("field4", TypeDescription.createTimestamp());

        Writer writer = OrcFile.createWriter(testFilePath, OrcFile.writerOptions(conf).setSchema(schema).compress(CompressionKind.SNAPPY));
        VectorizedRowBatch batch = schema.createRowBatch();

        final int BATCH_SIZE = batch.getMaxSize();

        // 定义map列，对key和value要做cast
        MapColumnVector map = (MapColumnVector) batch.cols[0];
        BytesColumnVector mapKey = (BytesColumnVector) map.keys;
        LongColumnVector mapValue = (LongColumnVector) map.values;

        // 每个map包含5个元素
        final int MAP_SIZE = 5;


        // 确保map的空间充足
        mapKey.ensureSize(BATCH_SIZE * MAP_SIZE, false);
        mapValue.ensureSize(BATCH_SIZE * MAP_SIZE, false);



        LongColumnVector second = (LongColumnVector) batch.cols[1];
        LongColumnVector longColumnVector = new LongColumnVector();
        StructColumnVector structColumnVector = new StructColumnVector(1, longColumnVector);
        batch.cols[2] =  structColumnVector;
        TimestampColumnVector forty = (TimestampColumnVector) batch.cols[3];


        // add 1500 rows to file
        for (int r = 0; r < 10; ++r) {
            int row = batch.size++;

            // 处理map列偏移
            map.offsets[row] = map.childCount;
            map.lengths[row] = MAP_SIZE;
            map.childCount += MAP_SIZE;

            // 处理map列的值
            for (int mapElem = (int) map.offsets[row]; mapElem < map.offsets[row] + MAP_SIZE; ++mapElem) {
                String key = "row " + r + "." + (mapElem - map.offsets[row]);
                mapKey.setVal(mapElem, key.getBytes(StandardCharsets.UTF_8));
                mapValue.vector[mapElem] = mapElem;
            }

            second.vector[row] = r * 3;
            longColumnVector.vector[row] = r;
            forty.set(row, new Timestamp(System.currentTimeMillis()));

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



}
