package cn.software.utils;

import java.util.UUID;

/**
 * @author ：xiaopingzhang8@creditease.cn
 * @Description ：UUID 生成器
 * @ClassName ：UUIDGenerator
 * @date ：2017/4/6 11:02
 */
public class UUIDGenerator {

    /**
     *  获取一个新的Id
     * @return
     */
    public static String getNewId(){
        String uuid = UUID.randomUUID().toString();
        StringBuffer uuidBuf = new StringBuffer();
        uuidBuf.append(uuid.substring(0,8)).append(uuid.substring(9,13)).append(uuid.substring(14,18)).append(uuid.substring(19,23)).append(uuid.substring(24));
        return uuidBuf.toString();
    }

    public static String getUUId(){
        String uuid = UUID.randomUUID().toString().replace("-","");
        return uuid;
    }

    public static void main(String[] args) {
        System.out.println(getNewId().length());
        System.out.println(getUUId().length());
    }

}
