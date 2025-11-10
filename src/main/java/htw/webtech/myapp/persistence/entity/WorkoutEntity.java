package htw.webtech.myapp.persistence.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workouts")
public class WorkoutEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String dayOfWeek;
    private String muskelgruppe;


    @ManyToOne
    @JoinColumn(name = "training_plan_id", nullable = true)
    private TrainingPlanEntity trainingPlan;

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseEntity> exercises = new ArrayList<>();

    public WorkoutEntity() {
    }

    public WorkoutEntity(String name, String muskelgruppe, String dayOfWeek, TrainingPlanEntity trainingPlan) {
        this.name = name;
        this.muskelgruppe = muskelgruppe;
        this.dayOfWeek = dayOfWeek;
        this.trainingPlan = trainingPlan;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMuskelgruppe() {
        return muskelgruppe;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public TrainingPlanEntity getTrainingPlan() {
        return trainingPlan;
    }

    public List<ExerciseEntity> getExercises() {
        return exercises;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMuskelgruppe(String muskelgruppe) {
        this.muskelgruppe = muskelgruppe;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setTrainingPlan(TrainingPlanEntity trainingPlan) {
        this.trainingPlan = trainingPlan;
    }

    public void setExercises(List<ExerciseEntity> exercises) {
        this.exercises = exercises;
    }

    // Hilfsmethoden für bidirektionale Beziehung
    public void addExercise(ExerciseEntity exercise) {
        exercises.add(exercise);
        exercise.setWorkout(this);
    }

    public void removeExercise(ExerciseEntity exercise) {
        exercises.remove(exercise);
        exercise.setWorkout(null);
    }
}
