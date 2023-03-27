import java.net.URL

object Util {

    val SCORE_TABLE: Map<String, Int> = mapOf(
        "a" to 1, "b" to 3, "c" to 3, "d" to 2, "e" to 1, "f" to 4, "g" to 2,
        "h" to 4, "i" to 1, "j" to 8, "k" to 5, "l" to 1, "m" to 3, "n" to 1,
        "o" to 1, "p" to 3, "q" to 10, "r" to 1, "s" to 1, "t" to 1, "u" to 1,
        "v" to 4, "w" to 4, "x" to 8, "y" to 4, "z" to 10
    )

    fun getTextFromUrl(urlString: String): String? {
        return try {
            URL(urlString).readText().toLowerCase()
        } catch (e: Exception) {
            null
        }
    }

    /*fun countWords(text: String): Map<String, Int> {
        return text.split(Regex("\\s+")).groupingBy { it }.eachCount()
    }*/
    fun countWords(text: String): HashMap<String, Int> {
        val wordCounts = HashMap<String, Int>()
        text.split(Regex("\\s+")).forEach { word ->
            wordCounts[word] = wordCounts.getOrDefault(word, 0) + 1
        }
        return wordCounts
    }

    /*fun getMostFrequentWord(wordCounts: Map<String, Int>): String {
        return wordCounts.maxByOrNull { it.value }?.key ?: ""
    }*/
    fun getMostFrequentWord(wordCounts: HashMap<String, Int>): String {
        var maxWord = ""
        var maxCount = 0
        for ((word, count) in wordCounts) {
            if (count > maxCount) {
                maxWord = word
                maxCount = count
            }
        }
        return maxWord
    }

    /*fun getMostFrequentSevenCharacterWord(wordCounts: Map<String, Int>): String {
        return wordCounts.filter { it.key.length == 7 }.maxByOrNull { it.value }?.key ?: ""
    }*/
    fun getMostFrequentSevenCharacterWord(wordCounts: HashMap<String, Int>): String {
        var maxWord = ""
        var maxCount = 0
        for ((word, count) in wordCounts) {
            if (word.length == 7 && count > maxCount) {
                maxWord = word
                maxCount = count
            }
        }
        return maxWord
    }

    /*fun getHighestScoringWords(wordCounts: Map<String, Int>): List<Pair<String, Int>> {
        val scores = wordCounts.mapValues { word ->
            word.key.map { SCORE_TABLE[it.toLowerCase().toString()] ?: 0 }.sum() * word.value
        }
        val maxScore = scores.maxOfOrNull { it.value } ?: 0
        return scores.filter { it.value == maxScore }.toList()
    }*/
    fun getHighestScoringWords(wordCounts: HashMap<String, Int>): List<Pair<String, Int>> {
        val scores = HashMap<String, Int>()
        for ((word, count) in wordCounts) {
            val score = word.map { SCORE_TABLE[it.toLowerCase().toString()] ?: 0 }.sum() * count
            scores[word] = score
        }
        val maxScore = scores.values.maxOrNull() ?: 0
        return scores.filter { it.value == maxScore }.toList()
    }


}