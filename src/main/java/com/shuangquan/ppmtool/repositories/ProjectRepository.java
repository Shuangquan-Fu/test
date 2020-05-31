package com.shuangquan.ppmtool.repositories;

import com.shuangquan.ppmtool.domain.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
//extends from curdRespository
@Repository
public interface ProjectRepository extends CrudRepository<Project,Long> {


}
