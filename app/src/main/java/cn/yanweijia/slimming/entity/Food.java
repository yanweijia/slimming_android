package cn.yanweijia.slimming.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author
 */
public class Food implements Serializable {
    /**
     * 食物编号
     */
    private Integer foodId;

    /**
     * 分类
     */
    private Integer category;

    /**
     * 名字
     */
    private String name;

    /**
     * 每100g卡路里
     */
    private BigDecimal calorie;

    /**
     * 推荐性,10最好,1不推荐,0未知
     */
    private Short recommended;

    /**
     * 评价
     */
    private String comment;

    /**
     * 碳水化合物(克)
     */
    private BigDecimal nutritionCarbohydrate;

    /**
     * 脂肪(克)
     */
    private BigDecimal nutritionFat;

    /**
     * 蛋白质(克)
     */
    private BigDecimal nutritionProtein;

    /**
     * 纤维素(克)
     */
    private BigDecimal nutritionFibre;

    /**
     * 维生素A(微克)
     */
    private BigDecimal nutritionVitaminA;

    /**
     * 维生素C(毫克)
     */
    private BigDecimal nutritionVitaminC;

    /**
     * 维生素E(毫克)
     */
    private BigDecimal nutritionVitaminE;

    /**
     * 胡萝卜素(微克)
     */
    private BigDecimal nutritionCarotene;

    /**
     * 硫胺素(毫克)
     */
    private BigDecimal nutritionThiamine;

    /**
     * 核黄素(毫克)
     */
    private BigDecimal nutritionRiboflavin;

    /**
     * 烟酸(毫克)
     */
    private BigDecimal nutritionNiacin;

    /**
     * 胆固醇(毫克)
     */
    private BigDecimal nutritionCholesterol;

    /**
     * 镁(毫克)
     */
    private BigDecimal nutritionMagnesium;

    /**
     * 钙(毫克)
     */
    private BigDecimal nutritionCalcium;

    /**
     * 铁(毫克)
     */
    private BigDecimal nutritionIron;

    /**
     * 锌(毫克)
     */
    private BigDecimal nutritionZinc;

    /**
     * 铜(毫克)
     */
    private BigDecimal nutritionCopper;

    /**
     * 锰(毫克)
     */
    private BigDecimal nutritionManganese;

    /**
     * 钾(毫克)
     */
    private BigDecimal nutritionPotassium;

    /**
     * 磷(毫克)
     */
    private BigDecimal nutritionPhosphorus;

    /**
     * 钠(毫克)
     */
    private BigDecimal nutritionSodium;

    /**
     * 硒(微克)
     */
    private BigDecimal nutritionSelenium;

    private static final long serialVersionUID = 1L;

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCalorie() {
        return calorie;
    }

    public void setCalorie(BigDecimal calorie) {
        this.calorie = calorie;
    }

    public Short getRecommended() {
        return recommended;
    }

