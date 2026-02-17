package com.login.gymcrm.service;

import com.login.gymcrm.dao.TraineeDao;
import com.login.gymcrm.model.Trainee;
import com.login.gymcrm.service.exception.EntityNotFoundException;
import com.login.gymcrm.service.exception.ValidationException;
import com.login.gymcrm.util.RandomPasswordGenerator;
import com.login.gymcrm.util.UsernameGenerator;
import com.login.gymcrm.util.UuidGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang3.StringUtils;


import java.util.List;

@Service
public class TraineeService {
    private static final Logger log = LoggerFactory.getLogger(TraineeService.class);

    private TraineeDao traineeDao;
    private UuidGenerator idGenerator;
    private UsernameGenerator usernameGenerator;
    private RandomPasswordGenerator passwordGenerator;

    public Trainee createProfile(String firstName, String lastName) {
        validateName(firstName, lastName);
        String id = idGenerator.generate();
        String username = usernameGenerator.generate(firstName, lastName);
        String password = passwordGenerator.generate(10);

        Trainee trainee = new Trainee(id, firstName.trim(), lastName.trim(), username, password, true);
        traineeDao.save(trainee);
        log.info("Created trainee profile id={} username={}", id, username);
        return trainee;
    }

    public Trainee updateProfile(Trainee updated) {
        if (updated == null || StringUtils.isBlank(updated.getId())) {
            throw new ValidationException("Trainee id is required for update");
        }
        Trainee existing = traineeDao.findById(updated.getId())
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found: " + updated.getId()));

        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setActive(updated.isActive());

        traineeDao.update(existing);
        log.info("Updated trainee profile id={}", existing.getId());
        return existing;
    }

    public void deleteProfile(String id) {
        if (StringUtils.isBlank(id)) {
            throw new ValidationException("Trainee id is required for delete");
        }
        traineeDao.deleteById(id);
        log.info("Deleted trainee profile id={}", id);
    }

    public Trainee selectProfile(String id) {
        if (StringUtils.isBlank(id)) {
            throw new ValidationException("Trainee id is required for select");
        }
        return traineeDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found: " + id));
    }

    public List<Trainee> listAll() {
        return traineeDao.findAll();
    }

    private void validateName(String firstName, String lastName) {
        if (StringUtils.isBlank(firstName) || StringUtils.isBlank(lastName)) {
            throw new ValidationException("First and last name are required");
        }
    }

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
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
