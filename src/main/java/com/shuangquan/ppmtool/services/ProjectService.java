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
        return projectRepository.findByProjectIdentifier(projectId);
    }
}
