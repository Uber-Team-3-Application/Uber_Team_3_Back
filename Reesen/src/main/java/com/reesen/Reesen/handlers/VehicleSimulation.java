package com.reesen.Reesen.handlers;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import static com.reesen.Reesen.handlers.RideHandler.addSession;
import static com.reesen.Reesen.handlers.RideHandler.removeSession;

public class VehicleSimulation implements WebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        addSession(session);

    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        removeSession(session);

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
