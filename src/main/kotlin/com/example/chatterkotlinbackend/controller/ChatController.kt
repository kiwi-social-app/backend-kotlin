package com.example.chatterkotlinbackend.controller

import com.example.chatterkotlinbackend.dto.ChatDTO
import com.example.chatterkotlinbackend.dto.MessageDTO
import com.example.chatterkotlinbackend.dto.StartChatRequestDTO
import com.example.chatterkotlinbackend.mapper.ChatMapper
import com.example.chatterkotlinbackend.service.ChatService
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/chat")
class ChatController(
    private val objectMapper: ObjectMapper,
    private val simpMessagingTemplate: SimpMessagingTemplate,
    private val mapper: ChatMapper,
    private val service: ChatService
) {
    private val logger = LoggerFactory.getLogger(ChatController::class.java)

    @MessageMapping("/sendMessage")
    fun sendMessage(@RequestBody messageDTO: MessageDTO): MessageDTO {
        val savedMessage = service.saveMessage(messageDTO)

        val destination = "/topic/chats/${savedMessage.chatId}"
        simpMessagingTemplate.convertAndSend(destination, savedMessage)

        val jsonMessage = objectMapper.writeValueAsString(savedMessage)
        logger.info("Sending JSON message: $jsonMessage")

        return savedMessage
    }

    @GetMapping("/messages")
    fun getAllMessages(): ResponseEntity<List<MessageDTO>> {
        val messages = service.getAllMessages()
        return ResponseEntity.ok(messages)
    }

    @GetMapping("/messages/{chatId}")
    fun getMessagesByChatId(@PathVariable chatId: String): ResponseEntity<List<MessageDTO>> {
        val messages = service.getMessagesByChatId(chatId)
        return ResponseEntity.ok(messages)
    }

    @GetMapping("/{chatId}")
    fun getChat(@PathVariable chatId: String, principal: Principal): ChatDTO {
        return mapper.toDto(service.getChat(principal.name, chatId))
    }

    @GetMapping("/user/me")
    fun getUserChats(principal: Principal): List<ChatDTO> {
        return mapper.toDto(service.getChats(principal.name))
    }

    @PostMapping("/start")
    fun startChat(
        @RequestBody request: StartChatRequestDTO,
        principal: Principal
    ): ResponseEntity<ChatDTO> {
        val chat = service.startChat(principal.name, request.participantIds)

        return ResponseEntity.ok(mapper.toDto(chat))
    }
}