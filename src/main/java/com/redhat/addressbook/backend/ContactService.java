package com.redhat.addressbook.backend;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.redhat.addressbook.backend.Contact;

public class ContactService {

	private static ContactService instance;
	private static MongoDatabase db;
	private static MongoClient mongo;
	private static Properties prop = new Properties();
	private  static String mongoProperties = "mongodb.properties";
	private static   InputStream is;
	private static String dbUrl = null;
	private static int dbPort = 0;
	
	public static ContactService createDemoService() {

		if (instance == null) {

			final ContactService contactService = new ContactService();
			
			instance = contactService;
			
			is = ContactService.class.getClassLoader().getResourceAsStream(mongoProperties);
			
			if (is != null){
				try {
					prop.load(is);
					System.out.println("IS NOT NULL");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dbUrl = prop.getProperty("mongodb.url");
				dbPort = Integer.parseInt(prop.getProperty("mongodb.port"));
				
			}
			
			mongo = new MongoClient(dbUrl, dbPort);
			db = mongo.getDatabase("contactsdb");
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

				c = new Contact((String) document.get("firstName"),
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

				c = new Contact((String) document.get("firstName"),
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
		
		System.out.println("UPDATE TYPE " + updateType);
		
		if (updateType.equals("update")){
			System.out.println("in update");
			DB db = mongo.getDB("contactsdb");
            DBCollection collection = db.getCollection("contacts");
       
            while (cursor.hasNext()){
            	
            	if (((Integer) current.get("contactid")).intValue() == contact
                        .getId())
            		break;
            	current = cursor.next();
            }
            BasicDBObject query = new BasicDBObject();
            query.append("contactid", current.get("contactid"));
            query.append("firstName", current.get("firstName"));
            query.append("lastName", current.get("lastName"));
            query.append("phone", current.get("phone"));
            
            WriteResult wr =collection.remove(query);
            
            System.out.println(wr.toString());

            Document doc = new Document("contact", "details")
                        .append("contactid", contact.getId())
                        .append("firstName", contact.getFirstName())
                        .append("lastName", contact.getLastName())
                        .append("email", contact.getEmail())
                        .append("bdate", contact.getBirthDate())
                        .append("phone", contact.getPhone());
           
            table.insertOne(doc);
            
            status = 1;
            cursor.close();
		}
		else if (updateType.equals("insert"))
		{
			System.out.println("in insert");
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
			System.out.println("duplicate?");
            System.out.println(((Integer) current.get("contactid")).intValue());
            status = 2;
            
		}
		return status;
	}

}
