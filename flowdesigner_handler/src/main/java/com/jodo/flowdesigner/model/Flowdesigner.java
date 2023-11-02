/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jodo.flowdesigner.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author abhishek.d
 */

//@Document(collection = "flowdesigner")
public class Flowdesigner 
{
    private Object id;
    private int flowid;
    private String flowname;
    private String createdatetime;
    private String modifieddatetime;
    private int createbyuserid;
    private int modifybyuserid;
    private int active;
    private int isdelete;
    private String flowdetails;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public int getFlowid() {
        return flowid;
    }

    public void setFlowid(int flowid) {
        this.flowid = flowid;
    }

    public String getFlowname() {
        return flowname;
    }

    public void setFlowname(String flowname) {
        this.flowname = flowname;
    }

    public String getCreatedatetime() {
        return createdatetime;
    }

    public void setCreatedatetime(String createdatetime) {
        this.createdatetime = createdatetime;
    }

    public String getModifieddatetime() {
        return modifieddatetime;
    }

    public void setModifieddatetime(String modifieddatetime) {
        this.modifieddatetime = modifieddatetime;
    }

    public int getCreatebyuserid() {
        return createbyuserid;
    }

    public void setCreatebyuserid(int createbyuserid) {
        this.createbyuserid = createbyuserid;
    }

    public int getModifybyuserid() {
        return modifybyuserid;
    }

    public void setModifybyuserid(int modifybyuserid) {
        this.modifybyuserid = modifybyuserid;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(int isdelete) {
        this.isdelete = isdelete;
    }

    public String getFlowdetails() {
        return flowdetails;
    }

    public void setFlowdetails(String flowdetails) {
        this.flowdetails = flowdetails;
    }

    public Flowdesigner(Object id, int flowid, String flowname, String createdatetime, String modifieddatetime, int createbyuserid, int modifybyuserid, int active, int isdelete, String flowdetails) {
        this.id = id;
        this.flowid = flowid;
        this.flowname = flowname;
        this.createdatetime = createdatetime;
        this.modifieddatetime = modifieddatetime;
        this.createbyuserid = createbyuserid;
        this.modifybyuserid = modifybyuserid;
        this.active = active;
        this.isdelete = isdelete;
        this.flowdetails = flowdetails;
    }

    public Flowdesigner() {
    }

    @Override
    public String toString() {
        return "Flowdesigner{" + "id=" + id + ", flowid=" + flowid + ", flowname=" + flowname + ", createdatetime=" + createdatetime + ", modifieddatetime=" + modifieddatetime + ", createbyuserid=" + createbyuserid + ", modifybyuserid=" + modifybyuserid + ", active=" + active + ", isdelete=" + isdelete + ", flowdetails=" + flowdetails + '}';
    }
        
}
