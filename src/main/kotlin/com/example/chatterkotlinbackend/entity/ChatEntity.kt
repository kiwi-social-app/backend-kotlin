package com.example.chatterkotlinbackend.entity

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "chats")
data class ChatEntity(
    @Id
    @Column(name = "id") var id: String = UUID.randomUUID().toString(),

    @OneToMany(
        fetch = FetchType.EAGER,
        cascade = [(CascadeType.ALL)],
        orphanRemoval = true
    )
    @JoinColumn(name = "chat_id")
    var messages: MutableSet<MessageEntity> = mutableSetOf(),

    @ManyToMany
    @JoinTable(
        name = "chat_participants",
        joinColumns = [JoinColumn(name = "chat_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    @JsonManagedReference("user-chats")
    var participants: MutableSet<UserEntity> = mutableSetOf()
) {
}