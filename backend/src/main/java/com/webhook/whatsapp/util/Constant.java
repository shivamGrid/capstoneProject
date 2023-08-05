package com.webhook.whatsapp.util;

public class Constant {
    //twilio authentication details
    public static final String ACCOUNT_SID = "AC636d5c041b9e7a9afefc8e860b58ef97";
    public static final String AUTH_TOKEN = "1edb31ba43cac430df5d39c447b5383c";
    public static final String TWILIO_NUMBER = "whatsapp:+14155238886";

    //send response to user for an error
    public static final String ERROR_RESPONSE = "Oops! \uD83E\uDD7A Something went wrong. \nPlease try again later.";

    //for mongodb
    public static final String MONGO_CLIENT = "mongodb://localhost:27017";
    public static final String DATABASE_NAME = "Conversation&Cart";
    public static final String ORDER_COLLECTION = "order";
    public static final String CONVERSATIONS_COLLECTION = "conversations";
    public static final String FEEDBACK_COLLECTION = "feedback";
    public static final String ORDER_TOTAL_FIELD = "total";

    //for order status
    public static final String PENDING_STATUS = "Pending";
    public static final String PROCESSING_STATUS = "Processing";
    public static final String DISPATCH_STATUS = "Dispatch";
    public static final String DELIVERED_STATUS = "Delivered";
    public static final String CANCELLED_STATUS = "Cancelled";

    //to keep in formatted decimal pattern
    public static final String DECIMAL_PATTERN = "#.00";

    // default image url
    public static final String DEFAULT_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/65/No-Image-Placeholder.svg/1200px-No-Image-Placeholder.svg.png";
}
