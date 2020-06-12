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

import java.util.List;

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
            //PTs to be added to a specific project, project != null, BL exists
            Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
            //set the bl to pt
            projectTask.setBacklog(backlog);
            //we want our project sequence to be like this: IDPRO-1  IDPRO-2  ...100 101
            Integer BacklogSequence = backlog.getPTSequence();
            // Update the BL SEQUENCE
            BacklogSequence++;

            backlog.setPTSequence(BacklogSequence);

            //Add Sequence to Project Task
            projectTask.setProjectSequence(backlog.getProjectIdentifier()+"-"+BacklogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            //INITIAL priority when priority null

            //INITIAL status when status is null
            if(projectTask.getStatus()==""|| projectTask.getStatus()==null){
                projectTask.setStatus("TO_DO");
            }

            if(projectTask.getPriority()==null){ //In the future we need projectTask.getPriority()== 0 to handle the form
                projectTask.setPriority(3);
            }

            return projectTaskRepository.save(projectTask);
        }catch (Exception e){
            throw new ProjectNotFoundException("Project not Found");
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
        ProjectTask project = projectTaskRepository.findByProjectSequence(sequence);
        if(project == null) {
            throw new ProjectNotFoundException("Project Task with ID: "+sequence + "does not found" );
        }
        if(!backlogId.equals(project.getProjectIdentifier())){
            System.out.println(project.getProjectIdentifier());
            System.out.println(backlogId);
            throw new ProjectNotFoundException("Project Task with ID: "+sequence + "does not exist in project" + backlogId);
        }

        return project;
    }
    public ProjectTask updateProjectTask(ProjectTask updatedProject,String backlogId,String pptId){
        ProjectTask projectTask1 = findProjectTaskBySequence(backlogId,pptId);
        projectTask1 = updatedProject;
        ProjectTask updated = projectTaskRepository.save(projectTask1);
        return updated;
    }

    public void deleteProjectTask(String backlogId, String pptId){
        ProjectTask projectTask = findProjectTaskBySequence(backlogId,pptId);

        projectTaskRepository.delete(projectTask);
    }
}
