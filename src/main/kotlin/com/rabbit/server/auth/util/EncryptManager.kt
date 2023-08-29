package com.rabbit.server.auth.util

import java.security.SecureRandom
import java.security.spec.KeySpec
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

class EncryptManager(
    private val algorithm: String,
    private val iterations: Int,
    private val keyLength: Int,
    private val secret: String
) {

    fun generateRandomSalt(): String {
        val random = SecureRandom()
        val salt = ByteArray(16)
        random.nextBytes(salt)
        return salt.toHexString()
    }

    fun ByteArray.toHexString(): String = joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }

    fun generateHash(salt: String, password: String): String {
        val combinedSalt = "$salt$secret".toByteArray()
        val factory: SecretKeyFactory = SecretKeyFactory.getInstance(algorithm)
        val spec: KeySpec = PBEKeySpec(password.toCharArray(), combinedSalt, iterations, keyLength)
        val key: SecretKey = factory.generateSecret(spec)
        val hash: ByteArray = key.encoded
        return hash.toHexString()
    }
}