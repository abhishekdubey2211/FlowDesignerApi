/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jodo.flowdesigner.controller;

import com.jodo.flowdesigner.action.FlowdesignerAction;
import com.jodo.flowdesigner.constants.MessageConstants;
import static com.jodo.flowdesigner.constants.MessageConstants.strFlowdesignerversion;

import com.jodo.flowdesigner.model.FlowdesignerApiResponse;
import com.jodo.flowdesigner.model.FlowdesignerDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author abhishek.d
 */
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("flowdesign")
public class FlowdesignerController implements MessageConstants {

    private static final Logger log = LogManager.getLogger(FlowdesignerController.class);


    @RequestMapping(method = RequestMethod.GET, path = "/version")
    public String getVersion() {
        String version = strFlowdesignerversion;
        log.info("FlowdesignerController version : " + strFlowdesignerversion);
        return version;
    }

    @RequestMapping(method = RequestMethod.POST)
    public FlowdesignerApiResponse pushDataflow(@RequestBody FlowdesignerDTO pushFlowdesigner) {
        FlowdesignerApiResponse flowApiResponse = new FlowdesignerApiResponse();
        FlowdesignerAction flowdesignerAction = new FlowdesignerAction();

        try {
            log.info("***********************pushDataflow Api*************************");

            flowApiResponse = flowdesignerAction.pushFlowdesign(pushFlowdesigner);
            return flowApiResponse;
        } catch (Exception e) {
            log.error("SOME EXCEPTION IN pushDataflow >> " + e);
            // Error throws 500 | INTERNAL_SERVER_ERROR
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server error");
        }

    }

    @RequestMapping(method = RequestMethod.PUT)
    public FlowdesignerApiResponse putDataflow(@RequestBody FlowdesignerDTO pushFlowdesigner) {
        FlowdesignerApiResponse flowApiResponse = new FlowdesignerApiResponse();
        FlowdesignerAction flowdesignerAction = new FlowdesignerAction();

        try {
            log.info("***********************putDataflow Api*************************");
            flowApiResponse = flowdesignerAction.putFlowdesign(pushFlowdesigner);
            return flowApiResponse;
        } catch (Exception e) {
            log.error("SOME EXCEPTION IN pushDataflow >> " + e);
            // Error throws 500 | INTERNAL_SERVER_ERROR
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server error");
        }

    }
    @RequestMapping(method = RequestMethod.PUT,path="/editbyid")
    public FlowdesignerApiResponse putDataflowByid(@RequestBody FlowdesignerDTO pushFlowdesigner) {
        FlowdesignerApiResponse flowApiResponse = new FlowdesignerApiResponse();
        FlowdesignerAction flowdesignerAction = new FlowdesignerAction();

        try {
            log.info("***********************putDataflow Api*************************");
            flowApiResponse = flowdesignerAction.putFlowdesignByFlowid(pushFlowdesigner);
            return flowApiResponse;
        } catch (Exception e) {
            log.error("SOME EXCEPTION IN putDataflowByid >> " + e);
            // Error throws 500 | INTERNAL_SERVER_ERROR
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server error");
        }

    }



    @RequestMapping(method = RequestMethod.GET, path = "/flowname/{name}")
    public FlowdesignerApiResponse getByFlowname(@PathVariable("name") String flowname) {
  FlowdesignerApiResponse flowApiResponse = new FlowdesignerApiResponse();
        FlowdesignerAction flowdesignerAction = new FlowdesignerAction();

        try {
            log.info("***********************getByFlowname Api*************************");

            flowApiResponse = flowdesignerAction.getByFlowname(flowname);
            return flowApiResponse;
        } catch (Exception e) {
            log.error("SOME EXCEPTION IN pushDataflow >> " + e);
            // Error throws 500 | INTERNAL_SERVER_ERROR
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server error");
        }

    }

    @RequestMapping(method = RequestMethod.GET, path = "/flowid/{id}")
    public FlowdesignerApiResponse getByFlowId(@PathVariable("id") int flowid) {
  FlowdesignerApiResponse flowApiResponse = new FlowdesignerApiResponse();
        FlowdesignerAction flowdesignerAction = new FlowdesignerAction();

        try {
            log.info("***********************getByFlowname Api*************************");

            flowApiResponse = flowdesignerAction.getByFlowid(flowid);
            return flowApiResponse;
        } catch (Exception e) {
            log.error("SOME EXCEPTION IN getByFlowId >> " + e);
            // Error throws 500 | INTERNAL_SERVER_ERROR
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server error");
        }

    }



    @RequestMapping(method = RequestMethod.GET, path = "/flowdesign_all")
    public FlowdesignerApiResponse getAllFlow() {
        FlowdesignerApiResponse flowApiResponse = new FlowdesignerApiResponse();
        FlowdesignerAction flowdesignerAction = new FlowdesignerAction();

        try {
            log.info("***********************getAllFlow Api*************************");
            flowApiResponse = flowdesignerAction.getAllDocuments();
            return flowApiResponse;
        } catch (Exception e) {
            log.error("SOME EXCEPTION IN pushDataflow >> " + e);
            // Error throws 500 | INTERNAL_SERVER_ERROR
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server error");
        }

    }



    @RequestMapping(method = RequestMethod.GET, path = "/count")
    public int getAllcount() {
        FlowdesignerApiResponse flowApiResponse = new FlowdesignerApiResponse();
        FlowdesignerAction flowdesignerAction = new FlowdesignerAction();
        int count =0;
        try {
            log.info("***********************getAllcount Api*************************");
            count = flowdesignerAction.getTotaldataIncollection();
            return count;
        } catch (Exception e) {
            log.error("SOME EXCEPTION IN getAllcount >> " + e);
            // Error throws 500 | INTERNAL_SERVER_ERROR
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server error");
        }

    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/flowname/{name}")
    public FlowdesignerApiResponse deleteByFlowname(@PathVariable("name") String flowname) {
        FlowdesignerApiResponse flowApiResponse = new FlowdesignerApiResponse();
        FlowdesignerAction flowdesignerAction = new FlowdesignerAction();

        try {
            log.info("***********************deleteByFlowname Api*************************");
            flowApiResponse = flowdesignerAction.deleteDocumentByName(flowname);
            return flowApiResponse;
        } catch (Exception e) {
            log.error("SOME EXCEPTION IN pushDataflow >> " + e);
            // Error throws 500 | INTERNAL_SERVER_ERROR
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server error");
        }

    }


    @RequestMapping(method = RequestMethod.DELETE, path = "/flowid/{id}")
    public FlowdesignerApiResponse deleteByFlowid(@PathVariable("id") int flowid) {
        FlowdesignerApiResponse flowApiResponse = new FlowdesignerApiResponse();
        FlowdesignerAction flowdesignerAction = new FlowdesignerAction();

        try {
            log.info("***********************deleteByFlowid Api*************************");
            flowApiResponse = flowdesignerAction.deleteDocumentByFlowId(flowid);
            return flowApiResponse;
        } catch (Exception e) {
            log.error("SOME EXCEPTION IN deleteByFlowid >> " + e);
            // Error throws 500 | INTERNAL_SERVER_ERROR
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server error");
        }

    }

}
