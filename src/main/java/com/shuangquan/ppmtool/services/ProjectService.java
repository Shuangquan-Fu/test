package com.shuangquan.ppmtool.services;

import com.shuangquan.ppmtool.domain.Backlog;
import com.shuangquan.ppmtool.domain.Project;
import com.shuangquan.ppmtool.domain.User;
import com.shuangquan.ppmtool.exceptions.ProjectIdException;
import com.shuangquan.ppmtool.repositories.BacklogRepository;
import com.shuangquan.ppmtool.repositories.ProjectRepository;
import com.shuangquan.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private BacklogRepository backlogRepository;
    @Autowired
    private UserRepository userRepository;

    public Project saveOrUpdateProject(Project project, String username){
        try {
            User user =  userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(username);
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            if(project.getId() == null){
                System.out.println("id == null");
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }
            if(project.getId() != null){
                System.out.println("id != null ");
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }
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
    public Iterable<Project> findAllProject(String username){
        Iterable<Project> projects = projectRepository.findByProjectLeader(username);
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
