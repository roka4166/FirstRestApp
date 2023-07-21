package se.roman.FirstRestApp.controllers;


import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import se.roman.FirstRestApp.DTO.SensorDTO;
import se.roman.FirstRestApp.models.Sensor;
import se.roman.FirstRestApp.services.SensorsService;
import se.roman.FirstRestApp.utils.SensorErrorResponse;
import se.roman.FirstRestApp.utils.SensorNotCreatedException;
import se.roman.FirstRestApp.utils.SensorValidator;

import java.util.List;

@RestController
@RequestMapping("/sensors")
public class SensorsController {
    private final ModelMapper modelMapper;
    private final SensorsService sensorsService;
    private final SensorValidator sensorValidator;
    @Autowired
    public SensorsController(ModelMapper modelMapper, SensorsService sensorsService, SensorValidator sensorValidator) {
        this.modelMapper = modelMapper;
        this.sensorsService = sensorsService;
        this.sensorValidator = sensorValidator;
    }

    @PostMapping("/register")
    public ResponseEntity<HttpStatus> register(@RequestBody @Valid SensorDTO sensorDTO, BindingResult bindingResult){
        sensorValidator.validate(convertToSensor(sensorDTO), bindingResult);
        if(bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errorMessage.append(fieldError.getField()).append(" -- ").append(fieldError.getDefaultMessage())
                        .append(": ");
            }
            throw new SensorNotCreatedException(errorMessage.toString());
        }
        sensorsService.registerNewSensor(sensorDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<SensorErrorResponse> error(SensorNotCreatedException e){
        return new ResponseEntity<>(new SensorErrorResponse("sensor was not created "+ e.getMessage(), System.currentTimeMillis()), HttpStatus.BAD_REQUEST);
    }

    private Sensor convertToSensor(SensorDTO sensorDTO){
        return modelMapper.map(sensorDTO, Sensor.class);
    }




}
