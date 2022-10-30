package com.himanshu.websocketclientserver.server;


import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Slf4j
@Controller
public class ServerController {
 
    @MessageMapping("/process-message")
    @SendTo("/topic/messages")
    public OutgoingMessage processMessage(IncomingMessage incomingMessage) throws Exception{
        Thread.sleep(1000);
        log.info("incomingMessage: {}", incomingMessage);
        return new OutgoingMessage("Hello " + incomingMessage.name());
    }
}
