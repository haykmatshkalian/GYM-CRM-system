package com.login.gymcrm.dao;

import com.login.gymcrm.model.Trainee;
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
public class InMemoryTraineeDao implements TraineeDao {
    private static final Logger log = LoggerFactory.getLogger(InMemoryTraineeDao.class);

    private InMemoryStorage storage;

    @Override
    public void save(Trainee entity) {
        Map<String, Trainee> map = storage.getNamespace(StorageNamespaces.TRAINEE);
        map.put(entity.getId(), entity);
        log.debug("Saved trainee with id={}", entity.getId());
    }

    @Override
    public void update(Trainee entity) {
        Map<String, Trainee> map = storage.getNamespace(StorageNamespaces.TRAINEE);
        map.put(entity.getId(), entity);
        log.debug("Updated trainee with id={}", entity.getId());
    }

    @Override
    public void deleteById(String id) {
        Map<String, Trainee> map = storage.getNamespace(StorageNamespaces.TRAINEE);
        map.remove(id);
        log.debug("Deleted trainee with id={}", id);
    }

    @Override
    public Optional<Trainee> findById(String id) {
        Map<String, Trainee> map = storage.getNamespace(StorageNamespaces.TRAINEE);
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<Trainee> findAll() {
        Map<String, Trainee> map = storage.getNamespace(StorageNamespaces.TRAINEE);
        return new ArrayList<>(map.values());
    }

    @org.springframework.beans.factory.annotation.Autowired
    public void setStorage(InMemoryStorage storage) {
        this.storage = storage;
    }
}
