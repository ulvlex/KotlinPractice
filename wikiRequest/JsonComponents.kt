package wikiRequest

import com.google.gson.annotations.SerializedName

data class JsonComponents(
    @SerializedName("batchcomplete")
    val batchComplete: String,
    @SerializedName("continue")
    val cContinue: Continue,
    val query: Query
)

data class Continue(
    @SerializedName("sroffset")
    val srOffset: Int,
    @SerializedName("continue")
    val cContinue: String
)

data class Query(
    @SerializedName("searchinfo")
    val searchInfo: SearchInfo,
    val search: List<Search>
)

data class SearchInfo(
    @SerializedName("totalhits")
    val totalHits: Int
)

data class Search(
    val ns: Int,
    val title: String,
    @SerializedName("pageid")
    val pageId: Int,
    val size: Int,
    @SerializedName("wordcount")
    val wordCount: Int,
    val snippet: String,
    val timestamp: String
)
