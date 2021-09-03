package com.example.lamdasqs.function;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class LambdaRequestFunction implements Function<Object, String> {


    private static final Logger LOGGER = LoggerFactory.getLogger(LambdaRequestFunction.class);


//    @EventListener(ApplicationReadyEvent.class)
//    public void test() {
//        List<SQSEvent.SQSMessage> body = new ArrayList<>();
//        SQSEvent.SQSMessage message = new SQSEvent.SQSMessage();
//        message.setBody("hi");
//        body.add(message);
//        SQSEvent event = new SQSEvent();
//        event.setRecords(body);
//        apply(event);
//    }

    @Override
    public String apply(Object sqsEventObj) {

        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        LOGGER.info("message received");

        try {
            String temp = mapper.writeValueAsString(sqsEventObj);
            SQSEvent sqsEvent = mapper.readValue(temp,SQSEvent.class);
            System.out.println(sqsEvent);
            for(SQSEvent.SQSMessage sqsMessage : sqsEvent.getRecords()){
                System.out.println(sqsMessage.getBody());
            }
        } catch (Exception e) {
            LOGGER.info("unable to process request");
            e.printStackTrace();
        }
        return "";
    }
}
