package cn.yanweijia.slimming.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author
 */
public class BloodPressure implements Serializable {
    private Integer id;

    /**
     * 用户编号
     */
    private Integer userId;

    /**
     * 舒张压,60~90
     */
    private BigDecimal diastolicPressure;

    /**
     * 测量时间
     */
    private Date time;

    /**
     * 收缩压100~140
     */
    private BigDecimal systolicPressure;

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

    public BigDecimal getDiastolicPressure() {
        return diastolicPressure;
    }

    public void setDiastolicPressure(BigDecimal diastolicPressure) {
        this.diastolicPressure = diastolicPressure;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public BigDecimal getSystolicPressure() {
        return systolicPressure;
    }

    public void setSystolicPressure(BigDecimal systolicPressure) {
        this.systolicPressure = systolicPressure;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}