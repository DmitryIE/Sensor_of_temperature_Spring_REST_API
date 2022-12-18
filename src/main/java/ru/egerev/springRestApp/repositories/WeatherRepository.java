package ru.egerev.springRestApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.egerev.springRestApp.models.Weather;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Integer> {
}
