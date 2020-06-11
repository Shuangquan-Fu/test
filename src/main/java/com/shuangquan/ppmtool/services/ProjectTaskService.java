package com.shuangquan.ppmtool.services;

import com.shuangquan.ppmtool.domain.Backlog;
import com.shuangquan.ppmtool.domain.Project;
import com.shuangquan.ppmtool.domain.ProjectTask;
import com.shuangquan.ppmtool.exceptions.ProjectNotFoundException;
import com.shuangquan.ppmtool.repositories.BacklogRepository;
import com.shuangquan.ppmtool.repositories.ProjectRepository;
import com.shuangquan.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {
    @Autowired
    private ProjectTaskRepository projectTaskRepository;
    @Autowired
    private BacklogRepository backlogRepository;
    @Autowired
    private ProjectRepository projectRepository;
    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){
        try {
            Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());
            projectTask.setBacklog(backlog);
            Integer sequence = backlog.getPTSequence();
            sequence ++;
            backlog.setPTSequence(sequence);
            projectTask.setProjectSequence(projectIdentifier + "-" + sequence);
            projectTask.setProjectIdentifier(projectIdentifier);
            if(projectTask.getPriority() == null){
                projectTask.setPriority(3);
            }
            if(projectTask.getStatus() == "" || projectTask.getStatus() == null){
                projectTask.setStatus("TO_DO");
            }
            return projectTaskRepository.save(projectTask);
        } catch (Exception e) {
            throw new ProjectNotFoundException("Project Not Found");
        }

    }
    public Iterable<ProjectTask> findAllProjectTasks(String projectIdentifier){
        Project project = projectRepository.findByProjectIdentifier(projectIdentifier);
        if(project == null){
            throw new ProjectNotFoundException("Project with ID: " + projectIdentifier + "dose not exist");
        }
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(projectIdentifier);
    }
    public ProjectTask findProjectTaskBySequence(String backlogId, String sequence){
        //check the backlogId is valuable or not
        Backlog backlog = backlogRepository.findByProjectIdentifier(backlogId);
        if(backlog == null) {
            throw new ProjectNotFoundException("The project with ID: "+ backlogId + " dose not exist");
        }
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(sequence);
        if(projectTask == null) {
            throw new ProjectNotFoundException("Project Task with ID: "+sequence + "does not found" );
        }
        if(projectTask.getProjectIdentifier() != backlogId){
            throw new ProjectNotFoundException("Project Task with ID: "+sequence + "does not exist in project" + backlogId);
        }

        return projectTask;
    }

}
