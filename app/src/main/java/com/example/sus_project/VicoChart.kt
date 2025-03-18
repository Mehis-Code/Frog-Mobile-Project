package com.example.sus_project

import androidx.compose.runtime.Composable
import com.patrykandpatryk.vico.core.axis.AxisPosition
import com.patrykandpatryk.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatryk.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatryk.vico.compose.axis.vertical.startAxis
import com.patrykandpatryk.vico.compose.chart.column.columnChart
import com.patrykandpatryk.vico.compose.chart.Chart
import com.patrykandpatryk.vico.core.entry.ChartEntryModel


//Utilizing VicoChart for charting tool. Displays frog data as a bar chart
@Composable
fun VicoChart(entryModel: ChartEntryModel) {
    val labels = listOf("RanaArva", "BufoBufo", "RanaTemp")
    val axisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
        labels.getOrNull(value.toInt()) ?: ""
    }
    Chart(
        chart = columnChart(),
        model = entryModel,
        startAxis = startAxis(),
        bottomAxis = bottomAxis(valueFormatter = axisValueFormatter),
    )
}