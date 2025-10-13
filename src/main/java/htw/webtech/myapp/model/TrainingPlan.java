package htw.webtech.myapp.model;

public class TrainingPlan {
    private String name;
    private String goal;
    private int durationWeeks;

    public TrainingPlan() {
    }

    public TrainingPlan(String name, String goal, int durationWeeks) {
        this.name = name;
        this.goal = goal;
        this.durationWeeks = durationWeeks;
    }

    // Getter & Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public int getDurationWeeks() {
        return durationWeeks;
    }

    public void setDurationWeeks(int durationWeeks) {
        this.durationWeeks = durationWeeks;
    }
}
