package com.shuangquan.ppmtool.repositories;

import com.shuangquan.ppmtool.domain.ProjectTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectTaskRepository extends CrudRepository<ProjectTask,Long> {
    public Iterable<ProjectTask> findByProjectIdentifierOrderByPriority(String projectIdentifier);

    ProjectTask findByProjectSequence(String sequence);
    public void deleteByProjectIdentifier(String projectIdentifier);
}
