# multiple-event-consolidation

Following StackOverflow link explains the problem 

https://stackoverflow.com/questions/58507380/event-consolidation-when-there-is-no-defined-time-window-for-event-arrival?noredirect=1#comment103442970_58507380

## Stateful operation using Processor API of Kafka Stream 
Following is the Processor Topology which we used to solve the problem describe in stackOverflow link. 

![](/custom-processor-topology.png)

For the basics of processor API. Please follow the confluent's link mentioned below:
https://docs.confluent.io/current/streams/developer-guide/processor-api.html

## Preparation to run the project

Please create the following topics in your local/remote Kafka Cluster.

```
kafka-topics --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 2 --topic migration-candidate-topic

kafka-topics --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 2 --topic migration-candidate-out-topic

kafka-topics --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 2 --topic status-consolidation-topic --config cleanup.policy=compact

```

Start the producer as described below:


```
kafka-console-producer \
--broker-list broker:29092 \
  --topic migration-candidate-topic \
  --property "parse.key=true" \
  --property "key.separator=:"
54545456:{"migrationCandidateNumber": 54545456,"meterPointReference": 115978697867,"migrationStatus":"Registration Complete", "statusDate": "19102019","statusTime": "100000","reason": ""}
```
Start the consumer as described below to display final processed output
```
kafka-console-consumer --bootstrap-server broker:29092 \
--topic status-consolidation-topic \
--from-beginning \
--formatter kafka.tools.DefaultMessageFormatter \
--property print.key=true \
--property print.value=true \
--property   key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
--property value.deserializer=org.apache.kafka.common.serialization.StringDeserializer
```

Start the consumer as described below to display the original event out topic for further processing. 
```
kafka-console-consumer --bootstrap-server broker:29092 \
--topic migration-candidate-out-topic \
--from-beginning \
--formatter kafka.tools.DefaultMessageFormatter \
--property print.key=true \
--property print.value=true \
--property   key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
--property value.deserializer=org.apache.kafka.common.serialization.StringDeserializer

```

Once you have created the topics. Please run the code and start producing the message. 


