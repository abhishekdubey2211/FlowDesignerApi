/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jodo.flowdesigner.model;

import java.util.List;

/**
 *
 * @author abhishek.d
 */
public class FlowdesignerApiResponse 
{
    	private String resdatetime;
	private int status;
	private String statusdesc;
	private int revisionno;
        private List<FlowdesignerDTO> flowdesign;

    public String getResdatetime() {
        return resdatetime;
    }

    public void setResdatetime(String resdatetime) {
        this.resdatetime = resdatetime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusdesc() {
        return statusdesc;
    }

    public void setStatusdesc(String statusdesc) {
        this.statusdesc = statusdesc;
    }

    public int getRevisionno() {
        return revisionno;
    }

    public void setRevisionno(int revisionno) {
        this.revisionno = revisionno;
    }

    public List<FlowdesignerDTO> getFlowdesign() {
        return flowdesign;
    }

    public void setFlowdesign(List<FlowdesignerDTO> flowdesign) {
        this.flowdesign = flowdesign;
    }

    public FlowdesignerApiResponse(String resdatetime, int status, String statusdesc, int revisionno, List<FlowdesignerDTO> flowdesign) {
        this.resdatetime = resdatetime;
        this.status = status;
        this.statusdesc = statusdesc;
        this.revisionno = revisionno;
        this.flowdesign = flowdesign;
    }

    public FlowdesignerApiResponse() {
    }

    @Override
    public String toString() {
        return "FlowdesignerApiResponse{" + "resdatetime=" + resdatetime + ", status=" + status + ", statusdesc=" + statusdesc + ", revisionno=" + revisionno + ", flowdesign=" + flowdesign + '}';
    }

   
}

