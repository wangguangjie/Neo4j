package com.org.wangguangjie.databaseproject;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.io.fs.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by wangguangjie on 2016/12/1.
 */
public class ConstructNode {
    //数据库文件;
    final  File db_path=new File("mydatabase/neo4j-db");
    GraphDatabaseService graphDb;
    Node firstNode;
    Node seconNnode;
    Relationship relationship;

    //为每个节点分别添加索引;
    Index<Node> tourist_nodeIndex;
    Index<Node> attraction_nodeindex;
    SourceData sourceData;

    public ConstructNode()
    {
        sourceData=new SourceData();
    }
    //关系类型；
    private static enum RelTypes implements RelationshipType
    {
        VIST
    }
    //创建数据库;
    public void createDB() throws IOException
    {
        FileUtils.deleteRecursively(db_path);
        graphDb=new GraphDatabaseFactory().newEmbeddedDatabase(db_path);
        registerShutdownHook(graphDb);
        //createNode();
        inserData();
    }

    //插入数据;
    public void inserData()
    {
        int i=0;
        for(;i<30000000;) {
            try (Transaction t = graphDb.beginTx()) {
                tourist_nodeIndex = graphDb.index().forNodes("tourist");
                attraction_nodeindex = graphDb.index().forNodes("tourist_attraction");
                for (int k = 0; k < 100000; k++,i++) {
                    Data d = sourceData.readData();
                    Node findNode1 = tourist_nodeIndex.get("id", d.getTourist_id()).getSingle();
                    if (findNode1 == null) {
                        findNode1 = graphDb.createNode();
                        findNode1.setProperty("id", d.getTourist_id());
                        tourist_nodeIndex.add(findNode1, "id", d.getTourist_id());
                        findNode1.addLabel(Label.label("tourist"));
                    }
                    Node findNode2 = attraction_nodeindex.get("id", d.getTourist_attraction_id()).getSingle();
                    if (findNode2 == null) {
                        findNode2 = graphDb.createNode();
                        findNode2.setProperty("id", d.getTourist_attraction_id());
                        attraction_nodeindex.add(findNode2, "id", d.getTourist_attraction_id());
                        findNode2.addLabel(Label.label("tourist_attraction"));
                    }
                    Relationship relationship = findNode1.createRelationshipTo(findNode2, RelTypes.VIST);
                    relationship.setProperty("time", d.getBegin_time() + " " + d.getEnd_time());
                    //Relationship relationship1=findNode1.hasRelationship();
                }
                t.success();
            }
        }
    }
    //创建节点;
    public void createNode()
    {
        try(Transaction t=graphDb.beginTx())
        {
            firstNode=graphDb.createNode();
            firstNode.setProperty("id",1);
            seconNnode=graphDb.createNode();
            seconNnode.setProperty("id",2);
            relationship=firstNode.createRelationshipTo(seconNnode,RelTypes.VIST);
            relationship.setProperty("time","2016.7.2");
            System.out.println(firstNode.getProperties("id"));
            System.out.println(seconNnode.getProperties("id"));
            t.success();
        }
    }
    //移除节点;
    public void removeNode()
    {
        try ( Transaction tx = graphDb.beginTx() )
        {
            // START SNIPPET: removingData
            // let's remove the data
            firstNode.getSingleRelationship( RelTypes.VIST, Direction.OUTGOING ).delete();
            firstNode.delete();
            seconNnode.delete();
            // END SNIPPET: removingData

            tx.success();
        }
    }
    //关闭数据库;
    public void shutDownDB()
    {
        System.out.println("数据节点插入完毕！");
        System.out.println("Shutting down database...");
        graphDb.shutdown();
    }

    //
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
