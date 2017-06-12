package com.org.wangguangjie.databaseproject;

import java.io.*;

/**
 * Created by wangguangjie on 2016/12/2.
 */
public class SourceData {
    final String file_path="data_Max.txt";
    BufferedReader bufferedReader;
    public void test()
    {
        System.out.println(readData().getTourist_id());
    }
    public SourceData()
    {
        try
        {
            bufferedReader=new BufferedReader(new FileReader(new File(file_path)));
        }
        catch (FileNotFoundException e)
        {
            System.out.println("无法找到文件！");
        }
    }
    public void closeFile()
    {
        try
        {
            bufferedReader.close();
        }
        catch (IOException e)
        {
            System.out.println("文件关闭异常！");
        }
    }
    public Data readData()
    {
        Data data=new Data();
        try
        {
            String line;
            String strings[];
            line=bufferedReader.readLine();
            strings=line.split(" ");
            data.setTourist_id(Integer.parseInt(strings[0]));
            data.setTourist_attraction_id(Integer.parseInt(strings[1]));
            String ss1[]=strings[2].split("-");
            String ss2[]=strings[3].split(":");
            data.setBegin_time(ss1[0]+ss1[1]+ss1[2]+ss2[0]+ss2[1]);
            String ss3[]=strings[4].split("-");
            String ss4[]=strings[5].split(":");
            data.setEnd_time(ss3[0]+ss3[1]+ss3[2]+ss4[0]+ss4[1]);
            //list_data.add(data);
            return data;
        }
        catch (IOException ie)
        {
            System.out.println("文件读取异常！");
        }
        return data;
    }
}
