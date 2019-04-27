package com.melon.cau_capstone2_ict;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ParseWeather {
    public void setXML() throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document document = builder.parse("http://www.kma.go.kr/weather/forecast/mid-term-rss3.jsp?stnId=109");

        if(document != null) {
            NodeList list = document.getElementsByTagName("data");

            for (int i = 0; i < list.getLength(); i++) {
                Log.d("Tag","==="+list.item(i).getAttributes().getNamedItem("seq").getTextContent()+"===");
                //childNode 출력
                for(int k = 0; k < list.item(i).getChildNodes().getLength(); k++){
                    if(list.item(i).getChildNodes().item(k).getNodeType() == Node.ELEMENT_NODE){
                        Log.d("Tag", k+":"+list.item(i).getChildNodes().item(k).getNodeName()+"====>");
                        Log.d("Tag", list.item(i).getChildNodes().item(k).getTextContent());
                    }
                }
            }
        }
    }
}
