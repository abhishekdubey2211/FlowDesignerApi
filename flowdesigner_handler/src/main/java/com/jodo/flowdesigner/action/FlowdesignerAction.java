/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jodo.flowdesigner.action;

import com.jodo.flowdesigner.constants.MessageConstants;
import com.jodo.flowdesigner.model.Flowdesigner;
import com.jodo.flowdesigner.model.FlowdesignerApiResponse;
import com.jodo.flowdesigner.model.FlowdesignerDTO;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.service.Validator.getMethods.CommonGetMethods;
import com.service.mongodb.connection.MongoDBConnection;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.service.Validator.validator.Validations;

/**
 *
 * @author abhishek.d
 */
public class FlowdesignerAction implements MessageConstants {

    private static final Logger log = LogManager.getLogger(FlowdesignerAction.class);
    private MongoDBConnection mongoDBConnection;
    private String databaseName = "dataflowdesigner";
    private String collectionName = "data";

//    public String getVersion() {
//        String version = strFlowdesignerversion;
//        log.info("FlowdesignerController version : " + strFlowdesignerversion);
//        return version;
//    }
    public boolean checkFlownameExists(String flowname) {
        Document filter = new Document("flowname", flowname).append("isdelete", 0);
        Document existingDocument = mongoDBConnection.getSingleDocument(databaseName, collectionName, filter);
        return existingDocument != null;
    }

    public int getTotaldataIncollection() {
        try {
            // Connect to MongoDB server (default port 27017)
            MongoClient mongoClient = new MongoClient("localhost");

            // Connect to your database
            MongoDatabase database = mongoClient.getDatabase(databaseName);

            // Get or create a collection to store data
            MongoCollection<Document> collection = database.getCollection(collectionName);

            // Use countDocuments to get the total count of documents
            int totalDocumentCount = (int) collection.countDocuments();

            return totalDocumentCount;
        } catch (Exception e) {
            // Handle exceptions appropriately
            e.printStackTrace();
        } finally {
            // Close resources as needed (e.g., close the MongoClient)
        }
        return 0;
    }

