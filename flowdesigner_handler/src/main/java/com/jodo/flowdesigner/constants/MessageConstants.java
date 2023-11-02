/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jodo.flowdesigner.constants;

/**
 *
 * @author abhishek.d
 */
public interface MessageConstants {

    String strFlowdesignerversion = "Flow Designer API Version 0.0.1    01 November 2023";
    
    
    int FLOWNAME_BLANK = 20211;
    int FLOWNAME_MAX_LENGTH = 20212;
    int FLOWNAME_MIN_LENGTH = 20213;
    int FLOWNAME_ALREADY_EXISTS = 20214;
    int ACTIVE_INVALID = 20215;
    int FLOWNAME_NOTEXISTS = 20216;
    int FLOWID_NOTEXISTS=20217;
    int FLOWID_INVALID=20218;
    
    String str_FLOWNAME_BLANK = "flowname should not be blank";
    String str_FLOWNAME_MAX_LENGTH = "flowname length should  be less than 100";
    String str_FLOWNAME_MIN_LENGTH = "flowname length should be greater than 3";
    String str_FLOWNAME_ALREADY_EXISTS = "flowname already exists";
    String str_ACTIVE_INVALID = "active should be 0 or 1";
    String str_FLOWNAME_NOTEXISTS="flowname not exists";
    String str_FLOWID_NOTEXISTS ="flowid not exists";
    String str_FLOWID_INVALID="flowid should not be 0 or negative";
    
    int STATUS_SUCESS=1;
    String STATUS_SUCESS_MSG="Sucess";
    
     int STATUS_FAIL_TO_GET_BY_NAME = 20205;
    String STATUS_FAIL_TO_GET_BY_NAME_MSG = "Fail to get Flow Design ";

    int STATUS_FAIL_TO_GET_LIST = 20206;
    String STATUS_FAIL_TO_GET_LIST_MSG = "Fail to get Flow Designlist";

    int STATUS_FAIL_TO_ADD = 20207;
    String STATUS_FAIL_TO_ADD_MSG = "Fail to add Flow Design";

    int STATUS_FAIL_TO_EDIT = 20208;
    String STATUS_FAIL_TO_EDIT_MSG = "Fail to edit Flow Design";

    int STATUS_FAIL_TO_DELETE = 20209;
    String STATUS_FAIL_TO_DELETE_MSG = "Fail to delete Flow Design";
    
    
}
