package com.example.demo;

import oracle.AQ.*;


import java.sql.Connection;
import java.sql.DriverManager;

public class AdvancedQueue {
    public static void main(String[] args) throws AQException {
 AQSession aqSession=createSession(args);
 runTest(aqSession);
    }
    public static AQSession createSession(String args[])
    {
        Connection db_conn;
        AQSession aq_sess = null;

        try
        {

            Class.forName("oracle.jdbc.driver.OracleDriver");


            db_conn =
                    DriverManager.getConnection(
                            "jdbc:oracle:thin:@localhost:1521:orcl",
                            "mehran", "1234");

            System.out.println("JDBC Connection opened ");
            db_conn.setAutoCommit(false);

            Class.forName("oracle.AQ.AQOracleDriver");

            aq_sess = AQDriverManager.createAQSession(db_conn);
            System.out.println("Successfully created AQSession ");
        }
        catch (Exception ex)
        {
            System.out.println("Exception: " + ex);
            ex.printStackTrace();
        }
        return aq_sess;
    }
    public static void runTest(AQSession aq_sess) throws AQException
    {
        AQQueueTableProperty qtable_prop;
        AQQueueProperty queue_prop;
        AQQueueTable             q_table ;
        AQQueue                 queue;
        AQAgent                  subs1, subs2;


        qtable_prop = new AQQueueTableProperty("RAW");
        qtable_prop.setMultiConsumer(true);


        q_table = aq_sess.createQueueTable ("mehran", "aq", qtable_prop);
        System.out.println("Successful createQueueTable");


        queue_prop = new AQQueueProperty();

       queue= aq_sess.createQueue (q_table, "mehran", queue_prop);
        queue.start();
        System.out.println("Successful start queue");

        /* Add subscribers to this queue: */
        subs1 = new AQAgent("GREEN", null, 0);
        subs2 = new AQAgent("BLUE", null, 0);

        queue.addSubscriber(subs1, null);
        System.out.println("Successful addSubscriber 1");

        queue.addSubscriber(subs2, "priority < 2");
        System.out.println("Successful addSubscriber 2");
    }



}
