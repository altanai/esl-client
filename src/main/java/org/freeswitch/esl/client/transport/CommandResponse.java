/*
 * Copyright 2010 david varnes.
 *
<<<<<<< HEAD
 * Licensed under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
=======
 * Licensed under the Apache License, version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
>>>>>>> 67fa4ece90c827803b84bff101189aa21416d6f3
 * You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
<<<<<<< HEAD
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
=======
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
>>>>>>> 67fa4ece90c827803b84bff101189aa21416d6f3
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.freeswitch.esl.client.transport;

import org.freeswitch.esl.client.transport.message.EslHeaders.Name;
import org.freeswitch.esl.client.transport.message.EslMessage;

/**
 * Result object to carry the results of a command sent to the FreeSWITCH Event Socket.
 */
public class CommandResponse {

    private final String command;
    private final String replyText;
    private final EslMessage response;
    private final boolean success;

    public CommandResponse(String command, EslMessage response) {
        this.command = command;
        this.response = response;
        this.replyText = response.getHeaderValue(Name.REPLY_TEXT);
        this.success = replyText.startsWith("+OK");
    }

    /**
     * @return the original command sent to the server
     */
    public String getCommand() {
        return command;
    }

    /**
     * @return true if and only if the response Reply-Text line starts with "+OK"
     */
    public boolean isOk() {
        return success;
    }

    /**
     * @return the full response Reply-Text line.
     */
    public String getReplyText() {
        return replyText;
    }

    /**
     * @return {@link EslMessage} the full response from the server
     */
    public EslMessage getResponse() {
        return response;
    }
}
