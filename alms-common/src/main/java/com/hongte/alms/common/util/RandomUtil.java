package com.hongte.alms.common.util;

import java.util.Random;

/**
 * @author zengkun
 * @since 2018/3/6
 */
public class RandomUtil {

    public static Integer generateRandomInt(Integer min,Integer max){

        Random random = new Random();

        int s = random.nextInt(max)%(max-min+1) + min;
        return s;
    }
}
