package com.androidbelieve.drawerwithswipetabs.Model;

public class AddLabourDetailModelClass {

    String cDescription,cNoOfLabour,cRemark,cDescriptionId,Seqs_No,rate, lvl;


    public AddLabourDetailModelClass(String cDescription, String cNoOfLabour, String cRemark,String cDescriptionId,String Seqs_No ) {
        this.cDescription = cDescription;
        this.cNoOfLabour = cNoOfLabour;
        this.cNoOfLabour = cNoOfLabour;
        this.cRemark = cRemark;
        this.lvl = cDescriptionId.split(":")[1];
        this.rate = cDescriptionId.split(":")[2];
        this.cDescriptionId = cDescriptionId.split(":")[0];
        this.Seqs_No = Seqs_No;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getLvl() {
        return lvl;
    }

    public void setLvl(String lvl) {
        this.lvl = lvl;
    }

    public String getSeqs_No() {
        return Seqs_No;
    }

    public void setSeqs_No(String seqs_No) {
        Seqs_No = seqs_No;
    }

    public String getcDescription() {
        return cDescription;
    }

    public void setcDescription(String cDescription) {
        this.cDescription = cDescription;
    }

    public String getcNoOfLabour() {
        return cNoOfLabour;
    }

    public void setcNoOfLabour(String cNoOfLabour) {
        this.cNoOfLabour = cNoOfLabour;
    }

    public String getcRemark() {
        return cRemark;
    }

    public void setcRemark(String cRemark) {
        this.cRemark = cRemark;
    }

    public String getcDescriptionId() {
        return cDescriptionId;
    }

    public void setcDescriptionId(String cDescriptionId) {
        this.cDescriptionId = cDescriptionId;
    }
}
