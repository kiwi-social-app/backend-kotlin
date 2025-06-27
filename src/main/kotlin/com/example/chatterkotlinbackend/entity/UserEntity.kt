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
    var posts: MutableList<PostEntity>? = null,

    @OneToMany(mappedBy = "sender", cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JsonBackReference("user-messages")
    var messages: MutableList<MessageEntity>? = mutableListOf(),

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    val contacts: MutableSet<ContactEntity> = mutableSetOf(),

    @ManyToMany(mappedBy = "participants")
    var chats: MutableSet<ChatEntity> = mutableSetOf(),

    @OneToMany(
        mappedBy = "author",
        orphanRemoval = true
    )
    @JsonBackReference("user-comments")
    var comments: MutableSet<CommentEntity>? = null,

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
    val likedPosts: MutableSet<PostEntity> = mutableSetOf(),

    @ManyToMany
    @JoinTable(
        name = "user_dislikes",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "post_id")]
    )
    val dislikedPosts: MutableSet<PostEntity> = mutableSetOf()

) {
    override fun toString(): String {
        return ("UserEntity(id=$id, username=$username, firstname=$firstname, lastname=$lastname, email=$email), posts=$posts, messages=$messages, contacts=$contacts, chats=$chats, comments=$comments, favorites=$favorites, likedPosts=$likedPosts, dislikedPosts=$dislikedPosts")
    }
}