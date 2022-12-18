package ru.egerev.springRestApp.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class WeatherDTO {

    @NotNull(message = "Temperature should not be empty")
    @DecimalMax(value = "100", message = "Temperature should be between -100 and 100 degrees")
    @DecimalMin(value = "-100", message = "Temperature should be between -100 and 100 degrees")
    private Double temperature;

    @NotNull(message = "Value of raining should not be empty")
    private Boolean raining;

    private Date dateOfMeasurement;

    private SensorDTO sensor;

    public WeatherDTO(Double temperature, Boolean raining, SensorDTO sensor) {
        this.temperature = temperature;
        this.raining = raining;
        this.sensor = sensor;
    }

    public WeatherDTO() {
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Boolean getRaining() {
        return raining;
    }

    public void setRaining(Boolean raining) {
        this.raining = raining;
    }

    public SensorDTO getSensor() {
        return sensor;
    }

    public void setSensor(SensorDTO sensor) {
        this.sensor = sensor;
    }

    public Date getDateOfMeasurement() {
        return dateOfMeasurement;
    }

    public void setDateOfMeasurement(Date dateOfMeasurement) {
        this.dateOfMeasurement = dateOfMeasurement;
    }

    @Override
    public String toString() {
        return "Measurements{" +
                "temperature=" + temperature +
                ", raining=" + raining +
                ", dateOfMeasurement=" + dateOfMeasurement +
                ", sensor=" + sensor +
                '}';
    }
}
