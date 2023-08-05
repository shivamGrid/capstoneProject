package com.webhook.whatsapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.webhook.whatsapp.util.Constant.*;

@Service
public class ReceiveMessageService {

    private WebClient webClient = WebClient.create();



    public ResponseEntity<String> receiveMessage(String requestBody) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        String value = "";
        Pattern regex = Pattern.compile("SmsMessageSid=(\\w+)");
        Matcher matcher = regex.matcher(requestBody);
        if (matcher.find()) {
            value = matcher.group(1);
        }
        Message message = Message.fetcher(value)
                .fetch();
        String messageBody = message.getBody();
        String contact = String.valueOf(message.getFrom());
        String startTime = String.valueOf(message.getDateCreated());
        // Create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();
        // Create a JSON object
        Object json = objectMapper.createObjectNode()
                .put("text", messageBody)
                .put("phone_number",contact)
                .put("start_time",startTime);
        //send details
        Mono<JSONObject> responseMono = webClient.post()
                .uri("https://290d-61-246-192-170.ngrok-free.app/webhook")
                .bodyValue(json)
                .retrieve()
                .bodyToMono(JSONObject.class);

        JSONObject response = responseMono.onErrorResume(ex -> Mono.empty()).block();

        if (response != null) {
            // Handle the successful response
            return ResponseEntity.ok("");
        } else {
            // Return an error message
            return ResponseEntity.ok(ERROR_RESPONSE);
        }
    }


}
