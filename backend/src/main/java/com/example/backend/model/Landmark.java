package com.example.backend.model;

import com.example.backend.model.enums.Category;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Landmark {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String tid;
  private String tlid;
  private String themeCategory;
  private String addr1;
  private String addr2;
  private String title;
  private double mapX;
  private double mapY;
  private String langCheck;
  private String langCode;
  private String imageUrl;
  private String createdTime;
  private String modifiedTime;
  @Column(nullable = true, columnDefinition = "TEXT")
  private String description;

  @ElementCollection(targetClass = Category.class)
  @Enumerated(EnumType.STRING)
  @Column(nullable = true)
  private List<Category> categories;
}