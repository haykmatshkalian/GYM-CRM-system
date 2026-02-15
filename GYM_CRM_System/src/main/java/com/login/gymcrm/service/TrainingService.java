package com.login.gymcrm.service;

import com.login.gymcrm.dao.TrainingDao;
import com.login.gymcrm.model.Training;
import com.login.gymcrm.service.exception.EntityNotFoundException;
import com.login.gymcrm.service.exception.ValidationException;
import com.login.gymcrm.util.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TrainingService {
    private static final Logger log = LoggerFactory.getLogger(TrainingService.class);

    private TrainingDao trainingDao;
    private IdGenerator idGenerator;

    public Training createTraining(String traineeId, String trainerId, String name, LocalDate date, int durationMinutes) {
        if (traineeId == null || traineeId.isBlank() || trainerId == null || trainerId.isBlank()) {
            throw new ValidationException("Trainee and Trainer ids are required");
        }
        if (name == null || name.isBlank()) {
            throw new ValidationException("Training name is required");
        }
        if (date == null) {
            throw new ValidationException("Training date is required");
        }
        if (durationMinutes <= 0) {
            throw new ValidationException("Duration must be positive");
        }

        String id = idGenerator.generate();
        Training training = new Training(id, traineeId, trainerId, name.trim(), date, durationMinutes);
        trainingDao.save(training);
        log.info("Created training id={} traineeId={} trainerId={}", id, traineeId, trainerId);
        return training;
    }

    public Training selectTraining(String id) {
        if (id == null || id.isBlank()) {
            throw new ValidationException("Training id is required for select");
        }
        return trainingDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Training not found: " + id));
    }

    public List<Training> listAll() {
        return trainingDao.findAll();
    }

    @org.springframework.beans.factory.annotation.Autowired
    public void setTrainingDao(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }

    @org.springframework.beans.factory.annotation.Autowired
    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }
}
