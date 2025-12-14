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

    @ManyToOne
    @JoinColumn(name = "workout_id", nullable = false)
    private WorkoutEntity workout;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private int calories;

    public boolean isActive() {
        return endTime == null;
    }

    public void setStartTime(LocalDateTime now) {
        this.startTime = now;
    }
    public void setEndTime(LocalDateTime now) {
        this.endTime = now;
    }

    public void setCalories(int i) {
        this.calories = i;
    }

    public Temporal getStartTime() {
    return startTime;}

    public Temporal getEndTime() {
        return endTime;
    }

    public void setWorkout(WorkoutEntity workout) {
        this.workout = workout;
    }

    public WorkoutEntity getWorkout() {
        return workout;
    }

    public Long getId() {
        return id;
    }

    public int getCalories() {
        return calories;
    }


    // Getter & Setter
}
