package se.roman.FirstRestApp.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import se.roman.FirstRestApp.DTO.MeasurementDTO;
import se.roman.FirstRestApp.models.Measurement;
import se.roman.FirstRestApp.models.Sensor;
import se.roman.FirstRestApp.repositories.MeasurementRepository;
import se.roman.FirstRestApp.repositories.SensorRepository;

import java.util.Optional;

@Component
public class MeasurementValidator implements Validator {

    private final SensorRepository sensorRepository;
    @Autowired
    public MeasurementValidator(MeasurementRepository measurementRepository, SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return MeasurementDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MeasurementDTO measurementDTO = (MeasurementDTO) target;
        Optional<Sensor> sensor = sensorRepository.findByName(measurementDTO.getSensor().getName());
        if(sensor.isEmpty()){
            errors.rejectValue("sensor","", "This sensor does not exist");
        }

    }
}
