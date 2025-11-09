package htw.webtech.myapp.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TrainingPlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String dauer;
    private String intensitaet;
    private String zielmuskeln;

    // Getter + Setter
    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDauer() { return dauer; }
    public void setDauer(String dauer) { this.dauer = dauer; }
    public String getIntensitaet() { return intensitaet; }
    public void setIntensitaet(String intensitaet) { this.intensitaet = intensitaet; }
    public String getZielmuskeln() { return zielmuskeln; }
    public void setZielmuskeln(String zielmuskeln) { this.zielmuskeln = zielmuskeln; }
}