package com.example.sus_project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.sus_project.utils.*
import com.patrykandpatryk.vico.core.entry.entryModelOf

/**
 * GatheringInfo data class used here represents information about a frog sighting
 * @property FrogLatitude The minimum latitude (WGS84) of the frog sighting as a string.
 * @property FrogLongitude The maximum longitude (WGS84) of the frog sighting as a string.
 * @property FrogName The scientific name of the frog.
 * @property Date The date when the gathering began in the format "yyyy-MM-dd".
 */

//This is the parent component for the chart and individual sightings
@Composable
fun FrogRadar(chosenLat: Double, chosenLon: Double) {
    val context = LocalContext.current
    var infoList by remember { mutableStateOf<List<GatheringInfo>>(emptyList()) }

    //Load data on render
    LaunchedEffect(Unit) {
        copyFileToInternalStorage(context, "FilteredFrog.json", "FilteredFrog.json")
        infoList = loadGatheringInfo(context)
    }
    val filteredLocations = filterSightings(infoList, chosenLat, chosenLon)

    //Count all different frogs from data
    val frogTypesCount = countFrogTypes(filteredLocations)
    val ranaCount = frogTypesCount["Rana arvalis"] ?: 0
    val bufoCount = frogTypesCount["Bufo bufo"] ?: 0
    val ranaTempCount = frogTypesCount["Rana temporaria"] ?: 0
    //Calculate percentages of frogs in data
    val ranaDiv = ranaCount.toFloat() / filteredLocations.size.toFloat()
    val bufoDiv = bufoCount.toFloat() / filteredLocations.size.toFloat()
    val ranaTempDiv = ranaTempCount.toFloat() / filteredLocations.size.toFloat()

    //The container holding full count of all frog sightings
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "${filteredLocations.size} Frog sightings within 10km",
        )
        Text(
            text = "Rana Arvalis: ${ranaCount} (${"%.0f".format(ranaDiv * 100)}%)",
            fontSize = 11.sp
        )
        Text(
            text = "Bufo Bufo: ${bufoCount} (${"%.0f".format(bufoDiv * 100)}%)",
            fontSize = 11.sp
        )
        Text(
            text = "Rana Temporaria: ${ranaTempCount} (${"%.0f".format(ranaTempDiv * 100)}%)",
            fontSize = 11.sp
        )
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(MaterialTheme.colorScheme.secondary)
            .zIndex(10f)
    )
    //Scrollable element to display Chart/Individual Frog sighting
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        //Utilizing VicoChart for displaying visual frog data
        item {
            val entryModel = entryModelOf(ranaCount, bufoCount, ranaTempCount)
            VicoChart(entryModel)
        }
        //Individual Frog sighting composables
        items(filteredLocations) { info ->
            FrogComposable(info)
        }
    }

}

//Counts the number of sightings for each frog type and returns a map of frog types and counts
fun countFrogTypes(gatheringInfoList: List<GatheringInfo>): Map<String, Int> {
    val frogTypes = listOf("Rana arvalis", "Bufo bufo", "Rana temporaria")

    return frogTypes.associateWith { frogType ->
        gatheringInfoList.count { it.FrogName == frogType }
    }
}

//Filter function using coordinates to filter sightings within 10km
//0.09 coordinates correlate to around ~10km. Not entirely accurate, but for the sake of the project, it's good enough
fun filterSightings(gatheringInfoList: List<GatheringInfo>, chosenLat: Double, chosenLon: Double): List<GatheringInfo> {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return gatheringInfoList.filter { info ->
        val latMin = info.FrogLatitude.toDoubleOrNull() ?: return@filter false
        val lonMax = info.FrogLongitude.toDoubleOrNull() ?: return@filter false
        latMin in (chosenLat - 0.09)..(chosenLat + 0.09) && lonMax in (chosenLon - 0.09)..(chosenLon + 0.09)
        //Also order by date, to put the most recent first
    }.sortedByDescending { info ->
        val frogDate = info.Date.trim().ifEmpty { "1900-01-01" } // Assign dummy date if empty
        try {
            dateFormat.parse(frogDate)
        } catch (e: Exception) {
            dateFormat.parse("1900-01-01") //Assign dummy date also, if dateformat unrecognisable
        }
    }
}


