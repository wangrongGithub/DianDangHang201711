package com.alisure.controller;

import com.alisure.entity.*;
import com.alisure.service.CommonService;
import com.alisure.service.TaskService;
import com.alisure.tool.core.CoreTime;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;

/**
 * Created by ALISURE on 2017/9/10.
 *
 * 任务相关
 * 1、根据条件获取大厅的任务
 * 2、根据TaskId获取具体的任务信息
 * 3、修改任务状态
 * 4、发布任务
 * 5、接手任务
 * 6、获取类别
 */
@Controller
@RequestMapping("/task")
@Api(value = "/task",description = "about task")
public class TaskController {

    @Autowired
    TaskService taskService;

    @Autowired
    CommonService commonService;

    @Autowired
    private HttpServletRequest request;

    @ResponseBody
    @RequestMapping(value = "taskhall", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value="get hall task", httpMethod="POST", response = Result.class, nickname = "ALISURE")
    public Result getHallTask(
            @ApiParam(required = false,name = "page",value = "page") @RequestParam(defaultValue = "1", required = false) String page,
            @ApiParam(required = false,name = "pageSize",value = "pageSize") @RequestParam(defaultValue = "10", required = false) String pageSize,
            @ApiParam(required = false,name = "type",value = "type") @RequestParam(defaultValue = "0", required = false) String type,
            @ApiParam(required = false,name = "sortType",value = "sortType") @RequestParam(defaultValue = "1", required = false) String sortType,
            @ApiParam(required = false, name = "school",value = "school") @RequestParam(required = false) String school) throws Exception{
        if (school == null) school = commonService.getSchool(commonService.getUserId(request));
        else school = URLDecoder.decode(school, "UTF-8");
        return new Result(taskService.getHallTask(Integer.parseInt(page), Integer.parseInt(pageSize), 1,
                Integer.parseInt(type), Integer.parseInt(sortType), school));
    }

    @ResponseBody
    @RequestMapping(value = "task/{tid}")
    @ApiOperation(value="get task by tid", httpMethod="GET", response = Result.class, nickname = "ALISURE")
    public Result getTaskInfo(
            @ApiParam(name = "tid", value = "tid") @PathVariable int tid){
        return new Result(taskService.getTask(tid, commonService.getUserId(request)));
    }

    @ResponseBody
    @RequestMapping(value = "taskStatus/{tid}", method = RequestMethod.GET)
    @ApiOperation(value="update task status", httpMethod="GET", response = Result.class, nickname = "ALISURE")
    public Result updateTaskStatus(
            @ApiParam(name = "tid", value = "tid") @PathVariable int tid,
            @ApiParam(name = "    means: 0 表示确认完成，1 表示取消, 2 表示重新发布", value = "means") @RequestParam int means){
        return new Result(taskService.updateTaskStatus(tid, means, commonService.getUserId(request)));
    }

    @ResponseBody
    @RequestMapping(value = "publish", method = RequestMethod.POST)
    @ApiOperation(value="update task status", httpMethod="POST", response = Result.class, nickname = "ALISURE")
    public Result publishTask(
            @ApiParam(name = "title", value = "title") @RequestParam String title,
            @ApiParam(name = "content", value = "content") @RequestParam String content,
            @ApiParam(name = "words", value = "words") @RequestParam String words,
            @ApiParam(name = "pubDate", value = "startTime") @RequestParam String startTime,
            @ApiParam(name = "endDate", value = "endTime") @RequestParam String endTime,
            @ApiParam(name = "catId", value = "catId") @RequestParam int catId,
            @ApiParam(name = "place", value = "place") @RequestParam String place,
            @ApiParam(name = "coins", value = "coins") @RequestParam int coins,
            @ApiParam(name = "school", value = "school") @RequestParam String school,
            @ApiParam(name = "rewards", value = "rewards") @RequestParam String rewards,
            @ApiParam(name = "task_images", value = "task_images") @RequestParam(required = false) String images,
            @ApiParam(name = "weixin", value = "weixin") @RequestParam String weixin,
            @ApiParam(name = "qq", value = "qq") @RequestParam String qq,
            @ApiParam(name = "telephone", value = "telephone") @RequestParam String telephone){
        InfoTask task = new InfoTask();
        task.setTitle(title);
        task.setContent(content);
        task.setWords(words);
        task.setStartTime(startTime);
        task.setEndTime(endTime);
        task.setSchool(school);
        task.setCatId(catId);
        task.setPlace(place);
        task.setCoins(coins);
        task.setRewards(rewards);
        task.setImages(images);
        task.setWeixin(weixin);
        task.setQq(qq);
        task.setTelephone(telephone);

        // 设置一些必要的值
        task.setPubId(commonService.getUserId(request));
        task.setPubStateId(1);
        task.setRecStateId(1);
        task.setPubTime(CoreTime.getDataTime());
        int tid = taskService.publishTask(task, commonService.getOpenId(request));
        if(tid > 0){
            return new Result(Status.Status_OK, tid);
        }
        return new Result(Status.Status_Error);
    }

    @ResponseBody
    @RequestMapping(value = "takeOver/{tid}", method = RequestMethod.POST)
    @ApiOperation(value="take over task", httpMethod="GET", response = Result.class, nickname = "ALISURE")
    public Result takeOver(
            @ApiParam(name = "tid", value = "tid") @PathVariable int tid){
        return new Result(taskService.takeOver(tid, commonService.getUserId(request)));
    }

    @ResponseBody
    @RequestMapping(value = "category", method = RequestMethod.GET)
    @ApiOperation(value = "get all category", httpMethod = "GET", response = Result.class, nickname = "ALISURE")
    public Result getCategory(){
        return new Result(taskService.getCategory());
    }
}