    public FlowdesignerApiResponse pushFlowdesign(FlowdesignerDTO pushFlowdesignerDTO) {
            log.info("******************pushFlowdesign******************");
        FlowdesignerApiResponse flowApiResponse = new FlowdesignerApiResponse();
        Flowdesigner flowdesigner = new Flowdesigner();
        int revisionno = 1;
        int active = 0;
        String timeStamp = CommonGetMethods.getTimeStamp();
        String flowname, createdatetime, modifieddatetime, createbylogin, flowdetails, modifybylogin = null;
        Document document;
        Document lastInsertedDocument = null;
        List<FlowdesignerDTO> flowdesignerList = new ArrayList<>();
        int totaldataincollection = getTotaldataIncollection();
        
        try {
            mongoDBConnection = new MongoDBConnection();
            mongoDBConnection.Connect("localhost", "27017");
            log.info("******************Connection done sucessfull******************");
            flowApiResponse.setResdatetime(timeStamp);
            flowApiResponse.setRevisionno(revisionno);

            flowname = pushFlowdesignerDTO.getFlowname();
            flowdetails = pushFlowdesignerDTO.getFlowdetails();
            active = pushFlowdesignerDTO.getActive();

            if (flowname == null || flowname.isBlank() || flowname.trim().length() < 1) {

                flowApiResponse.setStatus(FLOWNAME_BLANK);
                flowApiResponse.setStatusdesc(str_FLOWNAME_BLANK);

                return flowApiResponse;

            }

            if (Validations.checkMaxStringLength(flowname, 100) != true) {

                flowApiResponse.setStatus(FLOWNAME_MAX_LENGTH);
                flowApiResponse.setStatusdesc(str_FLOWNAME_MAX_LENGTH);

                return flowApiResponse;
            }

            if (Validations.checkMinStringLength(flowname, 3) != true) {

                flowApiResponse.setStatus(FLOWNAME_MIN_LENGTH);
                flowApiResponse.setStatusdesc(str_FLOWNAME_MIN_LENGTH);

                return flowApiResponse;
            }

            if (active < 0 || active > 1) {
                flowApiResponse.setStatus(ACTIVE_INVALID);
                flowApiResponse.setStatusdesc(str_ACTIVE_INVALID);

                return flowApiResponse;
            }

            if (checkFlownameExists(flowname)) {
                flowApiResponse.setStatus(FLOWNAME_ALREADY_EXISTS); // Flowname already exists
                flowApiResponse.setStatusdesc(str_FLOWNAME_ALREADY_EXISTS);
                return flowApiResponse;
            }

            int flowid = totaldataincollection + 1;

            document = new org.bson.Document("_id", new ObjectId())
                    .append("flowid", flowid)
                    .append("flowname", flowname)
                    .append("flowdetails", flowdetails)
                    .append("active", active)
                    .append("isdelete", 0)
                    .append("createdatetime", timeStamp)
                    .append("createbyuserid", 1111);

            boolean success = mongoDBConnection.insertDocument(databaseName, collectionName, document);

            if (success) {
                Document filter = new Document("flowname", document.get("flowname"));
                lastInsertedDocument = getDataflow(filter);
            }
            pushFlowdesignerDTO.setFlowid(lastInsertedDocument.getInteger("flowid"));
            pushFlowdesignerDTO.setFlowname(lastInsertedDocument.getString("flowname"));
            pushFlowdesignerDTO.setActive(lastInsertedDocument.getInteger("active"));
            pushFlowdesignerDTO.setFlowdetails(lastInsertedDocument.getString("flowdetails"));

            flowdesignerList.add(pushFlowdesignerDTO);

            if (flowdesignerList != null && flowdesignerList.size() > 0) {

                flowApiResponse.setStatus(STATUS_SUCESS);
                flowApiResponse.setStatusdesc(STATUS_SUCESS_MSG);
                flowApiResponse.setFlowdesign(flowdesignerList);
            } else {
                flowApiResponse.setStatus(STATUS_FAIL_TO_ADD);
                flowApiResponse.setStatusdesc(STATUS_FAIL_TO_ADD_MSG);
                flowApiResponse.setFlowdesign(flowdesignerList);
            }
            return flowApiResponse;

        } catch (Exception e) {

        } finally {
            document = null;
            timeStamp = null;
            lastInsertedDocument = null;
            flowApiResponse = null;
            mongoDBConnection.Disconnect();
        }

        return null;
    }

    public Document getDataflow(Document filter) {
        boolean success = false;
        Document lastInsertedDocument = null;
        try {
            log.info("***************getDataflow************************");
            // Retrieve the last inserted document based on the _id field
            lastInsertedDocument = mongoDBConnection.getSingleDocument(databaseName, collectionName, filter);
            log.info("Retrived data getDataflow :: " + lastInsertedDocument);

            if (lastInsertedDocument != null) {
                return lastInsertedDocument;
            }
        } catch (Exception e) {

        } finally {
            lastInsertedDocument = null;
        }
        return null;
    }

