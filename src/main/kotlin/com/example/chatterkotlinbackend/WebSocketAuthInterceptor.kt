package com.example.chatterkotlinbackend

import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.stereotype.Component

@Component
class WebSocketAuthInterceptor(
    private val jwtDecoder: JwtDecoder
) : ChannelInterceptor {

    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
        val accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)
            ?: return message

        if (accessor.command == StompCommand.CONNECT) {
            val token = accessor.getFirstNativeHeader("Authorization")
                ?.removePrefix("Bearer ")
                ?: throw IllegalArgumentException("Missing Authorization header in STOMP CONNECT")

            val jwt = jwtDecoder.decode(token)
            val authentication = UsernamePasswordAuthenticationToken(jwt.subject, null, emptyList())
            accessor.user = authentication
        }

        return message
    }
}
