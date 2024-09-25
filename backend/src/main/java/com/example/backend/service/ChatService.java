package com.example.backend.service;

import com.example.backend.model.ChatEntity;
import com.example.backend.model.Landmark;
import com.example.backend.repository.ChatRepository;
import com.example.backend.repository.LandmarkRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import okhttp3.*;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChatService {
    @Autowired
    private ChatRepository chatRepository;
    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.model.id}")
    private String modelId;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private LandmarkRepository landmarkRepository;


    // 챗GPT 답변 생성
    public ChatEntity createResponse(ChatEntity entity) throws IOException {
        validate(entity);
        String response = getCompletion(entity.getSent());
        entity.setReceived(response);
        entity.setDate(LocalDateTime.now());
        return chatRepository.save(entity);
    }

    // 이전 대화 내용 기억하기
    public String getCompletion(String prompt) throws IOException {
        List<ChatEntity> entities = chatRepository.findAll();
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append("너는 한국인이야. 너는 알려주는 것을 좋아해. " +
                "너는 긍정적이고, 낙천적이고, 박학다식해. 너는 꼼꼼하고, 친절해." +
                "user는 너에게 '제주도에 있는 다양한 장소 중 자연 혹은 문화 혹은 역사에 관련된 궁금한 장소'를 물어볼 거야.");

        // 이전 질문, 답변 받아와서 기억하기
        for (ChatEntity entity : entities) {
            String request = entity.getSent();
            String response = entity.getReceived().replace("\n", "");

            contentBuilder.append("사용자가 '")
                    .append(request)
                    .append("', 너가 '")
                    .append(response)
                    .append("', ");
        }
        // 마지막 쉼표와 공백 제거
        int length = contentBuilder.length();
        if (length > 2) {
            contentBuilder.setLength(length - 2); // 마지막 두 문자 제거 (', ')
        }

        JSONObject system = new JSONObject();
        JSONObject user = new JSONObject();
        system.put("role", "system");
        system.put("content", contentBuilder.toString() + "라고 했어.");
        user.put("role", "user");
        user.put("content", prompt);

        JSONArray messagesArray = new JSONArray();
        messagesArray.add(system);
        messagesArray.add(user);

        JSONObject json = new JSONObject();
        json.put("model", modelId);
        json.put("messages", messagesArray);

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .post(body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        log.info(request.toString());
        try (Response response = client.newCall(request).execute()) {
            if(!response.isSuccessful() || response.body() == null) {
                throw new IOException("Unexpected : " + response);
            }
            String responseBody =  response.body().string(); //print(completion.choices[0].message)
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            return jsonNode
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();
        }
    }

    // 대화 - 1:1 대화 내역
    public List<ChatEntity> showChat(Long landmarkId) {
        return chatRepository.findByLandmarkId(landmarkId);
    }

    // 대화 - 대화 내역 검색하기
    public List<ChatEntity> searchChatting(final String text) {
        return chatRepository.findBySentContainingOrReceivedContaining(text, text);
    }

    // 목록 - 랜드마크 리스트
    public List<Map<String, Object>> showList() {
        // 모든 LandmarkId 가져오기
        List<Landmark> landmarks = landmarkRepository.findAll();

        return convert(landmarks);
    }

    // 목록 - 랜드마크 이름 검색하기
    public List<Map<String, Object>> searchLandmark(final String title) {
        List<Landmark> landmarks = landmarkRepository.findByTitleContaining(title);

        return convert(landmarks);
    }

    // List<Landmark>에서 id 추출 -> List<ChatEntity>로 변환
    public List<Map<String, Object>> convert(List<Landmark> landmarks) {

        // id & title
        Map<Long, String> landmarkTitle = landmarks.stream()
                .collect(Collectors.toMap(Landmark::getId, Landmark::getTitle)); // id와 title 가져오기

        // id만
        List<Long> landmarkIds = landmarks.stream()
                .map(Landmark::getId) // 각 LandmarkEntity의 id 추출
                .toList();

        // 이 id를 토대로 ChatEntity 반환하기.
        List<ChatEntity> chatEntities = chatRepository.findLatestChatByLandmarkIds(landmarkIds);

        return chatEntities.stream()
                .map(chatEntity -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("landmarkId", chatEntity.getLandmarkId());
                    map.put("landmarkTitle", landmarkTitle.get(chatEntity.getLandmarkId()));
                    map.put("received", chatEntity.getReceived());
                    map.put("date", chatEntity.getDate());
                    return map;
                })
                .collect(Collectors.toList());
    }

    // 유효성 검사
    public void validate(ChatEntity entity) {
        if (entity == null) {
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }
//        if (entity.getUserId() == null) {
//            log.warn("Unknown user.");
//            throw new RuntimeException("Unknown user.");
//        }
    }
}