    public FlowdesignerApiResponse getByFlowname(String flowname) {
        FlowdesignerApiResponse flowApiResponse = new FlowdesignerApiResponse();
        Flowdesigner flowdesigner = new Flowdesigner();
        int revisionno = 1;
        int active = 0;
        String timeStamp = CommonGetMethods.getTimeStamp();
        Document document;
        Document retrivedDocument = null;
        FlowdesignerDTO flowdesignerDTO = new FlowdesignerDTO();
        List<FlowdesignerDTO> flowdesignerList = new ArrayList<>();

        try {
            log.info("**************getByFlowname******************");
            mongoDBConnection = new MongoDBConnection();
            mongoDBConnection.Connect("localhost", "27017");
            log.info("******************Connection done successfully******************");
            flowApiResponse.setResdatetime(timeStamp);
            flowApiResponse.setRevisionno(revisionno);

            if (flowname == null || flowname.isBlank() || flowname.trim().length() < 1) {

                flowApiResponse.setStatus(FLOWNAME_BLANK);
                flowApiResponse.setStatusdesc(str_FLOWNAME_BLANK);

                return flowApiResponse;

            }

            if (Validations.checkMaxStringLength(flowname, 100) != true) {

                flowApiResponse.setStatus(FLOWNAME_MAX_LENGTH);
                flowApiResponse.setStatusdesc(str_FLOWNAME_MAX_LENGTH);

                return flowApiResponse;
            }

            if (Validations.checkMinStringLength(flowname, 3) != true) {

                flowApiResponse.setStatus(FLOWNAME_MIN_LENGTH);
                flowApiResponse.setStatusdesc(str_FLOWNAME_MIN_LENGTH);

                return flowApiResponse;
            }

            if (!checkFlownameExists(flowname)) {
                flowApiResponse.setStatus(FLOWNAME_NOTEXISTS); // Flowname already exists
                flowApiResponse.setStatusdesc(str_FLOWNAME_NOTEXISTS);
                return flowApiResponse;
            }

            Document filter = new Document("flowname", flowname).append("isdelete", 0);
            retrivedDocument = getDataflow(filter);
            log.info("lastInsertedDocument :: " + retrivedDocument);

            if (retrivedDocument != null) {
                flowdesignerDTO.setFlowid(retrivedDocument.getInteger("flowid"));
                flowdesignerDTO.setFlowname(retrivedDocument.getString("flowname"));
                flowdesignerDTO.setActive(retrivedDocument.getInteger("active"));
                flowdesignerDTO.setFlowdetails(retrivedDocument.getString("flowdetails"));

                flowdesignerList.add(flowdesignerDTO);
                log.info("flowdesignerList :: " + flowdesignerList);
            }

            if (flowdesignerList != null && !flowdesignerList.isEmpty()) {
                flowApiResponse.setStatus(STATUS_SUCESS);
                flowApiResponse.setStatusdesc(STATUS_SUCESS_MSG);
                flowApiResponse.setFlowdesign(flowdesignerList);
            } else {
                flowApiResponse.setStatus(STATUS_FAIL_TO_GET_BY_NAME);
                flowApiResponse.setStatusdesc(STATUS_FAIL_TO_GET_BY_NAME_MSG);
                flowApiResponse.setFlowdesign(null); // Set an empty list or null here, depending on your requirements.
            }

            return flowApiResponse;

        } catch (Exception e) {

        } finally {
            document = null;
            timeStamp = null;
            retrivedDocument = null;
            flowApiResponse = null;
            mongoDBConnection.Disconnect();
        }
        return null;
    }

