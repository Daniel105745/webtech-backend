package htw.webtech.myapp.rest.controller;

import htw.webtech.myapp.business.service.WorkoutSessionService;
import htw.webtech.myapp.persistence.entity.PerformedExerciseEntity;
import htw.webtech.myapp.rest.model.UpdatePerformedDTO;
import htw.webtech.myapp.rest.model.WorkoutSessionDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
// ... Imports wie gehabt (List, Map etc.) ...


@RestController
@CrossOrigin(origins = { "http://localhost:5173", "https://webtech-frontend-fe2m.onrender.com" })
@RequestMapping("/api")
public class WorkoutSessionController {

    private final WorkoutSessionService service;

    public WorkoutSessionController(WorkoutSessionService service) {
        this.service = service;
    }

    // --- NEU: Endpoint zum Updaten einer durchgeführten Übung ---
    @PutMapping("/performed/{id}")
    public void updatePerformed(@PathVariable Long id, @RequestBody UpdatePerformedDTO dto) {
        service.updatePerformedExercise(id, dto.reps(), dto.weight());
    }

    // --- NEU: Endpoint zum Laden der Details (für History & Live-View) ---
    @GetMapping("/sessions/{sessionId}/exercises")
    public List<PerformedExerciseEntity> getSessionExercises(@PathVariable Long sessionId) {
        return service.getExercisesForSession(sessionId);
    }

    // ... Rest bleibt gleich (Start, Stop, Dashboard, Sessions) ...
    @PostMapping("/sessions/start/{workoutId}")
    public WorkoutSessionDTO start(@PathVariable Long workoutId) { return service.start(workoutId); }

    @PostMapping("/sessions/end/{sessionId}")
    public WorkoutSessionDTO stop(@PathVariable Long sessionId, @RequestBody(required = false) Map<String, Integer> body) {
        Integer calories = (body != null) ? body.get("calories") : null;
        return service.stop(sessionId, calories);
    }

    @GetMapping("/dashboard")
    public Map<String, Integer> getDashboardStats() { return service.getDashboardStats(); }

    @GetMapping("/sessions")
    public List<WorkoutSessionDTO> getAllSessions() { return service.getAllSessions(); }

    @GetMapping("/sessions/week")
    public List<WorkoutSessionDTO> getSessionsWeek() { return service.getSessionsThisWeek(); }
}