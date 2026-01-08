package htw.webtech.myapp.business.service;

import htw.webtech.myapp.persistence.entity.ExerciseEntity;
import htw.webtech.myapp.persistence.entity.WorkoutEntity;
import htw.webtech.myapp.persistence.repository.ExerciseRepository;
import htw.webtech.myapp.persistence.repository.WorkoutRepository;
import htw.webtech.myapp.rest.model.ExerciseDTO;
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
class ExerciseServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private WorkoutRepository workoutRepository;

    private ExerciseService exerciseService;

    private WorkoutEntity workout;
    private ExerciseEntity exercise1;

    @BeforeEach
    void setup() throws Exception {
        exerciseService = new ExerciseService(exerciseRepository, workoutRepository);

        // Erstelle Workout
        workout = new WorkoutEntity();
        setId(workout, 1L); // nutzt reflection set id

        // Tạo Exercise mẫu
        exercise1 = new ExerciseEntity("Pushups", 3, 15, 0.0, workout);
        setId(exercise1, 1L); // nutzt reflection  set id
    }

    // nutzt reflection set private id field
    private void setId(Object entity, Long id) throws Exception {
        Field idField = entity.getClass().getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(entity, id);
    }

    // -------------------------------------------------------
    // 1. GET EXERCISES BY WORKOUT
    // -------------------------------------------------------
    @Test
    void testGetExercisesByWorkout() {
        when(exerciseRepository.findByWorkoutId(1L)).thenReturn(List.of(exercise1));

        List<ExerciseDTO> result = exerciseService.getExercisesByWorkout(1L);

        assertEquals(1, result.size());
        assertEquals("Pushups", result.get(0).name());
        verify(exerciseRepository, times(1)).findByWorkoutId(1L);
    }

    // -------------------------------------------------------
    // 2. CREATE EXERCISE SUCCESS
    // -------------------------------------------------------
    @Test
    void testCreateExercise() {
        ExerciseDTO dto = new ExerciseDTO(null, "Pullups", 4, 12, 0.0, 1L);

        when(workoutRepository.findById(1L)).thenReturn(Optional.of(workout));
        when(exerciseRepository.save(Mockito.any())).thenAnswer(invocation -> {
            ExerciseEntity saved = invocation.getArgument(0);
            setId(saved, 2L); // set id cho ExerciseEntity vừa save
            return saved;
        });

        ExerciseDTO result = exerciseService.createExercise(dto, 1L);

        assertNotNull(result);
        assertEquals("Pullups", result.name());
        assertEquals(1L, result.workoutId());
        verify(exerciseRepository, times(1)).save(Mockito.any());
    }

    // -------------------------------------------------------
    // 3. CREATE EXERCISE WORKOUT NOT FOUND
    // -------------------------------------------------------
    @Test
    void testCreateExerciseWorkoutNotFound() {
        ExerciseDTO dto = new ExerciseDTO(null, "Pullups", 4, 12, 0.0, 99L);

        when(workoutRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> exerciseService.createExercise(dto, 99L));
    }

    // -------------------------------------------------------
    // 4. UPDATE EXERCISE SUCCESS
    // -------------------------------------------------------
    @Test
    void testUpdateExerciseSuccess() {
        ExerciseDTO dto = new ExerciseDTO(1L, "ModifiedPushups", 5, 20, 0.0, 1L);

        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exercise1));
        when(exerciseRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0));

        ExerciseDTO result = exerciseService.updateExercise(1L, dto);

        assertNotNull(result);
        assertEquals("ModifiedPushups", result.name());
        assertEquals(5, result.saetze());
        verify(exerciseRepository, times(1)).save(Mockito.any());
    }

    // -------------------------------------------------------
    // 5. UPDATE EXERCISE NOT FOUND
    // -------------------------------------------------------
    @Test
    void testUpdateExerciseNotFound() {
        ExerciseDTO dto = new ExerciseDTO(99L, "X", 1, 1, 0.0, 1L);

        when(exerciseRepository.findById(99L)).thenReturn(Optional.empty());

        ExerciseDTO result = exerciseService.updateExercise(99L, dto);

        assertNull(result);
        verify(exerciseRepository, never()).save(Mockito.any());
    }

    // -------------------------------------------------------
    // 6. DELETE EXERCISE SUCCESS
    // -------------------------------------------------------
    @Test
    void testDeleteExerciseSuccess() {
        when(exerciseRepository.existsById(1L)).thenReturn(true);
        doNothing().when(exerciseRepository).deleteById(1L);

        boolean deleted = exerciseService.deleteExercise(1L);

        assertTrue(deleted);
        verify(exerciseRepository, times(1)).deleteById(1L);
    }

    // -------------------------------------------------------
    // 7. DELETE EXERCISE NOT FOUND
    // -------------------------------------------------------
    @Test
    void testDeleteExerciseNotFound() {
        when(exerciseRepository.existsById(99L)).thenReturn(false);

        boolean deleted = exerciseService.deleteExercise(99L);

        assertFalse(deleted);
        verify(exerciseRepository, never()).deleteById(any());
    }
}
