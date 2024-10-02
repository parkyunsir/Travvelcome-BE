package com.example.backend.model;

import com.example.backend.model.enums.Category;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="Interest")
public class Interest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private UsersEntity user;

  @Enumerated(EnumType.STRING)
  @Column(name = "category")
  private Category category;
}
