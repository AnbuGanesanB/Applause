package com.example.award.service;

import com.example.award.exception.AwardTypeException;
import com.example.award.model.AwardType;
import com.example.award.repo.AwardTypeRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AwardService {

    private final AwardTypeRepo awardTypeRepo;

    public AwardType createAward(AwardType awardType) {
        awardType.setId(null);

        Set<String> validCategories = Set.of("individual", "team", "department");
        String category = awardType.getCategory().toLowerCase();

        if (!validCategories.contains(category)) {
            throw new IllegalArgumentException("Invalid category: " + category);
        }

        return awardTypeRepo.save(awardType);
    }

    @Transactional
    public AwardType editAward(int awardId,Map<String, Object> awardTypeData) {
        AwardType awardType = getAwardById(awardId);

        if(awardTypeData.get("points")!=null){
            awardType.setPoints((Integer)awardTypeData.get("points"));
        }

        return awardTypeRepo.save(awardType);
    }

    public AwardType getAward(int awardId) {
        return getAwardById(awardId);
    }

    public List<AwardType> getAllAwards() {
        return awardTypeRepo.findAll();
    }

    private AwardType getAwardById(int awardId){
        return awardTypeRepo.findById(awardId).orElseThrow(()->new AwardTypeException.AwardTypeNotFoundException("Award Not Found"));
    }
}
