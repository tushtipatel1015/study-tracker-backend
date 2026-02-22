package com.tushti.studytracker.config;

import java.util.List;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.tushti.studytracker.Task;
import com.tushti.studytracker.TaskRepository;

@Component
public class BackfillUserId implements CommandLineRunner {

    private final TaskRepository repo;

    public BackfillUserId(TaskRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) {
        List<Task> all = repo.findAll();
        boolean changed = false;

        for (Task t : all) {
            if (t.getUserId() == null || t.getUserId().isBlank()) {
                // Put old global tasks under a single "legacy" user bucket
                t.setUserId("legacy-" + UUID.randomUUID());
                changed = true;
            }
        }

        if (changed) {
            repo.saveAll(all);
            System.out.println("Backfilled missing user_id values.");
        } else {
            System.out.println("No missing user_id values to backfill.");
        }
    }
}