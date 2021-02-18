import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

import java.security.MessageDigest
import javax.crypto.BadPaddingException


fun main(){
    descifrar(cifrar("Hola", "qwertyuiopasdfg"), "qwertyuiopasdfg")
    descifrar(cifrar("Que tal", "jajajaja"), "jajajaja")

    do {
        println("******************************************")
        println("Introduce un mensaje: ")

        val mensaje = readLine()
        println("Introduce la llave de cifrado:")
        val llaveCifrado = readLine()

        if (mensaje != null && llaveCifrado != null) {
            val textCifrado = cifrar(mensaje, llaveCifrado)
            println("Introduce la llave de descifrado:")
            val llaveDescifrado = readLine()
            if (llaveDescifrado != null) {
                try {
                    val mensajeDescifrado = descifrar(textCifrado, llaveDescifrado)

                    if (mensaje.equals(mensajeDescifrado, true)) {
                        println("Enhorabuena")
                    } else {
                        println("Fail")
                    }
                    println("\n")
                } catch (e : BadPaddingException) {
                    e.printStackTrace()
                    println("Fallo al descifrar")
                }

            }
        }

    } while(true)

}

private fun cifrar(textoEnString : String, llaveEnString : String) : String {
    println("Voy a cifrar $textoEnString")
    val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    cipher.init(Cipher.ENCRYPT_MODE, getKey(llaveEnString))
    val textCifrado = Base64.getEncoder().encodeToString(cipher.doFinal(textoEnString.toByteArray(Charsets.UTF_8)))
    println("He obtenido $textCifrado")
    return textCifrado
}

@Throws(BadPaddingException::class)
private fun descifrar(textoCifrrado : String, llaveEnString : String) : String {
    println("Voy a descifrar $textoCifrrado")
    val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    cipher.init(Cipher.DECRYPT_MODE, getKey(llaveEnString));
    val textDescifrado = String(cipher.doFinal(Base64.getDecoder().decode(textoCifrrado)))
    println("He obtenido $textDescifrado")
    return textDescifrado
}


private fun getKey(llaveEnString : String): SecretKeySpec {
    var llaveUtf8 = llaveEnString.toByteArray(Charsets.UTF_8)
    val sha = MessageDigest.getInstance("SHA-1")
    llaveUtf8 = sha.digest(llaveUtf8)
    llaveUtf8 = llaveUtf8.copyOf(16)
    return SecretKeySpec(llaveUtf8, "AES")
}