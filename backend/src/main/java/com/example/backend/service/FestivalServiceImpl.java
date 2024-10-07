package com.example.backend.service;

import com.example.backend.dto.FestivalResponseDTO;
import com.example.backend.model.Festival;
import com.example.backend.repository.FestivalRepository;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@Service
@RequiredArgsConstructor
public class FestivalServiceImpl implements FestivalService{

  private RestTemplate createRestTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.getMessageConverters()
        .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    return restTemplate;
  }

  @Override
  public void fetchAndSaveFestivals() {
    String baseUrl = "http://apis.data.go.kr/B551011/KorService1/searchFestival1";
    String serviceKey = "5tjj4/cu195FExR9HgtMLZE10SrTcISc0HT/lE/BwE06lF/1UjV573QgJOKJN99zuYTV5EtJpkGuYmVY7rRB0Q==";
    int numOfRows = 10; // 한 페이지 결과 수
    int pageNo = 1; // 페이지 번호
    boolean hasMoreData = true;

    // UTF-8 인코딩을 지원하는 RestTemplate 사용
    RestTemplate restTemplate = createRestTemplate();

    while (hasMoreData) {
      String url = String.format("%s?serviceKey=%s&numOfRows=%d&pageNo=%d&MobileOS=ETC&MobileApp=AppTest&arrange=A&listYN=Y&eventStartDate=20170901",
          baseUrl, serviceKey, numOfRows, pageNo);

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
          saveFestivals(items);
          pageNo++; // 다음 페이지로 이동
        }
      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Error occurred while parsing and saving festivals", e);
      }
    }
  }

  private void saveFestivals(NodeList items) {
    for (int i = 0; i < items.getLength(); i++) {
      Element item = (Element) items.item(i);

      // 각 필드를 한 번에 추출 및 변환
      String title = item.getElementsByTagName("title").item(0) != null ?
          item.getElementsByTagName("title").item(0).getTextContent() : null;
      String addr1 = item.getElementsByTagName("addr1").item(0) != null ?
          item.getElementsByTagName("addr1").item(0).getTextContent() : null;
      String addr2 = item.getElementsByTagName("addr2").item(0) != null ?
          item.getElementsByTagName("addr2").item(0).getTextContent() : null;
      String booktour = item.getElementsByTagName("booktour").item(0) != null ?
          item.getElementsByTagName("booktour").item(0).getTextContent() : null;
      String cat1 = item.getElementsByTagName("cat1").item(0) != null ?
          item.getElementsByTagName("cat1").item(0).getTextContent() : null;
      String cat2 = item.getElementsByTagName("cat2").item(0) != null ?
          item.getElementsByTagName("cat2").item(0).getTextContent() : null;
      String cat3 = item.getElementsByTagName("cat3").item(0) != null ?
          item.getElementsByTagName("cat3").item(0).getTextContent() : null;
      Long contentId = item.getElementsByTagName("contentid").item(0) != null ?
          Long.parseLong(item.getElementsByTagName("contentid").item(0).getTextContent()) : null;
      Long contentTypeId = item.getElementsByTagName("contenttypeid").item(0) != null ?
          Long.parseLong(item.getElementsByTagName("contenttypeid").item(0).getTextContent()) : null;
      String createdTime = item.getElementsByTagName("createdtime").item(0) != null ?
          item.getElementsByTagName("createdtime").item(0).getTextContent() : null;
      String eventStartDate = item.getElementsByTagName("eventstartdate").item(0) != null ?
          item.getElementsByTagName("eventstartdate").item(0).getTextContent() : null;
      String eventEndDate = item.getElementsByTagName("eventenddate").item(0) != null ?
          item.getElementsByTagName("eventenddate").item(0).getTextContent() : null;
      String firstImage = item.getElementsByTagName("firstimage").item(0) != null ?
          item.getElementsByTagName("firstimage").item(0).getTextContent() : null;
      String firstImage2 = item.getElementsByTagName("firstimage2").item(0) != null ?
          item.getElementsByTagName("firstimage2").item(0).getTextContent() : null;
      String cpyrhtDivCd = item.getElementsByTagName("cpyrhtDivCd").item(0) != null ?
          item.getElementsByTagName("cpyrhtDivCd").item(0).getTextContent() : null;
      Double mapX = item.getElementsByTagName("mapx").item(0) != null ?
          Double.parseDouble(item.getElementsByTagName("mapx").item(0).getTextContent()) : null;
      Double mapY = item.getElementsByTagName("mapy").item(0) != null ?
          Double.parseDouble(item.getElementsByTagName("mapy").item(0).getTextContent()) : null;
      Integer mlevel = item.getElementsByTagName("mlevel").item(0) != null ?
          Integer.parseInt(item.getElementsByTagName("mlevel").item(0).getTextContent()) : null;
      String modifiedTime = item.getElementsByTagName("modifiedtime").item(0) != null ?
          item.getElementsByTagName("modifiedtime").item(0).getTextContent() : null;
      Integer areaCode = item.getElementsByTagName("areacode").item(0) != null ?
          Integer.parseInt(item.getElementsByTagName("areacode").item(0).getTextContent()) : null;
      Integer sigunguCode = item.getElementsByTagName("sigungucode").item(0) != null ?
          Integer.parseInt(item.getElementsByTagName("sigungucode").item(0).getTextContent()) : null;
      String tel = item.getElementsByTagName("tel").item(0) != null ?
          item.getElementsByTagName("tel").item(0).getTextContent() : null;

      // addr1에 '제주'가 포함된 경우만 저장
      if (addr1 != null && addr1.contains("제주")) {
        Festival festival = new Festival();
        festival.setTitle(title);
        festival.setAddr1(addr1);
        festival.setAddr2(addr2);
        festival.setBooktour(booktour);
        festival.setCat1(cat1);
        festival.setCat2(cat2);
        festival.setCat3(cat3);
        festival.setContentId(contentId);
        festival.setContentTypeId(contentTypeId);
        festival.setCreatedTime(createdTime);
        festival.setEventStartDate(eventStartDate);
        festival.setEventEndDate(eventEndDate);
        festival.setFirstImage(firstImage);
        festival.setFirstImage2(firstImage2);
        festival.setCpyrhtDivCd(cpyrhtDivCd);
        festival.setMapX(mapX);
        festival.setMapY(mapY);
        festival.setMlevel(mlevel);
        festival.setModifiedTime(modifiedTime);
        festival.setAreaCode(areaCode);
        festival.setSigunguCode(sigunguCode);
        festival.setTel(tel);

        festivalRepository.save(festival);
      }
    }
  }

  private final FestivalRepository festivalRepository;

  @Override
  public List<FestivalResponseDTO.FestivalDTO> getFestivals() {
    LocalDate today = LocalDate.now();

    List<Festival> festivals = festivalRepository.findAll();

    return festivals.stream()
        .filter(festival -> {
          LocalDate startDate = parseDate(festival.getEventStartDate());
          LocalDate endDate = parseDate(festival.getEventEndDate());

          return (startDate != null && endDate != null && !startDate.isBefore(today) && !endDate.isBefore(today));
        })
        .map(festival -> FestivalResponseDTO.FestivalDTO.builder()
            .id(festival.getId())
            .title(festival.getTitle())
            .addr1(festival.getAddr1())
            .addr2(festival.getAddr2())
            .contentId(festival.getContentId())
            .createdTime(festival.getCreatedTime())
            .eventStartDate(festival.getEventStartDate())
            .eventEndDate(festival.getEventEndDate())
            .firstImage(festival.getFirstImage())
            .firstImage2(festival.getFirstImage2())
            .mapX(festival.getMapX())
            .mapY(festival.getMapY())
            .modifiedTime(festival.getModifiedTime())
            .tel(festival.getTel())
            .build())
        .collect(Collectors.toList());
  }

  private LocalDate parseDate(String dateStr) {
    try {
      return dateStr != null && !dateStr.isEmpty() ? LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyyMMdd")) : null;
    } catch (DateTimeParseException e) {
      e.printStackTrace();
      return null;
    }
  }

}
