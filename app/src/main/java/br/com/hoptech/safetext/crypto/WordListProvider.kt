package br.com.hoptech.safetext.crypto

import android.content.Context
import br.com.hoptech.safetext.R
import java.util.Locale
import kotlin.random.Random

class WordListProvider(context: Context) {

    private val words: List<String>

    init {
        val resId = when (Locale.getDefault().language) {
            "pt" -> R.raw.words_pt
            else -> R.raw.words_en
        }
        words = context.resources.openRawResource(resId)
            .bufferedReader()
            .readLines()
            .filter { it.isNotBlank() }
    }

    fun generate(wordCount: Int = 3): String {
        require(words.isNotEmpty()) { "Word list is empty" }
        return (1..wordCount)
            .map { words[Random.nextInt(words.size)] }
            .joinToString("-")
    }
}
