package com.weighttracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.entryOf
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import java.time.ZoneId
import com.weighttracker.data.entity.WeightEntry
import com.weighttracker.utils.DateUtils
import com.weighttracker.viewmodel.WeightViewModel
import java.time.LocalDateTime

enum class ChartPeriod {
    WEEK, MONTH, YEAR
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartsScreen(viewModel: WeightViewModel) {
    var selectedPeriod by remember { mutableStateOf(ChartPeriod.WEEK) }

    val startDate = when (selectedPeriod) {
        ChartPeriod.WEEK -> DateUtils.getStartOfWeek()
        ChartPeriod.MONTH -> DateUtils.getStartOfMonth()
        ChartPeriod.YEAR -> DateUtils.getStartOfYear()
    }

    val entries by viewModel.getEntriesForPeriod(startDate, LocalDateTime.now())
        .collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weight Trends", style = MaterialTheme.typography.headlineMedium) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Period Selector
            PeriodSelector(
                selectedPeriod = selectedPeriod,
                onPeriodSelected = { selectedPeriod = it }
            )

            // Statistics Card
            StatisticsCard(entries = entries)

            // Chart Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Weight Chart",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    if (entries.isNotEmpty()) {
                        WeightChart(entries = entries)
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No data for this period",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PeriodSelector(
    selectedPeriod: ChartPeriod,
    onPeriodSelected: (ChartPeriod) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PeriodButton(
            text = "Week",
            selected = selectedPeriod == ChartPeriod.WEEK,
            onClick = { onPeriodSelected(ChartPeriod.WEEK) },
            modifier = Modifier.weight(1f)
        )
        PeriodButton(
            text = "Month",
            selected = selectedPeriod == ChartPeriod.MONTH,
            onClick = { onPeriodSelected(ChartPeriod.MONTH) },
            modifier = Modifier.weight(1f)
        )
        PeriodButton(
            text = "Year",
            selected = selectedPeriod == ChartPeriod.YEAR,
            onClick = { onPeriodSelected(ChartPeriod.YEAR) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun PeriodButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primary
                           else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (selected) MaterialTheme.colorScheme.onPrimary
                          else MaterialTheme.colorScheme.onSurfaceVariant
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (selected) 4.dp else 0.dp
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun StatisticsCard(entries: List<WeightEntry>) {
    if (entries.isEmpty()) return

    val currentWeight = entries.lastOrNull()?.weight ?: 0f
    val startWeight = entries.firstOrNull()?.weight ?: 0f
    val weightChange = currentWeight - startWeight
    val minWeight = entries.minOfOrNull { it.weight } ?: 0f
    val maxWeight = entries.maxOfOrNull { it.weight } ?: 0f
    val avgWeight = if (entries.isNotEmpty()) entries.map { it.weight }.average().toFloat() else 0f

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (weightChange < 0)
                MaterialTheme.colorScheme.secondaryContainer
            else MaterialTheme.colorScheme.tertiaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Weight Change
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Period Change",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (weightChange < 0) Icons.Default.TrendingDown
                                     else Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = if (weightChange < 0) Color(0xFF4CAF50)
                              else Color(0xFFFF6B9B)
                    )
                    Text(
                        text = "${if (weightChange > 0) "+" else ""}%.1f kg".format(weightChange),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (weightChange < 0) Color(0xFF4CAF50)
                               else Color(0xFFFF6B9B)
                    )
                }
            }

            Divider(color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.2f))

            // Statistics Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(label = "Min", value = "%.1f kg".format(minWeight))
                StatItem(label = "Max", value = "%.1f kg".format(maxWeight))
                StatItem(label = "Avg", value = "%.1f kg".format(avgWeight))
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
fun WeightChart(entries: List<WeightEntry>) {
    if (entries.isEmpty()) return

    val sortedEntries = entries.sortedBy { it.timestamp }

    // Create chart entries with x (timestamp) and y (weight) coordinates
    val chartEntries = sortedEntries.mapIndexed { index, entry ->
        entryOf(index.toFloat(), entry.weight)
    }

    val chartEntryModel = entryModelOf(chartEntries)

    // Determine the date format based on the time span
    val daysDiff = if (sortedEntries.size > 1) {
        java.time.temporal.ChronoUnit.DAYS.between(
            sortedEntries.first().timestamp.toLocalDate(),
            sortedEntries.last().timestamp.toLocalDate()
        )
    } else 0L

    // Create a custom axis value formatter for dates
    val dateFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
        val index = value.toInt()
        if (index >= 0 && index < sortedEntries.size) {
            val entry = sortedEntries[index]
            when {
                daysDiff <= 7 -> {
                    // For week view, show short date (e.g., "Jan 15")
                    DateUtils.formatShortDate(entry.timestamp.toLocalDate())
                }
                daysDiff <= 31 -> {
                    // For month view, show short date
                    DateUtils.formatShortDate(entry.timestamp.toLocalDate())
                }
                else -> {
                    // For year view, show month and year
                    entry.timestamp.format(java.time.format.DateTimeFormatter.ofPattern("MMM yy"))
                }
            }
        } else {
            ""
        }
    }

    ProvideChartStyle(chartStyle = m3ChartStyle()) {
        Chart(
            chart = lineChart(),
            model = chartEntryModel,
            startAxis = rememberStartAxis(),
            bottomAxis = rememberBottomAxis(
                valueFormatter = dateFormatter,
                guideline = null
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}
