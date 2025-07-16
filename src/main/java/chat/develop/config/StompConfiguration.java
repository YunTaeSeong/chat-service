package chat.develop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@EnableWebSocketMessageBroker
@Configuration
// STOMP는 Controller를 통해서 메시지 작성
public class StompConfiguration implements WebSocketMessageBrokerConfigurer {

    // WebSocket Client 가 어떤 경로로 서버에 접속해야 하는지 EndPoints 지정
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp/chats");
    }

    // 클라이언트에서 메시지를 발행하고 클라이언트는 브로커로부터 전달되는 메시지를 받기위해 구독을 설정
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메시지를 published 할 수 있는 url 지정
        registry.setApplicationDestinationPrefixes("/pub");
        // 메시지 구독 신청
        registry.enableSimpleBroker("/sub");
    }
}
