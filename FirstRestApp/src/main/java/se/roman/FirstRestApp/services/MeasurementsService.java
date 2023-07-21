package se.roman.FirstRestApp.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.roman.FirstRestApp.models.Measurement;
import se.roman.FirstRestApp.repositories.MeasurementRepository;
import se.roman.FirstRestApp.repositories.SensorRepository;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class MeasurementsService {
   private final MeasurementRepository measurementRepository;

   private final SensorRepository sensorRepository;
    @Autowired
    public MeasurementsService(MeasurementRepository measurementRepository, SensorRepository sensorRepository) {
        this.measurementRepository = measurementRepository;
        this.sensorRepository = sensorRepository;
    }

    public List<Measurement> getAll(){
        return measurementRepository.findAll();
    }

    @Transactional
    public void save(Measurement measurement){
        Measurement enrichedMeasurement  = enrichMeasurement(measurement);
        measurementRepository.save(enrichedMeasurement);
    }

    private Measurement enrichMeasurement(Measurement measurement){
        measurement.setSensor(sensorRepository.findByName(measurement.getSensor().getName()).orElse(null));
        measurement.setCreated_at(LocalDateTime.now());
        return measurement;
    }

    public int countRainyDays(Boolean raining) {
        return measurementRepository.countByRaining(raining);
    }
}

