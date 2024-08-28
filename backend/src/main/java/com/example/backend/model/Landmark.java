package com.example.backend.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
}