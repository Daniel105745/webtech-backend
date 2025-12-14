package htw.webtech.myapp.rest.model;

import java.time.LocalDateTime;

public record WorkoutSessionDTO(
        Long id,
        Long workoutId,
        String workoutName,
        LocalDateTime startTime,
        LocalDateTime endTime,
        int calories,
        long minutes // <-- Wichtig für das Diagramm im Frontend
) {}