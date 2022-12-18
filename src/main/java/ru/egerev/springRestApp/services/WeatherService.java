package ru.egerev.springRestApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.egerev.springRestApp.models.Sensor;
import ru.egerev.springRestApp.models.Weather;
import ru.egerev.springRestApp.repositories.WeatherRepository;
import ru.egerev.springRestApp.util.WeatherAddException;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class WeatherService {

    private WeatherRepository weatherRepository;
    private SensorService sensorService;

    @Autowired
    public WeatherService(WeatherRepository weatherRepository, SensorService sensorService) {
        this.weatherRepository = weatherRepository;
        this.sensorService = sensorService;
    }

    @Transactional
    public void addMeasurements(Weather weather) {
        weather.setDateOfMeasurement(new Date());
        Sensor sensor = sensorService.findByName(weather.getSensor().getName()).orElse(null);
        if (sensor == null) {
            throw new WeatherAddException("Sensor is not registered");
        }
        weather.setSensor(sensor);
        weatherRepository.save(weather);
    }

    public List<Weather> getMeasurements() {
        return weatherRepository.findAll();
    }

    public int NumberOfRainyDays() {
        return (int) weatherRepository.findAll().stream()
                .filter(weather -> weather.isRaining())
                .map(weather -> weather.getDateOfMeasurement().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .collect(Collectors.toSet())
                .stream()
                .count();
    }

}
