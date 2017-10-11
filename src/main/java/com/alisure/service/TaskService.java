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
     * ��ȡ����������
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
     * ��ȡ�������Ϣ���������񡢷����ߡ������ߡ�����Ŀǰ��״̬��Ϣ
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

        if(infoTask.getPubStateId() == 1){ // ���ڵȴ�����״̬
            if(infoTask.getPubId() == userId){ // �û��Ƿ�����
                message = "״̬�Ǵ����֣��û��Ƿ�����";
                statusCode = 1;
            }else{ // ������
                infoTask.setQq("");
                infoTask.setWeixin("");
                infoTask.setTelephone("");
                infoTask.setWords("");
                message = "״̬�Ǵ����֣��û���·��";
                statusCode = 2;
            }
        } else if (infoTask.getPubStateId() == 4){ /* �������񳹵׽���*/
            if(infoTask.getPubId() == userId){ // �û��Ƿ�����
                message = "״̬�ǽ������û��Ƿ�����";
                statusCode = 9;
            }else if(infoTask.getRecId() == userId){ // �û��ǽ�����
                message = "״̬�ǽ������û��ǽ�����";
                statusCode = 10;
            }else{ // �����˿�����
                return null;
            }
        }else{ // ��������״̬
            if (infoTask.getRecStateId() == 2){ // �����ڽ�����
                if (infoTask.getPubId() == userId){
                    message = "״̬�ǽ����У��û��Ƿ�����";
                    statusCode = 3;
                }else if (infoTask.getRecId() == userId){
                    message = "״̬�ǽ����У��û��ǽ�����";
                    statusCode = 4;
                }else{ // �����˿��������
                    return null;
                }
            } else if (infoTask.getRecStateId() == 3 || infoTask.getPubStateId() == 3){ //������ȡ��
                if (infoTask.getPubId() == userId){
                    message = "״̬����ȡ�����û��Ƿ�����";
                    statusCode = 5;
                }else if (infoTask.getRecId() == userId){
                    message = "״̬����ȡ�����û��ǽ�����";
                    statusCode = 6;
                }else{ // ��������ȡ����Ӧ�����»ص��������������ݿ�û��Ū���������е㸴�ӡ�������
                    return null;
                }
            } else if (infoTask.getRecStateId() == 4){ // ���������
                if(infoTask.getPubId() == userId){ // �û��Ƿ�����
                    message = "״̬������ɣ��û��Ƿ�����";
                    statusCode = 7;
                }else if(infoTask.getRecId() == userId){ // �û��ǽ�����
                    message = "״̬������ɣ��û��ǽ�����";
                    statusCode = 8;
                }else{ // �����˿�����
                    return null;
                }
            }
        }
        result.put("message", message);
        result.put("statusCode", statusCode);
        return result;
    }

    /**
     * ���������״̬
     * @param tid
     * @param means��0��ȷ����ɣ�1��ȡ��
     * @param userId
     * @return
     */
    public boolean updateTaskStatus(int tid, int means, int userId) {
        if(tid <= 0 || means < 0 || means > 1 || userId <= 0) return false;
        InfoTask infoTask = taskDao.getTask(tid);
        infoTask.setPub(commonDao.getUser(infoTask.getPubId()));
        if(infoTask.getRecId() > 0){
            infoTask.setRec(commonDao.getUser(infoTask.getRecId()));
        }
        if(means == 0){ // ȷ�����
            if(infoTask.getRecId() == userId){ // ����û��ǽ�����
                if(taskDao.setRecSituation(4, tid, CoreTime.getDataTime())){
                    // ��������΢���з�����Ϣ
                    Template.finshTaskOK(commonDao.getUserOpenid(infoTask.getRecId()), infoTask);
                    Template.finshTaskOK(commonDao.getUserOpenid(infoTask.getPubId()), infoTask);
                    // ������ȷ����ɣ������л��ֵĲ�����
                    return true;
                }
            }else if(infoTask.getPubId() == userId){ // ����û��Ƿ����ߣ��ѽ�ҽ�����������
                if(taskDao.setPubSituation(4, tid, CoreTime.getDataTime())){
                    // ��������΢���з�����Ϣ
                    Template.endTaskOK(commonDao.getUserOpenid(infoTask.getRecId()), infoTask);
                    Template.endTaskOK(commonDao.getUserOpenid(infoTask.getPubId()), infoTask);
                    // ������ȷ����ɣ���ʱӦ�ý��л��ֵĴ����ڷ���ģ���ʱ��Ӧ�����ֳ�����
                    return userService.updateCoinsByTask(infoTask.getRecId(), infoTask.getTid(), infoTask.getCoins(), InfoMoneyChange.ChangeReason.Reason_End_Task);
                }
            }
        }else{ // ȡ�����ѽ�һ���������
            if(infoTask.getRecId() == userId){ // ����û��ǽ�����
                if(taskDao.setRecSituation(3, tid, CoreTime.getDataTime())){
                    // ��������΢���з�����Ϣ
                    Template.cancelTaskOK_rec(commonDao.getUserOpenid(infoTask.getRecId()), infoTask);
                    Template.cancelTaskOK_pub(commonDao.getUserOpenid(infoTask.getPubId()), infoTask);
                    // ��ʱ��Ӧ�ô����������
                    return userService.updateCoinsByTask(infoTask.getPubId(), infoTask.getTid(), infoTask.getCoins(), InfoMoneyChange.ChangeReason.Reason_Cancel_Task_Rec);
                }
            }else if(infoTask.getPubId() == userId){ // ����û��Ƿ�����
                // ������������ȡ��Ӧ�ü��һ���Ƿ񱻽����ˡ�
                if(taskDao.setPubSituation(3, tid, CoreTime.getDataTime())){
                    // ��������΢���з�����Ϣ
                    Template.cancelTaskOK_pub(commonDao.getUserOpenid(infoTask.getPubId()), infoTask);
                    // ��ʱ��Ӧ�ô����������
                    return userService.updateCoinsByTask(infoTask.getPubId(), infoTask.getTid(), infoTask.getCoins(), InfoMoneyChange.ChangeReason.Reason_Cancel_Task_Pub);
                }
            }
        }
        return false;
    }

    /**
     * �����ɹ�
     * @param infoTask
     * @param openId
     * @return
     */
    public int publishTask(InfoTask infoTask, String openId) {
        // ���
        int nowCoins = userDao.getUserCoins(infoTask.getPubId());
        if(nowCoins < infoTask.getCoins()){
            return 0;
        }
        // ��������
        int tid = taskDao.publishTask(infoTask);
        infoTask.setTid(tid);
        infoTask.setPub(commonDao.getUser(infoTask.getPubId()));
        // ����������
        boolean resultCoins = userDao.updateUserCoins(infoTask.getPubId(), infoTask.getTid(), 0 - infoTask.getCoins(), InfoMoneyChange.ChangeReason.Reason_Publish_Task);
        if(resultCoins) {
            // ���ͷ����ɹ�����Ϣ
            Template.pubTaskOK(openId, infoTask);
            return tid;
        }
        return 0;
    }

    /**
     * ��������
     * @param tid
     * @param userId
     * @return
     */
    public boolean takeOver(int tid, int userId) {
        // ���
        InfoTask infoTask = taskDao.getTask(tid);
        infoTask.setRecId(userId);
        if(infoTask == null || infoTask.getPubId() == userId) return false;  // �Լ�����
        if(infoTask.getPubStateId() == 2 || infoTask.getRecStateId() == 2) return false;  // �Ѿ�������

        // ���ý���
        int pubStateId = 2;
        int recStateId = 2;
        String taskTime = CoreTime.getDataTime();
        infoTask.setPub(commonDao.getUser(infoTask.getPubId()));
        infoTask.setRec(commonDao.getUser(infoTask.getRecId()));
        if(taskDao.takeOver(userId, pubStateId, recStateId, taskTime, tid)) {
            // �������߷���ģ����Ϣ
            Template.recTaskOK_pub(commonDao.getUserOpenid(infoTask.getPubId()), infoTask);
            // �������߷���ģ����Ϣ
            Template.recTaskOK_rec(commonDao.getUserOpenid(infoTask.getRecId()), infoTask);
            return true;
        }
        return false;
    }

    /**
     * ��ȡ�������
     * @return
     */
    public List<InfoCategory> getCategory() {
        return taskDao.getCategory();
    }
}
