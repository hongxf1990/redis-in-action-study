package com.zer.redis.test;

import org.junit.Test;

import java.io.File;
import java.util.Arrays;

/**
 * @author zer
 * @create 2016-11-24 10:30
 */
public class MainTest {
    private static int VOTE_SCORE = 432;

    @Test
    public void test() {
        System.out.println(-VOTE_SCORE);
    }

    @Test
    public void test2() {
        File file = new File("E:\\Git");
        File[] files = file.listFiles();
        for (File file1 : files) {
            System.out.println("not sorted   " + file1.getName());
        }

        Arrays.sort(files);
        for (File file1 : files) {
            System.out.println("sorted   " + file1.getAbsolutePath());
        }
    }
}
