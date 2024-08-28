package com.example.backend.dto;

import com.example.backend.model.ChatEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChatDTO {
    private String chatId;
    private String sent;
    private String received;
    private Long landmarkId;
    private String tagId;
    private String categoryId;

    public ChatDTO(ChatEntity entity) {
        this.chatId = entity.getChatId();
        this.sent = entity.getSent();
        this.received = entity.getReceived();
        this.landmarkId = entity.getLandmarkId();
        this.tagId = entity.getTagId();
        this.categoryId = entity.getCategoryId();
    }

    public static ChatEntity toEntity(final ChatDTO dto) {
        return ChatEntity.builder()
                .chatId(dto.getChatId())
                .sent(dto.getSent())
                .received(dto.getReceived())
                .landmarkId(dto.getLandmarkId())
                .tagId(dto.getTagId())
                .categoryId(dto.getCategoryId())
                .build();
    }
}

