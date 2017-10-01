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
     * create user table
     */
    public static final String CREATE_TABLE_USER = "CREATE TABLE `user` (" +
            "`id`INTEGER NOT NULL," +
            "`username`varchar ( 20 ) NOT NULL UNIQUE," +
            "`password`VARCHAR ( 32 ) NOT NULL," +
            "`phone`VARCHAR ( 11 ) DEFAULT NULL," +
            "`email`VARCHAR ( 50 ) DEFAULT NULL," +
            "`name`VARCHAR ( 30 ) DEFAULT ' 佚名'," +
            "`birthday`DATE DEFAULT NULL," +
            "`gender`VARCHAR ( 5 ) NOT NULL DEFAULT '未知'," +
            "`height`DECIMAL ( 5 , 2 ) DEFAULT NULL," +
            "`weight`DECIMAL ( 5 , 2 ) DEFAULT NULL," +
            "`status`TINYINT NOT NULL DEFAULT 0," +
            "`reg_time`DATETIME DEFAULT NULL," +
            "`reg_ip`VARCHAR ( 50 ) DEFAULT NULL," +
            "`last_login`DATETIME DEFAULT NULL," +
            "PRIMARY KEY(`id`)" +
            ");";
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

}
