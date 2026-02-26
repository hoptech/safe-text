package br.com.hoptech.safetext.crypto

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class CryptoEngineTest {

    @Test
    fun `round trip with simple text`() {
        val passkey = "mysecretkey"
        val plaintext = "Hello, World!"
        val encrypted = CryptoEngine.encrypt(plaintext, passkey)
        val decrypted = CryptoEngine.decrypt(encrypted, passkey)
        assertEquals(plaintext, decrypted)
    }

    @Test
    fun `round trip with unicode text`() {
        val passkey = "chave-secreta"
        val plaintext = "Olá mundo! Emojis: \uD83D\uDD12\uD83D\uDCAC Kanji: \u6697\u53F7"
        val encrypted = CryptoEngine.encrypt(plaintext, passkey)
        val decrypted = CryptoEngine.decrypt(encrypted, passkey)
        assertEquals(plaintext, decrypted)
    }

    @Test
    fun `round trip with empty text`() {
        val passkey = "key"
        val plaintext = ""
        val encrypted = CryptoEngine.encrypt(plaintext, passkey)
        val decrypted = CryptoEngine.decrypt(encrypted, passkey)
        assertEquals(plaintext, decrypted)
    }

    @Test
    fun `round trip with long text`() {
        val passkey = "key123"
        val plaintext = "A".repeat(10_000)
        val encrypted = CryptoEngine.encrypt(plaintext, passkey)
        val decrypted = CryptoEngine.decrypt(encrypted, passkey)
        assertEquals(plaintext, decrypted)
    }

    @Test
    fun `wrong passkey throws exception`() {
        val encrypted = CryptoEngine.encrypt("secret message", "correctKey")
        assertThrows(Exception::class.java) {
            CryptoEngine.decrypt(encrypted, "wrongKey")
        }
    }

    @Test
    fun `each encryption produces different ciphertext`() {
        val passkey = "key"
        val plaintext = "same message"
        val encrypted1 = CryptoEngine.encrypt(plaintext, passkey)
        val encrypted2 = CryptoEngine.encrypt(plaintext, passkey)
        assertNotEquals(encrypted1, encrypted2)
    }

    @Test
    fun `invalid base64 throws exception`() {
        assertThrows(Exception::class.java) {
            CryptoEngine.decrypt("not-valid-base64!!!", "key")
        }
    }

    @Test
    fun `truncated ciphertext throws exception`() {
        assertThrows(Exception::class.java) {
            // Too short to contain salt + iv
            CryptoEngine.decrypt("AAAA", "key")
        }
    }

    @Test
    fun `passkey with special characters`() {
        val passkey = "p@ss k3y! with spaces & symbols #\$%"
        val plaintext = "test message"
        val encrypted = CryptoEngine.encrypt(plaintext, passkey)
        val decrypted = CryptoEngine.decrypt(encrypted, passkey)
        assertEquals(plaintext, decrypted)
    }
}
