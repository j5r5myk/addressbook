package com.redhat.addressbook.backend;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;


public class ContactService {

	private static ContactService instance;
	private static MongoDatabase db;
	private static MongoClient mongo;
	private static Properties prop = new Properties();
	private  static String mongoProperties = "mongodb.properties";
	private static   InputStream is;
	private static String dbUrl = null;
	private static int dbPort = 0;
	private static String username = "";
	private static String password = "";
	private static String database = "contactsdb";
	
	public static ContactService createDemoService() {

		if (instance == null) {

			final ContactService contactService = new ContactService();
			
			instance = contactService;
			
			is = ContactService.class.getClassLoader().getResourceAsStream(mongoProperties);
			
			if (is != null){
				try {
					prop.load(is);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dbUrl = prop.getProperty("mongodb.url");
				dbPort = Integer.parseInt(prop.getProperty("mongodb.port"));
				username = prop.getProperty("mongodb.username");
				password = prop.getProperty("mongodb.password");
				database = prop.getProperty("mongodb.database");
			}
			
			System.out.println("url " + dbUrl + " port " + dbPort + " username " + username + " password " + " database " + database);
			
			mongo = new MongoClient(dbUrl, dbPort);
//			List<ServerAddress> seeds = new ArrayList<ServerAddress>();
//			seeds.add( new ServerAddress( dbUrl, dbPort ));
//			
//			List <MongoCredential> credentials = new ArrayList<MongoCredential>();
//			credentials.add(
//				    MongoCredential.createMongoCRCredential(
//				        username,
//				        database,
//				        password.toCharArray()
//				    )
//				);
//			
			//mongo = new MongoClient(seeds, credentials);
			
			db = mongo.getDatabase(database);
			if (db.getCollection("contacts") == null)

				db.createCollection("contacts");

		}

		return instance;
	}



	public synchronized List<Contact> findAll(String stringFilter) {

		ArrayList<Contact> contacts = new ArrayList<Contact>();

		MongoCollection<Document> table = db.getCollection("contacts");

		MongoCursor<Document> cursor = table.find().iterator();

		cursor = table.find().iterator();
		if (stringFilter == null || stringFilter.length() == 0) {
			while (cursor.hasNext()) {

				Document document = cursor.next();

				Contact c = null;

				c = new Contact((UUID)document.get("contactid"),(String) document.get("firstName"),
						(String) document.get("lastName"),
						(String) document.get("phone"),
						(String) document.get("email"),
						(Date) document.get("bdate"));

				contacts.add(c);
			}
		}

		else {
			while (cursor.hasNext()) {
				Document document = cursor.next();

				Contact c = null;

				c = new Contact((UUID)document.get("contactid"),(String) document.get("firstName"),
						(String) document.get("lastName"),
						(String) document.get("phone"),
						(String) document.get("email"),
						(Date) document.get("bdate"));
				if (c.toString().toLowerCase()
						.contains(stringFilter.toLowerCase()))
					contacts.add(c);
				else {

					c = null;
				}

			}
		}

		return contacts;

	}

	public int save(Contact contact, String updateType) {

		MongoCollection<Document> table = db.getCollection("contacts");
		MongoCursor<Document> cursor = table.find().iterator();
		Document current = null;
		int status = 0;
		
		
		
		if (updateType.equals("update")){
		
			DB db = mongo.getDB("contactsdb");
            DBCollection collection = db.getCollection("contacts");
       

          BasicDBObject carrier = new BasicDBObject();
          BasicDBObject query = new BasicDBObject();
          query.put("contactid", contact.getId());
          
          BasicDBObject set  = new BasicDBObject("$set", carrier);
          carrier.put("contactid", contact.getId());
          carrier.append("firstName", contact.getFirstName())
          .append("lastName", contact.getLastName())
                        .append("email", contact.getEmail())
                        .append("bdate", contact.getBirthDate())
                        .append("phone", contact.getPhone());
          
          collection.update(query, set);
          
          status = 1;
		}
		else if (updateType.equals("insert"))
		{
			
			Document doc = new Document("contact", "details")
            .append("contactid", contact.getId())
            .append("firstName", contact.getFirstName())
            .append("lastName", contact.getLastName())
            .append("email", contact.getEmail())
            .append("bdate", contact.getBirthDate())
            .append("phone", contact.getPhone());

			table.insertOne(doc);
			
			status = 0;

		}
		else if (updateType.equals("duplicate")){
		    status = 2;
            
		}
		
		cursor.close();
		return status;
	}

}
