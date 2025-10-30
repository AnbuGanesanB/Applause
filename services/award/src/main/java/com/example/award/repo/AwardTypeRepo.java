package com.example.award.repo;

import com.example.award.model.AwardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AwardTypeRepo extends JpaRepository<AwardType, Integer> {


}
