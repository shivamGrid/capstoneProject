package com.webhook.whatsapp.service;

import com.webhook.whatsapp.repository.ConversationRepository;
import com.webhook.whatsapp.dto.VisitorCountDto;
import com.webhook.whatsapp.entity.Conversation;
import com.webhook.whatsapp.entity.ConversationList;
import com.webhook.whatsapp.entity.ParseJSON;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationService {
    private final ConversationRepository conversationRepository;

    public Page<Conversation> getConversations(int page, int size, String fieldName,String sortConvertsation) {
        Sort.Direction direction = Sort.Direction.ASC;

        if (sortConvertsation.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }

        Sort sort = Sort.by(direction, fieldName);
        Pageable pageable = PageRequest.of(page, size, sort);
        return conversationRepository.findAll(pageable);
    }

    public void saveConversation(ParseJSON requestBody) {
        Conversation conversation = conversationRepository.findByPhoneNumber(requestBody.getPhn().substring(9));
        if (conversation == null) {
            conversation = new Conversation();
            conversation.setPhoneNumber(requestBody.getPhn().substring(9));

            String startTime = requestBody.getStartTime();
            Instant instant = Instant.parse(startTime);
            ZoneId localZoneId = ZoneId.systemDefault();
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, localZoneId);
            String[] splitTimestamp = localDateTime.toString().split("T");
            String date = splitTimestamp[0];
            String timeAndZone = splitTimestamp[1];

            conversation.setStartDate(date);
            conversation.setStartTime(timeAndZone);
        }
        ConversationList conversationList = new ConversationList();
        conversation.setLastUpdated(LocalDateTime.now());
        String endDateTime = conversation.getLastUpdated().toString();
        String[] splitTimestamp1 = endDateTime.split("T");
        String endDate = splitTimestamp1[0];
        String endTimeAndZone = splitTimestamp1[1];
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSS");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime time = LocalTime.parse(endTimeAndZone, inputFormatter);
        String formattedTime = time.format(outputFormatter);

        conversation.setEndDate(endDate);
        conversation.setEndTime(formattedTime);

        conversationList.setHuman(requestBody.getHuman());
        conversationList.setBot(requestBody.getBot());
        conversation.getMessages().add(conversationList);
        conversationRepository.save(conversation);
    }

    public List<VisitorCountDto> countSimilarDates() {
        return conversationRepository.countSimilarDates();
    }

}