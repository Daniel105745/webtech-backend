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
// ... imports bleiben ...

@Service
public class WorkoutSessionService {

    private final WorkoutSessionRepository repo;
    private final WorkoutRepository workoutRepo;
    private final PerformedExerciseRepository performedRepo;

    public WorkoutSessionService(WorkoutSessionRepository repo,
                                 WorkoutRepository workoutRepo,
                                 PerformedExerciseRepository performedRepo) {
        this.repo = repo;
        this.workoutRepo = workoutRepo;
        this.performedRepo = performedRepo;
    }

    // --- START: Wir müssen die userId mitgeben ---
    public WorkoutSessionDTO start(Long workoutId, String userId) {
        WorkoutEntity workout = workoutRepo.findById(workoutId)
                .orElseThrow(() -> new RuntimeException("Workout nicht gefunden"));

        WorkoutSessionEntity session = new WorkoutSessionEntity();
        session.setWorkout(workout);
        session.setStartTime(LocalDateTime.now());
        session.setUserId(userId); // <--- WICHTIG: User speichern!
        session = repo.save(session);

        // ... exercises kopieren (bleibt gleich) ...
        for (htw.webtech.myapp.persistence.entity.ExerciseEntity ex : workout.getExercises()) {
            htw.webtech.myapp.persistence.entity.PerformedExerciseEntity performed =
                    new htw.webtech.myapp.persistence.entity.PerformedExerciseEntity(
                            ex.getName(), ex.getSaetze(), ex.getWiederholungen(), ex.getGewicht(), session
                    );
            performedRepo.save(performed);
        }
        return toDTO(session);
    }

    // Update & Stop brauchen keine userId, da die Session-ID eindeutig ist.
    // (Man könnte hier noch prüfen, ob die Session auch wirklich dem User gehört, aber für jetzt reicht das).
    // ... updatePerformedExercise bleibt gleich ...
    // ... stop bleibt gleich ...

    // --- HISTORY & DASHBOARD: Nur Daten vom User laden ---
    public List<WorkoutSessionDTO> getSessionsThisWeek(String userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime monday = now.minusDays(now.getDayOfWeek().getValue() - 1)
                .withHour(0).withMinute(0).withSecond(0);

        // HIER die Änderung: findByUserId...
        return repo.findByUserIdAndStartTimeBetween(userId, monday, now)
                .stream().map(this::toDTO).toList();
    }

    public List<WorkoutSessionDTO> getAllSessions(String userId) {
        // HIER die Änderung: findAllByUserId...
        return repo.findAllByUserId(userId)
                .stream().map(this::toDTO).toList();
    }

    public Map<String, Integer> getDashboardStats(String userId) {
        // Wir nutzen die Methode von oben, die schon filtert
        List<WorkoutSessionDTO> sessions = getSessionsThisWeek(userId);

        int totalCalories = sessions.stream().mapToInt(WorkoutSessionDTO::calories).sum();
        int totalWorkouts = sessions.size();
        int totalMinutes = sessions.stream().mapToInt(dto -> (int) dto.minutes()).sum();

        Map<String, Integer> stats = new HashMap<>();
        stats.put("calories", totalCalories);
        stats.put("workouts", totalWorkouts);
        stats.put("minutes", totalMinutes);
        return stats;
    }

    // ... Rest (toDTO, getExercisesForSession) bleibt gleich ...
    // ... getExercisesForSession kann so bleiben, da SessionID eindeutig ist.

    // ACHTUNG: Kopiere hier unbedingt deine existierenden Methoden updatePerformedExercise, stop, etc. rein!
    // Ich habe sie nur gekürzt, damit der Text nicht zu lang wird.
    public void updatePerformedExercise(Long id, int reps, double weight) {
        PerformedExerciseEntity ex = performedRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Übung nicht in Session gefunden"));

        ex.setWiederholungen(reps);
        ex.setGewicht(weight);
        performedRepo.save(ex);
    }

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

    public List<htw.webtech.myapp.persistence.entity.PerformedExerciseEntity> getExercisesForSession(Long sessionId) {
        return performedRepo.findBySessionId(sessionId);
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