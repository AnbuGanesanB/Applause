package com.example.goody.repo;

import com.example.goody.model.GoodyDistribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodyDistRepo extends JpaRepository<GoodyDistribution,Integer> {

    @Query(
            value = "SELECT SUM(points) FROM goody_distribution " +
                    "WHERE emp_uuid = :empUuid " +
                    "AND status IN ('ORDERED', 'DELIVERED')",
            nativeQuery = true
    )
    Integer findTotalPointsToOrderByEmpUuid(@Param("empUuid") String empUuid);

    List<GoodyDistribution> findAllByEmpUuid(String empUuid);

    List<GoodyDistribution> findAllByEmployeeId(int employeeId);

    @Query(
            value = """
                    SELECT * 
                    FROM goody_distribution gd1
                    WHERE gd1.emp_uuid IN (
                        SELECT DISTINCT gd2.emp_uuid
                        FROM goody_distribution gd2
                        WHERE gd2.employee_id IS NULL
                    )
                    """,
            nativeQuery = true
    )
    List<GoodyDistribution> findRecordsWithNoEmpId();

}
