package com.example.chatterkotlinbackend.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "users")
class UserEntity(
    @Id
    @Column(name = "id")
    var id: String = UUID.randomUUID().toString(),

    @Column(name = "username")
    var username: String? = null,

    @Column(name = "email", unique = true)
    var email: String,

    @Column(name = "firstname")
    var firstname: String? = null,

    @Column(name = "lastname")
    var lastname: String? = null,

    @OneToMany(
        mappedBy = "author",
        cascade = [CascadeType.PERSIST, CascadeType.MERGE],
    )
    @JsonBackReference("user-posts")
    var posts: MutableList<PostEntity> = mutableListOf(),

    @OneToMany(mappedBy = "sender", cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JsonBackReference("user-messages")
    var messages: MutableList<MessageEntity>? = mutableListOf(),

    @OneToMany(mappedBy = "requester")
    @JsonBackReference("user-contacts-sent")
    val sentContacts: MutableSet<ContactEntity> = mutableSetOf(),

    @OneToMany(mappedBy = "recipient")
@JsonBackReference("user-contacts-received")
val receivedContacts: MutableSet<ContactEntity> = mutableSetOf(),

    @ManyToMany(mappedBy = "participants")
    @JsonBackReference("user-chats")
    var chats: MutableSet<ChatEntity> = mutableSetOf(),

    @OneToMany(
        mappedBy = "author",
        orphanRemoval = true
    )
    @JsonBackReference("user-comments")
    var comments: MutableSet<CommentEntity>? = mutableSetOf(),

    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "users_favorites",
        joinColumns = [JoinColumn(name = "user_entity_id")],
        inverseJoinColumns = [JoinColumn(name = "favorites_id")]
    )
    @JsonManagedReference("user-favorites")
    val favorites: MutableSet<PostEntity> = mutableSetOf(),

    @ManyToMany
    @JoinTable(
        name = "user_likes",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "post_id")]
    )
    @JsonManagedReference("user-likes")
    val likedPosts: MutableSet<PostEntity> = mutableSetOf(),

    @ManyToMany
    @JoinTable(
        name = "user_dislikes",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "post_id")]
    )
    @JsonManagedReference("user-dislikes")
    val dislikedPosts: MutableSet<PostEntity> = mutableSetOf()

) {
    override fun toString(): String {
        return ("UserEntity(id=$id, username=$username, firstname=$firstname, lastname=$lastname, email=$email, posts=$posts, messages=$messages, sentContacts=$sentContacts, receivedContacts=$receivedContacts, chats=$chats, comments=$comments, favorites=$favorites, likedPosts=$likedPosts, dislikedPosts=$dislikedPosts")
    }
}