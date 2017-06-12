package com.org.wangguangjie.databaseproject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangguangjie on 2016/12/2.
 */
public class CommonPath {
    private List<Attraction> path=new ArrayList<>();

    public void addAttraction(Attraction a)
    {
        path.add(a);
    }
    public void remveAttratciton(Attraction a)
    {
        path.remove(a);
    }
    public void printCommonPath()
    {
        for(int i=path.size()-1;i>=0;i--)
        {
            System.out.println("tourist attraction: "+path.get(i).getAttraction_tid()+" "+
                    " from "+path.get(i).getBegin_time()+" to "+path.get(i).getEnd_time());
        }
    }
}
