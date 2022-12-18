package ru.egerev.springRestApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.egerev.springRestApp.models.Sensor;
import ru.egerev.springRestApp.repositories.SensorRepository;
import ru.egerev.springRestApp.util.SensorRegistrationException;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SensorService {

    private SensorRepository sensorRepository;

    @Autowired
    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    @Transactional
    public void register(Sensor sensor) {
        if (!sensorRepository.findByName(sensor.getName()).isEmpty()) {
            throw new SensorRegistrationException("Sensor with current name already exist");
        }
        sensorRepository.save(sensor);
    }

    public Optional<Sensor> findByName (String name) {
        Optional<Sensor> sensor = sensorRepository.findByName(name).stream().findAny();
        return sensor;
    }

}
