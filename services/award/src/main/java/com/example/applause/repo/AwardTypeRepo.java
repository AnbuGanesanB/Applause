package com.example.applause.repo;

import com.example.applause.model.AwardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AwardTypeRepo extends JpaRepository<AwardType, Integer> {


}
