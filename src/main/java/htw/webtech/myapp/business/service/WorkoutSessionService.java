package htw.webtech.myapp.business.service;

import htw.webtech.myapp.persistence.entity.ExerciseEntity;
import htw.webtech.myapp.persistence.entity.PerformedExerciseEntity;
import htw.webtech.myapp.persistence.entity.WorkoutEntity;
import htw.webtech.myapp.persistence.entity.WorkoutSessionEntity;
import htw.webtech.myapp.persistence.repository.PerformedExerciseRepository;
import htw.webtech.myapp.persistence.repository.WorkoutRepository;
import htw.webtech.myapp.persistence.repository.WorkoutSessionRepository;
import htw.webtech.myapp.rest.model.WorkoutSessionDTO;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WorkoutSessionService {

    private final WorkoutSessionRepository repo;
    private final WorkoutRepository workoutRepo;
    private final PerformedExerciseRepository performedRepo; // <--- NEU

    public WorkoutSessionService(WorkoutSessionRepository repo,
                                 WorkoutRepository workoutRepo,
                                 PerformedExerciseRepository performedRepo) {
        this.repo = repo;
        this.workoutRepo = workoutRepo;
        this.performedRepo = performedRepo;
    }

    // --- 1. START: Kopiert Plan -> Live-Session (Snapshot) ---
    public WorkoutSessionDTO start(Long workoutId) {
        WorkoutEntity workout = workoutRepo.findById(workoutId)
                .orElseThrow(() -> new RuntimeException("Workout nicht gefunden"));

        // Session anlegen
        WorkoutSessionEntity session = new WorkoutSessionEntity();
        session.setWorkout(workout);
        session.setStartTime(LocalDateTime.now());
        session = repo.save(session);

        // Übungen kopieren (Einfacher Snapshot)
        for (ExerciseEntity ex : workout.getExercises()) {
            PerformedExerciseEntity performed = new PerformedExerciseEntity(
                    ex.getName(),
                    ex.getSaetze(),
                    ex.getWiederholungen(),
                    ex.getGewicht(),
                    session
            );
            performedRepo.save(performed);
        }

        return toDTO(session);
    }

    // --- 2. UPDATE: Werte während des Trainings ändern ---
    public void updatePerformedExercise(Long id, int reps, double weight) {
        PerformedExerciseEntity ex = performedRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Übung nicht in Session gefunden"));

        ex.setWiederholungen(reps);
        ex.setGewicht(weight);
        performedRepo.save(ex);
    }

    // --- 3. STOP: Nur noch Zeit & Kalorien setzen ---
    public WorkoutSessionDTO stop(Long sessionId, Integer calories) {
        WorkoutSessionEntity s = repo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session nicht gefunden"));

        if (s.getEndTime() == null) {
            s.setEndTime(LocalDateTime.now());
            if (calories != null && calories >= 0) {
                s.setCalories(calories);
            } else {
                long minutes = Duration.between(s.getStartTime(), s.getEndTime()).toMinutes();
                s.setCalories((int)(minutes * 7));
            }
            repo.save(s);
        }
        return toDTO(s);
    }

    // Helper für Frontend (History & Live View)
    public List<PerformedExerciseEntity> getExercisesForSession(Long sessionId) {
        return performedRepo.findBySessionId(sessionId);
    }

    // ... Rest bleibt gleich (Statistiken etc.) ...
    public List<WorkoutSessionDTO> getSessionsThisWeek() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime monday = now.minusDays(now.getDayOfWeek().getValue() - 1)
                .withHour(0).withMinute(0).withSecond(0);
        return repo.findByStartTimeBetween(monday, now).stream().map(this::toDTO).toList();
    }

    public List<WorkoutSessionDTO> getAllSessions() {
        return repo.findAll().stream().map(this::toDTO).toList();
    }

    public Map<String, Integer> getDashboardStats() {
        List<WorkoutSessionDTO> sessions = getSessionsThisWeek();
        int totalCalories = sessions.stream().mapToInt(WorkoutSessionDTO::calories).sum();
        int totalWorkouts = sessions.size();
        int totalMinutes = sessions.stream().mapToInt(dto -> (int) dto.minutes()).sum();
        Map<String, Integer> stats = new HashMap<>();
        stats.put("calories", totalCalories);
        stats.put("workouts", totalWorkouts);
        stats.put("minutes", totalMinutes);
        return stats;
    }

    private WorkoutSessionDTO toDTO(WorkoutSessionEntity entity) {
        long minutes = 0;
        if (entity.getStartTime() != null && entity.getEndTime() != null) {
            minutes = Duration.between(entity.getStartTime(), entity.getEndTime()).toMinutes();
        }
        return new WorkoutSessionDTO(
                entity.getId(),
                entity.getWorkout() != null ? entity.getWorkout().getId() : null,
                entity.getWorkout() != null ? entity.getWorkout().getName() : "Gelöscht",
                (LocalDateTime) entity.getStartTime(),
                (LocalDateTime) entity.getEndTime(),
                entity.getCalories(),
                minutes
        );
    }
}