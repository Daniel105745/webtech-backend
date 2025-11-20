package htw.webtech.myapp.rest.model;

public record ExerciseDTO(
        Long id,
        String name,
        int saetze,
        int wiederholungen,
        double gewicht,
        Long workoutId
) {}

