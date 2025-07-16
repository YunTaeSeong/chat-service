package chat.develop.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
// WebSocketHandler 를 implements 로 안받아도, TextWebSocketHandler 를 상속 받으면 필요한 부분만 구현 가능
public class WebSocketChatHandler extends TextWebSocketHandler {

    final Map<String, WebSocketSession> webSocketSessionMap = new ConcurrentHashMap<>();

    // WebSocket 클라이언트가 서버로 연결된 이후에 실행되는 코드 작성
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("{} connected", session.getId());

        this.webSocketSessionMap.put(session.getId(), session);
    }

    // WebSocket 클라이언트에서 메시지가 왔을 때 그 메시지를 처리
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("{} send {}", session.getId(), message.getPayload());

        // A라는 클라이언트에서 온 메시지를 다른 클라이언트가 처리
        this.webSocketSessionMap.values().forEach(
                webSocketSession -> {
                    try {
                        webSocketSession.sendMessage(message);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
        ;
    }

    // 서버에 접속해 있던 WebSocketClient랑 연결을 끊었을 때 처리
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("{} disconnected", session.getId());

        this.webSocketSessionMap.remove(session.getId());
    }
}
