package com.example.backend.controller;

import com.example.backend.apiPayload.ApiResponse;
import com.example.backend.dto.ChatDTO;
import com.example.backend.dto.KakaoDto;
import com.example.backend.model.ChatEntity;
import com.example.backend.model.Interest;
import com.example.backend.model.Landmark;
import com.example.backend.model.enums.Category;
import com.example.backend.repository.LandmarkRepository;
import com.example.backend.service.ChatService;
import com.example.backend.service.InterestService;
import com.example.backend.service.KakaoService;
import com.example.backend.service.LandmarkService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;


    // userId 추가하기
    // 관심사로 질문할 수 있는 거 추가하기 (과학에 대해 알려줘!)

    @Autowired
    private LandmarkRepository landmarkRepository;

    @Autowired
    private KakaoService kakaoService;

    @Autowired
    private LandmarkService landmarkService;

    @Autowired
    private InterestService interestService;

    // 대화 - 질문하기 , 대화하기
    @Operation(summary = "챗봇 대화 API", description = "챗봇과 대화할 수 있는 API입니다. sent: 내가 보낸 메세지, received: gpt가 자동 생성하는 메세지입니다." +
            " body에는 sent와 landmarkId만 넣어서 execute 해주세요. landmarkId, userId(token) RequestParam 입니다!")
    @PostMapping // /chat?landmarkId={}
    public ResponseEntity<?> createResponse(@RequestParam("landmarkId") Long landmarkId, @RequestParam("userId") String userId, @RequestBody ChatDTO dto) {
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

            // userId
            KakaoDto Kdto = kakaoService.getUserInfo(userId);
            Long id = Kdto.getId();
            entity.setUserId(null);

            // 대화
            ChatEntity savedEntity = chatService.createResponse(entity, id);

            ChatDTO savedDto = new ChatDTO(savedEntity);
            return ResponseEntity.ok().body(savedDto);
        } catch(Exception e) {
            String error = e.getMessage();
            ApiResponse<ChatDTO> response = ApiResponse.onFailure("400", error, null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    // [/chatting/history]
    // 대화 - 대화 내역 보여주기
    @Operation(summary = "[/chatting/history] 챗봇 history API", description = "챗봇과 대화 내역을 보여주는 API입니다. landmarkId RequestParam 입니다!")
    @GetMapping // /chat?landmarkId={}
    public ResponseEntity<?> showLandmarkChat(@RequestParam("landmarkId") Long landmarkId, @RequestParam("userId") String userId) {

        Landmark landmark = landmarkRepository.findById(landmarkId)
                .orElseThrow(() -> new IllegalArgumentException("Landmark not found"));

        KakaoDto Kdto = kakaoService.getUserInfo(userId);
        Long id = Kdto.getId();

        List<ChatEntity> entities = chatService.showChat(landmarkId, id);
        List<ChatDTO> dtos = entities.stream()
                .map(ChatDTO::new)
                .toList();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("landmarkTitle", landmark.getTitle()); // landmark Title
        response.put("landmarkImage",landmark.getImageUrl()); // landmark ImageUrl
        response.put("landmarkCategory", landmark.getCategories());
        response.put("chatList", dtos);

        return ResponseEntity.ok().body(response);
    }

    // [/chatting/history]
    // 대화 - 대화 검색하기
    @Operation(summary = "[/chatting/history] 챗봇 history 검색 API", description = "챗봇과 대화 내역을 검색할 수 있는 API입니다. landmarkId RequestParam, 검색어(text) RequestParam 입니다!")
    @GetMapping("/search") // /chat/search?landmarkId={}&text={}
    public ResponseEntity<?> searchLandmarkChatting(@RequestParam("landmarkId") Long landmarkId, @RequestParam("text") String text, @RequestParam("userId") String userId) {

        Landmark landmark = landmarkRepository.findById(landmarkId)
                .orElseThrow(() -> new IllegalArgumentException("Landmark not found"));

        KakaoDto Kdto = kakaoService.getUserInfo(userId);
        Long id = Kdto.getId();

        List<ChatEntity> entities = chatService.searchChatting(landmarkId, text, id);

        List<ChatDTO> dtos = entities.stream()
                .map(ChatDTO::new)
                .toList();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("landmarkTitle", landmark.getTitle()); // landmark Title
        response.put("landmarkImage",landmark.getImageUrl()); // landmark ImageUrl
        response.put("landmarkCategory", landmark.getCategories());
        response.put("chatList", dtos);

        return ResponseEntity.ok().body(response);
    }

    // [/chatting/history]
    // 대화 - 대화 검색하기 (단어만 추출)
    @Operation(summary = "[/chatting/history] 챗봇 history 검색 - 단어 추출 API", description = "챗봇과 대화 내역을 검색하면, 검색된 단어만 추출하는 API입니다." +
            " (ex- 나는 학교에 갑니다. text에 '학교' 입력 시 '학교에' return) landmarkId RequestParam, 검색어(text) RequestParam 입니다!")
    @GetMapping("/search/word") // /chat/search/word?landmarkId={}&text={}
    public ResponseEntity<?> searchLandmarkChattingWord(@RequestParam("landmarkId") Long landmarkId, @RequestParam("text") String text, @RequestParam("userId") String userId) {

        Landmark landmark = landmarkRepository.findById(landmarkId)
                .orElseThrow(() -> new IllegalArgumentException("Landmark not found"));

        KakaoDto Kdto = kakaoService.getUserInfo(userId);
        Long id = Kdto.getId();

        List<ChatDTO> dtos = chatService.searchChattingWord(landmarkId, text, id);

        return ResponseEntity.ok().body(dtos);
    }


    @Operation(summary = "[주제 pick (1) ] 관심사 질문 추천 API", description = "관광지의 category와 내가 저장한 관심사 중 일치하는 카테고리만 보여주는 API입니다!" )
    // 주제 추천
    @GetMapping("/topic")
    public ResponseEntity<?> compareLandmarkCategories(@RequestParam("landmarkId") Long landmarkId, @RequestParam("userId") String userId) throws IOException {

        KakaoDto Kdto = kakaoService.getUserInfo(userId);
        Long id = Kdto.getId();

        // 현재 관광지에서 Category를 추출
        Landmark landmark = landmarkRepository.findById(landmarkId)
                .orElseThrow(() -> new IllegalArgumentException("Landmark not found"));
        List<String> landmarkCategories = landmark.getCategories().stream()
                .map(Category::toString) // Category를 String으로 변환
                .toList();

        // 나의 관심사 목록에서 Category를 추출
        List<Interest> interests = interestService.getAllInterest(id);
        List<String> interestCategories = interests.stream()
                .map(interest -> interest.getCategory().toString()) // Category를 String으로 변환
                .toList();

        // 서비스 호출하여 일치하는 카테고리 반환
        List<String> topics = landmarkService.findCategories(landmarkCategories, interestCategories);

        return ResponseEntity.ok(topics); // 일치하는 카테고리 리스트 반환
    }

    @Operation(summary = "[주제 pick (2)] 관심사 질문 대화 API", description = "[주제 pick (1) ]을 먼저 진행하고 해주세요. 관심사 질문 추천 API에서 선택된 selectedTopic으로 chatbot과 대화할 수 있는 API입니다! (선택하는 과정은 프론트에서 진행해주세요!)")
    @PostMapping("/topic")
    public ResponseEntity<?> autoChatting(@RequestParam("landmarkId") Long landmarkId, @RequestParam("userId") String userId,
                                          @RequestParam("selectedTopic") String selectedTopic, @RequestBody ChatDTO dto) throws IOException {

        KakaoDto Kdto = kakaoService.getUserInfo(userId);
        Long id = Kdto.getId();

        ChatEntity entity = ChatDTO.toEntity(dto);
        entity.setChatId(null);
        entity.setReceived(null);
        entity.setLandmarkId(landmarkId);

        // 대화
        // topics의 각 항목에 대해 createTopicResponse 호출
        ChatEntity savedEntity = chatService.createTopicResponse(entity, id, selectedTopic);

        ChatDTO savedDto = new ChatDTO(savedEntity);

        return ResponseEntity.ok().body(savedDto);
    }

    // [/chatting]
    // 목록 - (최신순) landmark 대화 list
    @Operation(summary = "[/chatting] 챗봇 목록 API", description = "대화한 챗봇 목록을 보여주는 API입니다!")
    @GetMapping("/list") // /chat/list
    public ResponseEntity<?> showChatList(@RequestParam("userId") String userId){

        KakaoDto Kdto = kakaoService.getUserInfo(userId);
        Long id = Kdto.getId();

        List<Map<String, Object>> entities =  chatService.showList(id);

        return convertEntityToDto(entities);
    }

    // [/chatting]
    // 목록 - landmark 검색
    @Operation(summary = "[/chatting] 챗봇 목록 검색 API", description = "대화한 챗봇 목록에서 관광지를 검색할 수 있는 API입니다! 관광지 이름(title) RequestParam 입니다!")
    @GetMapping("/list/search") // /chat/list/search?title={}
    public ResponseEntity<?> searchLandmarkList(@RequestParam("title") String title, @RequestParam("userId") String userId) {

        KakaoDto Kdto = kakaoService.getUserInfo(userId);
        Long id = Kdto.getId();

        List<Map<String, Object>> entities = chatService.searchLandmark(title, id);

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