    public FlowdesignerApiResponse getAllDocuments() {
        FlowdesignerApiResponse flowApiResponse = new FlowdesignerApiResponse();
        int revisionno = 1;
        String timeStamp = CommonGetMethods.getTimeStamp();
        List<FlowdesignerDTO> flowdesignerList = new ArrayList<>();
        // Connect to MongoDB server (default port 27017)
        MongoClient mongoClient = new MongoClient("localhost");

        // Connect to your database
        MongoDatabase database = mongoClient.getDatabase(databaseName);

        // Get or create a collection to store voter information
        MongoCollection<Document> collection = database.getCollection(collectionName);

        try {
            log.info("**************getAllDocuments******************");
            mongoDBConnection = new MongoDBConnection();
            mongoDBConnection.Connect("localhost", "27017");
            log.info("******************Connection done successfully******************");
            flowApiResponse.setResdatetime(timeStamp);
            flowApiResponse.setRevisionno(revisionno);

            Document filter = new Document("isdelete", 0);

            MongoCursor<Document> documents = collection.find()
                    .projection(Projections.excludeId()) // Exclude the _id field from the output
                    .iterator();

            documents = mongoDBConnection.getMutipleDocuments(databaseName, collectionName, filter);
            log.info("Retrieved documents: " + documents);
            Document document2 = null;

            List<Document> doclist = new ArrayList<>();
            try {
                while (documents.hasNext()) {
                    document2 = documents.next();
//                System.out.println(document.toJson(prettyPrint));
                    doclist.add(document2);

                }
            } finally {
                documents.close();
            }

            for (Document document : doclist) {
                FlowdesignerDTO pushFlowdesignerDTO = new FlowdesignerDTO();
                pushFlowdesignerDTO.setFlowid(document.getInteger("flowid"));
                pushFlowdesignerDTO.setFlowname(document.getString("flowname"));
                pushFlowdesignerDTO.setActive(document.getInteger("active"));
                pushFlowdesignerDTO.setFlowdetails(document.getString("flowdetails"));
                flowdesignerList.add(pushFlowdesignerDTO);
            }

            if (flowdesignerList != null && !flowdesignerList.isEmpty()) {
                flowApiResponse.setStatus(STATUS_SUCESS);
                flowApiResponse.setStatusdesc(STATUS_SUCESS_MSG);
                flowApiResponse.setFlowdesign(flowdesignerList);
            } else {
                flowApiResponse.setStatus(STATUS_FAIL_TO_GET_LIST);
                flowApiResponse.setStatusdesc(STATUS_FAIL_TO_GET_LIST_MSG);
                flowApiResponse.setFlowdesign(flowdesignerList);
            }
            return flowApiResponse;

        } catch (Exception e) {
            // Handle exceptions appropriately
        } finally {
            timeStamp = null;
            flowApiResponse = null;
            mongoDBConnection.Disconnect();
        }
        return null;
    }

    public FlowdesignerApiResponse deleteDocumentByName(String flowname) {
        FlowdesignerApiResponse flowApiResponse = new FlowdesignerApiResponse();
        int revisionno = 1;
        String timeStamp = CommonGetMethods.getTimeStamp();
        Document deleteInsertedDocument = null;
        FlowdesignerDTO flowdesignerDTO = new FlowdesignerDTO();
        List<FlowdesignerDTO> flowdesignerList = new ArrayList<>();
        try {
            mongoDBConnection = new MongoDBConnection();
            mongoDBConnection.Connect("localhost", "27017");
            log.info("Connection done successfully");

            flowApiResponse.setResdatetime(timeStamp);
            flowApiResponse.setRevisionno(revisionno);

            if (flowname == null || flowname.isBlank() || flowname.trim().length() < 1) {

                flowApiResponse.setStatus(FLOWNAME_BLANK);
                flowApiResponse.setStatusdesc(str_FLOWNAME_BLANK);

                return flowApiResponse;

            }

            if (Validations.checkMaxStringLength(flowname, 100) != true) {

                flowApiResponse.setStatus(FLOWNAME_MAX_LENGTH);
                flowApiResponse.setStatusdesc(str_FLOWNAME_MAX_LENGTH);

                return flowApiResponse;
            }

            if (Validations.checkMinStringLength(flowname, 3) != true) {

                flowApiResponse.setStatus(FLOWNAME_MIN_LENGTH);
                flowApiResponse.setStatusdesc(str_FLOWNAME_MIN_LENGTH);

                return flowApiResponse;
            }

            if (!checkFlownameExists(flowname)) {
                flowApiResponse.setStatus(FLOWNAME_NOTEXISTS); // Flowname already exists
                flowApiResponse.setStatusdesc(str_FLOWNAME_NOTEXISTS);
                return flowApiResponse;
            }

            Document updatedDocumentFilter = new Document("flowname", flowname).append("isdelete", 0);
            Document documentUpdate = new Document("isdelete", 1)
                    .append("modifieddatetime", timeStamp)
                    .append("modifybyuserid", 1425);

            log.info("documentUpdate  :: " + documentUpdate + "    ||  filter :: " + updatedDocumentFilter);

            long success = mongoDBConnection.updateDocument(databaseName, collectionName, updatedDocumentFilter, documentUpdate);
            log.info("success  :: " + success);

            if (success > 0) {
                flowApiResponse.setStatus(STATUS_SUCESS);
                flowApiResponse.setStatusdesc(STATUS_SUCESS_MSG);
            } else {
                flowApiResponse.setStatus(STATUS_FAIL_TO_DELETE);
                flowApiResponse.setStatusdesc(STATUS_FAIL_TO_DELETE_MSG);
            }

            return flowApiResponse;

        } catch (Exception e) {
        } finally {
            timeStamp = null;
            if (mongoDBConnection != null) {
                mongoDBConnection.Disconnect();
            }
        }

        return null;
    }

