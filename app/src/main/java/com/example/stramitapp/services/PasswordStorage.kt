package com.example.stramitapp.security

import android.util.Base64
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object PasswordStorage {

    fun encryptPassword(userPassword: String?): String {
        return try {

            val salt = byteArrayOf(
                172.toByte(), 137.toByte(), 25, 56, 156.toByte(),
                100, 136.toByte(), 211.toByte(), 84, 67, 96,
                10, 24, 111, 112, 137.toByte(), 3
            )

            val iterations = 1024
            val password = "2~Us4?KTH3#ge:U_"

            //  PBKDF2 (same as Rfc2898DeriveBytes)
            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            val spec: KeySpec = PBEKeySpec(password.toCharArray(), salt, iterations, 128)
            val keyBytes = factory.generateSecret(spec).encoded
            val secretKey = SecretKeySpec(keyBytes, "AES")

            //  AES Configuration
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")

            //  ENCRYPTION (

            //val plainBytes = userPassword.toByteArray(Charsets.UTF_8)
            //cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            //val cipherBytes = cipher.doFinal(plainBytes)
            //val cipherB64 = Base64.encodeToString(cipherBytes, Base64.DEFAULT)
            //val ivB64 = Base64.encodeToString(cipher.iv, Base64.DEFAULT)
            //println("IV: $ivB64")
            //println("Encrypted: $cipherB64")

            //  DECRYPTION

            // Remove first 4 chars (interop logic)
            val cipherB64 = userPassword?.substring(4)

            val ivB64 = "ZGNKNDc8RC5SWTwlWm05Iw=="

            val cipherBytes = Base64.decode(cipherB64, Base64.DEFAULT)
            val ivBytes = Base64.decode(ivB64, Base64.DEFAULT)
            val ivSpec = IvParameterSpec(ivBytes)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
            val decryptedBytes = cipher.doFinal(cipherBytes)
            String(decryptedBytes, Charsets.UTF_8)

        } catch (ex: Exception) {
            ex.printStackTrace()
            ex.message ?: "Error"
        }
    }
}