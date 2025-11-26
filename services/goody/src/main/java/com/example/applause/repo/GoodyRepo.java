package com.example.applause.repo;

import com.example.applause.model.Goody;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoodyRepo extends JpaRepository<Goody,Integer> {

    Optional<Goody> findByName(String name);
}
