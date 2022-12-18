package ru.egerev.springRestApp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
@Table (name = "Weather")
public class Weather {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "temperature")
    @NotNull(message = "Temperature should not be empty")
    @DecimalMax(value = "100", message = "Temperature should be between -100 and 100 degrees")
    @DecimalMin(value = "-100", message = "Temperature should be between -100 and 100 degrees")
    private double temperature;

    @Column(name = "raining")
    @NotNull(message = "Value of raining should not be empty")
    private Boolean raining;

    @ManyToOne
    @JoinColumn(name = "sensor_name", referencedColumnName = "name")
    private Sensor sensor;

    @Column(name = "date_of_measurement")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfMeasurement;

    public Weather() {
    }

    public Weather(int id, double temperature, Boolean raining) {
        this.id = id;
        this.temperature = temperature;
        this.raining = raining;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public Boolean isRaining() {
        return raining;
    }

    public void setRaining(Boolean raining) {
        this.raining = raining;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
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
        return "Weather{" +
                "id=" + id +
                ", temperature=" + temperature +
                ", raining=" + raining +
                ", sensor=" + sensor +
                ", dateOfMeasurement=" + dateOfMeasurement +
                '}';
    }
}
