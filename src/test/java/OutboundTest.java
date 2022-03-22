import com.google.common.base.Throwables;

import org.freeswitch.esl.client.dptools.Execute;
import org.freeswitch.esl.client.dptools.ExecuteException;
import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.internal.Context;
import org.freeswitch.esl.client.outbound.IClientHandler;
import org.freeswitch.esl.client.outbound.SocketClient;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.freeswitch.esl.client.transport.message.EslHeaders.Name;

import org.freeswitch.esl.client.transport.message.EslMessage;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Date;

import java.util.List;
import java.util.Map;

public class OutboundTest {

    //    private static final Logger logger = LoggerFactory.getLogger(OutboundTest.class);
    private static final String sb = "/usr/local/freeswitch/sounds/en/us/callie/ivr/8000/";
    String prompt = sb + "ivr-please_enter_extension_followed_by_pound.wav";
    String failed = sb + "ivr-that_was_an_invalid_entry.wav";


    public static void main(String[] args) {
        new OutboundTest();
    }

    public OutboundTest() {
        try {

            final Client inboudClient = new Client();
            inboudClient.connect(new InetSocketAddress("localhost", 8021), "ClueCon", 10);
            inboudClient.addEventListener((ctx, event) -> System.out.println("INBOUND onEslEvent: {}" + event.getEventName()));


            final SocketClient outboundServer = new SocketClient(
                    new InetSocketAddress("localhost", 8084),
                    () -> new IClientHandler() {
                        @Override

                        public void onConnect(Context context, EslEvent eslEvent) {

                            System.out.println(nameMapToString(eslEvent.getMessageHeaders(), eslEvent.getEventBodyLines()));


                            //  System.out.println(eslEvent.getEventHeaders());
                            // output in evenHeaders text file

                            String calleeNumber = eslEvent.getEventHeaders().get("Caller-Callee-ID-Number");
                            String callerNumber = eslEvent.getEventHeaders().get("Caller-Caller-ID-Number");
                            System.out.println("callerNumber：" + callerNumber + " , calleeNumber：" + calleeNumber);

//                            String uuid = eslEvent.getEventHeaders().get("unique-id"); //
                            String uuid = eslEvent.getEventHeaders().get("Unique-ID");
//                            if (uuid.equalsIgnoreCase(""))
//                                uuid = "altanai4321";

                            System.out.println("Creating execute app for uuid {}" + uuid);
                            Execute exe = new Execute(context, uuid);
                            try {

                                // transfer
//                                EslMessage response = inboudClient.sendApiCommand("sofia_contact", "internal/1002@192.168.0.121");
//                                System.out.println(response);

//                                exe.answer();
//                                exe.echo(); // sets up echo

                                long call_start = new Date().getTime();
                                System.out.println("Call Answered " + call_start);

                                exe.say("en", "123456", "number", "pronounced");

//                                String digits = exe.playAndGetDigits(3,
//                                        5, 10, 10 * 1000, "#", prompt,
//                                        failed, "^\\d+", 10 * 1000);
//
//                                System.out.println("Digits collected: {}" + digits);

                                // record

                            } catch (ExecuteException e) {
                                System.out.println("Could not prompt for digits");
                                e.printStackTrace();
                            } finally {
                                try {
                                    System.out.println("hangup");
                                    exe.hangup(null);
                                } catch (ExecuteException e) {
                                    System.out.println("Could not hangup");
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onEslEvent(Context ctx, EslEvent event) {
                            String eventname=event.getEventName();
                            System.out.println("OUTBOUND onEslEvent: {}" + eventname);

                            if ("CHANNEL_HANGUP_COMPLETE" .equals(eventname)) {
                                System.out.println( "Enter the on-hook completion event" );
                                Map <String, String> dd = event.getEventHeaders();
                                System.out.println( "variable_effective_caller_id_number is::::::" + dd.get("variable_effective_caller_id_number" ));
                            }
                        }
                    });
            outboundServer.startAsync();

        } catch (Throwable t) {
            Throwables.propagate(t);
        }
    }

    public static String nameMapToString(Map<Name, String> map, List<String> lines) {
        StringBuilder sb = new StringBuilder("\nHeaders:\n");
//        for (Name key : map.keySet()) {
//            if (key == null)
//                continue;
//            sb.append(key);
//            sb.append("\n\t\t\t\t = \t ");
//            sb.append(map.get(key));
//            sb.append("\n");
//        }
//        if (lines != null) {
//            sb.append("Body Lines:\n");
//            for (String line : lines) {
//                sb.append(line);
//                sb.append("\n");
//            }
//        }

        for (Map.Entry<Name, String> entry : map.entrySet()) {
            System.out.println("Key = " + entry.getKey() +
                    ", Value = " + entry.getValue());
        }

        for (int i = 0; i < lines.size(); i++) {
            System.out.print(lines.get(i) + ", ");
        }
        System.out.println("\n");

        return sb.toString();
    }
}

