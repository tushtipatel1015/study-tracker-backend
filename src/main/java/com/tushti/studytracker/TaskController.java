package com.tushti.studytracker;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:5173")
public class TaskController {

    private final TaskRepository repo;

    public TaskController(TaskRepository repo) {
        this.repo = repo;
    }

    // GET all
    @GetMapping
    public List<Task> getAll() {
        return repo.findAllByOrderByOrderIndexAsc();
    }

    // POST new
    @PostMapping
    public Task create(@RequestBody Task task) {
        Task last = repo.findTopByOrderByOrderIndexDesc();
        int nextIndex = (last == null) ? 0 : last.getOrderIndex() + 1;

        task.setOrderIndex(nextIndex);
    return repo.save(task);
    }

    // PATCH toggle
    @PatchMapping("/{id}")
    public Task toggle(@PathVariable Long id) {
        Task t = repo.findById(id).orElseThrow();
        t.setDone(!t.isDone());
        return repo.save(t);
    }

    @PutMapping("/{id}")
    public Task updateTitle(@PathVariable Long id, @RequestBody Task updated) {
        Task t = repo.findById(id).orElseThrow();
        t.setTitle(updated.getTitle());
        return repo.save(t);
    }

    // Reorders tasks
    @PutMapping("/reorder")
    public void reorder(@RequestBody ReorderRequest body) {
        List<Long> ids = body.orderedIds();

        for (int i = 0; i < ids.size(); i++) {
            Task t = repo.findById(ids.get(i)).orElseThrow();
            t.setOrderIndex(i);
            repo.save(t);
        }
    }

public record ReorderRequest(List<Long> orderedIds) {}

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}