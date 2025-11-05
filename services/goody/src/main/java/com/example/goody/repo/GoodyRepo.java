package com.example.goody.repo;

import com.example.goody.model.Goody;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoodyRepo extends JpaRepository<Goody,Integer> {

    Optional<Goody> findByName(String name);
}
