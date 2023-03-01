package com.lovesme.homegram.util

import java.util.UUID

object UUID {
    fun generate() = UUID.randomUUID().toString()
}