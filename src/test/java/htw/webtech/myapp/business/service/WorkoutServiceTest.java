package htw.webtech.myapp.business.service;

import htw.webtech.myapp.persistence.entity.TrainingPlanEntity;
import htw.webtech.myapp.persistence.entity.WorkoutEntity;
import htw.webtech.myapp.persistence.entity.WorkoutSessionEntity;
import htw.webtech.myapp.persistence.repository.TrainingPlanRepository;
import htw.webtech.myapp.persistence.repository.WorkoutRepository;
import htw.webtech.myapp.persistence.repository.WorkoutSessionRepository;
import htw.webtech.myapp.rest.model.WorkoutDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutServiceTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private TrainingPlanRepository trainingPlanRepository;

    @Mock
    private WorkoutSessionRepository workoutSessionRepository;

    @InjectMocks
    private WorkoutService service;

    TrainingPlanEntity plan;
    WorkoutEntity workout;

    @BeforeEach
    void setup() {
        plan = new TrainingPlanEntity("Plan A", "30 min", "High", "Beine");
        setPrivateId(plan, "id", 1L);

        workout = new WorkoutEntity("Workout A", "Beine", "Mon", plan);
        setPrivateId(workout, "id", 10L);
    }

    // --------------------------------------------------------------
    // TEST 1: getAllByTrainingPlan()
    // --------------------------------------------------------------
    @Test
    void testGetAllByTrainingPlan() {
        when(workoutRepository.findByTrainingPlanId(1L)).thenReturn(List.of(workout));

        List<WorkoutDTO> result = service.getAllByTrainingPlan(1L);

        assertEquals(1, result.size());
        assertEquals("Workout A", result.get(0).name());
        assertEquals("Beine", result.get(0).muskelgruppe());
    }

    // --------------------------------------------------------------
    // TEST 2: getWorkoutById() SUCCESS
    // --------------------------------------------------------------
    @Test
    void testGetWorkoutByIdSuccess() {
        when(workoutRepository.findById(10L)).thenReturn(Optional.of(workout));

        WorkoutDTO result = service.getWorkoutById(10L);

        assertNotNull(result);
        assertEquals("Workout A", result.name());
    }

    // --------------------------------------------------------------
    // TEST 3: getWorkoutById() FAIL
    // --------------------------------------------------------------
    @Test
    void testGetWorkoutByIdNotFound() {
        when(workoutRepository.findById(99L)).thenReturn(Optional.empty());

        WorkoutDTO result = service.getWorkoutById(99L);

        assertNull(result);
    }

    // --------------------------------------------------------------
    // TEST 4: createWorkout() SUCCESS
    // --------------------------------------------------------------
    @Test
    void testCreateWorkoutSuccess() {
        WorkoutDTO dto = new WorkoutDTO(null, "New Workout", "Tue", "Arme", 1L);

        when(trainingPlanRepository.findById(1L)).thenReturn(Optional.of(plan));
        when(workoutRepository.save(any())).thenAnswer(invocation -> {
            WorkoutEntity e = invocation.getArgument(0);
            setPrivateId(e, "id", 20L);
            return e;
        });

        WorkoutDTO result = service.createWorkout(1L, dto);

        assertNotNull(result);
        assertEquals("New Workout", result.name());
        assertEquals("Arme", result.muskelgruppe());
        verify(workoutRepository, times(1)).save(any());
    }

    // --------------------------------------------------------------
    // TEST 5: createWorkout() FAIL (plan not found)
    // --------------------------------------------------------------
    @Test
    void testCreateWorkoutPlanNotFound() {
        when(trainingPlanRepository.findById(99L)).thenReturn(Optional.empty());

        WorkoutDTO dto = new WorkoutDTO(null, "X", "Y", "Z", 99L);

        assertThrows(RuntimeException.class, () -> service.createWorkout(99L, dto));
    }

    // --------------------------------------------------------------
    // TEST 6: updateWorkout() SUCCESS
    // --------------------------------------------------------------
    @Test
    void testUpdateWorkoutSuccess() {
        WorkoutDTO dto = new WorkoutDTO(10L, "Updated", "Wed", "Rücken", 1L);

        when(workoutRepository.findById(10L)).thenReturn(Optional.of(workout));
        when(workoutRepository.save(any())).thenReturn(workout);

        WorkoutDTO result = service.updateWorkout(10L, dto);

        assertNotNull(result);
        assertEquals("Updated", result.name());
        assertEquals("Rücken", result.muskelgruppe());
    }

    // --------------------------------------------------------------
    // TEST 7: updateWorkout() FAIL
    // --------------------------------------------------------------
    @Test
    void testUpdateWorkoutNotFound() {
        when(workoutRepository.findById(99L)).thenReturn(Optional.empty());

        WorkoutDTO dto = new WorkoutDTO(99L, "X", "Y", "Z", 1L);

        WorkoutDTO result = service.updateWorkout(99L, dto);

        assertNull(result);
    }

    // --------------------------------------------------------------
    // TEST 8: deleteWorkout() SUCCESS (no sessions linked)
    // --------------------------------------------------------------
    @Test
    void testDeleteWorkoutSuccessNoSessions() {
        when(workoutRepository.existsById(10L)).thenReturn(true);
        when(workoutSessionRepository.findAll()).thenReturn(List.of());

        boolean deleted = service.deleteWorkout(10L);

        assertTrue(deleted);
        verify(workoutRepository, times(1)).deleteById(10L);
        verify(workoutSessionRepository, never()).save(any());
    }

    // --------------------------------------------------------------
    // TEST 9: deleteWorkout() SUCCESS (sessions linked)
    // --------------------------------------------------------------
    @Test
    void testDeleteWorkoutSuccessWithSessions() {
        WorkoutSessionEntity session = new WorkoutSessionEntity();
        setPrivateId(session, "id", 100L);
        session.setWorkout(workout);

        when(workoutRepository.existsById(10L)).thenReturn(true);
        when(workoutSessionRepository.findAll()).thenReturn(List.of(session));

        boolean deleted = service.deleteWorkout(10L);

        assertTrue(deleted);
        assertNull(session.getWorkout());
        verify(workoutSessionRepository, times(1)).save(session);
        verify(workoutRepository, times(1)).deleteById(10L);
    }

    // --------------------------------------------------------------
    // TEST 10: deleteWorkout() FAIL (not exist)
    // --------------------------------------------------------------
    @Test
    void testDeleteWorkoutNotFound() {
        when(workoutRepository.existsById(99L)).thenReturn(false);

        boolean deleted = service.deleteWorkout(99L);

        assertFalse(deleted);
        verify(workoutRepository, never()).deleteById(any());
        verify(workoutSessionRepository, never()).findAll();
    }

    // --------------------------------------------------------------
    // Helper: set private id durch reflection
    // --------------------------------------------------------------
    private static void setPrivateId(Object target, String fieldName, Long value) {
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, value);
        } catch (Exception e) {
            fail("Cannot set private field '" + fieldName + "' on " + target.getClass().getSimpleName());
        }
    }
}
