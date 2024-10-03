package com.example.backend.service;

import com.example.backend.dto.FestivalResponseDTO;
import com.example.backend.model.Festival;
import com.example.backend.repository.FestivalRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FestivalServiceImpl implements FestivalService{

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
