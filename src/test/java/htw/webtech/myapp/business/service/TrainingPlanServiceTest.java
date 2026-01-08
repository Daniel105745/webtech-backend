package htw.webtech.myapp.business.service;

import htw.webtech.myapp.persistence.entity.TrainingPlanEntity;
import htw.webtech.myapp.persistence.entity.WorkoutEntity;
import htw.webtech.myapp.persistence.entity.WorkoutSessionEntity;
import htw.webtech.myapp.persistence.repository.TrainingPlanRepository;
import htw.webtech.myapp.persistence.repository.WorkoutSessionRepository;
import htw.webtech.myapp.rest.model.TrainingPlanDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingPlanServiceTest {

    @Mock
    private TrainingPlanRepository repository;

    @Mock
    private WorkoutSessionRepository sessionRepository;

    @InjectMocks
    private TrainingPlanService service;

    TrainingPlanEntity entity1;
    TrainingPlanEntity entity2;

    @BeforeEach
    void setup() {
        entity1 = new TrainingPlanEntity("Plan A", "30 min", "High", "Beine");
        entity1.setId(1L);
        entity1.setUserId("user123");
        // đảm bảo có danh sách workouts rỗng ban đầu
        entity1.setWorkouts(new ArrayList<>());

        entity2 = new TrainingPlanEntity("Plan B", "45 min", "Medium", "Arme");
        entity2.setId(2L);
        entity2.setUserId("user123");
        entity2.setWorkouts(new ArrayList<>());
    }

    // --------------------------------------------------------------
    // TEST 1: getAllTrainingPlans(userId)
    // --------------------------------------------------------------
    @Test
    void testGetAllTrainingPlans() {
        when(repository.findAllByUserId("user123")).thenReturn(Arrays.asList(entity1, entity2));

        List<TrainingPlanDTO> result = service.getAllTrainingPlans("user123");

        assertEquals(2, result.size());
        assertEquals("Plan A", result.get(0).name());
        assertEquals("Plan B", result.get(1).name());
    }

    // --------------------------------------------------------------
    // TEST 2: create(dto, userId)
    // --------------------------------------------------------------
    @Test
    void testCreateTrainingPlan() {
        TrainingPlanDTO dto = new TrainingPlanDTO(0, "New Plan", "20 min", "Low", "Rücken");

        // gibt entity đã truyền vào zurück, gleichzeitig set id durch reflection
        when(repository.save(any())).thenAnswer((Answer<TrainingPlanEntity>) invocation -> {
            TrainingPlanEntity e = invocation.getArgument(0);
            setPrivateId(e, "id", 1L);
            return e;
        });

        TrainingPlanDTO result = service.create(dto, "user123");

        assertEquals(dto.name(), result.name());
        assertEquals(dto.dauer(), result.dauer());
        assertEquals(dto.intensitaet(), result.intensitaet());
        assertEquals(dto.trainingsziel(), result.trainingsziel());
        verify(repository, times(1)).save(any(TrainingPlanEntity.class));
    }

    // --------------------------------------------------------------
    // TEST 3: delete() SUCCESS (no sessions linked)
    // --------------------------------------------------------------
    @Test
    void testDeleteSuccessNoSessions() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity1));
        when(sessionRepository.findAll()).thenReturn(List.of());

        boolean deleted = service.delete(1L);

        assertTrue(deleted);
        verify(repository, times(1)).deleteById(1L);
        verify(sessionRepository, never()).save(any());
    }

    // --------------------------------------------------------------
    // TEST 4: delete() SUCCESS (sessions linked to workouts) — nutzt reflection set id für WorkoutEntity
    // --------------------------------------------------------------
    @Test
    void testDeleteSuccessWithSessions() {
        // erstelle workout , die zu plan gehören .
        WorkoutEntity workout = new WorkoutEntity("Workout A", "Beine", "Mon", entity1);
        setPrivateId(workout, "id", 10L);
        entity1.getWorkouts().add(workout);

        // Session vergleicht zum diesen workout
        WorkoutSessionEntity session = new WorkoutSessionEntity();
        setPrivateId(session, "id", 100L);
        session.setWorkout(workout);

        when(repository.findById(1L)).thenReturn(Optional.of(entity1));
        when(sessionRepository.findAll()).thenReturn(List.of(session));

        boolean deleted = service.delete(1L);

        assertTrue(deleted);

        assertNull(session.getWorkout());
        verify(sessionRepository, times(1)).save(session);
        verify(repository, times(1)).deleteById(1L);
    }

    // --------------------------------------------------------------
    // TEST 5: delete() SUCCESS mit vieler workouts +  sessions
    // --------------------------------------------------------------
    @Test
    void testDeleteSuccessMultipleWorkoutsAndSessions() {
        WorkoutEntity w1 = new WorkoutEntity("W1", "Arme", "Tue", entity1);
        WorkoutEntity w2 = new WorkoutEntity("W2", "Rücken", "Wed", entity1);
        setPrivateId(w1, "id", 11L);
        setPrivateId(w2, "id", 12L);
        entity1.getWorkouts().add(w1);
        entity1.getWorkouts().add(w2);

        WorkoutSessionEntity s1 = new WorkoutSessionEntity();
        WorkoutSessionEntity s2 = new WorkoutSessionEntity();
        WorkoutSessionEntity s3 = new WorkoutSessionEntity();
        setPrivateId(s1, "id", 201L);
        setPrivateId(s2, "id", 202L);
        setPrivateId(s3, "id", 203L);

        // s1 -> w1, s2 -> w2, s3 -> null (nicht verbunden )
        s1.setWorkout(w1);
        s2.setWorkout(w2);
        s3.setWorkout(null);

        when(repository.findById(1L)).thenReturn(Optional.of(entity1));
        when(sessionRepository.findAll()).thenReturn(List.of(s1, s2, s3));

        boolean deleted = service.delete(1L);

        assertTrue(deleted);
        assertNull(s1.getWorkout());
        assertNull(s2.getWorkout());
        assertNull(s3.getWorkout()); //
        // 2 session verbindet  -> save
        verify(sessionRepository, times(1)).save(s1);
        verify(sessionRepository, times(1)).save(s2);

        verify(sessionRepository, never()).save(s3);
        verify(repository, times(1)).deleteById(1L);
    }

    // --------------------------------------------------------------
    // TEST 6: delete() FAIL (ID not exist)
    // --------------------------------------------------------------
    @Test
    void testDeleteNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        boolean deleted = service.delete(99L);

        assertFalse(deleted);
        verify(repository, never()).deleteById(any());
        verify(sessionRepository, never()).findAll();
    }

    // --------------------------------------------------------------
    // TEST 7: update() SUCCESS
    // --------------------------------------------------------------
    @Test
    void testUpdateSuccess() {
        TrainingPlanDTO dto = new TrainingPlanDTO(1, "Updated", "40 min", "High", "Full Body");

        when(repository.findById(1L)).thenReturn(Optional.of(entity1));
        when(repository.save(any())).thenAnswer((Answer<TrainingPlanEntity>) invocation -> invocation.getArgument(0));

        TrainingPlanDTO result = service.update(1L, dto);

        assertNotNull(result);
        assertEquals("Updated", result.name());
        assertEquals("40 min", result.dauer());
        assertEquals("High", result.intensitaet());
        assertEquals("Full Body", result.trainingsziel());
    }

    // --------------------------------------------------------------
    // TEST 8: update() FAIL (ID not exist)
    // --------------------------------------------------------------
    @Test
    void testUpdateNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        TrainingPlanDTO result = service.update(99L, new TrainingPlanDTO(0, "X", "Y", "Z", "X"));

        assertNull(result);
        verify(repository, never()).save(any());
    }

    // --------------------------------------------------------------
    // TEST 9: getById() SUCCESS
    // --------------------------------------------------------------
    @Test
    void testGetByIdSuccess() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity1));

        TrainingPlanDTO result = service.getById(1L);

        assertNotNull(result);
        assertEquals("Plan A", result.name());
    }

    // --------------------------------------------------------------
    // TEST 10: getById() FAIL (ID not exist)
    // --------------------------------------------------------------
    @Test
    void testGetByIdNotFound() {
        when(repository.findById(5L)).thenReturn(Optional.empty());

        TrainingPlanDTO result = service.getById(5L);

        assertNull(result);
    }

    // --------------------------------------------------------------
    // Helper: set private id durch reflection (löst no setId-Felher ein)
    // --------------------------------------------------------------
    private static void setPrivateId(Object target, String fieldName, Long value) {
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Cannot set private field '" + fieldName + "' on " + target.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
}
