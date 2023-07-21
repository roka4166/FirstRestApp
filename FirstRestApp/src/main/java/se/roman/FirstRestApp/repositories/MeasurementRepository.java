package se.roman.FirstRestApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.roman.FirstRestApp.models.Measurement;

public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {
    int countByRaining(Boolean raining);


}
