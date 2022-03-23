/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.freeswitch.esl.client.example;

import org.freeswitch.esl.client.IEslEventListener;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.jboss.netty.channel.ExceptionEvent;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * @author leif
 */
public class EslEventListener implements IEslEventListener {
//    private final Logger log = LoggerFactory.getLogger( this.getClass() );

    public void eventReceived(EslEvent event) {
        System.out.println("Event received [{}]" + event);
    }

    public void backgroundJobResultReceived(EslEvent event) {
        System.out.println("Background job result received [{}]" + event);
    }

    public void conferenceEventJoin(String uniqueId, String confName, int confSize, EslEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append(uniqueId);
        Object[] args = new Object[3];
        args[0] = confName;
        args[1] = confSize;
        args[2] = sb.toString();
        System.out.println("Conference [{}]({}) JOIN [{}]" + args);
    }

    public void conferenceEventLeave(String uniqueId, String confName, int confSize, EslEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append(uniqueId);
        Object[] args = new Object[3];
        args[0] = confName;
        args[1] = confSize;
        args[2] = sb.toString();
        System.out.println("Conference [{}]({}) LEAVE [{}]" + args);
    }

    public void conferenceEventMute(String uniqueId, String confName, int confSize, EslEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append(uniqueId);
        System.out.println("Conference [{}] MUTE [{}]" + confName + " " + sb);
    }

    public void conferenceEventUnMute(String uniqueId, String confName, int confSize, EslEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append(uniqueId);
        System.out.println("Conference [{}] UNMUTE [{}]" + confName + " " + sb);
    }

    public void conferenceEventAction(String uniqueId, String confName, int confSize, String action, EslEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append(uniqueId);
        sb.append(" action=[");
        sb.append(action);
        sb.append("]");
        System.out.println("Conference [{}] Action [{}]" + confName + " " + sb);
    }

    public void conferenceEventTransfer(String uniqueId, String confName, int confSize, EslEvent event) {
        //Noop
    }

    public void conferenceEventRecord(String uniqueId, String confName, int confSize, EslEvent event) {
        //Noop
    }

    public void conferenceEventThreadRun(String uniqueId, String confName, int confSize, EslEvent event) {
        //Noop
    }

    public void conferenceEventPlayFile(String uniqueId, String confName, int confSize, EslEvent event) {
        //Noop
    }

    public void exceptionCaught(ExceptionEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

}