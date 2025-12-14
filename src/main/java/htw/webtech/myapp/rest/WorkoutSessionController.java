package htw.webtech.myapp.rest;

import htw.webtech.myapp.business.service.WorkoutSessionService;
import htw.webtech.myapp.persistence.entity.PerformedExerciseEntity;
import htw.webtech.myapp.rest.model.UpdatePerformedDTO;
import htw.webtech.myapp.rest.model.WorkoutSessionDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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

    // 1. START: User ID aus dem Token holen und übergeben
    @PostMapping("/sessions/start/{workoutId}")
    public WorkoutSessionDTO start(@PathVariable Long workoutId, @AuthenticationPrincipal Jwt token) {
        String userId = token.getSubject(); // Das ist die User-ID von Auth0
        return service.start(workoutId, userId);
    }

    // 2. DASHBOARD: User ID nutzen
    @GetMapping("/dashboard")
    public Map<String, Integer> getDashboardStats(@AuthenticationPrincipal Jwt token) {
        return service.getDashboardStats(token.getSubject());
    }

    // 3. ALLE SESSIONS (History): User ID nutzen
    @GetMapping("/sessions")
    public List<WorkoutSessionDTO> getAllSessions(@AuthenticationPrincipal Jwt token) {
        return service.getAllSessions(token.getSubject());
    }

    // 4. WOCHEN-STATS: User ID nutzen
    @GetMapping("/sessions/week")
    public List<WorkoutSessionDTO> getSessionsWeek(@AuthenticationPrincipal Jwt token) {
        return service.getSessionsThisWeek(token.getSubject());
    }

    // --- Der Rest braucht keine Änderung (Stop, Update, etc.) ---

    @PutMapping("/performed/{id}")
    public void updatePerformed(@PathVariable Long id, @RequestBody UpdatePerformedDTO dto) {
        service.updatePerformedExercise(id, dto.reps(), dto.weight());
    }

    @GetMapping("/sessions/{sessionId}/exercises")
    public List<PerformedExerciseEntity> getSessionExercises(@PathVariable Long sessionId) {
        return service.getExercisesForSession(sessionId);
    }

    @PostMapping("/sessions/end/{sessionId}")
    public WorkoutSessionDTO stop(@PathVariable Long sessionId, @RequestBody(required = false) Map<String, Integer> body) {
        Integer calories = (body != null) ? body.get("calories") : null;
        return service.stop(sessionId, calories);
    }
}