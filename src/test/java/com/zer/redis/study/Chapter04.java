package com.zer.redis.study;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 处理日志信息并储存到redis中
 * @author hongxf
 * @create 2016-11-29 9:06
 */
public class Chapter04 {

    public void processLogs(Jedis conn, String path) throws IOException {
        List<String> list = conn.mget("progress:file", "progress:position");
        String currentFile = list.get(0); //当前文件
        String offset = list.get(1);//当前位置

        Pipeline pipe = conn.pipelined();

        File file = new File(path);
        File[] files = file.listFiles();
        Arrays.sort(files);
        for (File file1 : files) {
            if (file1.getName().compareTo(currentFile) < 0) { //略过处理过的文件
                continue;
            }
            BufferedReader reader = null;
            if (file1.getName().compareTo(currentFile) == 0) { //处理未处理的文件，并略过处理的部分
                reader = new BufferedReader(new FileReader(file1));
                reader.skip(Long.valueOf(offset));
            } else {
                offset = "0";
            }
            currentFile = null;

            int index = 1;
            String line = reader.readLine();
            while (line != null) {
                calllback(pipe, line);
                offset = String.valueOf(Integer.parseInt(offset) + line.length());
                //处理完1000行就进行存储
                if (index % 1000 == 0) {
                    updateProcess(pipe, file1.getName(), offset);
                }
                line = reader.readLine();
                index++;
            }
            updateProcess(pipe, file1.getName(), offset);
            reader.close();
        }
    }

    private void updateProcess(Pipeline pipe, String fileName, String offset) {
        pipe.mset("progress:file", fileName, "progress:position", offset);
        pipe.exec();
    }

    private void calllback(Pipeline pipe, String line) {
        System.out.println("处理行");
    }
}
