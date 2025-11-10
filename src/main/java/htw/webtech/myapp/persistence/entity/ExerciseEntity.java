package htw.webtech.myapp.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "exercises")
public class ExerciseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int saetze;
    private int wiederholungen;
    private double gewicht;

    @ManyToOne
    @JoinColumn(name = "workout_id", nullable = false)
    private WorkoutEntity workout;

    public ExerciseEntity() {
    }

    public ExerciseEntity(String name, int saetze, int wiederholungen, double gewicht, WorkoutEntity workout) {
        this.name = name;
        this.saetze = saetze;
        this.wiederholungen = wiederholungen;
        this.gewicht = gewicht;
        this.workout = workout;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSaetze() {
        return saetze;
    }

    public int getWiederholungen() {
        return wiederholungen;
    }

    public double getGewicht() {
        return gewicht;
    }

    public WorkoutEntity getWorkout() {
        return workout;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSaetze(int saetze) {
        this.saetze = saetze;
    }

    public void setWiederholungen(int wiederholungen) {
        this.wiederholungen = wiederholungen;
    }

    public void setGewicht(double gewicht) {
        this.gewicht = gewicht;
    }

    public void setWorkout(WorkoutEntity workout) {
        this.workout = workout;
    }
}
