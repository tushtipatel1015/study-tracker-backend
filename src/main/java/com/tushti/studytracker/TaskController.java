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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = {
    //"http://localhost:5173",
    "https://study-tracker-nine-mu.vercel.app/"
})

public class TaskController {

    private final TaskRepository repo;

    public TaskController(TaskRepository repo) {
        this.repo = repo;
    }

    // GET all
    @GetMapping
    public List<Task> getTasks(@RequestParam String userId) {
        return repo.findByUserIdOrderByOrderIndexAsc(userId);
    }

    // PATCH toggle
    @PostMapping
    public Task create(@RequestParam String userId, @RequestBody Task task) {
        task.setId(null);          // make sure it becomes a NEW row
        task.setUserId(userId);

        Task last = repo.findTopByUserIdOrderByOrderIndexDesc(userId);
        int nextIndex = (last == null) ? 0 : last.getOrderIndex() + 1;
        task.setOrderIndex(nextIndex);

        return repo.save(task);
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

    @PatchMapping("/{id}")
    public Task toggleDone(@PathVariable Long id) {
        Task task = repo.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        task.setDone(!task.isDone());
        return repo.save(task);
    }

public record ReorderRequest(List<Long> orderedIds) {}

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}