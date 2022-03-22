import com.google.common.base.Throwables;
import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.inbound.IEslEventListener;
import org.freeswitch.esl.client.internal.Context;
import org.freeswitch.esl.client.internal.IModEslApi.EventFormat;
import org.freeswitch.esl.client.outbound.IClientHandler;
import org.freeswitch.esl.client.outbound.IClientHandlerFactory;
import org.freeswitch.esl.client.outbound.SocketClient;
import org.freeswitch.esl.client.transport.event.EslEvent;

import java.net.InetSocketAddress;
import java.util.List;

public class FreeSwitchEventListener {

    public static void main(String[] args) {
        try {

            final Client inboudClient = new Client();
            inboudClient.connect(new InetSocketAddress("localhost", 8021), "ClueCon", 10);
            inboudClient.addEventListener(new IEslEventListener() {
                @Override
                public void onEslEvent(Context ctx, EslEvent event) {
                    String eventName = event.getEventName();
                    System.out.println("eventName : " + eventName);

                    if (eventName.startsWith("CHANNEL_")) {

                        List<String> eventDesc = event.getEventBodyLines();
                        if (eventDesc != null)
                            System.out.println("eventDesc : " + eventDesc);

                        List<String> eventBodyLines = event.getEventBodyLines();
                        if (eventBodyLines != null)
                            System.out.println("eventBodyLines : " + eventBodyLines);

                        String calleeNumber = event.getEventHeaders().get("Caller-Callee-ID-Number");
                        String callerNumber = event.getEventHeaders().get("Caller-Caller-ID-Number");
                        switch (eventName) {
                            case "CHANNEL_CREATE":
                                System.out.println("CHANNEL_CREATE, callerNumber：" + callerNumber + " , calleeNumber：" + calleeNumber);
                                break;
                            case "CHANNEL_BRIDGE":
                                System.out.println("CHANNEL_BRIDGE, callerNumber：" + callerNumber + " , calleeNumber：" + calleeNumber);
                                break;
                            case "CHANNEL_ANSWER":
                                System.out.println("CHANNEL_ANSWER, callerNumber：" + callerNumber + " , calleeNumber：" + calleeNumber);
                                break;
                            case "CHANNEL_HANGUP":
                                String response = event.getEventHeaders().get("variable_current_application_response");
                                String hangupCause = event.getEventHeaders().get("Hangup-Cause");
                                System.out.println("CHANNEL_HANGUP, callerNumber" + callerNumber + " , calleeNumber：" + calleeNumber + " , response:" + response + " ,hangup cause:" + hangupCause);
                                break;
                            default:
                                break;
                        }
                    }
                }
////                @Override
//                public void backgroundJobResultReceived(EslEvent event) {
//                    String jobUuid = event.getEventHeaders().get("Job-UUID");
//                    System.out.println("jobUuid : " + jobUuid);
//                }
            });
            inboudClient.setEventSubscriptions(EventFormat.PLAIN, "all");

            final SocketClient outboundServer = new SocketClient(
                    new InetSocketAddress("localhost", 8084),
                    new IClientHandlerFactory() {
                        @Override
                        public IClientHandler createClientHandler() {
                            return new IClientHandler() {
                                @Override
                                public void onEslEvent(Context ctx, EslEvent event) {

                                }

                                @Override
                                public void onConnect(Context context, EslEvent eslEvent) {
                                }
                            };
                        }
                    });


        } catch (Throwable t) {
            Throwables.propagate(t);
        }
    }
}