    public void setRecommended(Short recommended) {
        this.recommended = recommended;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public BigDecimal getNutritionCarbohydrate() {
        return nutritionCarbohydrate;
    }

    public void setNutritionCarbohydrate(BigDecimal nutritionCarbohydrate) {
        this.nutritionCarbohydrate = nutritionCarbohydrate;
    }

    public BigDecimal getNutritionFat() {
        return nutritionFat;
    }

    public void setNutritionFat(BigDecimal nutritionFat) {
        this.nutritionFat = nutritionFat;
    }

    public BigDecimal getNutritionProtein() {
        return nutritionProtein;
    }

    public void setNutritionProtein(BigDecimal nutritionProtein) {
        this.nutritionProtein = nutritionProtein;
    }

    public BigDecimal getNutritionFibre() {
        return nutritionFibre;
    }

    public void setNutritionFibre(BigDecimal nutritionFibre) {
        this.nutritionFibre = nutritionFibre;
    }

    public BigDecimal getNutritionVitaminA() {
        return nutritionVitaminA;
    }

    public void setNutritionVitaminA(BigDecimal nutritionVitaminA) {
        this.nutritionVitaminA = nutritionVitaminA;
    }

    public BigDecimal getNutritionVitaminC() {
        return nutritionVitaminC;
    }

    public void setNutritionVitaminC(BigDecimal nutritionVitaminC) {
        this.nutritionVitaminC = nutritionVitaminC;
    }

    public BigDecimal getNutritionVitaminE() {
        return nutritionVitaminE;
    }

    public void setNutritionVitaminE(BigDecimal nutritionVitaminE) {
        this.nutritionVitaminE = nutritionVitaminE;
    }

    public BigDecimal getNutritionCarotene() {
        return nutritionCarotene;
    }

    public void setNutritionCarotene(BigDecimal nutritionCarotene) {
        this.nutritionCarotene = nutritionCarotene;
    }

    public BigDecimal getNutritionThiamine() {
        return nutritionThiamine;
    }

    public void setNutritionThiamine(BigDecimal nutritionThiamine) {
        this.nutritionThiamine = nutritionThiamine;
    }

    public BigDecimal getNutritionRiboflavin() {
        return nutritionRiboflavin;
    }

    public void setNutritionRiboflavin(BigDecimal nutritionRiboflavin) {
        this.nutritionRiboflavin = nutritionRiboflavin;
    }

    public BigDecimal getNutritionNiacin() {
        return nutritionNiacin;
    }

    public void setNutritionNiacin(BigDecimal nutritionNiacin) {
        this.nutritionNiacin = nutritionNiacin;
    }

    public BigDecimal getNutritionCholesterol() {
        return nutritionCholesterol;
    }

    public void setNutritionCholesterol(BigDecimal nutritionCholesterol) {
        this.nutritionCholesterol = nutritionCholesterol;
    }

    public BigDecimal getNutritionMagnesium() {
        return nutritionMagnesium;
    }

    public void setNutritionMagnesium(BigDecimal nutritionMagnesium) {
        this.nutritionMagnesium = nutritionMagnesium;
    }

    public BigDecimal getNutritionCalcium() {
        return nutritionCalcium;
    }

    public void setNutritionCalcium(BigDecimal nutritionCalcium) {
        this.nutritionCalcium = nutritionCalcium;
    }

    public BigDecimal getNutritionIron() {
        return nutritionIron;
    }

    public void setNutritionIron(BigDecimal nutritionIron) {
        this.nutritionIron = nutritionIron;
    }

    public BigDecimal getNutritionZinc() {
        return nutritionZinc;
    }

    public void setNutritionZinc(BigDecimal nutritionZinc) {
        this.nutritionZinc = nutritionZinc;
    }

    public BigDecimal getNutritionCopper() {
        return nutritionCopper;
    }

    public void setNutritionCopper(BigDecimal nutritionCopper) {
        this.nutritionCopper = nutritionCopper;
    }

    public BigDecimal getNutritionManganese() {
        return nutritionManganese;
    }

    public void setNutritionManganese(BigDecimal nutritionManganese) {
        this.nutritionManganese = nutritionManganese;
    }

    public BigDecimal getNutritionPotassium() {
        return nutritionPotassium;
    }

    public void setNutritionPotassium(BigDecimal nutritionPotassium) {
        this.nutritionPotassium = nutritionPotassium;
    }

    public BigDecimal getNutritionPhosphorus() {
        return nutritionPhosphorus;
    }

    public void setNutritionPhosphorus(BigDecimal nutritionPhosphorus) {
        this.nutritionPhosphorus = nutritionPhosphorus;
    }

    public BigDecimal getNutritionSodium() {
        return nutritionSodium;
    }

    public void setNutritionSodium(BigDecimal nutritionSodium) {
        this.nutritionSodium = nutritionSodium;
    }

    public BigDecimal getNutritionSelenium() {
        return nutritionSelenium;
    }

    public void setNutritionSelenium(BigDecimal nutritionSelenium) {
        this.nutritionSelenium = nutritionSelenium;
    }
}
