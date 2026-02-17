package com.login.gymcrm.service;

import com.login.gymcrm.dao.TrainerDao;
import com.login.gymcrm.model.Trainer;
import com.login.gymcrm.service.exception.EntityNotFoundException;
import com.login.gymcrm.service.exception.ValidationException;
import com.login.gymcrm.util.UuidGenerator;
import com.login.gymcrm.util.RandomPasswordGenerator;
import com.login.gymcrm.util.UsernameGenerator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;


@Service
public class TrainerService {
    private static final Logger log = LoggerFactory.getLogger(TrainerService.class);

    private TrainerDao trainerDao;
    private UuidGenerator idGenerator;
    private RandomPasswordGenerator passwordGenerator;
    private UsernameGenerator usernameGenerator;

    public Trainer createProfile(String firstName, String lastName, String specialization) {
        validateName(firstName, lastName);
        String id = idGenerator.generate();
        String username = usernameGenerator.generate(firstName, lastName);
        String password = passwordGenerator.generate(10);

        Trainer trainer = new Trainer(id, firstName.trim(), lastName.trim(), username, password, specialization);
        trainerDao.save(trainer);
        log.info("Created trainer profile id={} username={}", id, username);
        return trainer;
    }

    public Trainer updateProfile(Trainer updated) {
        if (updated == null || StringUtils.isBlank(updated.getId())) {
            throw new ValidationException("Trainer id is required for update");
        }
        Trainer existing = trainerDao.findById(updated.getId())
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found: " + updated.getId()));

        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setSpecialization(updated.getSpecialization());

        trainerDao.update(existing);
        log.info("Updated trainer profile id={}", existing.getId());
        return existing;
    }

    public Trainer selectProfile(String id) {
        if (StringUtils.isBlank(id)) {
            throw new ValidationException("Trainer id is required for select");
        }
        return trainerDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found: " + id));
    }

    public List<Trainer> listAll() {
        return trainerDao.findAll();
    }

    private void validateName(String firstName, String lastName) {
        if (StringUtils.isBlank(firstName) || StringUtils.isBlank(lastName)) {
            throw new ValidationException("First and last name are required");
        }
    }

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    @Autowired
    public void setIdGenerator(UuidGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Autowired
    public void setUsernameGenerator(UsernameGenerator usernameGenerator) {
        this.usernameGenerator = usernameGenerator;
    }

    @Autowired
    public void setPasswordGenerator(RandomPasswordGenerator passwordGenerator) {
        this.passwordGenerator = passwordGenerator;
    }
}
