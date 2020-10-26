package com.example.database.rdb;

import com.example.database.util.GsonUtil;
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.json.JsonWriter;
import org.bson.json.JsonWriterSettings;

import java.io.StringWriter;
import java.util.*;


public class Mongodb {


    public static void main(String[] args) {




//        MongoClient mongoClient = getClientWithUrl("mongodb://root:abc123@kudu5:27017/tudou?authSource=admin");
//        //连接到数据库
//        MongoDatabase mongoDatabase = mongoClient.getDatabase("tudou");
//
//        MongoCollection<Document> collection = mongoDatabase.getCollection("30707");


        ArrayList arrayList = new ArrayList<>(2);
        for (int i = 0; i < 10; i++) {
            arrayList.add(new Document("id", i)
                    .append("user_id", 18 + i)
                    .append("name", new Document().append("key1", "h1")));
        }

        //创建文档
        Document CountryValue = new Document().append("Code", "beijin");

        Document Country = new Document().append("CountryType", "Country of Asia").append("CountryValue", CountryValue);

        Document CountryDetails = new Document()
                .append("Country", Country);

        Document Description = new Document().append("Description",new Document().append("decription1","3").append("decription2",4));


//select * from `30707`
        //insert
        for (int i = 0; i < 100; i++) {
            HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put("json","json1");
            Document document =
//                    new Document()
                    new Document("ActiveStatus", "Inactive")
                            .append("12",12)
                    .append("CountryDetails", CountryDetails)
                    .append("Descriptions",Description)
                    .append("name", arrayList)
                    .append("map",objectObjectHashMap)
                    .append("time",new Date())
                    .append("haha",new Document().append("time",new Date()));
            String[] names = document.keySet().toArray(new String[0]);


            System.out.println(GsonUtil.GSON.toJson(document));

            for (int ii = 0; ii < names.length; ii++) {
                Object tempdata = document.get(names[ii]);
                if (tempdata instanceof List) {
                    System.out.println(conventDocument(tempdata));
                } else if (tempdata instanceof Document) {
                    ((Document) tempdata).get("time");
                    System.out.println(((Document) tempdata).toJson());
                }else if (tempdata instanceof Map) {
                    System.out.println(conventDocument(tempdata));
                }  else {
                    System.out.println(tempdata);
                }
            }

            GsonUtil.GSON.fromJson((String) document.toJson(), GsonUtil.gsonMapTypeToken);
//            collection.insertOne(document);
        }
//        Gson gson = new Gson();
//        FindIterable findIterable = collection.find();
//        MongoCursor cursor = findIterable.iterator();
//        while (cursor.hasNext()) {
//            Document next = (Document) cursor.next();
//            String[] names = next.keySet().toArray(new String[0]);
//            for (int i = 0; i < names.length; i++) {
//                Object tempdata = next.get(names[i]);
//                if (tempdata instanceof List) {
//                    System.out.println(gson.toJson(tempdata));
//                } else if (tempdata instanceof Map) {
//                    System.out.println(gson.toJson(tempdata));
//                } else if (tempdata instanceof Document) {
//                    System.out.println(((Document) tempdata).toJson());
//                } else {
//                    System.out.println(tempdata);
//                }
//            }
//            System.out.println("---------------");
//        }

        //申明删除条件
//        Bson filter = Filters.eq("id", "123");
//        //删除与筛选器匹配的所有文档
//        collection.deleteMany(filter);


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

    private static Object conventDocument(Object object){
        if (object instanceof Document){
            return ((Document) object).toJson();
        } else if( object instanceof  List || object instanceof Map){
            return GsonUtil.GSON.toJson(object);
        }
        return object;
    }

}
