package com.github.zamirarif.kafka.streams;

import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.AbstractProcessor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;

import com.github.zamirarif.kafka.model.MigrationCandidateEvent;
import com.github.zamirarif.kafka.model.StatusConsolidationEvent;
import com.github.zamirarif.kafka.model.StatusForwardEvent;

/**
 * test
 * @author zamir.arif
 *
 */
public class EventConsolidationProcessor extends AbstractProcessor<String, MigrationCandidateEvent> {

    private KeyValueStore<String, StatusConsolidationEvent> statusStore;
    private ProcessorContext context;
    private StatusForwardEvent statusForwardEvent = new StatusForwardEvent();

    public void process(String key, MigrationCandidateEvent migrationCandidateEvent) {
        String migrationCandidateId = migrationCandidateEvent.getMigrationCandidateNumber();
       
        StatusConsolidationEvent statusConsolidationEvent = null;
        /** create or update the record in stateStore [RockDB] **/
         
        if(migrationCandidateId!=null){
        	statusConsolidationEvent = statusStore.get(migrationCandidateId);
       }
        
        if (statusConsolidationEvent == null ) {
        	statusConsolidationEvent = ProcessingUtil.createRecord(migrationCandidateEvent);
        	statusStore.put(statusConsolidationEvent.getMigrationCandidateNumber(), statusConsolidationEvent);
        } else {
        	statusConsolidationEvent = ProcessingUtil.update(statusConsolidationEvent, migrationCandidateEvent);
        	statusStore.put(statusConsolidationEvent.getMigrationCandidateNumber(), statusConsolidationEvent);
        }
        
        /** get the data from state store and forward If condotion satifies then delete the data in rockdb **/
        
        // retrieve the key-value store named "status-consolidation"
    	//statusStore = (KeyValueStore) context.getStateStore("status-consolidation");
    	KeyValueIterator<String, StatusConsolidationEvent> iter = this.statusStore.all();
        
        
    	while (iter.hasNext()) {
            KeyValue<String, StatusConsolidationEvent> entry = iter.next();
           // statusConsolidationEvent = entry.value;
            List<String> migrationStatus =  new ArrayList<String>();
    		int posstiveCounter = 0;
    		if(entry.value != null){
    			migrationStatus = entry.value.getMigrationStatus();
    			for (String status : migrationStatus) {
    				if(status.equalsIgnoreCase("final bill produced")){
    					posstiveCounter++;
    				}else if(status.equalsIgnoreCase("product ready")){
    					posstiveCounter++;
    				}else if(status.equalsIgnoreCase("registration complete")){
    					posstiveCounter++;
    				}
    			}
    		}
    		
    		System.out.println("posstiveCounter value : "+posstiveCounter);
    		if(posstiveCounter==3){
    			System.out.println("MigrationCandidateNumber : "+entry.value.getMigrationCandidateNumber());
    			System.out.println("MigrationStatus : "+entry.value.getMigrationStatus());
    			System.out.println("MeterPointReference : "+entry.value.getMeterPointReference());
    			statusForwardEvent.setMeterPointReference(entry.value.getMeterPointReference());
    			statusForwardEvent.setMigrationCandidateNumber(entry.value.getMigrationCandidateNumber());
    			statusForwardEvent.setMigrationStatus("Ready for extract 2");
    			//this.statusStore.put(entry.key, null);
    			this.statusStore.delete(entry.key);
    			key= entry.key;
    			context.forward(key, statusForwardEvent);
    			this.context.commit();
    		}
            
    		
        }
        iter.close();
        
        
        
        this.context.commit();
    }


    @Override
    @SuppressWarnings({ "unchecked", "deprecation" })
    public void init(ProcessorContext context) {
    	
    	// keep the processor context locally because we need it in punctuate() and commit()
    	this.context = context;
    	
    	// retrieve the key-value store named "Counts"
    	statusStore = (KeyValueStore) context.getStateStore("status-consolidation");
    	
    	// schedule a punctuate() method every second based on event-time
        this.context.schedule(10000, PunctuationType.STREAM_TIME, (timestamp) -> {
        	KeyValueIterator<String, StatusConsolidationEvent> iter = this.statusStore.all();
            
        	while (iter.hasNext()) {
                KeyValue<String, StatusConsolidationEvent> entry = iter.next();
              //  context.forward(entry.key, entry.value);
                System.out.println("Data in state store : "+ entry.key + " " + entry.value);
            }
            iter.close();
         // commit the current processing progress
            context.commit();
        });
        

    }

   
  

}