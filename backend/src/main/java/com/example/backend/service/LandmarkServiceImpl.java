package com.example.backend.service;

import com.example.backend.apiPayload.code.status.ErrorStatus;
import com.example.backend.apiPayload.exception.handler.TempHandler;
import com.example.backend.dto.LandmarkResponseDTO.LandmarkFindDTO;
import com.example.backend.dto.LandmarkResponseDTO.LandmarkMapDTO;
import com.example.backend.dto.LandmarkResponseDTO.LandmarkPreViewDTO;
import com.example.backend.model.Festival;
import com.example.backend.model.Interest;
import com.example.backend.model.Landmark;
import com.example.backend.model.Stamp;
import com.example.backend.model.UsersEntity;
import com.example.backend.model.enums.Category;
import com.example.backend.repository.FestivalRepository;
import com.example.backend.repository.LandmarkRepository;
import com.example.backend.repository.StampRepository;
import com.example.backend.repository.UserRepository;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.w3c.dom.Element;

@Service
@RequiredArgsConstructor
public class LandmarkServiceImpl implements LandmarkService {

  private final LandmarkRepository landmarkRepository;
  private final UserRepository userRepository;
  private final StampRepository stampRepository;

  private RestTemplate createRestTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.getMessageConverters()
        .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    return restTemplate;
  }

  @Override
  public void fetchAndSaveLandmarks() {
    String baseUrl = "https://apis.data.go.kr/B551011/Odii/themeBasedList?serviceKey=5tjj4/cu195FExR9HgtMLZE10SrTcISc0HT/lE/BwE06lF/1UjV573QgJOKJN99zuYTV5EtJpkGuYmVY7rRB0Q==";
    int pageNo = 1;
    int numOfRows = 300; // 최대값 설정
    boolean hasMoreData = true;

    // UTF-8 인코딩을 지원하는 RestTemplate 사용
    RestTemplate restTemplate = createRestTemplate();

    while (hasMoreData) {
      String url = String.format("%s&numOfRows=%d&pageNo=%d&MobileOS=ETC&MobileApp=AppTest&langCode=ko", baseUrl, numOfRows, pageNo);
      String xmlResponse = restTemplate.getForObject(url, String.class);

      if (xmlResponse == null) {
        throw new RuntimeException("Failed to fetch XML data from URL: " + url);
      }

      try {
        // XML 파싱
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xmlResponse)));

        NodeList items = doc.getElementsByTagName("item");
        if (items.getLength() == 0) {
          hasMoreData = false; // 더 이상 데이터가 없을 경우 종료
        } else {
          // 데이터를 저장하는 로직
          saveLandmarks(items);
          pageNo++; // 다음 페이지로 이동
        }
      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Error occurred while parsing and saving landmarks", e);
      }
    }
  }

  @Override
  public List<LandmarkPreViewDTO> searchLandmark(String keyword) {
    List<Landmark> landmarks = landmarkRepository.findByTitleContainingOrDescriptionContaining(keyword, keyword);
    return landmarks.stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<LandmarkPreViewDTO> getLandmarks(String category, List<Category> interests) {
    List<Category> categoriesToFilter = new ArrayList<>();

    if (category != null) {
      categoriesToFilter = getCategoriesByMainCategory(category);
    }

    List<Landmark> landmarkList = new ArrayList<>();

    if (interests != null && !interests.isEmpty()) {
      landmarkList = landmarkRepository.findByCategoriesIn(interests);
    } else if (!categoriesToFilter.isEmpty()) {
      landmarkList = landmarkRepository.findByCategoriesIn(categoriesToFilter);
    } else {
      landmarkList = landmarkRepository.findAll();
    }

    return landmarkList.stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<LandmarkPreViewDTO> categoryLandmark(Category category) {
    if (category == null) {
      throw new TempHandler(ErrorStatus.NULL_CATEGORY);
    }
    List<Landmark> landmarkList = landmarkRepository.findByCategoriesContaining(category);

    return landmarkList.stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  @Override
  public LandmarkFindDTO getLandmarkFind(Long landmarkId) {
    Landmark landmark = landmarkRepository.findById(landmarkId)
        .orElseThrow(() -> new TempHandler(ErrorStatus.LANDMARK_NOT_FOUND));
    LandmarkFindDTO landmarkFindDTO = LandmarkFindDTO.builder()
        .landmarkId(landmark.getId())
        .title(landmark.getTitle())
        .description(landmark.getDescription())
        .categories(landmark.getCategories())
        .imageUrl(landmark.getImageUrl())
        .addr1(landmark.getAddr1())
        .addr2(landmark.getAddr2())
        .build();

    return landmarkFindDTO;
  }

  @Override
  public List<LandmarkPreViewDTO> getCloseLandmarks(double mapX, double mapY) {
    double radius = 1.0; // 조회할 반경 (km)

    // Haversine 공식에 따른 거리 계산을 통해 가까운 랜드마크들을 조회
    List<Landmark> closeLandmarks = landmarkRepository.findAll().stream()
        .filter(landmark -> calculateDistance(mapX, mapY, landmark.getMapX(), landmark.getMapY()) <= radius)
        .collect(Collectors.toList());

    return closeLandmarks.stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<LandmarkMapDTO> getMapLandmarks(long userId) {
    UsersEntity user = userRepository.findById(userId).orElseThrow(() -> new TempHandler(ErrorStatus.USER_NOT_FOUND));

    List<Category> userCategories = user.getInterests().stream()
        .map(Interest::getCategory)
        .collect(Collectors.toList());

    List<Landmark> landmarks = landmarkRepository.findByCategoriesIn(userCategories);

    return landmarks.stream()
        .map(landmark -> LandmarkMapDTO.builder()
            .landmarkId(landmark.getId())
            .mapX(landmark.getMapX())
            .mapY(landmark.getMapY())
            .build())
        .collect(Collectors.toList());
  }

  @Override
  public void findLandmark(Long landmarkId, long userId) {
    UsersEntity user = userRepository.findById(userId).orElseThrow(() -> new TempHandler(ErrorStatus.USER_NOT_FOUND));
    Landmark landmark = landmarkRepository.findById(landmarkId).orElseThrow(() -> new TempHandler(ErrorStatus.LANDMARK_NOT_FOUND));
    stampRepository.findByUserAndLandmark(user, landmark)
        .ifPresent(mb -> { throw new TempHandler(ErrorStatus.ALREADY_FIND_LANDMARK); });

    Stamp stamp = Stamp.builder()
        .user(user)
        .landmark(landmark)
        .build();
    stampRepository.save(stamp);
  }

  // Haversine 공식을 사용한 거리 계산 (단위: km)
  private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
    final int EARTH_RADIUS = 6371; // 지구 반경 (단위: km)

    double latDistance = Math.toRadians(lat2 - lat1);
    double lonDistance = Math.toRadians(lon2 - lon1);
    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
        + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
        * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return EARTH_RADIUS * c; // 두 지점 사이의 거리 (km)
  }


  private List<Category> getCategoriesByMainCategory(String category) {
    switch (category.toLowerCase()) {
      case "nature":
        return Arrays.asList(
            Category.MOUNTAIN, Category.BEACH_ISLAND, Category.GARDEN, Category.TRAIL, Category.WATERFALL, Category.DRIVE
        );
      case "knowledge":
        return Arrays.asList(
            Category.HISTORY, Category.ECOLOGY_SCIENCE, Category.MYTH_LEGEND, Category.STORY_FIGURES
        );
      case "culture":
        return Arrays.asList(
            Category.EXHIBITION, Category.ART, Category.CRAFT_EXPERIENCE, Category.ACTIVITY, Category.THEME_PARK, Category.TASTE, Category.RELIGION
        );
      default:
        throw new TempHandler(ErrorStatus.INVALID_CATEGORY);
    }
  }

  private LandmarkPreViewDTO convertToDTO(Landmark landmark) {
    return LandmarkPreViewDTO.builder()
        .landmarkId(landmark.getId())
        .title(landmark.getTitle())
        .description(landmark.getDescription())
        .categories(landmark.getCategories())
        .imageUrl(landmark.getImageUrl())
        .build();
  }

  private void saveLandmarks(NodeList items) {
    for (int i = 0; i < items.getLength(); i++) {
      NodeList itemData = items.item(i).getChildNodes();
      Landmark landmark = new Landmark();
      String addr1 = "";

      for (int j = 0; j < itemData.getLength(); j++) {
        String nodeName = itemData.item(j).getNodeName();
        String nodeValue = itemData.item(j).getTextContent();

        switch (nodeName) {
          case "tid":
            landmark.setTid(nodeValue);
            break;
          case "tlid":
            landmark.setTlid(nodeValue);
            break;
          case "themeCategory":
            landmark.setThemeCategory(nodeValue);
            break;
          case "addr1":
            addr1 = nodeValue; // addr1 값을 저장
            landmark.setAddr1(nodeValue);
            break;
          case "addr2":
            landmark.setAddr2(nodeValue);
            break;
          case "title":
            landmark.setTitle(nodeValue);
            break;
          case "mapX":
            landmark.setMapX(Double.parseDouble(nodeValue));
            break;
          case "mapY":
            landmark.setMapY(Double.parseDouble(nodeValue));
            break;
          case "langCheck":
            landmark.setLangCheck(nodeValue);
            break;
          case "langCode":
            landmark.setLangCode(nodeValue);
            break;
          case "imageUrl":
            landmark.setImageUrl(nodeValue);
            break;
          case "createdtime":
            landmark.setCreatedTime(nodeValue);
            break;
          case "modifiedtime":
            landmark.setModifiedTime(nodeValue);
            break;
        }
      }

      // addr1이 제주도인 경우에만 저장
      if ("제주도".equals(addr1)) {
        landmarkRepository.save(landmark);
        System.out.println("Landmark saved: " + landmark.getTitle());
      }
    }
  }
}