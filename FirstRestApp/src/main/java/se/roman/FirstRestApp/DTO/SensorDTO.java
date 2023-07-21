package se.roman.FirstRestApp.DTO;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;


public class SensorDTO {
    @Size(min = 3, max = 30, message = "sensors name must be between 3 and 30 characters")
    @NotEmpty(message = "this field cannot be empty")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
