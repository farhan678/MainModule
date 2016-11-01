package com.androidbelieve.drawerwithswipetabs.Model;


import java.util.Map;

public class VendorData {
    private String vendorId;
    private Map<String, String> contract;

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }


    public Map<String, String> getContract() {
        return contract;
    }

    public void setContract(Map<String, String> contract) {
        this.contract = contract;
    }
}
