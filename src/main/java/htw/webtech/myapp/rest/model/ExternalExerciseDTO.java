package htw.webtech.myapp.rest.model;

import lombok.Data;

@Data
public class ExternalExerciseDTO {
    private String name;
    private String type;
    private String muscle;
    private String equipment;
    private String difficulty;
    private String instructions;
}
