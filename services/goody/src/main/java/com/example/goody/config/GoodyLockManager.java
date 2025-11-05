package com.example.goody.config;

import com.example.goody.exception.GoodyException;
import com.example.goody.model.GoodyDistribution;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

@Component
public class GoodyLockManager {

    private final ConcurrentHashMap<Integer, ReentrantLock> lockMap = new ConcurrentHashMap<>();


    public <T>T executeWithLock(Integer goodyId, Supplier<T> action) {
        ReentrantLock lock = lockMap.computeIfAbsent(goodyId, id -> new ReentrantLock());

        boolean acquired = false;
        try {
            acquired = lock.tryLock(200, TimeUnit.MILLISECONDS);
            if (!acquired){
                throw new GoodyException.GoodyLockTimeoutException("Goody currently being processed.!!");
            }
            return action.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while waiting for lock", e);
        } finally {
            if (acquired) lock.unlock();
        }
    }
}
