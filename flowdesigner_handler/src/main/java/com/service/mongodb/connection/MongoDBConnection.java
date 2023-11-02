/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.service.mongodb.connection;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;

/**
 *
 * @author sneha.n
 */
public class MongoDBConnection {

    private static final Logger log = LogManager.getLogger(MongoDBConnection.class);

    public MongoClient connMongo;

    public static String username, path;
    private static boolean bSuccess;

    public void Connect(String dbhost, String dbport) {

        //SecurityHandle securityHandle = new SecurityHandle();
        boolean bSuccess = false;

        try {

            log.info("***************INSIDE CONNECTION CLASS**************");

            path = "mongodb://" + dbhost + ":" + dbport;
            log.info("URL -->" + path);
            // MongoClientURI uri = new MongoClientURI(path);
            connMongo = new MongoClient(dbhost + ":" + dbport);

            // MongoClient client=new MongoClient("localhost");
//            ConnectionString connectionString = new ConnectionString(path);
//            MongoClient mongoClient = MongoClients.create(connectionString);
            bSuccess = true;

            log.info("-------------- Connection class end --------------------");
        } catch (Exception e) {

            log.error("!!Some DB CONNECTION ERROR!! -->" + e);

        } finally {

        }

    }

    public boolean Disconnect() {

        try {
            connMongo.close();;
            connMongo = null;
            bSuccess = false;
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    public boolean insertDocument(String strDatabaseName, String strCollectionName, Document document) {
        try {
            MongoDatabase database = connMongo.getDatabase(strDatabaseName);

            // Get or create a collection to store voter information
            MongoCollection<Document> collection = database.getCollection(strCollectionName);

            collection.insertOne(document);
            
            return true;
        } finally {

//            driver = null;
//            password = null;
        }
    }

    public Document getSingleDocument(String strDatabaseName, String strCollectionName, Document documentFilter) {

        MongoDatabase database = null;
        MongoCollection<Document> collection = null;
        try {
            database = connMongo.getDatabase(strDatabaseName);

            // Get or create a collection to store voter information
            collection = database.getCollection(strCollectionName);

            log.info("getSingleDocument connected step 1-->");
//            Document document = new Document(filtername, filtervalue);
//
//            log.info("getSingleDocument step 2::" + document.toJson());

            MongoCursor<Document> cursor = collection.find(documentFilter).iterator();
            try {
                if (cursor != null) {
                    if (cursor.hasNext()) {
                        Document documentData = cursor.next();
                        log.info("getSingleDocument step 3-->" + documentData.toJson());
                        return documentData;

                    }
                }
                log.info("getSingleDocument step 4");
            } finally {
                try {
                    cursor.close();

                } catch (Exception e) {
                    log.info("Exception in cursor close -->" + e);
                }
            }
        } finally {

            database = null;
            collection = null;
        }
        return null;
    }

    public MongoCursor<Document> getMutipleDocuments(String strDatabaseName, String strCollectionName, Document documentFilter) {

        MongoDatabase database = null;
        MongoCollection<Document> collection = null;
        MongoCursor<Document> cursor = null;
        try {
            database = connMongo.getDatabase(strDatabaseName);

            // Get or create a collection to store voter information
            collection = database.getCollection(strCollectionName);

            log.info("getMutipleDocuments connected step 1-->");

            cursor = collection.find(documentFilter).iterator();

            return cursor;

        } finally {

            database = null;
            collection = null;
        }
        //return null;
    }

    public long updateDocument(String strDatabaseName, String strCollectionName, Document filter, Document documentUpdate) {

        MongoDatabase database = null;
        MongoCollection<Document> collection = null;
        try {
            database = connMongo.getDatabase(strDatabaseName);

            // Get or create a collection to store voter information
            collection = database.getCollection(strCollectionName);

            log.info("updateDocument connected step 1-->");
//            Document document = new Document(filtername, filtervalue);
//
//            log.info("getSingleDocument step 2::" + document.toJson());
            Document updateDocument = new Document("$set",
                    documentUpdate); 
            
            UpdateOptions options = new UpdateOptions();

            UpdateResult updateResult= collection.updateOne(filter, updateDocument,options);

            return updateResult.getModifiedCount();

        } finally {

            database = null;
            collection = null;
        }
      //  return 0;
    }
    
    public Document deleteDocument(String strDatabaseName, String strCollectionName, Document filter) {

        MongoDatabase database = null;
        MongoCollection<Document> collection = null;
        Document document=null;
        try {
            database = connMongo.getDatabase(strDatabaseName);

            // Get or create a collection to store voter information
            collection = database.getCollection(strCollectionName);

            log.info("deleteDocument connected step 1-->");
//        
            
            document=collection.findOneAndDelete(filter);

            return document;

        } finally {

            database = null;
            collection = null;
        }
      //  return 0;
    }
}
