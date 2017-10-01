package cn.yanweijia.slimming.entity;

import java.io.Serializable;

/**
 * @author
 */
public class FoodCategory implements Serializable {
    /**
     * 分类编号
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 图片编号
     */
    private Integer image;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }
}