    public FlowdesignerApiResponse putFlowdesign(FlowdesignerDTO putFlowdesignerDTO) {
        FlowdesignerApiResponse flowApiResponse = new FlowdesignerApiResponse();
        int revisionno = 1;
        String timeStamp = CommonGetMethods.getTimeStamp();
        Document lastUpdatedDocument = null;
        List<FlowdesignerDTO> flowdesignerList = new ArrayList<>();

        try {
            mongoDBConnection = new MongoDBConnection();
            mongoDBConnection.Connect("localhost", "27017");
            log.info("****************Connection done successfully****************");
            flowApiResponse.setResdatetime(timeStamp);
            flowApiResponse.setRevisionno(revisionno);

            String flowname = putFlowdesignerDTO.getFlowname();
            String flowdetails = putFlowdesignerDTO.getFlowdetails();
            int active = putFlowdesignerDTO.getActive();

            if (flowname == null || flowname.isBlank() || flowname.trim().length() < 1) {

                flowApiResponse.setStatus(FLOWNAME_BLANK);
                flowApiResponse.setStatusdesc(str_FLOWNAME_BLANK);

                return flowApiResponse;

            }

            if (Validations.checkMaxStringLength(flowname, 100) != true) {

                flowApiResponse.setStatus(FLOWNAME_MAX_LENGTH);
                flowApiResponse.setStatusdesc(str_FLOWNAME_MAX_LENGTH);

                return flowApiResponse;
            }

            if (Validations.checkMinStringLength(flowname, 3) != true) {

                flowApiResponse.setStatus(FLOWNAME_MIN_LENGTH);
                flowApiResponse.setStatusdesc(str_FLOWNAME_MIN_LENGTH);

                return flowApiResponse;
            }

            if (!checkFlownameExists(flowname)) {
                flowApiResponse.setStatus(FLOWNAME_NOTEXISTS); // Flowname already exists
                flowApiResponse.setStatusdesc(str_FLOWNAME_NOTEXISTS);
                return flowApiResponse;
            }

            if (active < 0 || active > 1) {
                flowApiResponse.setStatus(ACTIVE_INVALID);
                flowApiResponse.setStatusdesc(str_ACTIVE_INVALID);

                return flowApiResponse;
            }

            Document filter = new Document("flowname", flowname).append("isdelete", 0);

            Document documentUpdate = new Document("flowname", flowname)
                    .append("flowdetails", flowdetails)
                    .append("active", active)
                    .append("isdelete", 0)
                    .append("modifieddatetime", timeStamp)
                    .append("modifybyuserid", 1425);

            log.info("documentUpdate  :: " + documentUpdate + "    ||  filter :: " + filter);

            long success = mongoDBConnection.updateDocument(databaseName, collectionName, filter, documentUpdate);
            log.info("success  :: " + success);

            if (success > 0) {
                Document updatedDocumentFilter = new Document("flowname", flowname).append("isdelete", 0);
                lastUpdatedDocument = getDataflow(updatedDocumentFilter);
                putFlowdesignerDTO.setFlowid(lastUpdatedDocument.getInteger("flowid"));
                putFlowdesignerDTO.setFlowname(lastUpdatedDocument.getString("flowname"));
                putFlowdesignerDTO.setActive(lastUpdatedDocument.getInteger("active"));
                putFlowdesignerDTO.setFlowdetails(lastUpdatedDocument.getString("flowdetails"));

                flowdesignerList.add(putFlowdesignerDTO);
                log.info("flowdesignerList  :: " + flowdesignerList);
            }
            if (flowdesignerList != null && flowdesignerList.size() > 0) {
                flowApiResponse.setStatus(STATUS_SUCESS);
                flowApiResponse.setStatusdesc(STATUS_SUCESS_MSG);
                flowApiResponse.setFlowdesign(flowdesignerList);
            } else {
                flowApiResponse.setStatus(STATUS_FAIL_TO_EDIT);
                flowApiResponse.setStatusdesc(STATUS_FAIL_TO_EDIT_MSG);
                flowApiResponse.setFlowdesign(flowdesignerList);
            }
            return flowApiResponse;

        } catch (Exception e) {
            // Handle exceptions appropriately, e.g., log the error or throw it
        } finally {
            timeStamp = null;
            lastUpdatedDocument = null;
            flowApiResponse = null;
            mongoDBConnection.Disconnect();
        }

        return null;
    }
    
    
    
    
    //********************************************************************************************
        public FlowdesignerApiResponse getByFlowid(int flowid) {
        FlowdesignerApiResponse flowApiResponse = new FlowdesignerApiResponse();
        Flowdesigner flowdesigner = new Flowdesigner();
        int revisionno = 1;
        int active = 0;
        String timeStamp = CommonGetMethods.getTimeStamp();
        Document document;
        Document retrivedDocument = null;
        FlowdesignerDTO flowdesignerDTO = new FlowdesignerDTO();
        List<FlowdesignerDTO> flowdesignerList = new ArrayList<>();

        try {
            log.info("**************getByFlowname******************");
            mongoDBConnection = new MongoDBConnection();
            mongoDBConnection.Connect("localhost", "27017");
            log.info("******************Connection done successfully******************");
            flowApiResponse.setResdatetime(timeStamp);
            flowApiResponse.setRevisionno(revisionno);

            if(flowid <=0){
                flowApiResponse.setStatus(FLOWID_INVALID); // Flowname already exists
                flowApiResponse.setStatusdesc(str_FLOWID_INVALID);
                return flowApiResponse;
            }
            
            
            if (!checkFlowIdExists(flowid)) {
                flowApiResponse.setStatus(FLOWID_NOTEXISTS); // Flowname already exists
                flowApiResponse.setStatusdesc(str_FLOWID_NOTEXISTS);
                return flowApiResponse;
            }

            Document filter = new Document("flowid", flowid).append("isdelete", 0);
            retrivedDocument = getDataflow(filter);
            log.info("lastInsertedDocument :: " + retrivedDocument);

            if (retrivedDocument != null) {
                flowdesignerDTO.setFlowid(retrivedDocument.getInteger("flowid"));
                flowdesignerDTO.setFlowname(retrivedDocument.getString("flowname"));
                flowdesignerDTO.setActive(retrivedDocument.getInteger("active"));
                flowdesignerDTO.setFlowdetails(retrivedDocument.getString("flowdetails"));

                flowdesignerList.add(flowdesignerDTO);
                log.info("flowdesignerList :: " + flowdesignerList);
            }

            if (flowdesignerList != null && !flowdesignerList.isEmpty()) {
                flowApiResponse.setStatus(STATUS_SUCESS);
                flowApiResponse.setStatusdesc(STATUS_SUCESS_MSG);
                flowApiResponse.setFlowdesign(flowdesignerList);
            } else {
                flowApiResponse.setStatus(STATUS_FAIL_TO_GET_BY_NAME);
                flowApiResponse.setStatusdesc(STATUS_FAIL_TO_GET_BY_NAME_MSG);
                flowApiResponse.setFlowdesign(null); // Set an empty list or null here, depending on your requirements.
            }

            return flowApiResponse;

        } catch (Exception e) {

        } finally {
            document = null;
            timeStamp = null;
            retrivedDocument = null;
            flowApiResponse = null;
            mongoDBConnection.Disconnect();
        }
        return null;
    }

