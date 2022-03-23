/*
 * Copyright 2010 david varnes.
 *
 * Licensed under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.freeswitch.esl.client.inbound;

import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.freeswitch.esl.client.example.EslEventListener;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.freeswitch.esl.client.transport.message.EslHeaders.Name;
import org.freeswitch.esl.client.transport.message.EslMessage;
import org.jboss.netty.channel.ExceptionEvent;
import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class ClientTest
{
    private final Logger log = Logger.getLogger(String.valueOf(this.getClass()));

    private String host = "127.0.0.1";
    private int port = 8021;
    private String password = "ClueCon";

    @Test
    public void do_connect() throws InterruptedException
    {
        Client client = new Client();

        client.addEventListener( new EslEventListener()
        {
            public void eventReceived( EslEvent event )
            {
                log.log( Level.INFO,"Event received [{}]", event );
            }
            public void backgroundJobResultReceived( EslEvent event )
            {
                log.log( Level.INFO, "Background job result received [{}]", event );
            }

            public void conferenceEventJoin(String uniqueId, String confName, int confSize, EslEvent event) {
                log.log( Level.INFO, "Event received [{}]", event );
            }

            public void conferenceEventLeave(String uniqueId, String confName, int confSize, EslEvent event) {
                log.log( Level.INFO, "Event received [{}]", event );
            }

            public void conferenceEventMute(String uniqueId, String confName, int confSize, EslEvent event) {
                log.log( Level.INFO, "Event received [{}]", event );
            }

            public void conferenceEventUnMute(String uniqueId, String confName, int confSize, EslEvent event) {
                log.log( Level.INFO, "Event received [{}]", event );
            }

            public void conferenceEventAction(String uniqueId, String confName, int confSize, String action, EslEvent event) {
                log.log( Level.INFO, "Event received [{}]", event );
            }

            public void conferenceEventTransfer(String uniqueId, String confName, int confSize, EslEvent event) {
                log.log( Level.INFO, "Event received [{}]", event );
            }

            public void conferenceEventThreadRun(String uniqueId, String confName, int confSize, EslEvent event) {
                log.log( Level.INFO, "Event received [{}]", event );
            }

            public void conferenceEventPlayFile(String uniqueId, String confName, int confSize, EslEvent event) {
                log.log( Level.INFO, "Event received [{}]", event );
            }

            public void exceptionCaught(ExceptionEvent e) {
                log.log( Level.INFO, "exception received [{}]", e );
            }


        } );

        log.log( Level.INFO, "Client connecting .." );
        try
        {
            client.connect( host, port, password, 2 );
        }
        catch ( InboundConnectionFailure e )
        {
            log.log( Level.WARNING, "Connect failed", e );
            return;
        }
        log.log( Level.INFO, "Client connected .." );

//      client.setEventSubscriptions( "plain", "heartbeat CHANNEL_CREATE CHANNEL_DESTROY BACKGROUND_JOB" );
        client.setEventSubscriptions( "plain", "all" );
        client.addEventFilter( "Event-Name", "heartbeat" );
        client.cancelEventSubscriptions();
        client.setEventSubscriptions( "plain", "all" );
        client.addEventFilter( "Event-Name", "heartbeat" );
        client.addEventFilter( "Event-Name", "channel_create" );
        client.addEventFilter( "Event-Name", "background_job" );
        client.sendSyncApiCommand( "echo", "Foo foo bar" );

//        client.sendSyncCommand( "originate", "sofia/internal/101@192.168.100.201! sofia/internal/102@192.168.100.201!" );

//        client.sendSyncApiCommand( "sofia status", "" );
        String jobId = client.sendAsyncApiCommand( "status", "" );
        log.log( Level.INFO, "Job id [{}] for [status]", jobId );
        client.sendSyncApiCommand( "version", "" );
//        client.sendAsyncApiCommand( "status", "" );
//        client.sendSyncApiCommand( "sofia status", "" );
//        client.sendAsyncApiCommand( "status", "" );
        EslMessage response = client.sendSyncApiCommand( "sofia status", "" );
        log.log( Level.INFO, "sofia status = [{}]", response.getBodyLines().get( 3 ) );

        // wait to see the heartbeat events arrive
        Thread.sleep( 25000 );
        client.close();
    }

    @Test
    public void do_multi_connects() throws InterruptedException
    {
        Client client = new Client();

        log.log( Level.INFO, "Client connecting .." );
        try
        {
            client.connect( host, port, password, 2 );
        }
        catch ( InboundConnectionFailure e )
        {
            log.log( Level.WARNING, "Connect failed", e );
            return;
        }
        log.log( Level.INFO, "Client connected .." );

        log.log( Level.INFO, "Client connecting .." );
        try
        {
            client.connect( host, port, password, 2 );
        }
        catch ( InboundConnectionFailure e )
        {
            log.log( Level.WARNING, "Connect failed", e );
            return;
        }
        log.log( Level.INFO, "Client connected .." );

        client.close();
    }

    @Test
    public void sofia_contact()
    {
        Client client = new Client();
        try
        {
            client.connect( host, port, password, 2 );
        }
        catch ( InboundConnectionFailure e )
        {
            log.log( Level.WARNING, "Connect failed", e );
            return;
        }

        EslMessage response = client.sendSyncApiCommand( "sofia_contact", "internal/102@192.168.100.201" );

        log.log( Level.INFO, "Response to 'sofia_contact': [{}]", response );
        for ( Entry<Name, String> header : response.getHeaders().entrySet() )
        {
            log.log( Level.INFO, " * header [{}]", header );
        }
        for ( String bodyLine : response.getBodyLines() )
        {
            log.log( Level.INFO, " * body [{}]", bodyLine );
        }
        client.close();
    }
}