package com.androidbelieve.drawerwithswipetabs.Model;


import java.util.Map;

public class ProjectData {
    private String prjId;
    private Map<String, VendorData> vendor;

    public String getPrjId() {
        return prjId;
    }

    public void setPrjId(String prjId) {
        this.prjId = prjId;
    }


    public Map<String, VendorData> getVendor() {
        return vendor;
    }

    public void setVendor(Map<String, VendorData> vendor) {
        this.vendor = vendor;
    }
}
