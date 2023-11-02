/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jodo.flowdesigner.model;

/**
 *
 * @author abhishek.d
 */
public class FlowdesignerDTO 
{
    private int flowid;
    private String flowname;
    private int active;
    private String flowdetails;

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

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getFlowdetails() {
        return flowdetails;
    }

    public void setFlowdetails(String flowdetails) {
        this.flowdetails = flowdetails;
    }

    public FlowdesignerDTO(int flowid, String flowname, int active, String flowdetails) {
        this.flowid = flowid;
        this.flowname = flowname;
        this.active = active;
        this.flowdetails = flowdetails;
    }

    @Override
    public String toString() {
        return "FlowdesignerDTO{" + "flowid=" + flowid + ", flowname=" + flowname + ", active=" + active + ", flowdetails=" + flowdetails + '}';
    }

    public FlowdesignerDTO() {
    }
    
    
    
}