        public FlowdesignerApiResponse putFlowdesignByFlowid(FlowdesignerDTO putFlowdesignerDTO) {
        FlowdesignerApiResponse flowApiResponse = new FlowdesignerApiResponse();
        int revisionno = 1;
        String timeStamp = CommonGetMethods.getTimeStamp();
        Document lastUpdatedDocument = null;
        List<FlowdesignerDTO> flowdesignerList = new ArrayList<>();

        try {
            mongoDBConnection = new MongoDBConnection();
            mongoDBConnection.Connect("localhost", "27017");
            log.info("****************Connection done successfully****************");
            flowApiResponse.setResdatetime(timeStamp);
            flowApiResponse.setRevisionno(revisionno);

            int flowid =putFlowdesignerDTO.getFlowid();
            String flowname = putFlowdesignerDTO.getFlowname();
            String flowdetails = putFlowdesignerDTO.getFlowdetails();
            int active = putFlowdesignerDTO.getActive();

            
            if(flowid <=0){
                flowApiResponse.setStatus(FLOWID_INVALID); // Flowname already exists
                flowApiResponse.setStatusdesc(str_FLOWID_INVALID);
                return flowApiResponse;
            }
            
            
            if (!checkFlowIdExists(flowid)) {
                flowApiResponse.setStatus(FLOWID_NOTEXISTS); // Flowname already exists
                flowApiResponse.setStatusdesc(str_FLOWID_NOTEXISTS);
                return flowApiResponse;
            }
            
            
            
            if (flowname == null || flowname.isBlank() || flowname.trim().length() < 1) {

                flowApiResponse.setStatus(FLOWNAME_BLANK);
                flowApiResponse.setStatusdesc(str_FLOWNAME_BLANK);

                return flowApiResponse;

            }

            if (Validations.checkMaxStringLength(flowname, 100) != true) {

                flowApiResponse.setStatus(FLOWNAME_MAX_LENGTH);
                flowApiResponse.setStatusdesc(str_FLOWNAME_MAX_LENGTH);

                return flowApiResponse;
            }

            if (Validations.checkMinStringLength(flowname, 3) != true) {

                flowApiResponse.setStatus(FLOWNAME_MIN_LENGTH);
                flowApiResponse.setStatusdesc(str_FLOWNAME_MIN_LENGTH);

                return flowApiResponse;
            }

            if (!checkFlownameExists(flowname)) {
                flowApiResponse.setStatus(FLOWNAME_NOTEXISTS); // Flowname already exists
                flowApiResponse.setStatusdesc(str_FLOWNAME_NOTEXISTS);
                return flowApiResponse;
            }

            if (active < 0 || active > 1) {
                flowApiResponse.setStatus(ACTIVE_INVALID);
                flowApiResponse.setStatusdesc(str_ACTIVE_INVALID);

                return flowApiResponse;
            }

            Document filter = new Document("flowid", flowid).append("isdelete", 0);

            Document documentUpdate = new Document("flowname", flowname)
                    .append("flowdetails", flowdetails)
                    .append("active", active)
                    .append("isdelete", 0)
                    .append("modifieddatetime", timeStamp)
                    .append("modifybyuserid", 1425);

            log.info("documentUpdate  :: " + documentUpdate + "    ||  filter :: " + filter);

            long success = mongoDBConnection.updateDocument(databaseName, collectionName, filter, documentUpdate);
            log.info("success  :: " + success);

            if (success > 0) {
                Document updatedDocumentFilter = new Document("flowname", flowname).append("isdelete", 0);
                lastUpdatedDocument = getDataflow(updatedDocumentFilter);
                putFlowdesignerDTO.setFlowid(lastUpdatedDocument.getInteger("flowid"));
                putFlowdesignerDTO.setFlowname(lastUpdatedDocument.getString("flowname"));
                putFlowdesignerDTO.setActive(lastUpdatedDocument.getInteger("active"));
                putFlowdesignerDTO.setFlowdetails(lastUpdatedDocument.getString("flowdetails"));

                flowdesignerList.add(putFlowdesignerDTO);
                log.info("flowdesignerList  :: " + flowdesignerList);
            }
            if (flowdesignerList != null && flowdesignerList.size() > 0) {
                flowApiResponse.setStatus(STATUS_SUCESS);
                flowApiResponse.setStatusdesc(STATUS_SUCESS_MSG);
                flowApiResponse.setFlowdesign(flowdesignerList);
            } else {
                flowApiResponse.setStatus(STATUS_FAIL_TO_EDIT);
                flowApiResponse.setStatusdesc(STATUS_FAIL_TO_EDIT_MSG);
                flowApiResponse.setFlowdesign(flowdesignerList);
            }
            return flowApiResponse;

        } catch (Exception e) {
            // Handle exceptions appropriately, e.g., log the error or throw it
        } finally {
            timeStamp = null;
            lastUpdatedDocument = null;
            flowApiResponse = null;
            mongoDBConnection.Disconnect();
        }

        return null;
    }

