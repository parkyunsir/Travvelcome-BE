package com.example.backend.service;

import com.example.backend.dto.LandmarkResponseDTO.LandmarkPreViewDTO;
import com.example.backend.model.Landmark;
import com.example.backend.model.enums.Category;
import com.example.backend.repository.LandmarkRepository;
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

@Service
@RequiredArgsConstructor
public class LandmarkServiceImpl implements LandmarkService {

  private final LandmarkRepository landmarkRepository;

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
        .map(landmark -> {
          return LandmarkPreViewDTO.builder()
              .landmarkId(landmark.getId())
              .title(landmark.getTitle())
              .description(landmark.getDescription())
              .categories(landmark.getCategories())
              .imageUrl(landmark.getImageUrl())
              .build();
        })
        .collect(Collectors.toList());
  }

  @Override
  public List<LandmarkPreViewDTO> getLandmarks(String category, List<Category> interests) {
    List<Category> categoriesToFilter = new ArrayList<>();

    if (category != null) {
      // 상위 카테고리에 따른 세부 카테고리 추가
      switch (category.toLowerCase()) {
        case "nature":
          categoriesToFilter.addAll(Arrays.asList(
              Category.MOUNTAIN, Category.BEACH, Category.TRAIL, Category.ARBORETUM, Category.PARK, Category.SCENERY
          ));
          break;
        case "history":
          categoriesToFilter.addAll(Arrays.asList(
              Category.MUSEUM, Category.PALACE, Category.HISTORIC_SITE, Category.FOLK_VILLAGE, Category.TRADITIONAL_EXPERIENCE
          ));
          break;
        case "culture":
          categoriesToFilter.addAll(Arrays.asList(
              Category.LOCAL_CULTURE, Category.HUMANITIES, Category.ART_GALLERY, Category.RELIGIOUS_SITE, Category.STORY
          ));
          break;
        default:
          throw new IllegalArgumentException("Invalid category: " + category);
      }
    }
    List<Landmark> landmarkList = new ArrayList<>();
    if (interests != null && !interests.isEmpty()) {
      landmarkList = landmarkRepository.findByCategoriesIn(interests);
      return landmarkList.stream()
          .map(landmark -> {
            return LandmarkPreViewDTO.builder()
                .landmarkId(landmark.getId())
                .title(landmark.getTitle())
                .description(landmark.getDescription())
                .categories(landmark.getCategories())
                .imageUrl(landmark.getImageUrl())
                .build();
          })
          .collect(Collectors.toList());
    }

    landmarkList = landmarkRepository.findByCategoriesIn(categoriesToFilter);
    return landmarkList.stream()
        .map(landmark -> {
          return LandmarkPreViewDTO.builder()
              .landmarkId(landmark.getId())
              .title(landmark.getTitle())
              .description(landmark.getDescription())
              .categories(landmark.getCategories())
              .imageUrl(landmark.getImageUrl())
              .build();
        })
        .collect(Collectors.toList());
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