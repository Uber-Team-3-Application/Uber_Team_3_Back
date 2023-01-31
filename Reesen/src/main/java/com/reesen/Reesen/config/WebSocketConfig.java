package com.reesen.Reesen.config;

import com.reesen.Reesen.handlers.RideHandler;
import com.reesen.Reesen.handlers.RideSimulationHandler;
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
                "/topic/map-updates-regular/",
                "/topic/passenger/accept-ride",
                "/topic/driver/accept-ride",
                "/topic/admin/panic",
                "/topic/panic",
                "/topic/map-updates",
                "/topic/driver/start-ride",
                "/topic/passenger/start-ride",
                "/topic/passenger/end-ride",
                "/topic/driver/end-ride",
                "/topic/admin/end-ride");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/socket", "/vehicle-simulation")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new RideHandler(), "/socket");
        registry.addHandler(new RideSimulationHandler(), "/vehicle-simulation");
    }
}