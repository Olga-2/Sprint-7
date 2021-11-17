package org.spring.mvc.springsecurity.models

data class RequestLog(val login: String, val request: String){
}

class Message(text: String){
    val message = text
}