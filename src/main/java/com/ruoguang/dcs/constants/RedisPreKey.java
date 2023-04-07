package com.ruoguang.dcs.constants;



/**
 * RedisPreKey
 * redisKey前缀文件名常量，用于redis的key的有序分布
 * @author cc
 * @data 2020/06/03
 */
@SuppressWarnings("all")
public abstract class RedisPreKey {
    /**
     * sys:系统级别（不可应用层删）
     * cache：缓存级别（可应用层删）
     *
     */
    /**
     * 手机注册码+phoneNumber
     */
    public static final String CACHE_CODE_REGISTER = "cache:code:register:";
    /**
     * 单个user缓存+userId
     */
    public static final String CACHE_USER = "cache:user:";
    /**
     * 所有user
     */
    public static final String CACHE_USERS = "cache:users";
    /**
     * 用户权限缓存+userId
     */
    public static final String CACHE_PERMISSION_USER="cache:permission:user:";
    /**
     * pdf的全集缓存
     */
    public static final String CACHE_PDF_ALLQUERY="cache:pdf:allQuery:";
    /**
     * 用户封禁+userId
     */
    public static final String SYS_USER_BAN ="sys:user:ban:";
    /**
     * 用户限制+userId
     */
    public static final String SYS_USER_LIMIT ="sys:user:limit:";

}
