package com.example.award.repo;

import com.example.award.model.AwardDistribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AwardDistRepo extends JpaRepository<AwardDistribution, Integer> {

    List<AwardDistribution> findByEmpUuidIsNull();

    List<AwardDistribution> findByEmpUuid(String uuid);

    List<AwardDistribution> findByEmployeeId(int empId);

    @Query(
            value = "SELECT SUM(points) FROM award_distribution WHERE emp_uuid = :empUuid",
            nativeQuery = true
    )
    Integer findTotalPointsByEmpUuidNative(@Param("empUuid") String empUuid);

}
