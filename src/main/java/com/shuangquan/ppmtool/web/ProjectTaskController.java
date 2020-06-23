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
import java.security.Principal;
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
    public ResponseEntity<?> addPTtoBacklog(@Valid @RequestBody ProjectTask projectTask,
                                            BindingResult result, @PathVariable String backlog_id, Principal principal){
        //show delete
        //custom exception

        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if (errorMap != null) return errorMap;

        ProjectTask projectTask1 = projectTaskService.addProjectTask(backlog_id, projectTask,principal.getName());

        return new ResponseEntity<ProjectTask>(projectTask1, HttpStatus.CREATED);

    }
    @GetMapping("/{backlog_id}")
    public ResponseEntity<Iterable<ProjectTask>> findAllProjectTasks(@PathVariable String backlog_id,Principal principal){
        return new ResponseEntity<Iterable<ProjectTask>>(projectTaskService.findAllProjectTasks(backlog_id,principal.getName()),HttpStatus.OK);

    }

    @GetMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<ProjectTask> findProjectTaskBySe(@PathVariable String backlog_id,@PathVariable String pt_id,Principal principal){
        ProjectTask projectTask = projectTaskService.findProjectTaskBySequence(backlog_id,pt_id,principal.getName());
        return new ResponseEntity<ProjectTask>(projectTask,HttpStatus.OK);
    }

    @PatchMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask updatedTask,@PathVariable String backlog_id,
                                               @PathVariable String pt_id,BindingResult result,Principal principal){
        ResponseEntity<?> errMap = mapValidationErrorService.MapValidationService(result);
        if(errMap != null){
            return errMap;
        }
        ProjectTask projectTask = projectTaskService.updateProjectTask(updatedTask,backlog_id,pt_id,principal.getName());
        return new ResponseEntity<ProjectTask>(projectTask,HttpStatus.OK);
    }
    @DeleteMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<String> deleteProjectTask(@PathVariable String backlog_id,@PathVariable String pt_id,Principal principal){
        projectTaskService.deleteProjectTask(backlog_id,pt_id,principal.getName());
        return new ResponseEntity<String>("ProjectTask with Sequence" + pt_id + "is deleted successfully",HttpStatus.OK);
    }
}
