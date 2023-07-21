package se.roman.FirstRestApp.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import jakarta.validation.constraints.NotNull;
import se.roman.FirstRestApp.models.Sensor;


public class MeasurementDTO {
    @Min(value = -100, message = "temperature cannot be below -100 degrees")
    @Max(value = 100, message = "temperature cannot be above 100 degrees")
    @NotNull(message = "this value cannot be null")
    private Float value;
    @NotNull(message = "this field cannot be null")
    private Boolean raining;
    @NotNull(message = "this field cannot be null")
    private Sensor sensor;

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public Boolean getRaining() {
        return raining;
    }

    public void setRaining(Boolean raining) {
        this.raining = raining;
    }


}
