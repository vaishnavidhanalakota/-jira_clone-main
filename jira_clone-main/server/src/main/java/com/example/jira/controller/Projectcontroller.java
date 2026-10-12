
package com.example.jira.controller;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jira.dto.ProjectResponse;
import com.example.jira.model.Project;
import com.example.jira.model.User;
import com.example.jira.repository.Projectrepository;
import com.example.jira.repository.UserRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/projects")
public class Projectcontroller {

    private final Projectrepository projectrepository;
    private final UserRepository userRepository;

    public Projectcontroller(
            Projectrepository projectrepository,
            UserRepository userRepository) {
        this.projectrepository = projectrepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public Project createProject(@RequestBody Project project) {

        if (project.getName() == null || project.getName().isBlank()) {
            throw new IllegalArgumentException("Project name is required");
        }

        if (project.getMemberIds() == null) {
            project.setMemberIds(new ArrayList<>());
        }

        return projectrepository.save(project);
    }

    @GetMapping
    public List<Project> getAllProjects() {
        return projectrepository.findAll();
    }

    @GetMapping("/{id}")
    public ProjectResponse getProjectById(@PathVariable String id) {

        if (!ObjectId.isValid(id)) {
            throw new IllegalArgumentException("Invalid project ID");
        }

        Project project = projectrepository.findById(new ObjectId(id))
                .orElseThrow(() ->
                        new RuntimeException("Project not found"));

        User owner = null;

        if (project.getOwnerId() != null
                && ObjectId.isValid(project.getOwnerId())) {
            owner = userRepository
                    .findById(new ObjectId(project.getOwnerId()))
                    .orElse(null);
        }

        List<ObjectId> memberObjectIds =
                project.getMemberIds() == null
                        ? List.of()
                        : project.getMemberIds().stream()
                                .filter(memberId ->
                                        memberId != null
                                        && ObjectId.isValid(memberId))
                                .map(ObjectId::new)
                                .toList();

        List<User> members =
                userRepository.findByIdIn(memberObjectIds);

        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                owner,
                members);
    }

    @PutMapping("/{id}")
    public Project updaProject(
            @PathVariable String id,
            @RequestBody Project updated) {

        if (!ObjectId.isValid(id)) {
            throw new IllegalArgumentException("Invalid project ID");
        }

        Project project = projectrepository.findById(new ObjectId(id))
                .orElseThrow(() ->
                        new RuntimeException("Project not found"));

        project.setName(updated.getName());
        project.setDescription(updated.getDescription());

        if (updated.getMemberIds() != null) {
            project.setMemberIds(updated.getMemberIds());
        }

        return projectrepository.save(project);
    }

    @DeleteMapping("/{id}")
    public void deleteproject(@PathVariable String id) {

        if (!ObjectId.isValid(id)) {
            throw new IllegalArgumentException("Invalid project ID");
        }

        if (!projectrepository.existsById(new ObjectId(id))) {
            throw new RuntimeException("Project not found");
        }

        projectrepository.deleteById(new ObjectId(id));
    }
}