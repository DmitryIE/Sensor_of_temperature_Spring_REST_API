package ru.egerev.springRestApp.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.egerev.springRestApp.dto.SensorDTO;
import ru.egerev.springRestApp.dto.WeatherDTO;
import ru.egerev.springRestApp.models.Sensor;
import ru.egerev.springRestApp.models.Weather;
import ru.egerev.springRestApp.services.WeatherService;
import ru.egerev.springRestApp.util.WeatherAddException;
import ru.egerev.springRestApp.util.WeatherErrorResponse;

import java.util.List;


@Controller
@RequestMapping("/measurements")
public class WeatherController {

    private WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> addMeasurements(@RequestBody @Valid WeatherDTO weatherDTO,
                                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrorList) {
                errors.append(fieldError.getField())
                        .append(" - ").append(fieldError.getDefaultMessage())
                        .append(";");
            }
            throw new WeatherAddException(errors.toString());
        }
        weatherService.addMeasurements(convertToWeather(weatherDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private Weather convertToWeather(WeatherDTO weatherDTO) {
        Weather weather = new Weather();
        weather.setTemperature(weatherDTO.getTemperature());
        weather.setRaining(weatherDTO.getRaining());

        Sensor sensor = new Sensor();
        sensor.setName(weatherDTO.getSensor().getName());
        weather.setSensor(sensor);

        return weather;
    }

    private WeatherDTO convertToWeatherDTO(Weather weather) {
        WeatherDTO weatherDTO = new WeatherDTO();
        weatherDTO.setTemperature(weather.getTemperature());
        weatherDTO.setRaining(weather.isRaining());
        weatherDTO.setDateOfMeasurement(weather.getDateOfMeasurement());
        SensorDTO sensor = new SensorDTO();
        sensor.setName(weather.getSensor().getName());
        weatherDTO.setSensor(sensor);
        return weatherDTO;
    }

    @ExceptionHandler
    private ResponseEntity<WeatherErrorResponse> handleException(WeatherAddException e) {
        WeatherErrorResponse response = new WeatherErrorResponse(
                e.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @GetMapping
    public List<WeatherDTO> getAllMeasurements() {
        return weatherService.getMeasurements().stream()
                .map(weather -> convertToWeatherDTO(weather))
                .toList();
    }

    @ResponseBody
    @GetMapping("/rainyDaysCount")
    public int getNumberOfRainyDays() {
        return weatherService.NumberOfRainyDays();
    }

}
