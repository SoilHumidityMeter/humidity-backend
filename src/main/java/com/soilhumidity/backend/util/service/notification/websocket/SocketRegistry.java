package com.soilhumidity.backend.util.service.notification.websocket;

import com.soilhumidity.backend.util.service.notification.websocket.event.WebSocketConnectEvent;
import com.soilhumidity.backend.util.service.notification.websocket.event.WebSocketDisconnectEvent;
import com.soilhumidity.backend.util.service.notification.websocket.event.WebSocketEvent;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@NoArgsConstructor
public class SocketRegistry implements SmartApplicationListener {
    private final BidiMap<String, String> users = new DualHashBidiMap<>();
    private final BidiMap<String, WebSocketSession> sessions = new DualHashBidiMap<>();
    private final Object sessionLock = new Object();

    @Override
    public boolean supportsEventType(@NotNull Class<? extends ApplicationEvent> eventType) {
        return WebSocketEvent.class.isAssignableFrom(eventType);
    }

    @Override
    @EventListener
    public void onApplicationEvent(@NotNull ApplicationEvent event) {
        WebSocketEvent wsEvent = (WebSocketEvent) event;
        String sessionId = wsEvent.getWebSocketSessionId();
        Assert.state(sessionId != null, "No session id");
        WebSocketSession session;
        String name;
        if (wsEvent instanceof WebSocketConnectEvent) {
            synchronized (sessionLock) {
                session = ((WebSocketConnectEvent) wsEvent).getWebSocketSession();
                name = (String) session.getAttributes().get("username");
                if (name != null) {
                    users.put(sessionId, name);
                    sessions.put(sessionId, session);
                }
            }
        } else if (wsEvent instanceof WebSocketDisconnectEvent) {
            synchronized (sessionLock) {
                users.remove(sessionId);
                sessions.remove(sessionId);
            }
        }
    }

    public Set<WebSocketSession> getSessions() {
        return sessions.values();
    }

    public Set<WebSocketSession> getSessions(Set<String> usernames) {

        return usernames.stream().flatMap(this::getUserSession).collect(Collectors.toSet());

    }

    public Stream<WebSocketSession> getUserSession(String username) {

        var key = users.getKey(username);

        if (key == null) {
            return Stream.empty();
        }

        return Stream.of(sessions.get(key));
    }


    @Override
    public String toString() {
        return "users=" + users;
    }

}
