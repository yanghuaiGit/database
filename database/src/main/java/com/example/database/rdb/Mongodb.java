package com.example.database.rdb;

import com.mongodb.AuthenticationMechanism;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;


public class Mongodb {


    public static void main(String[] args) {


        MongoClient mongoClient = getCLient();
//        //连接到数据库
        MongoDatabase mongoDatabase = mongoClient.getDatabase("operlogs2");
//
        MongoCollection<Document> collection = mongoDatabase.getCollection("erp_log");

        FindIterable findIterable = collection.find(BasicDBObject.parse("{'eid':'1af58390681811e69d0818c58a146fd2','happenedTime':{'$gte': ISODate('2020-12-01T07:58:51Z'),'$lte': ISODate('2020-12-02T07:58:51Z')},'interfaceName':'rewriteMemberCouponERP','desc':'请求成功'}"));
        MongoCursor cursor = findIterable.iterator();

        while (cursor.hasNext()) {
            Object next = cursor.next();
        }

    }

    public static MongoClient getCLient() {


        MongoCredential credential = MongoCredential.createCredential("data_user", "operlogs2", "YSp5wmMS3HYR" .toCharArray())
                .withMechanism(AuthenticationMechanism.fromMechanismName("SCRAM-SHA-1"));
        ServerAddress serverAddress = new ServerAddress("111.231.31.184", 27017);
        ArrayList<ServerAddress> objects = new ArrayList<>();
        objects.add(serverAddress);
        MongoClientOptions option = getOption();
        return new MongoClient(objects, credential, option);
    }


    private static MongoClientOptions getOption() {
        MongoClientOptions.Builder build = new MongoClientOptions.Builder();
        build.connectionsPerHost(100);
        build.threadsAllowedToBlockForConnectionMultiplier(100);
        build.connectTimeout(10000);
        build.maxWaitTime(5000);
        build.socketTimeout(0);
        build.writeConcern(WriteConcern.UNACKNOWLEDGED);
        return build.build();
    }
}
