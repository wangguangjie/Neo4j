package com.org.wangguangjie.databaseproject;

/**
 * Created by wangguangjie on 2016/12/2.
 */
public class Data {
    private int tourist_id;
    private int tourist_attraction_id;
    private String begin_time;
    private String end_time;

    public void setTourist_id(int id)
    {
        this.tourist_id=id;
    }
    public int getTourist_id()
    {
        return tourist_id;
    }
    public void setTourist_attraction_id(int id)
    {
        this.tourist_attraction_id=id;
    }
    public int getTourist_attraction_id()
    {
        return tourist_attraction_id;
    }
    public void setBegin_time(String s)
    {
        this.begin_time=s;
    }
    public String getBegin_time()
    {
        return begin_time;
    }
    public void setEnd_time(String s)
    {
        this.end_time=s;
    }
    public String getEnd_time()
    {
        return end_time;
    }
}
