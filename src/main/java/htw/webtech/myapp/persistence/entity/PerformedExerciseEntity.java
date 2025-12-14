package htw.webtech.myapp.persistence.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "performed_exercises")
public class PerformedExerciseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int saetze;
    private int wiederholungen;
    private double gewicht;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    @JsonBackReference
    private WorkoutSessionEntity session;

    public PerformedExerciseEntity() {}

    public PerformedExerciseEntity(String name, int saetze, int wiederholungen, double gewicht, WorkoutSessionEntity session) {
        this.name = name;
        this.saetze = saetze;
        this.wiederholungen = wiederholungen;
        this.gewicht = gewicht;
        this.session = session;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getSaetze() { return saetze; }
    public int getWiederholungen() { return wiederholungen; }
    public double getGewicht() { return gewicht; }

    // Setter für Updates im Frontend
    public void setSaetze(int saetze) { this.saetze = saetze; }
    public void setWiederholungen(int wiederholungen) { this.wiederholungen = wiederholungen; }
    public void setGewicht(double gewicht) { this.gewicht = gewicht; }

    public void setSession(WorkoutSessionEntity session) { this.session = session; }
}