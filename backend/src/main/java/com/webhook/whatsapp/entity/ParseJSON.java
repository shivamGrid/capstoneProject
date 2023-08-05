package com.webhook.whatsapp.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

import java.util.List;

@Getter
@Setter
public class ParseJSON {

    private String phn;
    private String startTime;
    private String human;
    private String bot;

    @Transient
    private List<String> productIdImage;

    public ParseJSON(String phn,String startTime, String human, String bot, List<String> productIdImage) {
        this.phn = phn;
        this.startTime = startTime;
        this.human = human;
        this.bot = bot;
        this.productIdImage = productIdImage;
    }

}
