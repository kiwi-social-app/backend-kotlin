package com.example.chatterkotlinbackend.dto

class UserDTO {
    var id: String = "exampleId";
    var username: String? = null;
    var email: String = "example@email.com";
    var firstname: String? = null;
    var lastname: String? = null;
    val sentContacts: List<ContactDTO>? = null;
    val receivedContacts: List<ContactDTO>? = null;
    val chats: List<ChatDTO>? = null;
    var favorites: List<String> = ArrayList();
    var likedPosts: List<String> = ArrayList();
    var dislikedPosts: List<String> = ArrayList();
}