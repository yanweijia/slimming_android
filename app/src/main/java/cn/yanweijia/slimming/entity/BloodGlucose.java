package cn.yanweijia.slimming.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author
 */
public class BloodGlucose implements Serializable {
    private Integer id;

    private Integer userId;

    /**
     * 血糖
     */
    private Integer glucose;

    /**
     * 测量时间
     */
    private Date time;

    /**
     * 测量方式
     */
    private String method;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGlucose() {
        return glucose;
    }

    public void setGlucose(Integer glucose) {
        this.glucose = glucose;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}