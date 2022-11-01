package com.himanshu.websocketclientserver.server;


import java.util.Random;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;


@Slf4j
@Controller
public class ServerController {
    @Autowired
    private SimpMessageSendingOperations messageSendingOperations;
 
    @MessageMapping("/process-message/{user}")
    public void processMessage(IncomingMessage incomingMessage, @DestinationVariable String user) throws Exception{
        int RANGE = 1000;
        int sleepTime = new Random().nextInt(RANGE);
        Thread.sleep(sleepTime);
        boolean even = sleepTime % 2 == 0;
        String uri = even ? "/topic/messages/%s".formatted(user) : "/topic/messages";
        OutgoingMessage outcome = new OutgoingMessage("Hello %s".formatted(incomingMessage.name()), user, sleepTime, even, uri);
        messageSendingOperations.convertAndSend(uri, outcome);
        log.info(
            "incomingMessage: {} | user: {} | outcome: {} | sleepTime: {} | even: {} | uri: {}",
            incomingMessage, user, outcome, sleepTime, even, uri
        );
    }
}
