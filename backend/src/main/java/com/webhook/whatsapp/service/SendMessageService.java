package com.webhook.whatsapp.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.webhook.whatsapp.entity.ParseJSON;
import com.webhook.whatsapp.entity.Product;
import com.webhook.whatsapp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Optional;

import static com.webhook.whatsapp.util.Constant.*;

@Service
@RequiredArgsConstructor
public class SendMessageService {


    public final ConversationService conversationService;
    public final ProductRepository productRepository;
    public void sendMessage(ParseJSON requestBody) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        String phoneNumber = requestBody.getPhn();
        String replyMessage = requestBody.getBot();

        Message.creator(
                        new com.twilio.type.PhoneNumber(phoneNumber),
                        new com.twilio.type.PhoneNumber(TWILIO_NUMBER)
                        , replyMessage)
                .create();

            if(!requestBody.getProductIdImage().isEmpty()) {
                for(int i=0;i<requestBody.getProductIdImage().size();i++) {
                    Optional<Product> optionalProduct = productRepository.findById(requestBody.getProductIdImage().get(i));
                    if(optionalProduct.isPresent()){
                        if(optionalProduct.get().getUrl()!=null) {
                            Message.creator(
                                            new com.twilio.type.PhoneNumber(phoneNumber),
                                            new com.twilio.type.PhoneNumber(TWILIO_NUMBER)
                                            , optionalProduct.get().getProduct()).setMediaUrl(URI.create(optionalProduct.get().getUrl()))
                                    .create();
                        }
                        else {
                            Message.creator(
                                            new com.twilio.type.PhoneNumber(phoneNumber),
                                            new com.twilio.type.PhoneNumber(TWILIO_NUMBER)
                                            , optionalProduct.get().getProduct()).setMediaUrl(URI.create(DEFAULT_IMAGE))
                                    .create();
                        }
                    }
                }

            }
        conversationService.saveConversation(requestBody);

    }

}
