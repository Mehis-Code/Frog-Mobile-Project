package com.example.sus_project

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.JsonSyntaxException
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Locale

data class GatheringInfo(
    @SerializedName("Gathering.Conversions.WGS84.LatMin(N)") val FrogLatitude: String,
    @SerializedName("Gathering.Conversions.WGS84.LonMax(E)") val FrogLongitude: String,
    @SerializedName("Taxon.ScientificName") val FrogName: String,
    @SerializedName("Gathering.Date.Begin") val Date: String
)

fun countFrogTypes(gatheringInfoList: List<GatheringInfo>): Map<String, Int> {
    // Define the three frog types
    val frogTypes = listOf("Rana arvalis", "Bufo bufo", "Rana temporaria")

    // Count the occurrences of each frog type
    return frogTypes.associateWith { frogType ->
        gatheringInfoList.count { it.FrogName == frogType }
    }
}

fun loadGatheringInfo(context: Context): List<GatheringInfo> {
    val file = File(context.filesDir, "FilteredFrog.json") // Same directory as FrogFilter
    Log.e("FrogRadar", "${file.absolutePath}")
    if (!file.exists()) {
        Log.e("FrogRadar", "FilteredFrog.json not found in internal storage!")
        return emptyList()
    }
    return try {
        Log.d("loadGatheringInfo", "Starting to load JSON data")
        val jsonString = file.readText()
        val gatheringInfoList = Gson().fromJson(jsonString, Array<GatheringInfo>::class.java).toList()
        Log.d("loadGatheringInfo", "Successfully loaded JSON data. Count: ${gatheringInfoList.size}")
        gatheringInfoList
    } catch (e: JsonSyntaxException) {
        Log.e("loadGatheringInfo", "Error parsing JSON: ${e.message}")
        emptyList() // Return an empty list in case of an error
    }
}


fun filterLocations(
    gatheringInfoList: List<GatheringInfo>,
    chosenLat: Double,
    chosenLon: Double
): List<GatheringInfo> {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Assuming date format "YYYY-MM-DD"

    return gatheringInfoList.filter { info ->
        val latMin = info.FrogLatitude.toDoubleOrNull() ?: return@filter false
        val lonMax = info.FrogLongitude.toDoubleOrNull() ?: return@filter false

        // Check if latMin and lonMax are within the 0.09 range of chosenLat and chosenLon
        latMin in (chosenLat - 0.09)..(chosenLat + 0.09) && lonMax in (chosenLon - 0.09)..(chosenLon + 0.09)
    }.sortedByDescending { info ->
        val frogDate = info.Date
        dateFormat.parse(frogDate) // Parse the Date and sort by it
    }
}
@Composable
fun FrogRadar(x0: Double, x1: Double) {
    val context = LocalContext.current
    var infoList by remember { mutableStateOf<List<GatheringInfo>>(emptyList()) }
    LaunchedEffect(Unit) {
        // Start loading the data
        copyFileToInternalStorage(context, "FilteredFrog.json", "FilteredFrog.json")
        infoList = loadGatheringInfo(context)
    }
        val chosenLat = x0
        val chosenLon = x1
        val filteredLocations = filterLocations(infoList, chosenLat, chosenLon)
        val frogTypesCount = countFrogTypes(filteredLocations)
        val ranaCount = frogTypesCount["Rana arvalis"] ?: 0
        val bufoCount = frogTypesCount["Bufo bufo"] ?: 0
        val ranaTempCount = frogTypesCount["Rana temporaria"] ?: 0

        val ranaDiv = ranaCount.toFloat() / filteredLocations.size.toFloat()
        val bufoDiv = bufoCount.toFloat() / filteredLocations.size.toFloat()
        val ranaTempDiv = ranaTempCount.toFloat() / filteredLocations.size.toFloat()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "${filteredLocations.size} Frog sightings within 10km",
                modifier = Modifier.padding(bottom = 2.dp)
            )
            Text(
                text = "Rana arvalis: ${ranaCount} (${"%.0f".format(ranaDiv * 100)}%)",
                modifier = Modifier.padding(bottom = 2.dp)
            )
            Text(
                text = "Bufo bufo: ${bufoCount} (${"%.0f".format(bufoDiv * 100)}%)",
                modifier = Modifier.padding(bottom = 2.dp)
            )
            Text(
                text = "Rana temporaria: ${ranaTempCount} (${"%.0f".format(ranaTempDiv * 100)}%)",
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(MaterialTheme.colorScheme.secondary)
                .zIndex(10f)
        )
        //Graph here

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
        ) {
            items(filteredLocations) { info ->
                Frog(info)
            }
        }

}

private fun copyFileToInternalStorage(context: Context, sourceFileName: String, destinationFileName: String) {
    try {
        val inputStream = context.assets.open(sourceFileName)
        val outputStream = FileOutputStream(File(context.filesDir, destinationFileName))
        inputStream.copyTo(outputStream)
        inputStream.close()
        outputStream.close()
        Log.d("CopyFile", "$sourceFileName copied to internal storage")
    } catch (e: IOException) {
        Log.e("CopyFile", "Error copying $sourceFileName: ${e.message}")
    }
}

@Composable
fun Frog(info: GatheringInfo) {
    var frogDrawable = R.drawable.bufobufo
    if (info.FrogName == "Rana arvalis") {
        frogDrawable = R.drawable.ranaarvalis
    }
    if (info.FrogName == "Rana temporaria") {
        frogDrawable = R.drawable.ranatemporaria
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
        ,
        verticalAlignment = Alignment.CenterVertically // Align items in the center vertically
    ) {
        Image(
            painter = painterResource(id = frogDrawable),
            contentDescription = "Frog Image",
            modifier = Modifier
                .height(30.dp)
                .width(70.dp)
                .padding(end = 8.dp) // Add spacing between image and text
        )
        Column {
            Text(
                text = "Frog Name: ${info.FrogName}",
            )
            Text(
                text = "Date: ${info.Date}",
            )
        }
    }
}

