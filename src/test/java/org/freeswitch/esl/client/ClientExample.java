<<<<<<< HEAD

=======
>>>>>>> 67fa4ece90c827803b84bff101189aa21416d6f3
package org.freeswitch.esl.client;

import com.google.common.base.Throwables;
import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.internal.IModEslApi.EventFormat;

import java.net.InetSocketAddress;

public class ClientExample {

    public static void main(String[] args) {
        try {
//            if (args.length < 1) {
//                System.out.println("Usage: java ClientExample PASSWORD");
//                return;
//            }
//            String password = args[0];
            String password ="ClueCon";
            Client client = new Client();
            client.addEventListener((ctx, event) ->  System.out.println("[ClientExample] Received event: {}"+ event.getEventName()));

            client.connect(new InetSocketAddress("localhost", 8021), password, 10);
            client.setEventSubscriptions(EventFormat.PLAIN, "all");

        } catch (Throwable t) {
            Throwables.propagate(t);
        }
    }
}

