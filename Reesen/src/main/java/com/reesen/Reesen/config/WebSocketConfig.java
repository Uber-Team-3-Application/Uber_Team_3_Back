package com.reesen.Reesen.config;

import com.reesen.Reesen.handlers.RideHandler;
import com.reesen.Reesen.handlers.VehicleSimulation;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer, WebSocketConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic/driver/ride",
                "/topic/passenger/ride",
                "/topic/admin/panic",
                "/map-updates");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/socket")
                .setAllowedOrigins("http://localhost:4200", "http://localhost:8082")
                .withSockJS();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new RideHandler(), "/socket");
        //registry.addHandler(new VehicleSimulation(), "/simulation");
    }
}