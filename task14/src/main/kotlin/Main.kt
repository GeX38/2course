import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import com.github.doyaaaaken.kotlin.csv
import com.google.gson.Gson

data class fields(
    val name: String,
    val category: String,
    val rating: Double,
    val reviews: Int,
    val size: String,
    val installs: Int,
    val type: String,
    val price: Boolean,
    val contentRating: String,
    val genres: List<String>,
    val lastUpdated: String,
    val currentVersion: String,
    val andVer: Int,
)

fun formatDate(date: String): String {
    val inputFormat = SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH)
    val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
    val pdDate = inputFormat.p(date)
    return outputFormat.format(pdDate)
}

fun pInstalls(installs: String): Int {
    return installs.replace("[^\\d]".toRegex(), "").toInt()
}

fun pPrice(price: String): Boolean {
    return price != "0"
}

fun pGenres(genres: String): List<String> {
    return genres.split(";")
}

fun pAndroidVersion(androidVer: String): Int {
    val versionRegex = "\\d+".toRegex()
    val versionDigits = versionRegex.findAll(androidVer).map { it.value.toInt() }
    return if (versionDigits.count() >= 2) {
        versionDigits.first() * 10 + versionDigits.last()
    } else {
        versionDigits.first()
    }
}

fun main(args: Array<String>) {
    val csvFile = File("src/main/resources/googleplaystore.csv")
    val appsList = mutableListOf<fields>()

    csvReader().readAll(csvFile) {
        readAllAsSequence().forEach { row ->
            val fields = fields(
                name = row[0],
                category = row[1],
                rating = row[2].toDouble(),
                reviews = row[3].toInt(),
                size = row[4],
                installs = pInstalls(row[5]),
                type = row[6],
                price = pPrice(row[7]),
                contentRating = row[8],
                genres = pGenres(row[9]),
                lastUpdated = formatDate(row[10]),
                currentVersion = row[11],
                andVer = pAndroidVersion(row[12])
            )
            appsList.add(fields)
        }
    }

    val gson = Gson()
    val json = gson.toJson(appsList)

    File("result.json").writeText(json)
}
