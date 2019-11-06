package com.github.zamirarif.kafka.streams;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;

import com.github.zamirarif.kafka.model.MigrationCandidateEvent;
import com.github.zamirarif.kafka.model.StatusConsolidationEvent;
import com.github.zamirarif.kafka.model.StatusForwardEvent;
import com.github.zamirarif.kafka.serde.JsonDeserializer;
import com.github.zamirarif.kafka.serde.JsonSerializer;

public class EventConsolidationApp {

    public static void main(String[] args) {
    	/**
         *  Value Serializer
         *  
         *  **/
    	JsonSerializer<MigrationCandidateEvent> migrationCandidateEventSerializer = new JsonSerializer<MigrationCandidateEvent>();
        JsonDeserializer<MigrationCandidateEvent> migrationCandidateEventDeserializer = new JsonDeserializer<>(MigrationCandidateEvent.class);
        /**
         *  Value Serializer
         *  
         *  **/
        JsonDeserializer<StatusConsolidationEvent> statusConsolidationDeserializer = new JsonDeserializer<>(StatusConsolidationEvent.class);
        JsonSerializer<StatusConsolidationEvent> statusConsolidationSerializer = new JsonSerializer<StatusConsolidationEvent>();
        
        /**
         *  Value Serializer
         *  
         *  **/
        JsonDeserializer<StatusForwardEvent> statusForwardDeserializer = new JsonDeserializer<>(StatusForwardEvent.class);
        JsonSerializer<StatusForwardEvent> statusForwardSerializer = new JsonSerializer<StatusForwardEvent>();
        
        /**
         *  Key Serializer
         *  
         *  **/
        StringSerializer stringSerializer = new StringSerializer();
        StringDeserializer stringDeserializer = new StringDeserializer();

        
        Topology builder = new Topology();
        /**
         * Pass all your custom setting required by changlog topic.  
         */
        Map<String, String> changlogConfig = new HashMap<String,String>();
        
		StoreBuilder<KeyValueStore<String, StatusConsolidationEvent>> statusConsolidationStoreSupplier = Stores.keyValueStoreBuilder(
        		  Stores.persistentKeyValueStore("status-consolidation"),
        		    Serdes.String(),
        		    Serdes.serdeFrom(statusConsolidationSerializer,statusConsolidationDeserializer))
        		  .withLoggingEnabled(changlogConfig); // enable changelogging, with custom changelog settings.
        
       
        builder.addSource("migration-candidate", stringDeserializer, migrationCandidateEventDeserializer, "migration-candidate-topic")
        .addProcessor("processor", EventConsolidationProcessor::new, "migration-candidate")
        .addStateStore(statusConsolidationStoreSupplier, "processor")
        .addSink("sink-original", "migration-candidate-out-topic", stringSerializer,migrationCandidateEventSerializer,"migration-candidate")
        .addSink("sink-status", "status-consolidation-topic", stringSerializer, statusForwardSerializer, "processor");
        
        KafkaStreams streams = new KafkaStreams(builder, getProperties());
        
        streams.cleanUp();
        streams.start();

        // print the processor topology
        System.out.println(builder.describe());

        // shutdown hook to correctly close the streams application
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }

   
    /**
     * Kafka configuration for the application. 
     * 
     * @return
     */
    private static Properties getProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "stateful-processor");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, 1);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", JsonDeserializer.class.getName());
        return props;
    }
}


