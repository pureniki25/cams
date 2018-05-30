package com.hongte.alms.finance.util.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kafka.admin.TopicCommand;

/**
 * 
 * 
 * @Filename KafkaUtils.java
 *
 * @Description
 *
 * @Version 1.0
 *
 * @Author Lijie
 *
 * @Email lijiewj39069@touna.cn
 * 
 * @History
 *          <li>Author: Lijie</li>
 *          <li>Date: 2017年3月8日</li>
 *          <li>Version: 1.0</li>
 *          <li>Content: create</li>
 *
 */
public class KafkaUtils {
	private static final Logger logger = LoggerFactory.getLogger(KafkaUtils.class);
	private static KafkaProducer<String, String> pro;
	private static String zkConf = null;
	static {
		Properties props = new Properties();
		props.put("bootstrap.servers", KafkaProperties.KAFKA_SERVER_URL + ":" + KafkaProperties.KAFKA_SERVER_PORT);
		props.put("client.id", "MyGroup");
		props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		pro = new KafkaProducer<>(props);
	}

	/**
	 * 
	 * @param topic
	 *            kafka主题
	 * @param value
	 *            kafka消息
	 */
	public static void sendMessage(String topic, String value) {
		ProducerRecord<String, String> pr = new ProducerRecord<String, String>(topic, value);

		pro.send(pr);
	}

	/**
	 * 
	 * @param topic
	 *            kafka主题
	 * @param key
	 *            messageId
	 * @param value
	 *            kafka消息
	 */
	public static void sendMessageAndUUId(String topic, String key, String value) {
		ProducerRecord<String, String> pr = new ProducerRecord<String, String>(topic, key, value);
		pro.send(pr);
	}

	/**
	 * 创建
	 * 
	 * @param partitions
	 *            --partitions参数
	 * @param topicName
	 *            主题名称
	 * @param replicationFactor
	 *            --replication-factor
	 */
	public static void createTopic(int partitions, String topicName, int replicationFactor) {
		String[] options = new String[] { "--create", "--zookeeper", zkConf, "--partitions", partitions + "", "--topic",
				topicName, "--replication-factor", replicationFactor + "" };
		execute(options);
	}

	/**
	 * 列出所有的主题
	 */
	public static void listTopic() {
		String[] options = new String[] { "--list", "--zookeeper", zkConf, };
		execute(options);
	}

	/**
	 * 列出所有的主题
	 */
	public static void getTopic(String topicName) {
		String[] options = new String[] { "--describe", "--zookeeper", zkConf, "--topic", topicName, };
		execute(options);
	}

	/**
	 * 删除topic
	 * 
	 * @param topicName
	 */
	public static void deleteTopic(String topicName) {
		String[] options = new String[] { "--delete", "--zookeeper", zkConf, "--topic", topicName,
				"delete.topic.enable", "true" };
		execute(options);
	}

	/**
	 * 执行总入口
	 * 
	 * @param options
	 */
	public static void execute(String[] options) {
		try {
			TopicCommand.main(options);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		KafkaUtils.sendMessage("storm_demo", "aaaaa");
	}
}
