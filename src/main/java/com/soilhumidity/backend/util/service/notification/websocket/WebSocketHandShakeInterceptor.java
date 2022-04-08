package com.soilhumidity.backend.util.service.notification.websocket;

import com.soilhumidity.backend.enums.EClientId;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import com.soilhumidity.backend.config.security.JwtUtil;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@AllArgsConstructor
public class WebSocketHandShakeInterceptor implements HandshakeInterceptor {

    private final JwtUtil jwtUtil;

    private final EClientId eClientId;

    @Override
    public boolean beforeHandshake(@NotNull ServerHttpRequest serverHttpRequest,
                                   @NotNull ServerHttpResponse serverHttpResponse,
                                   @NotNull WebSocketHandler webSocketHandler,
                                   @NotNull Map<String, Object> map) {
        ServletServerHttpRequest ssreq = (ServletServerHttpRequest) serverHttpRequest;
        ServletServerHttpResponse ssres = (ServletServerHttpResponse) serverHttpResponse;
        HttpServletRequest req = ssreq.getServletRequest();
        HttpServletResponse res = ssres.getServletResponse();

        try {


            var token = req.getParameter("access_token");
            var clientId = req.getParameter("client_id");

            if (clientId == null ||
                    !(clientId.equals(eClientId.getWebClientId())
                            || clientId.equals(eClientId.getMobileClientId()))) {
                throw new AuthenticationException("Client id invalid");
            }

            if (token == null || !jwtUtil.validateToken(token)) {
                throw new JwtException("Token Invalid");
            }

            map.put("username", jwtUtil.getUsername(token));

        } catch (Exception e) {

            res.setStatus(HttpServletResponse.SC_FORBIDDEN);

            return false;
        }

        return true;
    }

    @Override
    public void afterHandshake(@NotNull ServerHttpRequest serverHttpRequest,
                               @NotNull ServerHttpResponse serverHttpResponse,
                               @NotNull WebSocketHandler webSocketHandler, Exception e) {

    }
}
