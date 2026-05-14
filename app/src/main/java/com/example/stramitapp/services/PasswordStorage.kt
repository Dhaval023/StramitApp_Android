package com.example.stramitapp.services

import android.util.Base64
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object PasswordStorage {

    private val salt = byteArrayOf(
        172.toByte(), 137.toByte(), 25, 56, 156.toByte(),
        100, 136.toByte(), 211.toByte(), 84, 67, 96,
        10, 24, 111, 112, 137.toByte(), 3
    )
    private const val iterations = 1024
    private const val passwordKey = "2~Us4?KTH3#ge:U_"
    private const val FIXED_IV = "ZGNKNDc8RC5SWTwlWm05Iw=="

    private fun getSecretKey(): SecretKeySpec {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        val spec: KeySpec = PBEKeySpec(passwordKey.toCharArray(), salt, iterations, 128)
        val keyBytes = factory.generateSecret(spec).encoded
        return SecretKeySpec(keyBytes, "AES")
    }

    fun encrypt(plainText: String?): String {
        if (plainText == null) return ""
        return try {
            val secretKey = getSecretKey()
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            
            val ivBytes = Base64.decode(FIXED_IV, Base64.DEFAULT)
            val ivSpec = IvParameterSpec(ivBytes)
            
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
            val cipherBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
            
            "STRM" + Base64.encodeToString(cipherBytes, Base64.NO_WRAP)
        } catch (ex: Exception) {
            ex.printStackTrace()
            ""
        }
    }

    fun decrypt(encryptedText: String?): String {
        if (encryptedText == null) return ""
        return try {
            if (!encryptedText.startsWith("STRM")) return encryptedText
            
            val secretKey = getSecretKey()
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")

            val cipherB64 = encryptedText.substring(4)
            val ivBytes = Base64.decode(FIXED_IV, Base64.DEFAULT)
            val ivSpec = IvParameterSpec(ivBytes)
            
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
            val cipherBytes = Base64.decode(cipherB64, Base64.DEFAULT)
            val decryptedBytes = cipher.doFinal(cipherBytes)
            String(decryptedBytes, Charsets.UTF_8)
        } catch (ex: Exception) {
            ex.printStackTrace()
            encryptedText
        }
    }
    
    fun encryptPassword(userPassword: String?): String {
        return decrypt(userPassword)
    }
}
