package com.example.chatterkotlinbackend.service

import com.example.chatterkotlinbackend.dto.MessageDTO
import com.example.chatterkotlinbackend.entity.ChatEntity
import com.example.chatterkotlinbackend.mapper.MessageMapper
import com.example.chatterkotlinbackend.repository.ChatRepository
import com.example.chatterkotlinbackend.repository.MessageRepository
import com.example.chatterkotlinbackend.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChatService(
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository,
    private val mapper: MessageMapper,
    private val chatRepository: ChatRepository,
) {

    @Transactional
    fun saveMessage(dto: MessageDTO): MessageDTO {
        if (dto.sender.id.isBlank()) {
            throw IllegalArgumentException("Sender ID cannot be null or empty")
        }
        if (dto.chatId.isBlank()) {
            throw IllegalArgumentException("Chat ID cannot be null or empty")
        }

        val user = userRepository.findById(dto.sender.id)
            .orElseThrow { IllegalArgumentException("User not found with ID: ${dto.sender.id}") }

        chatRepository.findByIdAndParticipantId(dto.chatId, dto.sender.id)
            .orElseThrow { AccessDeniedException("User is not a participant in this chat") }

        dto.id = UUID.randomUUID().toString();
        val messageEntity = mapper.toEntity(dto, user)

        val savedMessage = messageRepository.save(messageEntity)
        return mapper.toDto(savedMessage)
    }

    fun getAllMessages(): List<MessageDTO> {
        return messageRepository.findAll().map { mapper.toDto(it) }
    }

    @Transactional
    fun getMessagesByChatId(chatId: String): List<MessageDTO> {
        val messages = messageRepository.findByChatIdWithSender(chatId) ?: emptyList()
        return mapper.toDto(messages)
    }

    @org.springframework.transaction.annotation.Transactional
    fun startChat(userId: String, participantIds: List<String>): ChatEntity {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        val users = userRepository.findAllById(participantIds).toMutableSet();
        users.add(user);

        val chat = ChatEntity()

        users.forEach {
            chat.participants.add(it)
            it.chats.add(chat)
        }

        return chatRepository.save(chat)
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    fun getChat(participantId: String, chatId: String): ChatEntity {
        return chatRepository.findByIdAndParticipantId(chatId, participantId)
            .orElseThrow { AccessDeniedException("User is not part of the chat") }
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    fun getChats(userId: String): List<ChatEntity> {
        return chatRepository.findAllByUserId(userId)
    }
}