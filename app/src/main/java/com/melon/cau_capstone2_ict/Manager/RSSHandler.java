package com.melon.cau_capstone2_ict.Manager;

import android.view.ViewParent;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class RSSHandler {
    private int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    private String temp = "온도";
    private String wfKor = "날씨";
    private String pty = "강수량";
    private String pop = "강수확률";
    private String urlString = null;
    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = true;

    public RSSHandler(String url) {
        this.urlString = url;
    }

    public int getHour() {
        return hour;
    }

    public String getTemp() {
        return temp;
    }

    public String getWfKor() {
        return wfKor;
    }

    public String getPty() {
        return pty;
    }

    public String getPop() {
        return pop;
    }

    private synchronized void parseXMLAndStoreIt(XmlPullParser myParser) {
        int event;
        String text = null;

        try {
            event = myParser.getEventType();
            while (parsingComplete) {
                String name = myParser.getName();

                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (name.equals("hour")) {
                            hour = Integer.parseInt(text);
                        } else if (name.equals("temp")) {
                            temp = text;
                        } else if (name.equals("wfKor")) {
                            wfKor = text;
                        } else if (name.equals("pty")) {
                            pty = text;
                        } else if (name.equals("pop")) {
                            pop = text;
                            parsingComplete = false;
                        }
                        break;
                }   // temp: 현재온도, wfKor: 날씨, pop: 강수확률, pty: 강수량
                event = myParser.next();
            }
        } catch (Exception e) {
        }
    }

    public void fetchXML() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);

                    // Starts the query
                    conn.connect();
                    InputStream stream = conn.getInputStream();

                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser myparser = xmlFactoryObject.newPullParser();

                    myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    myparser.setInput(stream, null);

                    parseXMLAndStoreIt(myparser);
                    stream.close();
                } catch (Exception e) {
                }
            }
        });
        try {
            thread.start();
        } catch (Exception e) {
        }
    }
}
