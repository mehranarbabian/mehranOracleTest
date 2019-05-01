package com.example.demo;

import oracle.AQ.AQQueueTable;
import oracle.AQ.AQQueueTableProperty;
import oracle.jms.AQjmsDestination;
import oracle.jms.AQjmsDestinationProperty;
import oracle.jms.AQjmsFactory;
import oracle.jms.AQjmsSession;

import javax.jms.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class QueueSample2 {
    public static void main(String args[]) {
        String userName = "mehran";
        String queue = "sample_aq";
        String qTable = "sample_aqtbl";
//        createQueue(userName, qTable, queue);
       sendMessage(userName, queue,"<user>asdsa</user>");
        browseMessage(userName, queue);
        //consumeMessage(userName, queue);
    }
    public static QueueConnection getConnection() {

        String hostname = "localhost";
        String oracle_sid = "orcl";
        int portno = 1521;
        String userName = "mehran";
        String password = "1234";
        String driver = "thin";
        QueueConnectionFactory QFac = null;
        QueueConnection QCon = null;
        try {
            QFac = AQjmsFactory.getQueueConnectionFactory(hostname, oracle_sid, portno, driver);
            QCon = QFac.createQueueConnection(userName, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return QCon;
    }

    public static void createQueue(String user, String qTable, String queueName) {
        try {
            /* Create Queue Tables */
            System.out.println("Creating Queue Table...");
            QueueConnection QCon = getConnection();
            Session session = QCon.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);

            AQQueueTableProperty qt_prop;
            AQQueueTable q_table = null;
            AQjmsDestinationProperty dest_prop;
            Queue queue = null;
            qt_prop = new AQQueueTableProperty("SYS.AQ$_JMS_TEXT_MESSAGE");

            q_table = ((AQjmsSession) session).createQueueTable(user, qTable, qt_prop);

            System.out.println("Qtable created");
            dest_prop = new AQjmsDestinationProperty();
            /* create a queue */
            queue = ((AQjmsSession) session).createQueue(q_table, queueName, dest_prop);
            System.out.println("Queue created");
            /* start the queue */
            ((AQjmsDestination) queue).start(session, true, true);

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
    public static void sendMessage(String user, String queueName,String message) {

        try {
            QueueConnection QCon = getConnection();
            Session session = QCon.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
            QCon.start();
            Queue queue = ((AQjmsSession) session).getQueue(user, queueName);
            MessageProducer producer = session.createProducer(queue);
            TextMessage tMsg = session.createTextMessage(message);

            //set properties to msg since axis2 needs this parameters to find the operation
            tMsg.setStringProperty("SOAPAction", "getQuote");
            producer.send(tMsg);
            System.out.println("Sent message = " + tMsg.getText());

            session.close();
            producer.close();
            QCon.close();

        } catch (JMSException e) {
            e.printStackTrace();
            return;
        }
    }
    public static void browseMessage(String user, String queueName) {
        Queue queue;
        try {
            QueueConnection QCon = getConnection();
            Session session = QCon.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);

            QCon.start();
            queue = ((AQjmsSession) session).getQueue(user, queueName);
            QueueBrowser browser = session.createBrowser(queue);
            Enumeration enu = browser.getEnumeration();
            List list = new ArrayList();
            while (enu.hasMoreElements()) {
                TextMessage message = (TextMessage) enu.nextElement();
                list.add(message.getText());
            }
            for (int i = 0; i < list.size(); i++) {
                System.out.println("Browsed msg " + list.get(i));
            }
            browser.close();
            session.close();
            QCon.close();

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
