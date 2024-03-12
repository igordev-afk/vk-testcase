package ru.wwerlosh.vktestcase.websocket;

import java.io.IOException;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class EchoServerListener extends TextWebSocketHandler {

    private final WebSocketSession clientSession;

    public EchoServerListener(WebSocketSession clientSession) {
        this.clientSession = clientSession;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        if (clientSession != null && clientSession.isOpen()) {
            try {
                clientSession.sendMessage(new TextMessage(message.getPayload()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
