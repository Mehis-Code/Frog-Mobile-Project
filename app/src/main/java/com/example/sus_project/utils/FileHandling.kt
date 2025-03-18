package com.example.sus_project.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.SerializedName
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

data class GatheringInfo(
    @SerializedName("Gathering.Conversions.WGS84.LatMin(N)") val FrogLatitude: String,
    @SerializedName("Gathering.Conversions.WGS84.LonMax(E)") val FrogLongitude: String,
    @SerializedName("Taxon.ScientificName") val FrogName: String,
    @SerializedName("Gathering.Date.Begin") val Date: String
)

//Copy to internal storage for access in the app
internal fun copyFileToInternalStorage(context: Context, sourceFileName: String, destinationFileName: String) {
    try {
        val inputStream = context.assets.open(sourceFileName)
        val outputStream = FileOutputStream(File(context.filesDir, destinationFileName))
        inputStream.copyTo(outputStream)
        inputStream.close()
        outputStream.close()
        Log.d("CopyFile", "$sourceFileName copied with success")
    } catch (e: IOException) {
        Log.e("CopyFile", "Error in copy process $sourceFileName: ${e.message}")
    }
}

//Transform JSON to GatheringInfo object
fun loadGatheringInfo(context: Context): List<GatheringInfo> {
    val file = File(context.filesDir, "FilteredFrog.json")
    Log.e("FrogRadar", "${file.absolutePath}")
    if (!file.exists()) {
        Log.e("FrogRadar", "File not reachable")
        return emptyList()
    }
    return try {
        Log.d("loadGatheringInfo", "Starting to load JSON data")
        val jsonString = file.readText()
        val gatheringInfoList = Gson().fromJson(jsonString, Array<GatheringInfo>::class.java).toList()
        Log.d("loadGatheringInfo", "JSON data loaded ${gatheringInfoList.size}")
        gatheringInfoList
    } catch (e: JsonSyntaxException) {
        Log.e("loadGatheringInfo", "JSON parsing error: ${e.message}")
        emptyList()
    }
}