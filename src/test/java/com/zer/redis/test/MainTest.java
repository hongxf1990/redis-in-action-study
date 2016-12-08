package com.zer.redis.test;

import com.google.gson.Gson;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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

    public static final int[] PRECISION = new int[]{1, 5, 60, 300, 3600, 18000, 86400};
    @Test
    public void test3(){
        for (int prec : PRECISION) {
            //取得当前时间片的开始时间
            long now = System.currentTimeMillis() / 1000;
            long pnow = (now / prec) * prec;
            System.out.println(prec + "--" + pnow);
        }
    }

    @Test
    public void test4() {
        int passes = 3;
        String hash = "300:hits";
        int prec = Integer.parseInt(hash.substring(0, hash.indexOf(':')));
        int bprec = (int)Math.floor(prec / 60);
        System.out.println("bprec---" + bprec);
        if (bprec == 0){
            bprec = 1;
        }
        System.out.println(passes % bprec);
        if ((passes % bprec) != 0){
            System.out.println("....");
        }
    }

    @Test
    public void test5() {
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("3");
        list.add("2");
//        String[] store = new String[3];
        list.subList(0, 2).toArray(new String[0]);
        System.out.println("...");
//        for (String str : store) {
//            System.out.println(str);
//        }
    }

    @Test
    public void testCSV() {
        String path = "E:\\1.csv";
        try (FileReader reader = new FileReader(new File(path))) {
            Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(reader);
            for (CSVRecord record : records) {
                System.out.println(record.size());
                String s = record.get(0);
                System.out.println(s);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test6() {
        Gson gson = new Gson();
        File file = new File("E:\\2.csv");
        FileReader reader = null;
        try{
            reader = new FileReader(file);
            Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(reader);
            for (CSVRecord record : records) {
                if (record.size() < 4 || !Character.isDigit(record.get(0).charAt(0))){
                    continue;
                }
                String cityId = record.get(0);
                String country = record.get(1);
                String region = record.get(2);
                String city = record.get(3);
                String json = gson.toJson(new String[]{city, region, country});
                System.out.println(json);
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }finally{
            try{
                reader.close();
            }catch(Exception e){
                // ignore
            }
        }
    }
}
