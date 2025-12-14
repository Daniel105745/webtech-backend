package htw.webtech.myapp.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;

@Entity
@Table(name = "workout_sessions")
public class WorkoutSessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- NEU: Hier merken wir uns, wem das gehört ---
    @Column(name = "user_id")
    private String userId;

    @ManyToOne
    @JoinColumn(name = "workout_id", nullable = true)
    private WorkoutEntity workout;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int calories;

    public boolean isActive() { return endTime == null; }

    // --- Getter & Setter für userId ---
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    // ... Deine alten Getter & Setter bleiben gleich ...
    public Long getId() { return id; }
    public void setStartTime(LocalDateTime now) { this.startTime = now; }
    public void setEndTime(LocalDateTime now) { this.endTime = now; }
    public void setCalories(int i) { this.calories = i; }
    public Temporal getStartTime() { return startTime; }
    public Temporal getEndTime() { return endTime; }
    public void setWorkout(WorkoutEntity workout) { this.workout = workout; }
    public WorkoutEntity getWorkout() { return workout; }
    public int getCalories() { return calories; }
}