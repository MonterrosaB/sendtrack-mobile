package Modelo

class SHA256 {

    fun hashSHA256(contraseniaEscrita: String): String {
        val bytes = java.security.MessageDigest.getInstance("SHA-256").digest(contraseniaEscrita.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

}