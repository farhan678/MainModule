package com.androidbelieve.drawerwithswipetabs.Model;

public class Labour {
    String BatchID,Project_NO,Vendor_Name,Reported_Date,Remark,custom_batch_id;

    public Labour(String batchID, String project_NO, String vendor_Name, String reported_Date, String remark, String custom_batch_id) {
        BatchID = batchID;
        Project_NO = project_NO;
        Vendor_Name = vendor_Name;
        Reported_Date = reported_Date;
        Remark = remark;
        this.custom_batch_id = custom_batch_id;
    }

    public String getCustom_batch_id() {
        return custom_batch_id;
    }

    public void setCustom_batch_id(String custom_batch_id) {
        this.custom_batch_id = custom_batch_id;
    }

    public String getBatchID() {
        return BatchID;
    }

    public void setBatchID(String batchID) {
        BatchID = batchID;
    }

    public String getProject_NO() {
        return Project_NO;
    }

    public void setProject_NO(String project_NO) {
        Project_NO = project_NO;
    }

    public String getVendor_Name() {
        return Vendor_Name;
    }

    public void setVendor_Name(String vendor_Name) {
        Vendor_Name = vendor_Name;
    }

    public String getReported_Date() {
        return Reported_Date;
    }

    public void setReported_Date(String reported_Date) {
        Reported_Date = reported_Date;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }
}
