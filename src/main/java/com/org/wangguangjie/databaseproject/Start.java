package com.org.wangguangjie.databaseproject;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.traversal.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.*;

/**
 * Created by wangguangjie on 2016/12/1.
 */
public class Start {

    final  File db_file=new File("mydatabase/neo4j-db");
    GraphDatabaseService graphDB;

    //存储用户输入的tourist;
    List<String> tourist_list=new ArrayList<>();
    //存储查询的tourist节点;
    List<Node> tourist_node_list=new ArrayList<>();
    //存储满足条件的旅游景点的时间段,添加到路径中;
    CommonPath commonPath;

    Index<Node> tourist_index;

    Date date;
    //构造函数初始化数据库等参数;
    public Start() throws IOException
    {
        commonPath=new CommonPath();
        Date date=new Date();
        System.out.println("正在开启图形数据库！");
        graphDB=new GraphDatabaseFactory().newEmbeddedDatabase(db_file );
        registerShutdownHook(graphDB);
        System.out.print("已经开启Neo4j图形数据库！");
        System.out.println("(用时"+(new Date().getTime()-date.getTime())+"毫秒!)");
    }

    public static void main(String[] args) throws IOException
    {
        //注释部分为图数据库的创建和节点的导入;
//        ConstructNode constructNode=new ConstructNode();
//        constructNode.createDB();
//        constructNode.shutDownDB();
        //创建好数据库并且导入数据节点之后，开启数据查询功能，查找公共旅游景点的公共时间段;
        Start start=new Start();
        start.inPut();
        start.findCommonAttraction();
        start.display();
        start.shutDonwDatabase();

    }

    //查找在共同旅游景点的公共时间段;
    public void findCommonAttraction()
    {
        try(Transaction t=graphDB.beginTx()) {
            Node node1=tourist_node_list.get(0);
            Iterable<Relationship> rels=node1.getRelationships(Direction.OUTGOING);
            int k=0;
            //扫描某一个tourist节点的全部关系;
            for(Relationship r:rels)
            {
                Node node=r.getEndNode();
                String start_time_max="0";
                String end_time_min="12312460";
                Iterable<Relationship> relss=node.getRelationships(Direction.INCOMING);
                int count=0;
                //对于每个tourist的关系，找到旅游景点节点，判断这个节点是否有公共的旅游时间段;
                for(Relationship rr:relss)
                {
                    Node node2=rr.getStartNode();
                    //System.out.println("k="+k++);
                    if(tourist_node_list.contains(node2))
                    {
                        //System.out.println("k="+k++);
                        String[] ss1,ss2;
                        ss1=r.getProperty("time").toString().split(" ");
                        ss2=rr.getProperty("time").toString().split(" ");
                        start_time_max=max(ss1[0].substring(4,12),ss2[0].substring(4,12));
                        end_time_min=min(ss1[1].substring(4,12),ss2[1].substring(4,12));
                        if(Integer.parseInt(start_time_max)<Integer.parseInt(end_time_min))
                        {
                            count++;
                        }
                    }
                }
                //找到某个有相同时间段的旅游景点,添加到路径中;
                if(count==tourist_node_list.size())
                {
                    Attraction attraction1=new Attraction();
                    int id=(int)node.getProperty("id");
                    attraction1.setAttraction_tid(id);
                    attraction1.setBegin_time(parseStringToTime("2016"+start_time_max));
                    attraction1.setEnd_time(parseStringToTime("2016"+end_time_min));
                    commonPath.addAttraction(attraction1);
                }

            }
            t.success();
        }

    }

    //min;
    public String min(String s1,String s2)
    {
        if(Integer.parseInt(s1)>Integer.parseInt(s2))
        {
            return s2;
        }
        else
            return s1;
    }
    //max;
    public String max(String s1,String s2)
    {
        if(Integer.parseInt(s1)>Integer.parseInt(s2))
        {
            return s1;
        }
        else
            return s2;
    }

    //转化时间格式;
    public String parseStringToTime(String s)
    {
        String time;
        time=s.substring(0,4)+"-"+s.substring(4,6)+"-"+s.substring(6,8)+" "+s.substring(8,10)+":"+s.substring(10,12);
        return time;
    }

    //用户输入景点;
    public void inPut()
    {
        while(true) {
            System.out.println("Please input tourists,and input ok finish:");
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String s = scanner.nextLine();
                if (s.equals("ok"))
                    break;
                if ((!s.equals(" ")) && (!s.equals("\n")) && (!s.equals("\t"))) {
                    tourist_list.add(s);
                }
            }

            if(tourist_list.size()>1)
            {
                date=new Date();
                idToNode();
                break;
            }
            System.out.println("输入有错，请重新输入！");
            tourist_list.clear();


        }
    }

    //通过索引找到对应tourist的节点;
    public void idToNode()
    {
        try(Transaction t=graphDB.beginTx())
        {
            tourist_index=graphDB.index().forNodes("tourist");
            for(int i=0;i<tourist_list.size();i++)
            {
                int id=Integer.parseInt(tourist_list.get(i));
                Node node = tourist_index.get("id",id).getSingle();
                if(node==null)
                {
                    System.out.println("输入的tourist有误，请重新输入！");
                    return;
                }
                tourist_node_list.add(node);
            }
            t.success();
        }
    }
    //输出结果;
    public void display()
    {
        System.out.println("查询用时"+(new Date().getTime()-date.getTime())+"毫秒");
        commonPath.printCommonPath();
    }
    public void shutDonwDatabase()
    {
        graphDB.shutdown();
        System.out.println("数据库关闭！");
    }
    //保证系统正常关闭数据库;
    private static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        } );
    }
}