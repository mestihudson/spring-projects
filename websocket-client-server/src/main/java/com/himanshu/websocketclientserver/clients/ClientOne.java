package com.himanshu.websocketclientserver.clients;

import com.himanshu.websocketclientserver.server.IncomingMessage;
import com.himanshu.websocketclientserver.server.OutgoingMessage;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;

import lombok.extern.slf4j.Slf4j;

/**
 * This client send its message to server which in turn send it to common topic for communication.
 */
public class ClientOne {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        if (args.length == 0) {
            throw new RuntimeException("Required args not supplied");
        }
        String name = args[0];
        WebSocketClient client = new StandardWebSocketClient();

        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        ClientOneSessionHandler clientOneSessionHandler = new ClientOneSessionHandler();
        String URI = "ws://localhost:8080/websocket-server";
        ListenableFuture<StompSession> sessionAsync =
                stompClient.connect(URI, clientOneSessionHandler);
        StompSession session = sessionAsync.get();
        session.subscribe("/topic/messages", clientOneSessionHandler);
        session.subscribe("/topic/messages/%s".formatted(name), clientOneSessionHandler);
        while (true) {
            session.send("/app/process-message/%s".formatted(name), new IncomingMessage("%s %s".formatted(name, System.currentTimeMillis())));
            Thread.sleep(2000);
        }
    }
}

@Slf4j
class ClientOneSessionHandler extends StompSessionHandlerAdapter {

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return OutgoingMessage.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        log.info("Received : {}", payload);
    }
}
