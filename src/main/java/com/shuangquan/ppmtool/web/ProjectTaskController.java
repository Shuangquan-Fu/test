package com.shuangquan.ppmtool.web;

import com.shuangquan.ppmtool.domain.ProjectTask;
import com.shuangquan.ppmtool.services.MapValidationErrorService;
import com.shuangquan.ppmtool.services.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/backlog")
public class ProjectTaskController {
    @Autowired
    private ProjectTaskService projectTaskService;
    @Autowired
    private MapValidationErrorService mapValidationErrorService;
    @PostMapping("/{backlog_id}")
    public ResponseEntity<?> addProjectTask(@Valid @RequestBody ProjectTask projectTask, @PathVariable String backlog_id, BindingResult result){
        ResponseEntity<?> errMap = mapValidationErrorService.MapValidationErrorService(result);
        if(errMap != null){
            return errMap;
        }
        ProjectTask projectTask1 = projectTaskService.addProjectTask(backlog_id,projectTask);
        return new ResponseEntity<ProjectTask>(projectTask1, HttpStatus.CREATED);
    }
    @GetMapping("/{backlog_id}")
    public ResponseEntity<Iterable<ProjectTask>> findAllProjectTasks(@PathVariable String backlog_id){
        return new ResponseEntity<Iterable<ProjectTask>>(projectTaskService.findAllProjectTasks(backlog_id),HttpStatus.OK);

    }
}
