package com.melon.cau_capstone2_ict.Manager;

import java.util.ArrayList;

public class OnBackManager {
    private static OnBackManager instance = null;

    private int position;
    private ArrayList<ArrayList> onBackList = new ArrayList<>();

    private OnBackManager(){
        for(int inx = 0; inx < 5; inx ++){
            ArrayList<Object> a = new ArrayList<>();
            onBackList.add(a);
        }
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setOnBackList(Object o){
        onBackList.get(position).add(o);
    }
    public Object getOnBackList(){
        int index = onBackList.get(position).size()-1;
        if(index != -1){
            return onBackList.get(position).get(index);
        }
        else{
            return null;
        }
    }
    public void removeOnBackList(){
        int index = onBackList.get(position).size()-1;
        if(index != -1){
            onBackList.get(position).remove(index);
        }
    }

    public static synchronized OnBackManager getInstance(){
        if(instance == null){
            instance = new OnBackManager();
        }
        return instance;
    }
}
