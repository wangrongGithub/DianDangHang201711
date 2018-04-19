package com.alisure.dao;

import com.alisure.entity.InfoCategory;
import com.alisure.entity.InfoTask;
import com.alisure.tool.core.CorePrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("taskDao")
public class TaskDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int publishTask(InfoTask task){
        String sql = "insert into t_task(coins,title,content,pubTime,startTime,endTime,place,qq,weixin,telephone,rewards,catId,words,images,school,recStateId,pubId,pubStateId)" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql, new Object[]{task.getCoins(), task.getTitle(), task.getContent(), task.getPubTime(), task.getStartTime(),
                task.getEndTime(), task.getPlace(), task.getQq(), task.getWeixin(), task.getTelephone(), task.getRewards(),
                task.getCatId(), task.getWords(), task.getImages(), task.getSchool(), task.getRecStateId(), task.getPubId(), task.getPubStateId()});
        try {
            sql = "select tid from t_task where pubTime=? and pubId=?";
            return jdbcTemplate.queryForObject(sql, new Object[]{task.getPubTime(), task.getPubId()}, Integer.class);
        }catch (EmptyResultDataAccessException e){
            CorePrint.printlnTime("EmptyResultDataAccessException: TaskDao.publishTask:" + task.getTid());
            return 0;
        }
    }

    public List<InfoTask> getHallTask(int page, int pageSize, int status, int type, int sortType, String school) {
        String order = sortType == 1 ? "t.tid" : "t.coins"; //�����������
        List<InfoTask> results = null;
        if (type == 0) {
            //δָ����ʾ���ֱ����ʾ
            String sql = "select t.*, c.* from t_task t, t_category c where t.pubStateId = ? and t.school = ? and t.catId = c.catId order by " + order + " desc limit ?, ?";
            results = jdbcTemplate.query(sql, new Object[]{status, school, (page-1) * pageSize, pageSize}, new InfoTask());
        }else {
            //ָ����ʾ���
            String sql = "select t.*, c.* from t_task t, t_category c where t.pubStateId = ? and t.school = ? and t.catId = ?  and t.catId = c.catId order by " + order + " desc limit ?, ?";
            results = jdbcTemplate.query(sql, new Object[]{status, school, type, (page-1) * pageSize, pageSize}, new InfoTask());
        }
        return results;
    }

    public InfoTask getTask(int tid) {
        try {
            String sql = "select t.*, c.* from t_task t, t_category c  where t.tid = ? and t.catId = c.catId ";
            return jdbcTemplate.queryForObject(sql, new Object[]{tid}, new InfoTask());
        }catch (EmptyResultDataAccessException e){
            CorePrint.printlnTime("EmptyResultDataAccessException: TaskDao.getTask:" + tid);
            return null;
        }
    }

    public boolean setPubSituation(int i, int tid, String time) {
        String timeType = "";
        if(i == 3){ /*��ȡ��*/
            timeType = "pubCancelTime";
        }else if(i == 4){ /*�����*/
            timeType = "pubCompleteTime";
        }else{/*�����֡��ѽ��ֽ����С�����*/
            return false;
        }
        String sql = "update t_task set pubStateId=?, " + timeType + "=? where tid=?";
        return jdbcTemplate.update(sql, new Object[]{i, time, tid}) > 0;
    }

    public boolean setRecSituation(int i, int tid, String time) {
        String timeType = "";
        if(i == 3){ /*��ȡ��*/
            timeType = "recCancelTime";
        }else if(i == 4){ /*�����*/
            timeType = "recCompleteTime";
        }else{/*�����֡��ѽ��ֽ����С�����*/
            return false;
        }
        String sql = "update t_task set recStateId=?, " + timeType + "=?  where tid=?";
        return jdbcTemplate.update(sql, new Object[]{i, time, tid}) > 0;
    }

    public boolean takeOver(int recId, int pubStateId, int recStateId, String recTime, int tid) {
        String sql = "update t_task set recId=?,pubStateId=?,recStateId=?,recTime=? where tid=?";
        return jdbcTemplate.update(sql, new Object[]{recId, pubStateId, recStateId, recTime, tid}) > 0;
    }

    public List<InfoCategory> getCategory() {
        String sql = "select * from t_category";
        return jdbcTemplate.query(sql, new InfoCategory());
    }

    public boolean setRePub(int tid) {
        /*���ж��ܷ����·���������ȡ��״̬�������·���*/
        String sql = "update t_task set recId=NULL, recStateId=1, pubStateId=1, recTime=NULL, recCancelTime=NULL, pubCancelTime=NULL where tid=? and (recStateId = 3 or pubStateId = 3)";
        return jdbcTemplate.update(sql, new Object[]{tid}) > 0;
    }
}