package com.melon.cau_capstone2_ict.Manager;

import android.content.Context;
import android.content.res.AssetManager;
import android.location.Location;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BuildingManager {
    Context mContext;
    ArrayList<building> buildingData;
    private static BuildingManager instance;

    private BuildingManager(){

    }
    public void setData(Context context) {
        mContext = context;
        AssetManager am = mContext.getResources().getAssets();
        InputStream is = null;
        buildingData = new ArrayList<>();

        try {
            is = am.open("data/m.txt");
            BufferedReader bufrd = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            String line = bufrd.readLine();
            while ((line = bufrd.readLine()) != null) {
                String str[] = line.split("\t");
                building b = new building();
                b.name = str[3];
                b.location.setLatitude(Float.parseFloat(str[1]));
                b.location.setLongitude(Float.parseFloat(str[2]));

                buildingData.add(b);
            }
        } catch(Exception e){
            Log.d("Tag", "data error : " +  e.getMessage());

        }

        Log.d("Tag", "data size : " +  buildingData.size());

    }

    public String getNearBuilding(Location location) {
        int min = 10000;
        int min_index = 0;

        for (int inx = 0; inx < buildingData.size(); inx++) {
            int dist = (int)location.distanceTo(buildingData.get(inx).location);
            if (dist < min) {
                min = dist;
                min_index = inx;
            }
        }
        if(min > 100){
            return "권외";
        }
        else{
            return buildingData.get(min_index).name;
        }
    }

    public static synchronized BuildingManager getInstance(){
        if(instance == null){
            instance = new BuildingManager();
        }
        return instance;
    }

    public class building{
        String name;
        Location location;

        building(){
            name = "";
            location = new Location("");
        }
    }
}
