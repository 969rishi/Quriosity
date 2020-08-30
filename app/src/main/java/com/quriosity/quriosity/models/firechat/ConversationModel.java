package com.quriosity.quriosity.models.firechat;

import java.util.List;
import java.util.Map;

public class ConversationModel {
    public ConversationModel(String conversationID,
                             String conversationTitle,
                             List<String> conversationActors,
                             String createdby,
                             String createdon,
                             String type,
                             List<String> typingarray,
                             Map<String, String> usersNameMap,
                             String conversationIconUrl
    ) {
        this.conversationActors = conversationActors;
        this.conversationTitle = conversationTitle;
        this.createdby = createdby;
        this.createdon = createdon;
        this.type = type;
        this.conversationID = conversationID;
        this.typingarray = typingarray;
        this.usersNameMap = usersNameMap;
        this.conversationIconUrl = conversationIconUrl;
    }

    public ConversationModel() {
    }

    public List<String> getConversationActors() {
        return conversationActors;
    }

    public void setConversationActors(List<String> conversationActors) {
        this.conversationActors = conversationActors;
    }

    public String getConversationTitle() {
        return conversationTitle;
    }

    public void setConversationTitle(String conversationTitle) {
        this.conversationTitle = conversationTitle;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getCreatedon() {
        return createdon;
    }

    public void setCreatedon(String createdon) {
        this.createdon = createdon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConversationID() {
        return conversationID;
    }

    public void setConversationID(String conversationID) {
        this.conversationID = conversationID;
    }

    private List<String> conversationActors;

    public List<String> getTypingarray() {
        return typingarray;
    }

    public void setTypingarray(List<String> typingarray) {
        this.typingarray = typingarray;
    }

    private List<String> typingarray;
    private String conversationTitle;
    private String createdby;
    private String createdon;
    private String type;
    private String conversationID;
    private Map<String, String> usersNameMap;
    private String conversationIconUrl;
    private MessageModel latestMessage;

    public String getConversationIconUrl() {
        return conversationIconUrl;
    }

    public void setConversationIconUrl(String conversationIconUrl) {
        this.conversationIconUrl = conversationIconUrl;
    }


    public Map<String, String> getUsersNameMap() {
        return usersNameMap;
    }

    public void setUsersNameMap(Map<String, String> usersNameMap) {
        this.usersNameMap = usersNameMap;
    }

    public MessageModel getLatestMessage() {
        return latestMessage;
    }

    public void setLatestMessage(MessageModel latestMessage) {
        this.latestMessage = latestMessage;
    }
}
