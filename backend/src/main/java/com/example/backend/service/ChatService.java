package com.example.backend.service;

import com.example.backend.dto.ChatDTO;
import com.example.backend.model.ChatEntity;
import com.example.backend.model.Landmark;
import com.example.backend.model.enums.Category;
import com.example.backend.repository.ChatRepository;
import com.example.backend.repository.LandmarkRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
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
    public ChatEntity createResponse(ChatEntity entity, Long userId) throws IOException {
        validate(entity);

        Long lid = entity.getLandmarkId(); // entity의 id
        Optional<String> title = landmarkRepository.findTitleById(lid); // id로 이름 출력.

        String ltitle = title.get();

        String response = getCompletion(entity.getSent(), ltitle, lid, userId);
        entity.setReceived(response);
        entity.setDate(LocalDateTime.now());
        entity.setUserId(userId);
        return chatRepository.save(entity);
    }

    // 대화 시작 및 이전 대화 내용 기억하기
    public String getCompletion(String prompt, String title, Long landmarkId, Long userId) throws IOException {
        List<ChatEntity> entities = chatRepository.findByLandmarkIdAndUserId(landmarkId,userId);
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append(String.format("너는 %s이야. ", title));
        contentBuilder.append("너는 알려주는 것을 좋아해. " +
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

    // 주제 선택 - 대화하기
    public ChatEntity createTopicResponse(ChatEntity entity, Long userId, String topic) throws IOException {
        validate(entity);

        Long lid = entity.getLandmarkId(); // entity의 id
        Optional<String> title = landmarkRepository.findTitleById(lid); // id로 이름 출력.

        String ltitle = title.get();

        List<String> response = getTopicCompletion(entity.getSent(), ltitle, lid, userId, topic);
        entity.setSent(response.get(0));
        entity.setReceived(response.get(1));
        entity.setDate(LocalDateTime.now());
        entity.setUserId(userId);
        return chatRepository.save(entity);
    }

    // 주제 선택 - 대화하기
    public List<String> getTopicCompletion(String prompt, String title, Long landmarkId,Long userId, String topic) throws IOException {
        List<ChatEntity> entities = chatRepository.findByLandmarkIdAndUserId(landmarkId,userId);
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append(String.format("너는 %s이야. ", title));
        contentBuilder.append("너는 알려주는 것을 좋아해. " +
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

        prompt = "너에 대한 " + topic +" 궁금해!";
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

        try (Response response = client.newCall(request).execute()) {
            if(!response.isSuccessful() || response.body() == null) {
                throw new IOException("Unexpected : " + response);
            }
            String responseBody =  response.body().string(); //print(completion.choices[0].message)
            JsonNode jsonNode = objectMapper.readTree(responseBody);

        String content = jsonNode
            .path("choices")
            .get(0)
            .path("message")
            .path("content")
            .asText();

        List<String> result = new ArrayList<>();
        result.add(prompt.toString());
        result.add(content);

        return result;
        }
    }

    // 대화 - 1:1 대화 내역
    public List<ChatEntity> showChat(Long landmarkId, Long userId) {
        // 채팅 데이터를 과거순으로 정렬
        return chatRepository.findByLandmarkIdAndUserId(landmarkId, userId)
                .stream()
                .sorted(Comparator.comparing(ChatEntity::getDate))
                .collect(Collectors.toList());
    }

    // 대화 - 대화 내역 검색하기
    public List<ChatEntity> searchChatting(Long landmarkId, String text, Long userId) {
        return chatRepository.findByLandmarkIdAndUserIdAndSentContainingOrReceivedContaining(landmarkId, userId, text, text);
    }

    // 대화 - 대화 내역 검색 & 검색된 부분 추출하기
    public List<ChatDTO> searchChattingWord(Long landmarkId, String text, Long userId) {
        // landmarkId와 sent/received에서 text를 포함한 결과를 가져옴
        List<ChatEntity> entities = chatRepository.findByLandmarkIdAndUserIdAndSentContainingOrReceivedContaining(landmarkId, userId , text, text);

        // 검색어가 포함된 부분만 추출한 결과로 DTO를 생성
        return entities.stream()
            .map(entity -> {
                // sent와 received에서 검색어가 포함된 부분만 추출
                String filteredSent = extractMatchingText(entity.getSent(), text);
                String filteredReceived = extractMatchingText(entity.getReceived(), text);

                // DTO에 넣어 반환
                return new ChatDTO(filteredSent, filteredReceived, landmarkId, entity.getChatId(), userId);
            })
            .collect(Collectors.toList());
    }

    // 검색어 부분 추출
    private String extractMatchingText(String message, String text) {
            // 메시지를 띄어쓰기 단위로 분리
            String[] words = message.split(" ");

            // 각 단어를 검사하여 검색어가 포함된 단어를 찾음
            for (String word : words) {
                if (word.contains(text)) {
                    return word;  // 검색어가 포함된 단어를 반환
                }
            }
            return "";  // 검색어가 포함된 단어가 없으면 빈 문자열 반환
        }

    // 목록 - 대화 리스트
    public List<Map<String, Object>> showList(Long userId) {

        // chat에서 userId가 있는 landmarId 가져오기
        List<Long> landmarkIds = chatRepository.findLandmarkIdsByUserId(userId);

        // 모든 LandmarkId 가져오기
        List<Landmark> landmarks = landmarkRepository.findAllById(landmarkIds);

        return convert(landmarks);
    }

    // 목록 - 대화 리스트 - 관광지 이름 검색하기
    public List<Map<String, Object>> searchLandmark(final String title, Long userId) {
        // userId와 연관된 landmarkId 목록 가져오기
        List<Long> landmarkIds = chatRepository.findLandmarkIdsByUserId(userId);

        // landmarkId와 title로 Landmark 목록 조회
        List<Landmark> landmarks = landmarkRepository.findByIdInAndTitleContaining(landmarkIds, title);

        return convert(landmarks);
    }

    // List<Landmark>에서 id 추출 -> List<ChatEntity>로 변환
    public List<Map<String, Object>> convert(List<Landmark> landmarks) {

        // id만
        List<Long> landmarkIds = landmarks.stream()
                .map(Landmark::getId) // 각 LandmarkEntity의 id 추출
                .toList();

        // title
        Map<Long, String> landmarkTitle = landmarks.stream()
                .collect(Collectors.toMap(
                        Landmark::getId,
                        Landmark::getTitle)); // id와 title 가져오기

        // image
        Map<Long, String> landmarkImage = landmarks.stream()
                .collect(Collectors.toMap(
                        Landmark::getId,
                        Landmark::getImageUrl));

        // category
        Map<Long, List<Category>> landmarkCategory = landmarks.stream()
                .collect(Collectors.toMap(
                        Landmark::getId,
                        Landmark::getCategories));

        // 이 id를 토대로 ChatEntity 반환하기.
        List<ChatEntity> chatEntities = chatRepository.findLatestChatByLandmarkIds(landmarkIds);

        return chatEntities.stream()
                .map(chatEntity -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("landmarkId", chatEntity.getLandmarkId());
                    map.put("landmarkTitle", landmarkTitle.get(chatEntity.getLandmarkId())); // title
                    map.put("landmarkImage", landmarkImage.get(chatEntity.getLandmarkId())); // image
                    map.put("landmarkCategory", landmarkCategory.get(chatEntity.getLandmarkId())); // category
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