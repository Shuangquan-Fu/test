package com.shuangquan.ppmtool.services;

import com.shuangquan.ppmtool.domain.Backlog;
import com.shuangquan.ppmtool.domain.ProjectTask;
import com.shuangquan.ppmtool.repositories.BacklogRepository;
import com.shuangquan.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {
    @Autowired
    private ProjectTaskRepository projectTaskRepository;
    @Autowired
    private BacklogRepository backlogRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){
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
    }
    public Iterable<ProjectTask> findAllProjectTasks(String projectIdentifier){
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(projectIdentifier);
    }

}
