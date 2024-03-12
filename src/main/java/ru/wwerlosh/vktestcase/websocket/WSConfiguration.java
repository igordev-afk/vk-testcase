package ru.wwerlosh.vktestcase.websocket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WSConfiguration implements WebSocketConfigurer {

    @Value("${spring.ws.echo-server}")
    private String ECHO_SERVER_URI;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WSListener(ECHO_SERVER_URI), "/ws").setAllowedOrigins("*");
    }
}
