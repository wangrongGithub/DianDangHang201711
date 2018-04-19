package com.alisure.service;

import com.alisure.dao.CommonDao;
import com.alisure.dao.TaskDao;
import com.alisure.dao.UserDao;
import com.alisure.entity.*;
import com.alisure.tool.core.CoreString;
import com.alisure.tool.core.CoreTime;
import com.alisure.weixin.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("taskService")
public class TaskService {

    @Autowired
    TaskDao taskDao;

    @Autowired
    UserDao userDao;
    @Autowired
    CommonDao commonDao;

    @Autowired
    UserService userService;

    /**
     * 获取大厅的任务
     * @param page
     * @param pageSize
     * @param status
     * @param type
     * @param sortType
     * @param school
     * @return
     */
    public List<InfoTask> getHallTask(int page, int pageSize, int status, int type, int sortType, String school) {
        if (page < 1) page = 1;
        if (pageSize < 1 || pageSize > 30) pageSize = 10;
        if (type < 0) type = 0;
        if (sortType < 0) sortType = 1;
        if (CoreString.isNull(school)) return null;
        List<InfoTask> infoTasks = taskDao.getHallTask(page, pageSize,status,type,sortType,school);
        for(InfoTask infoTask: infoTasks){
            infoTask.setPub(commonDao.getUser(infoTask.getPubId()) );
        }
        return infoTasks;
    }

    /**
     * 获取任务的信息，返回任务、发布者、接手者、任务目前的状态信息
     * @param tid
     * @param userId
     * @return
     */
    public Map<String, Object> getTask(int tid, int userId) {
        if (tid <= 0) return null;

        InfoTask infoTask = taskDao.getTask(tid);
        InfoUser pub = infoTask.getPubId() > 0 ? commonDao.getUser(infoTask.getPubId()) : null;
        InfoUser rec = infoTask.getRecId() > 0 ? commonDao.getUser(infoTask.getRecId()) : null;
        infoTask.setPub(pub);
        infoTask.setRec(rec);

        String message = null;
        int statusCode = 0;

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("task", infoTask);

        if(infoTask.getPubStateId() == 1){ // 处于等待接手状态
            if(infoTask.getPubId() == userId){ // 用户是发布者
                message = "状态是待接手，用户是发布者";
                statusCode = 1;
            }else{ // 其他人
                infoTask.setQq("");
                infoTask.setWeixin("");
                infoTask.setTelephone("");
                infoTask.setWords("");
                message = "状态是待接手，用户是路人";
                statusCode = 2;
            }
        } else if (infoTask.getPubStateId() == 4){ /* 代表任务彻底结束*/
            if(infoTask.getPubId() == userId){ // 用户是发布者
                message = "状态是结束，用户是发布者";
                statusCode = 9;
            }else if(infoTask.getRecId() == userId){ // 用户是接收者
                message = "状态是结束，用户是接收者";
                statusCode = 10;
            }else{ // 其他人看不到
                return null;
            }
        }else{ // 处于其他状态
            if (infoTask.getRecStateId() == 2){ // 任务处于进行中
                if (infoTask.getPubId() == userId){
                    message = "状态是进行中，用户是发布者";
                    statusCode = 3;
                }else if (infoTask.getRecId() == userId){
                    message = "状态是进行中，用户是接手者";
                    statusCode = 4;
                }else{ // 其他人看不到这个
                    return null;
                }
            } else if (infoTask.getRecStateId() == 3 || infoTask.getPubStateId() == 3){ //任务已取消
                if (infoTask.getPubId() == userId){
                    message = "状态是已取消，用户是发布者";
                    statusCode = 5;
                }else if (infoTask.getRecId() == userId){
                    message = "状态是已取消，用户是接手者";
                    statusCode = 6;
                }else{ // 讲道理，已取消的应该重新回到大厅，但是数据库没有弄，改起来有点复杂。。。。
                    return null;
                }
            } else if (infoTask.getRecStateId() == 4){ // 任务已完成
                if(infoTask.getPubId() == userId){ // 用户是发布者
                    message = "状态是已完成，用户是发布者";
                    statusCode = 7;
                }else if(infoTask.getRecId() == userId){ // 用户是接收者
                    message = "状态是已完成，用户是接收者";
                    statusCode = 8;
                }else{ // 其他人看不到
                    return null;
                }
            }
        }
        result.put("message", message);
        result.put("statusCode", statusCode);
        return result;
    }

