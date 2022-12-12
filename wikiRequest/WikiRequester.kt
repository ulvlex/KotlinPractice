package wikiRequest

import com.google.gson.Gson
import java.awt.Desktop
import java.net.URI

class WikiRequester(
    private val request: String
) {
    private var searchResults: List<Search> = listOf()
    fun createListOfSearches() {
        searchResults = Gson().fromJson(request, JsonComponents::class.java).query.search
        println("List of possible requests:")
        for (currSearch in searchResults.indices) {
            println("${currSearch + 1}: ${searchResults[currSearch].title}")
        }
    }

    fun browse() {
        println("Input the number of request, you want to open in browser...")
        val userAnswer: Int
        try {
            userAnswer = readln().toInt()
            if (userAnswer > 0 && userAnswer <= searchResults.size) {
                Desktop.getDesktop()
                    .browse(URI("https://ru.wikipedia.org/w/index.php?curid=${searchResults[userAnswer - 1].pageId}"))
            } else {
                println("Incorrect input.")
                browse()
            }
        } catch (e: Exception) {
            println("Input error $e")
        }
    }
}