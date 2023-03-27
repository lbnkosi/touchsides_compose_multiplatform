import Util.SCORE_TABLE
import Util.countWords
import Util.getHighestScoringWords
import Util.getMostFrequentSevenCharacterWord
import Util.getMostFrequentWord
import Util.getTextFromUrl
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import kotlin.math.roundToInt

const val TITLE = "Text Analyzer"
const val DESCRIPTION = "Enter a URL to analyze the txt"

@Composable
@Preview
fun App() {
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var wordCounts by remember { mutableStateOf(hashMapOf<String, Int>()) }
    var highestScoringWords by remember { mutableStateOf(listOf<Pair<String, Int>>()) }
    var urlText by remember { mutableStateOf("https://www.gutenberg.org/files/2600/2600-0.txt") }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {

        Text(TITLE, style = MaterialTheme.typography.h3, modifier = Modifier.padding(vertical = 16.dp))

        Text(DESCRIPTION, style = MaterialTheme.typography.body1, modifier = Modifier.padding(vertical = 16.dp))

        TextField(
            value = urlText,
            onValueChange = { urlText = it },
            label = { Text("Enter URL") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Uri),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )

        Button(
            onClick = {
                if (urlText.isNotEmpty()) {
                    isLoading = true
                    wordCounts = hashMapOf()
                    highestScoringWords = listOf()

                    coroutineScope.launch {
                        val newText = withContext(Dispatchers.IO) { getTextFromUrl(urlText) } ?: ""
                        wordCounts = countWords(newText)
                        highestScoringWords = getHighestScoringWords(wordCounts)
                        isLoading = false
                    }
                }
            },
            enabled = urlText.isNotEmpty(),
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Analyze Text")
        }


        if (isLoading) {
            CircularProgressIndicator()
        } else if (wordCounts.isNotEmpty()) {
            val mostFrequentWord = getMostFrequentWord(wordCounts)
            val mostFrequentSevenCharWord = getMostFrequentSevenCharacterWord(wordCounts)
            val averageScore = wordCounts.map { (word, count) -> count * SCORE_TABLE.getOrDefault(word, 0) }.sum().toDouble() / wordCounts.values.sum()

            Text("Most frequent word: $mostFrequentWord occurred ${wordCounts[mostFrequentWord]} times", modifier = Modifier.padding(16.dp))
            Text("Most frequent 7-character word: $mostFrequentSevenCharWord occurred ${wordCounts[mostFrequentSevenCharWord]} times", modifier = Modifier.padding(16.dp))
            Text("Highest scoring word: ${highestScoringWords.first().first} with a score of ${highestScoringWords.first().second}", modifier = Modifier.padding(16.dp))
            HighestScoringWordsList(highestScoringWords)
            //Text("Highest scoring word(s) (according to the score table): ${highestScoringWords.joinToString()} with a score of ${highestScoringWords.map { SCORE_TABLE.getOrDefault(it.first, 0) }.maxOrNull() ?: ""}", modifier = Modifier.padding(16.dp))
        }
    }
}

@Composable
fun HighestScoringWordsList(words: List<Pair<String, Int>>) {
    Column {
        Text("Highest Scoring Words:")
        for (word in words) {
            Text("${word.first}: ${word.second}")
        }
    }
}



fun main() = application {
    Window(onCloseRequest = ::exitApplication) { App() }
}
