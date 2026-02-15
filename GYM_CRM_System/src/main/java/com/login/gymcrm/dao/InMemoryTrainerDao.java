package com.login.gymcrm.dao;

import com.login.gymcrm.model.Trainer;
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
public class InMemoryTrainerDao implements TrainerDao {
    private static final Logger log = LoggerFactory.getLogger(InMemoryTrainerDao.class);

    private InMemoryStorage storage;

    @Override
    public void save(Trainer entity) {
        Map<String, Trainer> map = storage.getNamespace(StorageNamespaces.TRAINER);
        map.put(entity.getId(), entity);
        log.debug("Saved trainer with id={}", entity.getId());
    }

    @Override
    public void update(Trainer entity) {
        Map<String, Trainer> map = storage.getNamespace(StorageNamespaces.TRAINER);
        map.put(entity.getId(), entity);
        log.debug("Updated trainer with id={}", entity.getId());
    }

    @Override
    public void deleteById(String id) {
        Map<String, Trainer> map = storage.getNamespace(StorageNamespaces.TRAINER);
        map.remove(id);
        log.debug("Deleted trainer with id={}", id);
    }

    @Override
    public Optional<Trainer> findById(String id) {
        Map<String, Trainer> map = storage.getNamespace(StorageNamespaces.TRAINER);
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<Trainer> findAll() {
        Map<String, Trainer> map = storage.getNamespace(StorageNamespaces.TRAINER);
        return new ArrayList<>(map.values());
    }

    @org.springframework.beans.factory.annotation.Autowired
    public void setStorage(InMemoryStorage storage) {
        this.storage = storage;
    }
}
