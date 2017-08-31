package com.t3q.kafka;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLEncoder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JsonMain {
	
	//지하철 정보 ArrayList 메소드
	public JSONArray JsonSubwayList(String subwayName, int No) throws Exception {
		
		JSONParser jsonparser = new JSONParser();
        
		//json을 파싱하는 로직
		JSONObject jsonobject = (JSONObject)jsonparser.parse(readUrl(subwayName, No));
        
        JSONArray arraySubway = (JSONArray)jsonobject.get("realtimePositionList");
        
        
        
        return arraySubway;
	}
	
	//지하철 code Map 메소드
	public JSONObject JsonSubwayCode(String subwayName, int No) throws Exception {
		
		JSONParser jsonparser = new JSONParser();
        
		//json을 파싱하는 로직
		JSONObject jsonobject = (JSONObject)jsonparser.parse(readUrl(subwayName, No));
        
        JSONObject jsonSubway =  (JSONObject) jsonobject.get("errorMessage");
        
        if(jsonSubway==null) {
        	
        	return jsonobject;
        }else {
        	
        	return jsonSubway;
        }
        
	}
	
	
	
	//URL에서 json으로 받아오는 로직
	private static String readUrl(String subwayName, int No) throws Exception {
        BufferedInputStream reader = null;
        
        int startNo = 0;
		int lastNo = No;
		String subwayNameR = subwayName;
		String Line =URLEncoder.encode(subwayNameR,"UTF-8");
		
		String addr = "http://swopenapi.seoul.go.kr/api/subway/";
		String serviceKey = "5479556e78796b6339336b4d786e56";
		String parameter = "/json/realtimePosition/";
		
		addr = addr + serviceKey + parameter + startNo + "/" + lastNo + "/" + Line;
		
        
        try {
            URL url = new URL(addr);
            reader = new BufferedInputStream(url.openStream());
            StringBuffer buffer = new StringBuffer();
            int i;
            byte[] b = new byte[4096];
            while( (i = reader.read(b)) != -1){
              buffer.append(new String(b, 0, i));
            }
            
            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }
	
}
