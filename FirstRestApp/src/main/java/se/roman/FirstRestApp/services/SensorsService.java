package se.roman.FirstRestApp.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.roman.FirstRestApp.DTO.SensorDTO;
import se.roman.FirstRestApp.models.Sensor;
import se.roman.FirstRestApp.repositories.SensorRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SensorsService {
    private final SensorRepository sensorRepository;
    private ModelMapper modelMapper;
    @Autowired
    public SensorsService(SensorRepository sensorRepository, ModelMapper modelMapper) {
        this.sensorRepository = sensorRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void registerNewSensor(SensorDTO sensorDTO){
        sensorRepository.save(enrichSensor(convert(sensorDTO)));
    }

    private Sensor convert(SensorDTO sensorDTO){
        return modelMapper.map(sensorDTO, Sensor.class);
    }

    private Sensor enrichSensor(Sensor sensor){
        sensor.setCreated_at(LocalDateTime.now());
        return sensor;
    }

    public Optional<Sensor> findByName(String sensorsName){
        return sensorRepository.findByName(sensorsName);
    }
}
