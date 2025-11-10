package htw.webtech.myapp.persistence.entity;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class TrainingPlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String dauer;
    private String intensitaet;
    private String zielmuskeln;

    @OneToMany(mappedBy = "trainingPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<WorkoutEntity> workouts;

    public TrainingPlanEntity() {}

    public TrainingPlanEntity(String name, String dauer, String intensitaet, String zielmuskeln) {
        this.name = name;
        this.dauer = dauer;
        this.intensitaet = intensitaet;
        this.zielmuskeln = zielmuskeln;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDauer() { return dauer; }
    public void setDauer(String dauer) { this.dauer = dauer; }

    public String getIntensitaet() { return intensitaet; }
    public void setIntensitaet(String intensitaet) { this.intensitaet = intensitaet; }

    public String getZielmuskeln() { return zielmuskeln; }
    public void setZielmuskeln(String zielmuskeln) { this.zielmuskeln = zielmuskeln; }

    public List<WorkoutEntity> getWorkouts() { return workouts; }
    public void setWorkouts(List<WorkoutEntity> workouts) { this.workouts = workouts; }
}