    public FlowdesignerApiResponse deleteDocumentByFlowId(int flowid) {
        FlowdesignerApiResponse flowApiResponse = new FlowdesignerApiResponse();
        int revisionno = 1;
        String timeStamp = CommonGetMethods.getTimeStamp();
        Document deleteInsertedDocument = null;
        FlowdesignerDTO flowdesignerDTO = new FlowdesignerDTO();
        List<FlowdesignerDTO> flowdesignerList = new ArrayList<>();
        try {
            mongoDBConnection = new MongoDBConnection();
            mongoDBConnection.Connect("localhost", "27017");
            log.info("Connection done successfully");

            flowApiResponse.setResdatetime(timeStamp);
            flowApiResponse.setRevisionno(revisionno);

           if(flowid <=0){
                flowApiResponse.setStatus(FLOWID_INVALID); // Flowname already exists
                flowApiResponse.setStatusdesc(str_FLOWID_INVALID);
                return flowApiResponse;
            }
            
            
            if (!checkFlowIdExists(flowid)) {
                flowApiResponse.setStatus(FLOWID_NOTEXISTS); // Flowname already exists
                flowApiResponse.setStatusdesc(str_FLOWID_NOTEXISTS);
                return flowApiResponse;
            }

            Document updatedDocumentFilter = new Document("flowid", flowid).append("isdelete", 0);
            Document documentUpdate = new Document("isdelete", 1)
                    .append("modifieddatetime", timeStamp)
                    .append("modifybyuserid", 1425);

            log.info("documentUpdate  :: " + documentUpdate + "    ||  filter :: " + updatedDocumentFilter);

            long success = mongoDBConnection.updateDocument(databaseName, collectionName, updatedDocumentFilter, documentUpdate);
            log.info("success  :: " + success);

            if (success > 0) {
                flowApiResponse.setStatus(STATUS_SUCESS);
                flowApiResponse.setStatusdesc(STATUS_SUCESS_MSG);
            } else {
                flowApiResponse.setStatus(STATUS_FAIL_TO_DELETE);
                flowApiResponse.setStatusdesc(STATUS_FAIL_TO_DELETE_MSG);
            }

            return flowApiResponse;

        } catch (Exception e) {
        } finally {
            timeStamp = null;
            if (mongoDBConnection != null) {
                mongoDBConnection.Disconnect();
            }
        }

        return null;
    }
        
    public boolean checkFlowIdExists(int flowid) {
        Document filter = new Document("flowid", flowid).append("isdelete", 0);
        Document existingDocument = mongoDBConnection.getSingleDocument(databaseName, collectionName, filter);
        return existingDocument != null;
    }
}
