package org.freeswitch.esl.client.inbound;

import java.net.InetSocketAddress;
import java.util.Map.Entry;

import org.freeswitch.esl.client.inbound.IEslEventListener;
import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.inbound.InboundConnectionFailure;
import org.freeswitch.esl.client.internal.Context;
import org.freeswitch.esl.client.internal.IModEslApi;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.freeswitch.esl.client.transport.message.EslMessage;
import org.freeswitch.esl.client.transport.message.EslHeaders.Name;
import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class ClientTest {
    private final String host = "127.0.0.1";
    private final int port = 8021;
    private final String password = "ClueCon";

    @Test
    public void do_connect() throws InterruptedException {
        Client client = new Client();

        client.addEventListener(new IEslEventListener() {
                                    @Override
                                    public void onEslEvent(Context ctx, EslEvent event) {
                                        System.out.println("Event received [{}]" + event);
                                    }
                                }

//            public void eventReceived( EslEvent event )
//            {
//
//            }
//            public void backgroundJobResultReceived( EslEvent event )
//            {
//                System.out.println("Background job result received [{}]"+ event );
//            }

        );
        System.out.println("Client connecting ..");
        try {
//            client.connect(host, port, password, 2);
            client.connect(new InetSocketAddress("localhost", 8021), "ClueCon", 10);
        } catch (InboundConnectionFailure e) {
            System.out.println("Connect failed");
            e.printStackTrace();
            return;
        }
        System.out.println("Client connected ..");

        client.setEventSubscriptions(IModEslApi.EventFormat.PLAIN, "heartbeat CHANNEL_CREATE CHANNEL_DESTROY BACKGROUND_JOB");
        client.setEventSubscriptions(IModEslApi.EventFormat.PLAIN, "all");
        client.addEventFilter("Event-Name", "heartbeat");
        client.cancelEventSubscriptions();
        client.setEventSubscriptions(IModEslApi.EventFormat.PLAIN, "all");
        client.addEventFilter("Event-Name", "heartbeat");
        client.addEventFilter("Event-Name", "channel_create");
        client.addEventFilter("Event-Name", "background_job");

//        client.sendAsyncApiCommand("uuid_send_info", "<UUID> <my message>");
        client.sendApiCommand("echo", "Foo foo bar");

//        client.sendSyncCommand( "originate", "sofia/internal/101@192.168.100.201! sofia/internal/102@192.168.100.201!" );
//
        client.sendApiCommand("sofia status", "");
//        String jobId = client.sendAsyncApiCommand( "status", "" );
//        log.info( "Job id [{}] for [status]", jobId );
//        client.sendSyncApiCommand( "version", "" );
////        client.sendAsyncApiCommand( "status", "" );
////        client.sendSyncApiCommand( "sofia status", "" );
////        client.sendAsyncApiCommand( "status", "" );
//        EslMessage response = client.sendSyncApiCommand( "sofia status", "" );
//        log.info( "sofia status = [{}]", response.getBodyLines().get( 3 ) );
//
        // wait to see the heartbeat events arrive
        Thread.sleep(25000);
        client.close();
    }


    @Test
    public void do_multi_connects() throws InterruptedException {
        Client client = new Client();

        System.out.println("Client1 connecting ..");
        try {
            client.connect(new InetSocketAddress("localhost", 8021), "ClueCon", 10);
        } catch (InboundConnectionFailure e) {
            System.out.println("Connect1 failed");
            e.printStackTrace();
            return;
        }
        System.out.println("Client1 connected ..");

        System.out.println("Client2 connecting ..");
        try {
            client.connect(new InetSocketAddress("localhost", 8021), "ClueCon", 10);
        } catch (InboundConnectionFailure e) {
            System.out.println("Connect2 failed");
            e.printStackTrace();
            return;
        }
        System.out.println("Client2 connected ..");

        client.close();
    }

    @Test
    public void sofia_contact() {
        Client client = new Client();
        try {
            client.connect(new InetSocketAddress("localhost", 8021), "ClueCon", 10);
        } catch (InboundConnectionFailure e) {
            System.out.println("Connect failed");
            e.printStackTrace();
            return;
        }

        EslMessage response = client.sendApiCommand("sofia_contact", "internal/1000@192.168.0.121");

        System.out.println("Response to 'sofia_contact': [{}]" + response);
        for (Entry<Name, String> header : response.getHeaders().entrySet()) {
            System.out.println(" * header [{}]" + header);
        }
        for (String bodyLine : response.getBodyLines()) {
            System.out.println(" * body [{}]" + bodyLine);
        }
        client.close();
    }
}