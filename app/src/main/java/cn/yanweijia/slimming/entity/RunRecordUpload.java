package cn.yanweijia.slimming.entity;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by weijia on 15/10/2017.
 *
 * @author weijia
 */
public class RunRecordUpload implements Serializable {
    /**
     * 记录编号
     */
    private Integer id;

    /**
     * 用户编号
     */
    private Integer userId;

    /**
     * 开始运动时间
     */
    private Date starttime;

    /**
     * 结束运动时间
     */
    private Date endtime;

    /**
     * 距离,单位m
     */
    private BigDecimal distance;

    /**
     * 消耗卡路里
     */
    private BigDecimal calorie;

    /**
     * 时速km/h
     */
    private BigDecimal speed;

    /**
     * 配速min/km
     */
    private BigDecimal pace;

    /**
     * 备注
     */
    private String remark;

    /**
     * 路线信息
     */
    private String road;

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

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public BigDecimal getDistance() {
        return distance;
    }

    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }

    public BigDecimal getCalorie() {
        return calorie;
    }

    public void setCalorie(BigDecimal calorie) {
        this.calorie = calorie;
    }

    public BigDecimal getSpeed() {
        return speed;
    }

    public void setSpeed(BigDecimal speed) {
        this.speed = speed;
    }

    public BigDecimal getPace() {
        return pace;
    }

    public void setPace(BigDecimal pace) {
        this.pace = pace;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }
}