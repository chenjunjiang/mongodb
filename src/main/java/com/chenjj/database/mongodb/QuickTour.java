package com.chenjj.database.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;

public class QuickTour {

	public static void main(final String[] args) {
		MongoClient mongoClient;
		if (args.length == 0) {
			// connect to the local database server
			mongoClient = new MongoClient();
		} else {
			mongoClient = new MongoClient(new MongoClientURI(args[0]));
		}
		// if the database does not exist, MongoDB will create it for you
		MongoDatabase database = mongoClient.getDatabase("mydb");

		// if the collection does not exist, MongoDB will create it for you
		MongoCollection<Document> collection = database.getCollection("test");

		// drop all the data in it
		collection.drop();

		// make a document and insert it
		Document document = new Document("name", "MongoDB")
				.append("type", "database").append("count", 1)
				.append("info", new Document("x", 203).append("y", 102));

		// 插入一个文档
		collection.insertOne(document);

		// 查询出刚才插入的文档
		Document readDocument = collection.find().first();
		System.out.println(readDocument.toJson());

		// 批量插入文档
		List<Document> documents = new ArrayList<Document>();
		for (int i = 0; i < 100; i++) {
			documents.add(new Document("i", i));
		}

		collection.insertMany(documents);

		System.out.println("到目前为止test集合中一共插入了101条数据：" + collection.count());

		// 迭代所有的文档信息
		MongoCursor<Document> cursor = collection.find().iterator();
		try {
			while (cursor.hasNext()) {
				System.out.println(cursor.next().toJson());
			}
		} finally {
			cursor.close();
		}

		for (Document doc : collection.find()) {
			System.out.println(doc.toJson());
		}

		// now use a query to get 1 document out
		readDocument = collection.find(eq("i",71)).first();
		System.out.println(readDocument.toJson());
		
		// now use a range query to get a larger subset
		cursor = collection.find(gt("i", 50)).iterator();
		try{
			while(cursor.hasNext()){
				System.out.println(cursor.next().toJson());
			}
		}finally{
			cursor.close();
		}
		
		mongoClient.close();
	}
}
