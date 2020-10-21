package com.example.database.rdb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;


public class Mongodb {


    public static void main(String[] args) {
        MongoClient mongoClient = getClientWithUrl("mongodb://root:abc123@kudu5:27017/tudou?authSource=admin");
        //连接到数据库
        MongoDatabase mongoDatabase = mongoClient.getDatabase("tudou");

        MongoCollection<Document> collection = mongoDatabase.getCollection("test");


        ArrayList arrayList = new ArrayList<>(12);
        for (int i = 0; i < 10; i++) {
            arrayList.add(new Document("id", "123")
                    .append("user_id", 18)
                    .append("name", new Document().append("key1", "h1")));
        }
        //创建文档
        Document document = new Document("id", "123")
                .append("user_id", 18)
                .append("list", arrayList)
                .append("name", new Document().append("key1", "h1"));

        //insert
        collection.insertOne(document);

        FindIterable findIterable = collection.find();
        MongoCursor cursor = findIterable.iterator();
        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }

        //申明删除条件
        Bson filter = Filters.eq("name", "张三");
        //删除与筛选器匹配的所有文档
        collection.deleteMany(filter);


    }

    private static MongoClient getClientWithUrl(String url) {
        MongoClientURI clientUri = new MongoClientURI(url);
        return new MongoClient(clientUri);
    }


    private static MongoClient getClientWithUrl(String host, int port) {
        //连接到 mongodb 服务，默认端口号为27017
        return new MongoClient(host, port);
    }

    private static MongoClient getClientWithUserName(String host, int port, String username, String databaseName, String password) {
        List<ServerAddress> adds = new ArrayList<>();
//ServerAddress()两个参数分别为 服务器地址 和 端口
        ServerAddress serverAddress = new ServerAddress(host, port);
        adds.add(serverAddress);

        List<MongoCredential> credentials = new ArrayList<>();
//MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码
        MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(username, databaseName, password.toCharArray());
        credentials.add(mongoCredential);

//通过连接认证获取MongoDB连接
        return new MongoClient(adds, credentials);
    }
}
