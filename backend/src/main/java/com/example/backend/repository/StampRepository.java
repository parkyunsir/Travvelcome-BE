package com.example.backend.repository;

import com.example.backend.model.Landmark;
import com.example.backend.model.Stamp;
import com.example.backend.model.UsersEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StampRepository extends JpaRepository<Stamp, Long> {

  Optional<Object> findByUserAndLandmark(UsersEntity user, Landmark landmark);

  Long countByUser(UsersEntity user);
}
