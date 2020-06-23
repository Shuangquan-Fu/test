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
    @Autowired
    private ProjectService projectService;
    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask,String username){
        try {
            //PTs to be added to a specific project, project != null, BL exists
            Backlog backlog =  projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();
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

            if(projectTask.getPriority()==null||projectTask.getPriority()==0){ //In the future we need projectTask.getPriority()== 0 to handle the form
                projectTask.setPriority(3);
            }

            return projectTaskRepository.save(projectTask);
        }catch (Exception e){
            throw new ProjectNotFoundException("Project not Found");
        }

    }
    public Iterable<ProjectTask>findAllProjectTasks(String id, String username){

        projectService.findProjectByIdentifier(id, username);

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findProjectTaskBySequence(String backlogId, String sequence,String username){
        //check the backlogId is valuable or not
        projectService.findProjectByIdentifier(backlogId, username);

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
    public ProjectTask updateProjectTask(ProjectTask updatedProject,String backlogId,String pptId,String username){
        ProjectTask projectTask1 = findProjectTaskBySequence(backlogId,pptId,username);
        projectTask1 = updatedProject;
        ProjectTask updated = projectTaskRepository.save(projectTask1);
        return updated;
    }

    public void deleteProjectTask(String backlogId, String pptId,String username){
        ProjectTask projectTask = findProjectTaskBySequence(backlogId,pptId,username);
        projectTaskRepository.delete(projectTask);
    }
}
