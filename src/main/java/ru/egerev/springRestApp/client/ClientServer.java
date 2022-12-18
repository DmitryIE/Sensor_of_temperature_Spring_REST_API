package ru.egerev.springRestApp.client;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.egerev.springRestApp.dto.SensorDTO;
import ru.egerev.springRestApp.dto.WeatherDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClientServer {

    private static final String sensorName = "Test_Name_Sensor";
    private static final String sensorRegisterURL = "http://localhost:8080/sensors/registration";
    private static final String addMeasurementsURL = "http://localhost:8080/measurements/add";
    private static final String getMeasurementsURL = "http://localhost:8080/measurements";


    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        // регистрация сенсора
        SensorDTO sensorDTO = new SensorDTO(sensorName);
        HttpEntity<SensorDTO> request = new HttpEntity<>(sensorDTO);
        try {
            String response = restTemplate.postForObject(sensorRegisterURL, request, String.class);
        } catch (HttpClientErrorException e) {
        }

        // отправка 50 запросов со случайными температурами и "дождями"
        for (int i = 0; i < 50; i++) {
            WeatherDTO weatherDTO = createRandomWeatherDTO(sensorDTO);
            HttpEntity<WeatherDTO> requestForMeasurements = new HttpEntity<>(weatherDTO);
            restTemplate.postForObject(addMeasurementsURL, requestForMeasurements, String.class);
        }

        // получаем измерения с сервера
        ResponseEntity<List<WeatherDTO>> weatherDTOResponse
                = restTemplate.exchange(getMeasurementsURL, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<WeatherDTO>>() {
                });
        List<WeatherDTO> weatherDTOListResponse = weatherDTOResponse.getBody();
        createChart(weatherDTOListResponse);
    }

    public static WeatherDTO createRandomWeatherDTO(SensorDTO sensorDTO) {
        WeatherDTO weatherDTO = new WeatherDTO();
        Random random = new Random();
        double maxTemperature = 40.0;
        double minTemperature = 0.0;
        double randomTemperature = random.nextDouble() * maxTemperature;
        weatherDTO.setTemperature(randomTemperature);

        boolean randomRaining = random.nextBoolean();
        weatherDTO.setRaining(randomRaining);
        weatherDTO.setSensor(sensorDTO);
        return weatherDTO;
    }

    public static void createChart(List<WeatherDTO> weatherDTOList) {
        List<Double> temperatureList = weatherDTOList.stream().map(x -> x.getTemperature()).toList();
        List<Integer> measurementsForOrder = new ArrayList<>();
        for (int i = 1; i <= temperatureList.size(); i++) {
            measurementsForOrder.add(i);
        }
        XYChart chart = QuickChart.getChart("Chart of temperatures",
                "Serial number of the measurement", "Temperatures", "temperature", measurementsForOrder, temperatureList);
        new SwingWrapper(chart).displayChart();
    }
}
