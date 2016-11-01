package com.androidbelieve.drawerwithswipetabs;

import android.util.Log;

import com.androidbelieve.drawerwithswipetabs.Model.ProjectData;
import com.androidbelieve.drawerwithswipetabs.Model.VendorData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;


public class FetchLabourInfoService {
    public static Map projectInfo;

    public void transferData(String result) {
        projectInfo = new HashMap<String, ProjectData>();

        //Map mpd = new HashMap();

        if (result != null) {

            Log.v("Result", "Result : " + result);

            try {

                JSONObject jsonLabourData = new JSONObject(result);
                if (jsonLabourData.getString("status").equals("1")) {
                    JSONArray jsonLabourArray = jsonLabourData.getJSONArray("result");

                    for (int i = 0; i < jsonLabourArray.length(); i++) {

                        JSONObject jsonLabourObject = (JSONObject) jsonLabourArray.get(i);
                        String prjNo = jsonLabourObject.getString("prj_number");

                        if (!projectInfo.containsKey(prjNo)) {
                            ProjectData prjData = new ProjectData();
                            prjData.setPrjId(jsonLabourObject.getString("prj_id"));
                            projectInfo.put(prjNo, prjData);
                            Map<String, VendorData> venMap = new HashMap<String, VendorData>();
                            prjData.setVendor(venMap);

                            VendorData vData = new VendorData();
                            venMap.put(jsonLabourObject.getString("ven_number"), vData);
                            vData.setVendorId(jsonLabourObject.getString("ven_id"));
                            Map<String, String> contractData = new HashMap<String, String>();
                            vData.setContract(contractData);
                            contractData.put(jsonLabourObject.getString("con_number"), jsonLabourObject.getString("con_id"));

                        } else {


                            String vendorNumber = jsonLabourObject.getString("ven_number");
                            ProjectData prjData = (ProjectData) projectInfo.get(prjNo);

                            if (!prjData.getVendor().containsKey(vendorNumber)) {
                                VendorData vData = new VendorData();
                                prjData.getVendor().put(vendorNumber, vData);
                                vData.setVendorId(jsonLabourObject.getString("ven_id"));
                                Map<String, String> contractData = new HashMap<String, String>();
                                vData.setContract(contractData);
                                contractData.put(jsonLabourObject.getString("con_number"), jsonLabourObject.getString("con_id"));
                            } else {
                                VendorData vData = prjData.getVendor().get(vendorNumber);
                                vData.getContract().put(jsonLabourObject.getString("con_number"), jsonLabourObject.getString("con_id"));
                            }


                        }

                    }
                    TreeMap<String,ProjectData> mapSorted = new TreeMap<>(projectInfo);
                    projectInfo = mapSorted;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    public static Map getProjectInfo() {
        return projectInfo;
    }

    public static void setProjectInfo(Map projectInfo) {
        FetchLabourInfoService.projectInfo = projectInfo;
    }

}
