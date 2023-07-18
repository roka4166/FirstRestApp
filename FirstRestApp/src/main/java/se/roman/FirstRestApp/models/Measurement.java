package se.roman.FirstRestApp.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "measurements")
public class Measurement{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "value_")
    private float value;

    private Boolean raining;
    @ManyToOne
    @JoinColumn(name = "sensor_name", referencedColumnName = "name")
    private Sensor sensor;

    private LocalDateTime created_at;

}
