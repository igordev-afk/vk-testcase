package ru.wwerlosh.vktestcase.websocket;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class WSListener extends TextWebSocketHandler {

    private final String ECHO_SERVER_URI;

    private WebSocketSession echoSession;

    public WSListener(String ECHO_SERVER_URI) {
        this.ECHO_SERVER_URI = ECHO_SERVER_URI;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        WebSocketClient client = new StandardWebSocketClient();
        echoSession = client.doHandshake(new EchoServerListener(session), ECHO_SERVER_URI).get();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        if (echoSession != null && echoSession.isOpen()) {
            echoSession.sendMessage(new TextMessage(message.getPayload()));
        }
    }
}