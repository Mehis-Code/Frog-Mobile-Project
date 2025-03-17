package com.example.sus_project

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.JsonSyntaxException
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader

data class GatheringInfo(
    @SerializedName("Gathering.Conversions.WGS84.LatMin(N)") val FrogLatitude: String,
    @SerializedName("Gathering.Conversions.WGS84.LonMax(E)") val FrogLongitude: String,
    @SerializedName("Taxon.ScientificName") val FrogName: String,
    @SerializedName("Gathering.Date.Begin") val Date: String
)

fun loadGatheringInfo(context: android.content.Context): List<GatheringInfo> {
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
    return gatheringInfoList.filter { info ->
        val latMin = info.FrogLatitude.toDoubleOrNull() ?: return@filter false
        val lonMax = info.FrogLongitude.toDoubleOrNull() ?: return@filter false

        // Check if latMin and lonMax are within the 0.09 range of chosenLat and chosenLon
        latMin in (chosenLat - 0.09)..(chosenLat + 0.09) && lonMax in (chosenLon - 0.09)..(chosenLon + 0.09)
    }
}

@Composable
fun FrogRadar(x0: Double, x1: Double) {
    val context = LocalContext.current
    var infoList by remember { mutableStateOf<List<GatheringInfo>>(emptyList()) }
    LaunchedEffect(Unit) {
        // Copy from assets to internal storage
        copyFileToInternalStorage(context, "FilteredFrog.json", "FilteredFrog.json")
        infoList = loadGatheringInfo(context)
    }
    val chosenLat = x0
    val chosenLon = x1
    val filteredLocations = filterLocations(infoList, chosenLat, chosenLon)

    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        // Show total count at the top
        item {
            Text(
                text = "Total Frogs Found: ${filteredLocations.size}",
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Show each filtered frog info
        items(filteredLocations) { info ->
            Row {
                Text(
                    text = "Frog Name: ${info.FrogName}, Date: ${info.Date}",
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                )
                Image(
                    painter = painterResource(id = R.drawable.frog),
                    contentDescription = "My Image",
                    modifier = Modifier.fillMaxWidth()
                )
            }
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