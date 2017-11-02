package com.alisure.entity;

import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InfoMoneyChange implements RowMapper<InfoMoneyChange>,Serializable {
    private int changeId;
    private int changeCoins;
    private String changeTime;
    private int tid;
    private int uid;
    private String changeReason;

    public InfoMoneyChange(){

    }

    public InfoMoneyChange(int changeId, int changeCoins, String changeTime, int tid, int uid, String changeReason) {
        this.changeId = changeId;
        this.changeCoins = changeCoins;
        this.changeTime = changeTime;
        this.tid = tid;
        this.uid = uid;
        this.changeReason = changeReason;
    }

    public int getChangeId() {
        return changeId;
    }

    public void setChangeId(int changeId) {
        this.changeId = changeId;
    }

    public int getChangeCoins() {
        return changeCoins;
    }

    public void setChangeCoins(int changeCoins) {
        this.changeCoins = changeCoins;
    }

    public String getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(String changeTime) {
        this.changeTime = changeTime;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public InfoMoneyChange mapRow(ResultSet resultSet, int i) throws SQLException {
        return new InfoMoneyChange(
                resultSet.getInt("changeId"),
                resultSet.getInt("changeCoins"),
                resultSet.getString("changeTime"),
                resultSet.getInt("tid"),
                resultSet.getInt("uid"),
                resultSet.getString("changeReason")
        );
    }

    /*��ұ仯��ԭ��*/
    public class ChangeReason {
        public final static String Reason_New_User = "��ע�˹��ں�";

        public final static String Reason_Publish_Task = "����һ������";
        public final static String Reason_End_Task = "���һ������";
        public final static String Reason_Cancel_Task_Rec = "������ȡ��һ������";
        public final static String Reason_Cancel_Task_Pub = "������ȡ��һ������";
    }

}
