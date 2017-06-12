package com.org.wangguangjie.databaseproject;

/**
 * Created by wangguangjie on 2016/12/2.
 */
public class Attraction {
    private int attraction_tid;
    private String begin_time;
    private String end_time;

    public void setAttraction_tid(int id)
    {
        this.attraction_tid=id;
    }
    public int getAttraction_tid()
    {
        return attraction_tid;
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
