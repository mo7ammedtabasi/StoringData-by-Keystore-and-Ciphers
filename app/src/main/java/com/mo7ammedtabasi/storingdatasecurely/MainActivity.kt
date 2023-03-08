package com.mo7ammedtabasi.storingdatasecurely

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mo7ammedtabasi.storingdatasecurely.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStreamWriter
import java.util.Base64

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cryptoData = CryptoData()

        binding.btnEncryption.setOnClickListener {
            val byte = binding.edText.text.toString().trim().toByteArray()
            val cipherText = Base64.getEncoder().encodeToString(cryptoData.encrypt(bytes = byte))
            createAndSaveTextFileInternalStorage("secreteFilePrivet.txt", cipherText)
        }
        binding.btnDecryption.setOnClickListener {
            val cipherText = readTextFileInternalStorage("secreteFilePrivet.txt")
            val originalText = cryptoData.decrypt(Base64.getDecoder().decode(cipherText))?.decodeToString()
            binding.showEncryption.text = originalText
        }

    }

    private fun readTextFileExternalStorage(fileName: String) : String {
        try {
            val inputStream: InputStream = File(getExternalFilesDir(null), fileName).inputStream()
            return inputStream.bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            Log.e(TAG, "readTextFileExternalStorage: $e")
        }
        return ""
    }

    private fun readTextFileInternalStorage(fileName: String) : String{
        try {
            val inputStream: InputStream = File(filesDir, fileName).inputStream()
            return inputStream.bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            Log.e(TAG, "readTextFileInternalStorage: $e")
        }
        return ""
    }

    private fun createAndSaveTextFileExternalStorage(fileName: String, content: String) {
        val file = File(getExternalFilesDir(null), fileName)
        try {
            val outputStreamWriter = OutputStreamWriter(FileOutputStream(file))
            outputStreamWriter.write(content)
            outputStreamWriter.close()
        } catch (e: IOException) {
            Log.e(TAG, "createAndSaveTextFileExternalStorage: $e")
        }
    }

    private fun createAndSaveTextFileInternalStorage(fileName: String, content: String) {
        val file = File(filesDir, fileName)
        try {
            val outputStreamWriter = OutputStreamWriter(FileOutputStream(file))
            outputStreamWriter.write(content)
            outputStreamWriter.close()
        } catch (e: IOException) {
            Log.e(TAG, "createAndSaveTextFileInternalStorage: $e")
        }
    }
}