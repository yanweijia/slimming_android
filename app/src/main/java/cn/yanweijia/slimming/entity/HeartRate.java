package cn.yanweijia.slimming.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author
 */
public class HeartRate implements Serializable {
    /**
     * 测量编号
     */
    private Integer id;

    /**
     * 用户编号
     */
    private Integer userId;

    /**
     * 测量时间
     */
    private Date time;

    /**
     * 心率
     */
    private Integer rate;

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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}