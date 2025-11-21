package htw.webtech.myapp.business.service;

import htw.webtech.myapp.persistence.entity.TrainingPlanEntity;
import htw.webtech.myapp.persistence.entity.WorkoutEntity;
import htw.webtech.myapp.persistence.repository.TrainingPlanRepository;
import htw.webtech.myapp.persistence.repository.WorkoutRepository;
import htw.webtech.myapp.rest.model.WorkoutDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
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

    private WorkoutService workoutService;

    private WorkoutEntity workout1;
    private TrainingPlanEntity plan;

    @BeforeEach
    void setup() throws Exception {
        workoutService = new WorkoutService(workoutRepository, trainingPlanRepository);

        // Tạo TrainingPlan mẫu
        plan = new TrainingPlanEntity("Plan A", "4 weeks", "Medium", "Chest");
        setId(plan, 1L);

        // Tạo Workout mẫu
        workout1 = new WorkoutEntity("Workout 1", "Chest", "Monday", plan);
        setId(workout1, 1L);
    }

    // Reflection để set id private
    private void setId(Object entity, Long id) throws Exception {
        Field field = entity.getClass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(entity, id);
    }

    // =======================================================
    // 1. GET all workouts by training plan
    // =======================================================
    @Test
    void testGetAllByTrainingPlan() {
        when(workoutRepository.findByTrainingPlanId(1L)).thenReturn(List.of(workout1));

        List<WorkoutDTO> result = workoutService.getAllByTrainingPlan(1L);

        assertEquals(1, result.size());
        assertEquals("Workout 1", result.get(0).name());
        verify(workoutRepository, times(1)).findByTrainingPlanId(1L);
    }

    // =======================================================
    // 2. GET workout by id FOUND
    // =======================================================
    @Test
    void testGetWorkoutByIdFound() {
        when(workoutRepository.findById(1L)).thenReturn(Optional.of(workout1));

        WorkoutDTO dto = workoutService.getWorkoutById(1L);

        assertNotNull(dto);
        assertEquals("Workout 1", dto.name());
    }

    // =======================================================
    // 3. GET workout by id NOT FOUND
    // =======================================================
    @Test
    void testGetWorkoutByIdNotFound() {
        when(workoutRepository.findById(99L)).thenReturn(Optional.empty());

        WorkoutDTO dto = workoutService.getWorkoutById(99L);

        assertNull(dto);
    }

    // =======================================================
    // 4. CREATE workout
    // =======================================================
    @Test
    void testCreateWorkout() throws Exception {
        WorkoutDTO dto = new WorkoutDTO(null, "Workout 2", "Tuesday", "Back", 1L);

        when(trainingPlanRepository.findById(1L)).thenReturn(Optional.of(plan));
        when(workoutRepository.save(Mockito.any())).thenAnswer(invocation -> {
            WorkoutEntity entity = invocation.getArgument(0);
            setId(entity, 2L);
            return entity;
        });

        WorkoutDTO created = workoutService.createWorkout(1L, dto);

        assertNotNull(created);
        assertEquals("Workout 2", created.name());
        assertEquals(1L, created.trainingPlanId());
        verify(workoutRepository, times(1)).save(Mockito.any());
    }

    // =======================================================
    // 5. CREATE workout PLAN NOT FOUND
    // =======================================================
    @Test
    void testCreateWorkoutPlanNotFound() {
        WorkoutDTO dto = new WorkoutDTO(null, "Workout 2", "Tuesday", "Back", 99L);

        when(trainingPlanRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> workoutService.createWorkout(99L, dto));
    }

    // =======================================================
    // 6. UPDATE workout FOUND
    // =======================================================
    @Test
    void testUpdateWorkoutFound() {
        WorkoutDTO dto = new WorkoutDTO(1L, "Workout 1 Modified", "Monday", "Chest", 1L);

        when(workoutRepository.findById(1L)).thenReturn(Optional.of(workout1));
        when(workoutRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0));

        WorkoutDTO updated = workoutService.updateWorkout(1L, dto);

        assertNotNull(updated);
        assertEquals("Workout 1 Modified", updated.name());
        verify(workoutRepository, times(1)).save(Mockito.any());
    }

    // =======================================================
    // 7. UPDATE workout NOT FOUND
    // =======================================================
    @Test
    void testUpdateWorkoutNotFound() {
        WorkoutDTO dto = new WorkoutDTO(99L, "X", "Friday", "Legs", 1L);

        when(workoutRepository.findById(99L)).thenReturn(Optional.empty());

        WorkoutDTO updated = workoutService.updateWorkout(99L, dto);

        assertNull(updated);
        verify(workoutRepository, never()).save(Mockito.any());
    }

    // =======================================================
    // 8. DELETE workout SUCCESS
    // =======================================================
    @Test
    void testDeleteWorkoutSuccess() {
        when(workoutRepository.existsById(1L)).thenReturn(true);
        doNothing().when(workoutRepository).deleteById(1L);

        boolean deleted = workoutService.deleteWorkout(1L);

        assertTrue(deleted);
        verify(workoutRepository, times(1)).deleteById(1L);
    }

    // =======================================================
    // 9. DELETE workout NOT FOUND
    // =======================================================
    @Test
    void testDeleteWorkoutNotFound() {
        when(workoutRepository.existsById(99L)).thenReturn(false);

        boolean deleted = workoutService.deleteWorkout(99L);

        assertFalse(deleted);
        verify(workoutRepository, never()).deleteById(any());
    }
}
