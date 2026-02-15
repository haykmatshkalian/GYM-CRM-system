package com.login.gymcrm.dao;

import com.login.gymcrm.model.Training;
import com.login.gymcrm.storage.InMemoryStorage;
import com.login.gymcrm.storage.StorageNamespaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryTrainingDao implements TrainingDao {
    private static final Logger log = LoggerFactory.getLogger(InMemoryTrainingDao.class);

    private InMemoryStorage storage;

    @Override
    public void save(Training entity) {
        Map<String, Training> map = storage.getNamespace(StorageNamespaces.TRAINING);
        map.put(entity.getId(), entity);
        log.debug("Saved training with id={}", entity.getId());
    }

    @Override
    public void update(Training entity) {
        Map<String, Training> map = storage.getNamespace(StorageNamespaces.TRAINING);
        map.put(entity.getId(), entity);
        log.debug("Updated training with id={}", entity.getId());
    }

    @Override
    public void deleteById(String id) {
        Map<String, Training> map = storage.getNamespace(StorageNamespaces.TRAINING);
        map.remove(id);
        log.debug("Deleted training with id={}", id);
    }

    @Override
    public Optional<Training> findById(String id) {
        Map<String, Training> map = storage.getNamespace(StorageNamespaces.TRAINING);
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<Training> findAll() {
        Map<String, Training> map = storage.getNamespace(StorageNamespaces.TRAINING);
        return new ArrayList<>(map.values());
    }

    @org.springframework.beans.factory.annotation.Autowired
    public void setStorage(InMemoryStorage storage) {
        this.storage = storage;
    }
}
