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
package org.freeswitch.esl.client.example;

import org.freeswitch.esl.client.outbound.AbstractOutboundClientHandler;
import org.freeswitch.esl.client.transport.SendMsg;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.freeswitch.esl.client.transport.message.EslMessage;
import org.freeswitch.esl.client.transport.message.EslHeaders.Name;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;

/**
 * Simple example of a handler for outbound connection from FreeSWITCH server.
 * This class will log some of the FreeSWTICH call channel variables and
 * then hangup the call.
 *
 * @author david varnes
 */
public class SimpleHangupOutboundHandler extends AbstractOutboundClientHandler {

    @Override
    protected void handleConnectResponse(ChannelHandlerContext ctx, EslEvent event) {
        System.out.println("Received connect response [{}]" + event);
        if (event.getEventName().equalsIgnoreCase("CHANNEL_DATA")) {
            // this is the response to the initial connect
            System.out.println("=======================  incoming channel data  =============================");
            System.out.println("Event-Date-Local: [{}]" + event.getEventDateLocal());
            System.out.println("Unique-ID: [{}]" + event.getEventHeaders().get("Unique-ID"));
            System.out.println("Channel-ANI: [{}]" + event.getEventHeaders().get("Channel-ANI"));
            System.out.println("Answer-State: [{}]" + event.getEventHeaders().get("Answer-State"));
            System.out.println("Caller-Destination-Number: [{}]" + event.getEventHeaders().get("Caller-Destination-Number"));
            System.out.println("=======================  = = = = = = = = = = =  =============================");

            // now hangup the call
            hangupCall(ctx.getChannel());
        } else {
            throw new IllegalStateException("Unexpected event after connect: [" + event.getEventName() + ']');
        }
    }

    @Override
    protected void handleEslEvent(ChannelHandlerContext ctx, EslEvent event) {
        System.out.println("Received event [{}]" + event);
    }

    private void hangupCall(Channel channel) {
        SendMsg hangupMsg = new SendMsg();
        hangupMsg.addCallCommand("execute");
        hangupMsg.addExecuteAppName("hangup");

        EslMessage response = sendSyncMultiLineCommand(channel, hangupMsg.getMsgLines());

        if (response.getHeaderValue(Name.REPLY_TEXT).startsWith("+OK")) {
            System.out.println("Call hangup successful");
        } else {
            System.out.println("Call hangup failed: [{}}" + response.getHeaderValue(Name.REPLY_TEXT));
        }
    }
}