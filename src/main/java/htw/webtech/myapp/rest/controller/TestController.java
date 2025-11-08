package htw.webtech.myapp.rest.controller;

import htw.webtech.myapp.persistence.repository.TestRepository;
import htw.webtech.myapp.persistence.entity.TestEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/test-db")
public class TestController {

    private final TestRepository testRepository;

    public TestController(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @PostMapping
    public TestEntity saveTest(@RequestBody String message) {
        return testRepository.save(new TestEntity(message));
    }

    @GetMapping
    public List<TestEntity> getAllTests() {
        return testRepository.findAll();
    }
}
