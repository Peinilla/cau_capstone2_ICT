package com.melon.cau_capstone2_ict.Manager;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CalendarRequest extends StringRequest {
    final static String URL = "https://capston2webapp.azurewebsites.net/api/" + MyUserData.getInstance().getId() + "/calender";
    private Map<String, String> parameters;

    public CalendarRequest(MyCalendar myCalendar, String type, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Tag", "error " + error);
            }
        });
        parameters = new HashMap<>();
        parameters.put("title", myCalendar.getTitle());
        parameters.put("content", myCalendar.getContent());
        parameters.put("date", myCalendar.getDate());
        parameters.put("type", type);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}