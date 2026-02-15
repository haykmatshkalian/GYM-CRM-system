package com.login.gymcrm.model;

import java.time.LocalDate;
import java.util.Objects;

public class Training {
    private String id;
    private String traineeId;
    private String trainerId;
    private String name;
    private LocalDate date;
    private int durationMinutes;

    public Training() {
    }

    public Training(String id, String traineeId, String trainerId, String name, LocalDate date, int durationMinutes) {
        this.id = id;
        this.traineeId = traineeId;
        this.trainerId = trainerId;
        this.name = name;
        this.date = date;
        this.durationMinutes = durationMinutes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(String traineeId) {
        this.traineeId = traineeId;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Training training = (Training) o;
        return Objects.equals(id, training.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Training{" +
                "id='" + id + '\'' +
                ", traineeId='" + traineeId + '\'' +
                ", trainerId='" + trainerId + '\'' +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", durationMinutes=" + durationMinutes +
                '}';
    }
}
