package se.roman.FirstRestApp.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import se.roman.FirstRestApp.DTO.MeasurementDTO;
import se.roman.FirstRestApp.DTO.MeasurementResponse;
import se.roman.FirstRestApp.DTO.SensorDTO;
import se.roman.FirstRestApp.models.Measurement;
import se.roman.FirstRestApp.models.Sensor;
import se.roman.FirstRestApp.services.MeasurementsService;
import se.roman.FirstRestApp.utils.MeasurementErrorResponse;
import se.roman.FirstRestApp.utils.MeasurementNotCreatedException;
import se.roman.FirstRestApp.utils.MeasurementValidator;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
public class MeasurementsController {
    private final MeasurementsService measurementsService;
    private final MeasurementValidator measurementValidator;
    private final ModelMapper modelMapper;
    @Autowired
    public MeasurementsController(MeasurementsService measurementsService, MeasurementValidator measurementValidator, ModelMapper modelMapper) {
        this.measurementsService = measurementsService;
        this.measurementValidator = measurementValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping("")
    public MeasurementResponse getAll(){
        return new MeasurementResponse(measurementsService.getAll().stream().map(this::convertToMeasurementDTO).collect(Collectors.toList()));
    }

    @GetMapping("/rainydayscount")
    public int getRainyDaysCount(){
        return measurementsService.countRainyDays(true);
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid MeasurementDTO measurementDTO, BindingResult bindingResult){
        measurementValidator.validate(measurementDTO, bindingResult);
        if(bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errorMessage.append(fieldError.getField()).append(" -- ").append(fieldError.getDefaultMessage())
                        .append(": ");
            }
            throw new MeasurementNotCreatedException(errorMessage.toString());
        }
        measurementsService.save(convertToMeasurement(measurementDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<MeasurementErrorResponse> error(MeasurementNotCreatedException e){
        return new ResponseEntity<>(new MeasurementErrorResponse("measurement was not created  " + e.getMessage(), System.currentTimeMillis()), HttpStatus.BAD_REQUEST);
    }

    private SensorDTO convertToSensorDTO(Sensor sensor){
        return modelMapper.map(sensor, SensorDTO.class);
    }

    private Measurement convertToMeasurement(MeasurementDTO measurementDTO){
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement){
        return modelMapper.map(measurement, MeasurementDTO.class);
    }


}
