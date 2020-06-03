package com.shuangquan.ppmtool.services;

import com.shuangquan.ppmtool.domain.Project;
import com.shuangquan.ppmtool.exceptions.ProjectIdException;
import com.shuangquan.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Project saveOrUpdateProject(Project project){
        try {
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            return projectRepository.save(project);
        } catch (Exception e){
            throw new ProjectIdException("Project ID "+ project.getProjectIdentifier().toUpperCase()+"already exists");
        }
    }

    public Project findProjectByIdentifier(String projectId){
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if(project == null){
            throw new ProjectIdException(" Project dose not exist");
        }
        return project;
    }
    public Iterable<Project> findAllProject(){
        Iterable<Project> projects = projectRepository.findAll();
        return projects;
    }
    public void deleteProjectByIdentifier(String projectId){
        Project project = projectRepository.findByProjectIdentifier(projectId);
        if(project == null){
            throw new ProjectIdException("This project dose not exist");
        }
        projectRepository.delete(project);
    }

}
