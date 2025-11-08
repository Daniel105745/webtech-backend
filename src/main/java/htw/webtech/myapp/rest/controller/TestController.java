package htw.webtech.myapp.rest.controller;


import htw.webtech.myapp.persistence.entity.TestEntity;
import htw.webtech.myapp.persistence.repository.TestRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/test-db")
public class TestController {
    private final TestRepository repo;

    public TestController(TestRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<TestEntity> getAll() {
        return repo.findAll();
    }

    @PostMapping
    public TestEntity create(@RequestBody String msg) {
        return repo.save(new TestEntity(msg));
    }
}
