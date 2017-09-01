package com.t3q.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ProducerRealTimeSubway {

	public static void main(String[] args) throws Exception {

		Properties props = new Properties();

		//props.put("metadata.broker.list","big01:9092,big02:9092,big03:9092");
		//props.put("serializer.class", "kafka.serializer.StringEncoder");
		
		props.put("bootstrap.servers", "big01:9092,big02:9092,big03:9092");// broker list 필수! 
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
			
		
		Producer<String, String> producer  = new KafkaProducer<String,String>(props);	
		
		ProducerRecord<String, String> message;
		
		//추가코드
		JsonRealtimeSubway jsonA = new JsonRealtimeSubway();
		
		for(int i = 0; i<jsonA.subwayArray.length; i++) {
			if(!jsonA.subwayArray[i][0].equals("인천")){
				JSONObject jsonSubway = jsonA.JsonSubway(jsonA.subwayArray[i][0], jsonA.subwayArray[i][1], jsonA.Number);
				
				String id = jsonSubway.get("id").toString();
				
				String code = jsonSubway.toString();
				message = new ProducerRecord<String, String>("test", id,code);
				/*System.out.println(message);*/
				producer.send(message);
			}
		}
		
		
		//추가한 코드
		/*String subway = "1호선";
		int Number = 10;
		
		JsonMain jsonA = new JsonMain();
		
		JSONObject jsonSubwayCode = jsonA.JsonSubwayCode(subway, Number);
		
		String code = jsonSubwayCode.get("code").toString();
		message = new ProducerRecord<String, String>("test", "code",code);
		System.out.println(message);
		Thread.sleep(100);
		producer.send(message);
		
		String messageSubway = jsonSubwayCode.get("message").toString();
		message = new ProducerRecord<String, String>("test", "message",messageSubway);
		System.out.println(message);
		Thread.sleep(100);
		producer.send(message);
		
		if(code.equals("INFO-000")){
			JSONArray jsonSubwayList = jsonA.JsonSubwayList(subway, Number);
			
			for(int i = 0 ; i < jsonSubwayList.size(); i++){
				JSONObject entity = (JSONObject)jsonSubwayList.get(i);
	
				String trainNo = entity.get("trainNo").toString();
				message = new ProducerRecord<String, String>("test", i+"_trainNo",trainNo);
				System.out.println(message);
				Thread.sleep(100);
				producer.send(message);
				
				String statnTnm = entity.get("statnTnm").toString();
				message = new ProducerRecord<String, String>("test", i+"_statnTnm",statnTnm);
				System.out.println(message);
				Thread.sleep(100);
				producer.send(message);
	
			}
		}*/

		
		/*for(int i=0;  i<100; i++){
			ProducerRecord<String, String> message = new ProducerRecord<String, String>("test", i+"",i+" Hello, World!");
			Thread.sleep(100);
			producer.send(message);
		}*/
		
		
		producer.close();

	}

}
