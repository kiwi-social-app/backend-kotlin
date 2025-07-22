package com.example.chatterkotlinbackend.entity

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import org.jetbrains.annotations.NotNull
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "contacts")
data class ContactEntity(
    @Id
    @Column(name = "id")
    var id: String = UUID.randomUUID().toString(),

    @ManyToOne
    @JoinColumn(name = "requester_id")
    @JsonManagedReference("user-contacts-sent")
    val requester: UserEntity,

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    @JsonManagedReference("user-contacts-received")
    val recipient: UserEntity,

    @Enumerated(EnumType.STRING)
    var status: ContactStatus = ContactStatus.PENDING,

    val createdAt: OffsetDateTime = OffsetDateTime.now()

) {
    enum class ContactStatus {
        PENDING, ACCEPTED, BLOCKED
    }
}