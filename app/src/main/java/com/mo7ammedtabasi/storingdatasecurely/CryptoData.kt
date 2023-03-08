package com.mo7ammedtabasi.storingdatasecurely

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.widget.Space
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class CryptoData{

    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    private fun getKey() : SecretKey {
        val existKey = keyStore.getEntry("secret",null) as? KeyStore.SecretKeyEntry
        return existKey?.secretKey ?: createKey()
    }

    private fun createKey(): SecretKey{
        return KeyGenerator.getInstance(ALGORITHM).apply {
            init(
                KeyGenParameterSpec.Builder(
                    "secret",
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                ).setBlockModes(BLOCK_MODE)
                    .setEncryptionPaddings(PADDING)
                    .build()
            )
        }.generateKey()
    }

    private val cipher = Cipher.getInstance(TRANSFORMATION)
//    val keyStore = SecretKeySpec("123123".toByteArray(Charsets.UTF_8),"AES")

    fun encrypt(bytes: ByteArray) : ByteArray{
        cipher.init(Cipher.ENCRYPT_MODE,getKey())
        val iv = cipher.iv
        val encrypted = cipher.doFinal(bytes)
        return iv + encrypted
    }

    fun decrypt(bytes: ByteArray) : ByteArray?{
        val iv = bytes.copyOfRange(0,cipher.blockSize)
        val data = bytes.copyOfRange(cipher.blockSize,bytes.size)
        cipher.init(Cipher.DECRYPT_MODE,getKey(),IvParameterSpec(iv))
        return cipher.doFinal(data)
    }

    companion object {
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    }
}