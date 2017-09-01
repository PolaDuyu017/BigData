package com.t3q.kafka;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JsonRealtimeSubway {
	
	//Map 사용예시
	//public Map<String, String> subwayMap;
	
	public String[][] subwayArray;
	public int Number;

	//생성자(검색할 노선, 총 객차 개수 설정됨)
	public JsonRealtimeSubway(){
		
		//Map 사용예시
		/*subwayMap = new HashMap();
		subwayMap.put("1호선", "1001");
		subwayMap.put("2호선", "1002");
		subwayMap.put("3호선", "1003");
		subwayMap.put("4호선", "1004");
		subwayMap.put("5호선", "1005");
		subwayMap.put("6호선", "1006");
		subwayMap.put("7호선", "1007");
		subwayMap.put("8호선", "1008");
		subwayMap.put("9호선", "1009");
		subwayMap.put("경춘선", "1067");
		subwayMap.put("경의중앙선", "1063");
		subwayMap.put("공항철도", "1065");
		subwayMap.put("분당선", "1075");
		subwayMap.put("신분당선", "1077");
		subwayMap.put("인천", "0000");
		subwayMap.put("수인선", "1071");*/
		
		String[][] subways = {{"1호선","1001"}, {"2호선","1002"},{"3호선", "1003"},{"4호선", "1004"},{"5호선", "1005"},
							 {"6호선", "1006"},{"7호선", "1007"},{"8호선", "1008"},{"9호선", "1009"},{"경춘선", "1067"},
							 {"경의중앙선", "1063"},{"공항철도", "1065"},{"분당선", "1075"},{"신분당선", "1077"},{"인천", "0000"},{"수인선", "1071"}};
		subwayArray = subways;
		
		Number = 300;
		
	}
	
	//실시간 지하철 정보를 json통으로 가져오는  메소드
	public JSONObject JsonSubway(String subwayName, String subwayCode, int No) throws Exception {
		
		JSONParser jsonparser = new JSONParser();
        Date RealTime;
        long time1 = System.currentTimeMillis();
		//json을 파싱하는 로직
        RealTime = new Date();
		JSONObject jsonobject = (JSONObject)jsonparser.parse(readUrl(subwayName, No));
		
		String miltime = new SimpleDateFormat("yyMMddHHmmss").format(RealTime);
		int intTime = Integer.parseInt(miltime.substring(10));
		if(intTime<30){
			miltime = miltime.substring(0, 10)+"00";
		}else {
			miltime = miltime.substring(0, 10)+"30";
		}
		
		String time = subwayCode+"_"+miltime;
		System.out.println("id : "+time);
		jsonobject.put("id", time);
		long time2 = System.currentTimeMillis();
		System.out.println("밀리시간 : "+(time2-time1));
		
		
        return jsonobject;
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