    /**
     * 更新任务的状态
     * @param tid
     * @param means：0：确认完成，1：取消， 2：重新发布
     * @param userId
     * @return
     */
    public boolean updateTaskStatus(int tid, int means, int userId) {
        if(tid <= 0 || means < 0 || means > 2 || userId <= 0) return false;
        InfoTask infoTask = taskDao.getTask(tid);
        infoTask.setPub(commonDao.getUser(infoTask.getPubId()));
        if(infoTask.getRecId() > 0){
            infoTask.setRec(commonDao.getUser(infoTask.getRecId()));
        }
        if(means == 0){ // 确认完成
            if(infoTask.getRecId() == userId){ // 如果用户是接手者
                if(taskDao.setRecSituation(4, tid, CoreTime.getDataTime())){
                    // 在这里向微信中发送信息
                    Template.finshTaskOK(commonDao.getUserOpenid(infoTask.getRecId()), infoTask);
                    Template.finshTaskOK(commonDao.getUserOpenid(infoTask.getPubId()), infoTask);
                    // 接手者确认完成，不进行积分的操作！
                    return true;
                }
            }else if(infoTask.getPubId() == userId){ // 如果用户是发布者，把金币奖励给接手者
                if(taskDao.setPubSituation(4, tid, CoreTime.getDataTime())){
                    // 在这里向微信中发送信息
                    Template.endTaskOK(commonDao.getUserOpenid(infoTask.getRecId()), infoTask);
                    Template.endTaskOK(commonDao.getUserOpenid(infoTask.getPubId()), infoTask);
                    // 发布者确认完成，此时应该进行积分的处理，在发送模板的时候应该体现出来！
                    return userService.updateCoinsByTask(infoTask.getRecId(), infoTask.getTid(), infoTask.getCoins(), InfoMoneyChange.ChangeReason.Reason_End_Task);
                }
            }
        }else if(means == 2){ //重新发布
            if(infoTask.getPubId() == userId){  // 如果用户是发布者
                if(taskDao.setRePub(tid)){
                    // 在这里向微信中发送信息
                    // ...

                    // 此时还应该处理积分问题
                    return userService.updateCoinsByTask(infoTask.getPubId(), infoTask.getTid(), 0 - infoTask.getCoins(), InfoMoneyChange.ChangeReason.Reason_Publish_Task);
                }
            }
            return false;
        }else{ // 取消，把金币还给发布者
            if(infoTask.getRecId() == userId){ // 如果用户是接手者
                if(taskDao.setRecSituation(3, tid, CoreTime.getDataTime())){
                    // 在这里向微信中发送信息
                    Template.cancelTaskOK_rec(commonDao.getUserOpenid(infoTask.getRecId()), infoTask);
                    Template.cancelTaskOK_pub(commonDao.getUserOpenid(infoTask.getPubId()), infoTask);
                    // 此时还应该处理积分问题
                    return userService.updateCoinsByTask(infoTask.getPubId(), infoTask.getTid(), infoTask.getCoins(), InfoMoneyChange.ChangeReason.Reason_Cancel_Task_Rec);
                }
            }else if(infoTask.getPubId() == userId){ // 如果用户是发布者
                // 讲道理：发布者取消应该检查一下是否被接手了。
                if(taskDao.setPubSituation(3, tid, CoreTime.getDataTime())){
                    // 在这里向微信中发送信息
                    Template.cancelTaskOK_pub(commonDao.getUserOpenid(infoTask.getPubId()), infoTask);
                    // 此时还应该处理积分问题
                    return userService.updateCoinsByTask(infoTask.getPubId(), infoTask.getTid(), infoTask.getCoins(), InfoMoneyChange.ChangeReason.Reason_Cancel_Task_Pub);
                }
            }
        }
        return false;
    }

    /**
     * 发布成功
     * @param infoTask
     * @param openId
     * @return
     */
    public int publishTask(InfoTask infoTask, String openId) {
        // 检查
        int nowCoins = userDao.getUserCoins(infoTask.getPubId());
        if(nowCoins < infoTask.getCoins()){
            return 0;
        }
        // 保存任务
        int tid = taskDao.publishTask(infoTask);
        infoTask.setTid(tid);
        infoTask.setPub(commonDao.getUser(infoTask.getPubId()));
        // 处理金币消耗
        boolean resultCoins = userDao.updateUserCoins(infoTask.getPubId(), infoTask.getTid(), 0 - infoTask.getCoins(), InfoMoneyChange.ChangeReason.Reason_Publish_Task);
        if(resultCoins) {
            // 发送发布成功的消息
            Template.pubTaskOK(openId, infoTask);
            return tid;
        }
        return 0;
    }

    /**
     * 接手任务
     * @param tid
     * @param userId
     * @return
     */
    public boolean takeOver(int tid, int userId) {
        // 检查
        InfoTask infoTask = taskDao.getTask(tid);
        infoTask.setRecId(userId);
        if(infoTask == null || infoTask.getPubId() == userId) return false;  // 自己接手
        if(infoTask.getPubStateId() == 2 || infoTask.getRecStateId() == 2) return false;  // 已经被接手

        // 设置接手
        int pubStateId = 2;
        int recStateId = 2;
        String taskTime = CoreTime.getDataTime();
        infoTask.setPub(commonDao.getUser(infoTask.getPubId()));
        infoTask.setRec(commonDao.getUser(infoTask.getRecId()));
        if(taskDao.takeOver(userId, pubStateId, recStateId, taskTime, tid)) {
            // 给发布者发送模板信息
            Template.recTaskOK_pub(commonDao.getUserOpenid(infoTask.getPubId()), infoTask);
            // 给接手者发送模板信息
            Template.recTaskOK_rec(commonDao.getUserOpenid(infoTask.getRecId()), infoTask);
            return true;
        }
        return false;
    }

    /**
     * 获取所有类别
     * @return
     */
    public List<InfoCategory> getCategory() {
        return taskDao.getCategory();
    }
}
