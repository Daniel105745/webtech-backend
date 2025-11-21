package htw.webtech.myapp.business.service;

import htw.webtech.myapp.persistence.entity.TrainingPlanEntity;
import htw.webtech.myapp.persistence.repository.TrainingPlanRepository;
import htw.webtech.myapp.rest.model.TrainingPlanDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

    @ExtendWith(MockitoExtension.class)
    public class TrainingPlanServiceTest {



        @Mock
        private TrainingPlanRepository repository;

        @InjectMocks
        private TrainingPlanService service;

        TrainingPlanEntity entity1;
        TrainingPlanEntity entity2;

        @BeforeEach
        void setup() {
            entity1 = new TrainingPlanEntity("Plan A", "30 min", "High", "Beine");
            entity1.setId(1L);

            entity2 = new TrainingPlanEntity("Plan B", "45 min", "Medium", "Arme");
            entity2.setId(2L);
        }

        // --------------------------------------------------------------
        // TEST 1: getAllTrainingPlans()
        // --------------------------------------------------------------
        @Test
        void testGetAllTrainingPlans() {
            // given
            when(repository.findAll()).thenReturn(Arrays.asList(entity1, entity2));

            // when
            List<TrainingPlanDTO> result = service.getAllTrainingPlans();

            // then
            assertEquals(2, result.size());
            assertEquals("Plan A", result.get(0).name());
            assertEquals("Plan B", result.get(1).name());
        }

        // --------------------------------------------------------------
        // TEST 2: create()
        // --------------------------------------------------------------
        @Test
        void testCreateTrainingPlan() {
            // given
            TrainingPlanDTO dto = new TrainingPlanDTO(0, "New Plan", "20 min", "Low", "Rücken");

            // when
            TrainingPlanDTO result = service.create(dto);

            // then
            assertEquals(dto.name(), result.name());
            verify(repository, times(1)).save(any(TrainingPlanEntity.class));
        }

        // --------------------------------------------------------------
        // TEST 3: delete() SUCCESS
        // --------------------------------------------------------------
        @Test
        void testDeleteSuccess() {
            // given
            when(repository.existsById(1L)).thenReturn(true);

            // when
            boolean deleted = service.delete(1L);

            // then
            assertTrue(deleted);
            verify(repository, times(1)).deleteById(1L);
        }

        // --------------------------------------------------------------
        // TEST 4: delete() FAIL (ID not exist)
        // --------------------------------------------------------------
        @Test
        void testDeleteNotFound() {
            // given
            when(repository.existsById(1L)).thenReturn(false);

            // when
            boolean deleted = service.delete(1L);

            // then
            assertFalse(deleted);
            verify(repository, never()).deleteById(any());
        }

        // --------------------------------------------------------------
        // TEST 5: update() SUCCESS
        // --------------------------------------------------------------
        @Test
        void testUpdateSuccess() {
            // given
            TrainingPlanDTO dto = new TrainingPlanDTO(1, "Updated", "40 min", "High", "Full Body");

            when(repository.findById(1L)).thenReturn(Optional.of(entity1));
            when(repository.save(any())).thenReturn(entity1);

            // when
            TrainingPlanDTO result = service.update(1L, dto);

            // then
            assertNotNull(result);
            assertEquals("Updated", result.name());
            assertEquals("40 min", result.dauer());
        }

        // --------------------------------------------------------------
        // TEST 6: update() FAIL (ID not exist)
        // --------------------------------------------------------------
        @Test
        void testUpdateNotFound() {
            // given
            when(repository.findById(99L)).thenReturn(Optional.empty());

            // when
            TrainingPlanDTO result = service.update(99L, new TrainingPlanDTO(0, "X", "Y", "Z", "X"));

            // then
            assertNull(result);
        }

        // --------------------------------------------------------------
        // TEST 7: getById() SUCCESS
        // --------------------------------------------------------------
        @Test
        void testGetByIdSuccess() {
            // given
            when(repository.findById(1L)).thenReturn(Optional.of(entity1));

            // when
            TrainingPlanDTO result = service.getById(1L);

            // then
            assertNotNull(result);
            assertEquals("Plan A", result.name());
        }

        // --------------------------------------------------------------
        // TEST 8: getById() FAIL (ID not exist)
        // --------------------------------------------------------------
        @Test
        void testGetByIdNotFound() {
            // given
            when(repository.findById(5L)).thenReturn(Optional.empty());

            // when
            TrainingPlanDTO result = service.getById(5L);

            // then
            assertNull(result);
        }
    }


