package com.example.goody.service;

import com.example.goody.config.GoodyLockManager;
import com.example.goody.exception.GoodyException;
import com.example.goody.model.Goody;
import com.example.goody.repo.GoodyRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoodyService {

    private final GoodyRepo goodyRepo;
    private final GoodyLockManager goodyLockManager;

    public Goody addGoody(Goody goody) {
        goody.setId(null);
        return goodyRepo.save(goody);
    }

    @Transactional
    public Goody editPoints(int goodyId, int points) {
        return goodyLockManager.executeWithLock(goodyId,()->{
            Goody goody = getGoodyById(goodyId);
            goody.setPoints(points);
            return goodyRepo.save(goody);
        });
    }

    @Transactional
    public Goody editQuantity(int goodyId, int quantity) {
        return goodyLockManager.executeWithLock(goodyId,()->{
            customWait();                       //Wait To test concurrency
            Goody goody = getGoodyById(goodyId);
            goody.setAvailableQuantity(quantity);
            return goodyRepo.save(goody);
        });
    }

    public Goody getGoodyDetails(int goodyId) {
        return getGoodyById(goodyId);
    }

    public List<Goody> getAllGoodies() {
        return goodyRepo.findAll();
    }

    @Transactional
    public Goody decreaseStockByQty(Goody goody, int qty){
        return goodyLockManager.executeWithLock(goody.getId(),()->{
            goody.setAvailableQuantity(goody.getAvailableQuantity() - qty);
            return goodyRepo.save(goody);
        });
    }

    @Transactional
    public Goody increaseStockByQty(Goody goody, int qty){
        return goodyLockManager.executeWithLock(goody.getId(),()->{
            goody.setAvailableQuantity(goody.getAvailableQuantity() + qty);
            return goodyRepo.save(goody);
        });
    }

    private Goody getGoodyById(int goodyId){
        return goodyRepo.findById(goodyId)
                .orElseThrow(()->new GoodyException.GoodyNotFoundException("Goody not found"));
    }

    public Optional<Goody> getGoodyByName(String goodyName){
        return Optional.ofNullable(goodyRepo.findByName(goodyName)
                .orElseThrow(() -> new GoodyException.GoodyNotFoundException("Goody not found")));
    }

    //Wait To test concurrency
    private void customWait(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
