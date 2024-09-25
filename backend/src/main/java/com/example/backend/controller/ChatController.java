package com.example.backend.controller;

import com.example.backend.apiPayload.ApiResponse;
import com.example.backend.dto.ChatDTO;
import com.example.backend.model.ChatEntity;
import com.example.backend.model.Landmark;
import com.example.backend.repository.LandmarkRepository;
import com.example.backend.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @Autowired
    private LandmarkRepository landmarkRepository;

    // 질문하기 (답변도 이 때 출력됨)
    @PostMapping
    public ResponseEntity<?> createResponse(@RequestParam("landmarkId") Long landmarkId, @RequestBody ChatDTO dto) {
        try {
            // landmark Id 같은지 비교
            // - 오류 처리
            if (!landmarkId.equals(dto.getLandmarkId())) {
                String error = "Landmark ID in path and request body do not match.";
                ApiResponse<ChatDTO> response = ApiResponse.onFailure("400", error, null);
                return ResponseEntity.badRequest().body(response);
            }
            
            // - 정상 처리
            ChatEntity entity = ChatDTO.toEntity(dto);
            entity.setChatId(null);
            entity.setReceived(null);
            entity.setLandmarkId(landmarkId);
            ChatEntity savedEntity = chatService.createResponse(entity);

            ChatDTO savedDto = new ChatDTO(savedEntity);
            return ResponseEntity.ok().body(savedDto);
        } catch(Exception e) {
            String error = e.getMessage();
            ApiResponse<ChatDTO> response = ApiResponse.onFailure("400", error, null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 대화 - 대화 내역 보여주기
    @GetMapping
    public ResponseEntity<?> showLandmarkChat(@RequestParam("landmarkId") Long landmarkId, @RequestBody ChatDTO dto) {
        List<ChatEntity> entities = chatService.showChat(landmarkId);
        List<ChatDTO> dtos = entities.stream().map(ChatDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(dtos);
    }

    // 대화 - 대화 검색하기
    @GetMapping("/search")
    public ResponseEntity<?> searchLandmarkChatting(@RequestParam("text") String text) {
        List<ChatEntity> entities = chatService.searchChatting(text);

        List<ChatDTO> dtos = entities.stream().map(ChatDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(dtos);
    }


    // 목록 - landmark list
    @GetMapping("/list")
    public ResponseEntity<?> showChatList(){
//      landmarkid로 ChatEntity의 receive를 가져와서 date순으로 정렬
        List<ChatEntity> entities =  chatService.showList();

        return convertEntityToDto(entities);
    }

    // 목록 - landmark 검색
    @GetMapping("/list/search") // api/question/search?childId={}&output={}
    public ResponseEntity<?> searchLandmarkList(@RequestParam("title") String title) {
        List<ChatEntity> entities = chatService.searchLandmark(title);

        return convertEntityToDto(entities);
    }

    // entities -> dto -> dto의 landmarId, received, date 반환
    public ResponseEntity<?> convertEntityToDto(List<ChatEntity> entities) {

        if (entities.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<ChatDTO> dtos = entities.stream().map(ChatDTO::new).collect(Collectors.toList());

        // landmarkId, received, date 3개만 반환.
        List<Map<String, Object>> chatAllList = dtos.stream()
                .map(dto -> {
                    Map<String, Object> map = new HashMap<>(); // Map<String, Object>를 생성
                    map.put("date", dto.getDate());
                    map.put("received", dto.getReceived());
                    map.put("landmarkId", dto.getLandmarkId());
                    return map;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(chatAllList);
    }
}