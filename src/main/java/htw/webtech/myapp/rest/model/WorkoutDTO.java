package htw.webtech.myapp.rest.model;

public record WorkoutDTO(
        Long id,
        String name,
        String dayOfWeek,
        String muskelgruppe,
        Long trainingPlanId
) {}
