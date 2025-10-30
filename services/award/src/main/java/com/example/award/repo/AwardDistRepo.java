package com.example.award.repo;

import com.example.award.model.AwardDistribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AwardDistRepo extends JpaRepository<AwardDistribution, Integer> {

    List<AwardDistribution> findByEmpUuidIsNull();

    List<AwardDistribution> findByEmpUuid(String uuid);

    List<AwardDistribution> findByEmployeeId(int empId);

}
