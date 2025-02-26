package com.nexters.bandalart

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
