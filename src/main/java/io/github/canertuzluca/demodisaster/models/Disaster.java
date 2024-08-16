package io.github.canertuzluca.demodisaster.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Disaster {
    @JsonProperty("disasterType")
    private String disasterType;
    @JsonProperty("locations")
    private List<Location> disasterLocation;
}