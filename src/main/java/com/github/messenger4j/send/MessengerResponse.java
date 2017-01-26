package com.github.messenger4j.send;

import java.util.Objects;

/**
 * @author Max Grabenhorst
 * @since 0.6.0
 */
public final class MessengerResponse {

    private final String recipientId = null;
    private final String messageId = null;
    private final String attachmentId = null;

    public String getRecipientId() {
        return recipientId;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getAttachmentId() {
        return this.attachmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessengerResponse that = (MessengerResponse) o;
        return Objects.equals(recipientId, that.recipientId) &&
                Objects.equals(messageId, that.messageId) &&
                Objects.equals(attachmentId, that.attachmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipientId, messageId, attachmentId);
    }

    @Override
    public String toString() {
        return "MessengerResponse{" +
                "recipientId='" + recipientId + '\'' +
                ", messageId='" + messageId + '\'' +
                ", attachmentId='" + attachmentId + '\'' +
                '}';
    }
}