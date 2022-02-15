import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

import java.security.MessageDigest
import javax.crypto.BadPaddingException

val type = "AES/ECB/PKCS5Padding"

fun main(){

    repeat(5) {
        descifrar(cifrar("qwerasd1", "llave"), "llave")
    }

    val holaCifrado = cifrar("Hola", "qwertyu")
    val holaDescifrado = descifrar(holaCifrado, "qwertyu")

    descifrar(cifrar("Hola", "qwertyuiopasdfg"), "qwertyuiopasdfg")
    descifrar(cifrar("Que tal", "jajajaja"), "jajajaja")

    do {
        println("******************************************")
        println("Introduce un mensaje: ")

        val mensaje = readLine()
        println("Introduce la llave de cifrado:")
        val llaveCifrado = readLine()

        if (!mensaje.isNullOrBlank() && !llaveCifrado.isNullOrBlank()) {
            val textCifrado = cifrar(mensaje, llaveCifrado)
            println("Introduce la llave de descifrado:")
            val llaveDescifrado = readLine()
            if (!llaveDescifrado.isNullOrBlank()) {
                try {
                    val mensajeDescifrado = descifrar(textCifrado, llaveDescifrado)

                    if (mensaje.equals(mensajeDescifrado, true)) {
                        println("Enhorabuena")
                    } else {
                        println("Fail")
                    }
                    println("\n")
                } catch (e : BadPaddingException) {
                    println("Fallo al descifrar ${e.cause}")
                }

            }
        }

    } while(true)

}

private fun cifrar(textoEnString : String, llaveEnString : String) : String {
    println("Voy a cifrar: $textoEnString")
    val cipher = Cipher.getInstance(type)
    cipher.init(Cipher.ENCRYPT_MODE, getKey(llaveEnString))
    val textCifrado = cipher.doFinal(textoEnString.toByteArray(Charsets.UTF_8))
    println("Texto cifrado $textCifrado")
    val textCifradoYEncodado = Base64.getUrlEncoder().encodeToString(textCifrado)
    println("Texto cifrado y encodado $textCifradoYEncodado")
    return textCifradoYEncodado
    //return textCifrado.toString()
}

@Throws(BadPaddingException::class)
private fun descifrar(textoCifradoYEncodado : String, llaveEnString : String) : String {
    println("Voy a descifrar $textoCifradoYEncodado")
    val cipher = Cipher.getInstance(type)
    cipher.init(Cipher.DECRYPT_MODE, getKey(llaveEnString))
    val textCifradoYDencodado = Base64.getUrlDecoder().decode(textoCifradoYEncodado)
    println("Texto cifrado $textCifradoYDencodado")
    val textDescifradoYDesencodado = String(cipher.doFinal(textCifradoYDencodado))
    println("Texto cifrado y desencodado $textDescifradoYDesencodado")
    return textDescifradoYDesencodado
}

private fun getKey(llaveEnString : String): SecretKeySpec {
    var llaveUtf8 = llaveEnString.toByteArray(Charsets.UTF_8)
    val sha = MessageDigest.getInstance("SHA-1")
    llaveUtf8 = sha.digest(llaveUtf8)
    llaveUtf8 = llaveUtf8.copyOf(16)
    return SecretKeySpec(llaveUtf8, "AES")
}


