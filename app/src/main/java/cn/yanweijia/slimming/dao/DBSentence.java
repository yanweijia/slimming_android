package cn.yanweijia.slimming.dao;

/**
 * Created by weijia on 30/09/2017.
 *
 * @author weijia
 */

public class DBSentence {
    public static final String DB_NAME = "slimming.db";

    public static final int DB_VERSION = 1;

    /**
     * to replace sql sentence params
     */
    public static final String REPLACEMENT_FOODCATEGORY_ID = "$ID$";
    /**
     * create user table
     */
    public static final String CREATE_TABLE_USER = "CREATE TABLE `user` (" +
            "`id`INTEGER NOT NULL," +
            "`username`varchar ( 20 ) NOT NULL UNIQUE," +
            "`password`VARCHAR ( 32 ) NOT NULL," +
            "`phone`VARCHAR ( 11 ) DEFAULT NULL," +
            "`email`VARCHAR ( 50 ) DEFAULT NULL," +
            "`name`VARCHAR ( 30 ) DEFAULT ' 佚名'," +
            "`birthday`VARCHAR ( 20 ) DEFAULT NULL," +
            "`gender`VARCHAR ( 5 ) NOT NULL DEFAULT '未知'," +
            "`height`DECIMAL ( 5 , 2 ) DEFAULT NULL," +
            "`weight`DECIMAL ( 5 , 2 ) DEFAULT NULL," +
            "`status`TINYINT NOT NULL DEFAULT 0," +
            "`reg_time`VARCHAR ( 30 ) DEFAULT NULL," +
            "`reg_ip`VARCHAR ( 50 ) DEFAULT NULL," +
            "`last_login`DATETIME DEFAULT NULL," +
            "PRIMARY KEY(`id`)" +
            ");";
    /**
     * create food category table
     */
    public static final String CREATE_TABLE_FOOD_CATEGORY = "CREATE TABLE `food_category` (" +
            "`id` INTEGER NOT NULL," +
            "`name` VARCHAR(30) NOT NULL," +
            "`image` INTEGER," +
            " PRIMARY KEY(`id`)" +
            ");";

    /**
     * create user weight table
     */
    public static final String CREATE_TABLE_USER_WEIGHT = "CREATE TABLE `user_weight` (" +
            "`id` INTEGER NOT NULL," +
            "`user_id `INTEGER," +
            "`time` VARCHAR(30) UNIQUE," +
            "`method` VARCHAR(50)," +
            "`weight` DECIMAL(6,2)," +
            "PRIMARY KEY(`id`)" +
            ");";
    /**
     * create heart rate table
     */
    public static final String CREATE_TABLE_HEART_RATE = "CREATE TABLE `heart_rate` (" +
            " `id` INTEGER NOT NULL UNIQUE," +
            " `user_id` INTEGER NOT NULL," +
            " `time` VARCHAR(30) NOT NULL UNIQUE," +
            " `rate` INTEGER NOT NULL," +
            " `method` VARCHAR(50)," +
            " PRIMARY KEY(`id`)" +
            ");";
    /**
     * create blood glucose table
     */
    public static final String CREATE_TABLE_BLOOD_GLUCOSE = "CREATE TABLE `blood_glucose` (" +
            " `id` INTEGER NOT NULL," +
            " `user_id` INTEGER NOT NULL," +
            " `glucose` INTEGER NOT NULL," +
            " `time` VARCHAR(30) NOT NULL UNIQUE," +
            " `method` VARCHAR(50)," +
            " PRIMARY KEY(`id`)" +
            ");";
    /**
     * create blood pressure table
     */
    public static final String CREATE_TABLE_BLOOD_PRESSURE = "CREATE TABLE `blood_pressure` (" +
            " `id` INTEGER NOT NULL," +
            " `user_id` INTEGER NOT NULL," +
            " `diastolic_pressure` DECIMAL(6,2) NOT NULL," +
            " `time` VARCHAR(30) NOT NULL," +
            " `systolic_pressure` DECIMAL(6,2) NOT NULL," +
            " `method` VARCHAR(50)," +
            " PRIMARY KEY(`id`)" +
            ");";
    /**
     * save weight data
     */
    public static final String SAVE_USER_WEIGHT = "INSERT  INTO `user_weight` (`id`,`user_id`,`weight`,`time`,`method` ) VALUES (?,?,?,?,?);";
    /**
     * save heart rate data
     */
    public static final String SAVE_HEART_RATE = "INSERT  INTO `heart_rate` (`id`,`user_id`,`rate`,`time`,`method` )VALUES (?,?,?,?,?);";
    /**
     * save blood glucose data
     */
    public static final String SAVE_BLOOD_GLUCOSE = "INSERT  INTO `blood_glucose` (`id`,`user_id`,`glucose`,`time`,`method` )VALUES (?,?,?,?,?);";
    /**
     * save blood pressure data
     */
    public static final String SAVE_BLOOD_PRESSURE = "INSERT  INTO `blood_pressure` (`id`,`user_id`,`diastolic_pressure`,`systolic_pressure`,`time`,`method` )VALUES (?,?,?,?,?,?);";
    /**
     * initial food category table
     */
    public static final String INIT_FOOD_CATEGORY = "INSERT INTO `food_category` (`id`, `name`, `image`)" +
            "VALUES" +
            " (1,'谷薯芋、杂豆、主食',1)," +
            " (2,'蛋类、肉类及制品',2)," +
            " (3,'奶类及制品',3)," +
            " (4,'蔬果和菌藻',4)," +
            " (5,'坚果、大豆及制品',5)," +
            " (6,'饮料',6)," +
            " (7,'食用油、油脂及制品',7)," +
            " (8,'调味品',8)," +
            " (9,'零食、点心、冷饮',9)," +
            " (10,'其它',10)," +
            " (11,'菜肴',11)," +
            " (12,'未定义',0);";

    /**
     * delete all data from user table
     */
    public static final String CLEAN_TABLE_USER = "DELETE FROM user";

    /**
     * save new user
     */
    public static final String SAVE_USER = "INSERT INTO user" +
            "(id,username,password,phone,email,name,birthday,gender,height,weight,status,reg_time,reg_ip,last_login)" +
            "VALUES" +
            "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    /**
     * query the first element from result set.
     */
    public static final String GET_USER = "SELECT * FROM user LIMIT 0,1";

    /**
     * list food categorys
     */
    public static final String LIST_FOOD_CATEGORY = "SELECT * FROM food_category";

    /**
     * get food category
     */
    public static final String GET_FOOD_CATEGORY = "SELECT * FROM food_category where id=$ID$";

    /**
     * list user weight
     */
    public static final String LIST_USER_WEIGHT = "SELECT * FROM user_weight";
    /**
     * list heart rate
     */
    public static final String LIST_HEART_RATE = "SELECT * FROM heart_rate";
    /**
     * list blood glucose
     */
    public static final String LIST_BLOOD_GLUCOSE = "SELECT * FROM blood_glucose";
    /**
     * list blood pressure
     */
    public static final String LIST_BLOOD_PRESSURE = "SELECT * FROM blood_pressure";

}
