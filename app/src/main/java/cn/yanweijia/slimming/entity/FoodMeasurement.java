package cn.yanweijia.slimming.entity;


import java.io.Serializable;

/**
 * @author
 */
public class FoodMeasurement implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 食物编号
     */
    private Integer foodId;

    /**
     * 度量单位
     */
    private String unit;

    /**
     * 热量(可食部分热量)
     */
    private Integer calorie;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getCalorie() {
        return calorie;
    }

    public void setCalorie(Integer calorie) {
        this.calorie = calorie;
    }
}