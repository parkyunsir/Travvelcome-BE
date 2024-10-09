package com.example.backend.controller;

import com.example.backend.apiPayload.ApiResponse;
import com.example.backend.dto.ChatDTO;
import com.example.backend.model.ChatEntity;
import com.example.backend.model.Landmark;
import com.example.backend.repository.LandmarkRepository;
import com.example.backend.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @Autowired
    private LandmarkRepository landmarkRepository;

    @Operation(summary = "챗봇 대화 API", description = "챗봇과 대화할 수 있는 API입니다. landmarkId RequestParam 입니다!")
    // 대화 - 질문하기 , 대화하기
    @PostMapping // /chat?landmarkId={}
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

    @Operation(summary = "[/chatting/history] 챗봇 history API", description = "챗봇과 대화 내역을 보여주는 API입니다. landmarkId RequestParam 입니다!")
    // [/chatting/history]
    // 대화 - 대화 내역 보여주기
    @GetMapping // /chat?landmarkId={}
    public ResponseEntity<?> showLandmarkChat(@RequestParam("landmarkId") Long landmarkId) {

        Landmark landmark = landmarkRepository.findById(landmarkId)
                .orElseThrow(() -> new IllegalArgumentException("Landmark not found"));

        List<ChatEntity> entities = chatService.showChat(landmarkId);
        List<ChatDTO> dtos = entities.stream()
                .map(ChatDTO::new)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("landmarkTitle", landmark.getTitle()); // landmark Title
        response.put("landmarkImage",landmark.getImageUrl()); // landmark ImageUrl
        response.put("chatList", dtos);

        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "[/chatting/history] 챗봇 history 검색 API", description = "챗봇과 대화 내역을 검색할 수 있는 API입니다. landmarkId RequestParam, 검색어(text) RequestParam 입니다!")
    // [/chatting/history]
    // 대화 - 대화 검색하기
    @GetMapping("/search") // /chat/search?landmarkId={}&text={}
    public ResponseEntity<?> searchLandmarkChatting(@RequestParam("landmarkId") Long landmarkId, @RequestParam("text") String text) {
        List<ChatEntity> entities = chatService.searchChatting(text);

        List<ChatDTO> dtos = entities.stream().map(ChatDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(dtos);
    }

    @Operation(summary = "[/chatting] 챗봇 목록 API", description = "대화한 챗봇 목록을 보여주는 API입니다!")
    // [/chatting]
    // 목록 - (최신순) landmark 대화 list
    @GetMapping("/list") // /chat/list
    public ResponseEntity<?> showChatList(){
        List<Map<String, Object>> entities =  chatService.showList();

        return convertEntityToDto(entities);
    }

    @Operation(summary = "[/chatting] 챗봇 목록 검색 API", description = "대화한 챗봇 목록에서 관광지를 검색할 수 있는 API입니다! 랜드마크이름(title) RequestParam 입니다!")
    // [/chatting]
    // 목록 - landmark 검색
    @GetMapping("/list/search") // /chat/list/search?title={}
    public ResponseEntity<?> searchLandmarkList(@RequestParam("title") String title) {
        List<Map<String, Object>> entities = chatService.searchLandmark(title);

        return convertEntityToDto(entities);
    }

    // entities -> dto -> dto의 landmarId, received, date, landmarkTitle 반환
    public ResponseEntity<?> convertEntityToDto(List<Map<String, Object>> entities) {

        if (entities.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(entities);
    }
}