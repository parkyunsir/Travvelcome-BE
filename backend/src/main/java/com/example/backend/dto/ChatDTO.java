package com.example.backend.dto;

import com.example.backend.model.ChatEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChatDTO {
    private String chatId;
    private String sent; // 보낸 message
    private String received;
    private Long landmarkId;
    private LocalDateTime date;
    private Long userId;

    public ChatDTO(ChatEntity entity) {
        this.chatId = entity.getChatId();

        this.sent = entity.getSent();
        this.received = entity.getReceived();

        this.landmarkId = entity.getLandmarkId();
        this.date = entity.getDate();
        this.userId = entity.getUserId();
    }

    public ChatDTO(String sent, String received, Long landmarkId, String chatId, Long userId) {
        this.sent = sent;
        this.received = received;
        this.landmarkId = landmarkId;
        this.chatId = chatId;
        this.userId = userId;
    }

    public static ChatEntity toEntity(final ChatDTO dto) {
        return ChatEntity.builder()
                .chatId(dto.getChatId())
                .sent(dto.getSent())
                .received(dto.getReceived())
                .landmarkId(dto.getLandmarkId())
                .date(dto.getDate())
                .userId(dto.getUserId())
                .build();
    }
}

