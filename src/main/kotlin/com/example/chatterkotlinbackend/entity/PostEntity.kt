package com.example.chatterkotlinbackend.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import jakarta.persistence.*
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Entity
@Table(name = "posts")
data class PostEntity(
    @Id
    @NotNull
    @Column(name = "id")
    var id: String = UUID.randomUUID().toString(),

    @Version
    var version: Long? = null,

    @NotNull
    @Column(name = "body")
    var body: String,

    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    var updatedAt: LocalDateTime? = null,

    @Column(name = "published") var published: Boolean = false,

    @NotNull
    @ManyToOne(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.PERSIST, CascadeType.MERGE],
    ) @JoinColumn(name = "authorId", referencedColumnName = "id")
    @JsonManagedReference("user-posts")
    var author: UserEntity,

    @OneToMany(
        mappedBy = "post",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.PERSIST, CascadeType.MERGE],
    )
    @JsonBackReference("post-comments")
    var comments: MutableList<CommentEntity> = mutableListOf(),

    @ManyToMany(mappedBy = "favorites")
    @JsonBackReference("user-favorites")
    var favoritedBy: MutableList<UserEntity> = mutableListOf(),

    @ManyToMany(mappedBy = "likedPosts")
    @JsonBackReference("user-likes")
    val likedByUsers: MutableList<UserEntity> = mutableListOf(),

    @ManyToMany(mappedBy = "dislikedPosts")
    @JsonBackReference("user-dislikes")
    val dislikedByUsers: MutableList<UserEntity> = mutableListOf()

) {

    class LocalDateTimeDeserializer : JsonDeserializer<LocalDateTime>() {
        override fun deserialize(parser: JsonParser, context: DeserializationContext): LocalDateTime {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
            return LocalDateTime.parse(parser.valueAsString, formatter)
        }
    }


    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonDeserialize(using = LocalDateTimeDeserializer::class)
    var createdAt: LocalDateTime = LocalDateTime.now()

    @PrePersist
    fun prePersist() {
        createdAt = LocalDateTime.now()
    }
}
