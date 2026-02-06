package com.example.stramitapp.security

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object AesBase64Wrapper {

    private const val IV = "IV_VALUE_16_BYTE"
    private const val PASSWORD = "PASSWORD_VALUE"
    private const val SALT = "SALT_VALUE"
    private const val ITERATION_COUNT = 65536
    private const val KEY_LENGTH = 128

    private fun getCipher(encrypt: Boolean): Cipher {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        val spec = PBEKeySpec(
            PASSWORD.toCharArray(),
            SALT.toByteArray(Charsets.UTF_8),
            ITERATION_COUNT,
            KEY_LENGTH
        )

        val secretKey = SecretKeySpec(
            factory.generateSecret(spec).encoded,
            "AES"
        )

        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
        cipher.init(
            if (encrypt) Cipher.ENCRYPT_MODE else Cipher.DECRYPT_MODE,
            secretKey,
            IvParameterSpec(IV.toByteArray(Charsets.UTF_8))
        )

        return cipher
    }

    fun encryptAndEncode(raw: String): String {
        val cipher = getCipher(true)
        val encrypted = cipher.doFinal(raw.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(encrypted, Base64.NO_WRAP)
    }

    fun decodeAndDecrypt(encrypted: String): String {
        val cipher = getCipher(false)
        val decoded = Base64.decode(encrypted, Base64.NO_WRAP)
        return String(cipher.doFinal(decoded), Charsets.UTF_8)
    }
}