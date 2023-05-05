package com.example.appdriesenmauro



class ownException(message: String? = null, cause: Throwable? = null) : Exception(message, cause) {
    constructor(cause: Throwable) : this(null, cause) //i
}