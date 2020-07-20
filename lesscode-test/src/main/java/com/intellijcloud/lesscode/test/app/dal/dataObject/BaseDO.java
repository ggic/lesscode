package com.intellijcloud.lesscode.test.app.dal.dataObject;

import lombok.Data;

import java.util.Date;

@Data
public class BaseDO {
    private String isDeleted;

    private String creator;

    private Date gmtCreated;

    private String modifier;

    private Date gmtModified;

    private final static String SYSTEM = "SYSTEM";

    public void initBaseData() {
        this.setCreator(SYSTEM);
        this.setGmtCreated(new Date());
        this.setModifier(SYSTEM);
        this.setGmtModified(new Date());
        this.setIsDeleted("N");
    }


    public void initBaseData(String loginName){
        this.setCreator(loginName);
        this.setGmtCreated(new Date());
        this.setModifier(loginName);
        this.setGmtModified(new Date());
        this.setIsDeleted("N");
    }


    public void initModifyData() {
        this.setModifier(SYSTEM);
        this.setGmtModified(new Date());
    }

    /**
     * 根据用户名构建更新信息
     * @param userName
     */
    public void initModifyData(String userName){
        this.setModifier(userName);
        this.setGmtModified(new Date());
    }


    /**
     * 删除无效
     */
    public void setInvalid() {
        this.setIsDeleted("Y");
        this.setGmtModified(new Date());
    }
}
