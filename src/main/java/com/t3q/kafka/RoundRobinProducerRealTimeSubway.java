package com.t3q.kafka;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.Cluster;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class RoundRobinProducerRealTimeSubway {
	public static void main(String[] args) throws Exception {
		Properties props = new Properties();

		// props.put("metadata.broker.list","big01:9092,big02:9092,big03:9092");
		// props.put("serializer.class", "kafka.serializer.StringEncoder");

		props.put("bootstrap.servers", "big01:9092,big02:9092,big03:9092");
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
		props.put("partitioner.class", RoundRobinPartitioner.class.getName());
		
		Producer<String, String> producer = new KafkaProducer<String, String>(props);
		
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
		
		/*for(int i=0;  i<100; i++){
			message = new ProducerRecord<String, String>("test", i+"",i+" Hello, World!");
			Thread.sleep(100);
			producer.send(message);
		}*/
		
		producer.close();
	}

	
	public static class RoundRobinPartitioner implements Partitioner {
		private AtomicInteger n = new AtomicInteger(0);

		public RoundRobinPartitioner() {
		}

		@Override
		public void configure(Map<String, ?> configs) {
		}

		@Override
		public int partition(String topic, Object key, byte[] keyBytes,Object value, byte[] valueBytes, Cluster cluster) {
			
			int numPartitions = cluster.partitionCountForTopic(topic);
			
			int i = n.getAndIncrement();
			if (i == Integer.MAX_VALUE) {
				n.set(0);
				return 0;
			}
			System.out.format("[ %d th partition no]/[numPartitions]   =  %d/%d \n",i, (i % numPartitions),numPartitions);
			return i % numPartitions;

		}

		@Override
		public void close() {
		
		}

	}
}

// 출처: http://epicdevs.com/21 [Epic Developer]