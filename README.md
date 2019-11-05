# multiple-event-consolidation


## Stateful operation using Processor API of Kafka Stream 

## Preparation to run the project

### Please create the following topics in your local/remote Kafka Cluster.

```
kafka-topics --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 2 --topic migration-candidate-topic

kafka-topics --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 2 --topic migration-candidate-out-topic

kafka-topics --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 2 --topic status-consolidation-topic --config cleanup.policy=compact

```

### Start following producer/consumers in 2 different windows for input/output.

kafka-topics --list --zookeeper zookeeper:2181
```
kafka-console-producer \
--broker-list broker:29092 \
  --topic migration-candidate-topic \
  --property "parse.key=true" \
  --property "key.separator=:"
54545456:{"migrationCandidateNumber": 54545456,"meterPointReference": 115978697867,"migrationStatus":"Registration Complete", "statusDate": "19102019","statusTime": "100000","reason": ""}


kafka-console-consumer --bootstrap-server broker:29092 \
--topic status-consolidation-topic \
--from-beginning \
--formatter kafka.tools.DefaultMessageFormatter \
--property print.key=true \
--property print.value=true \
--property   key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
--property value.deserializer=org.apache.kafka.common.serialization.StringDeserializer


kafka-console-consumer --bootstrap-server broker:29092 \
--topic migration-candidate-out-topic \
--from-beginning \
--formatter kafka.tools.DefaultMessageFormatter \
--property print.key=true \
--property print.value=true \
--property   key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
--property value.deserializer=org.apache.kafka.common.serialization.StringDeserializer

```
