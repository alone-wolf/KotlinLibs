//package top.writerpass.cmpdatepicker
//
//import androidx.compose.animation.core.tween
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.fadeOut
//import androidx.compose.animation.scaleIn
//import androidx.compose.animation.scaleOut
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.systemBarsPadding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.layout.widthIn
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.text.BasicText
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.CalendarViewMonth
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.derivedStateOf
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.ColorFilter
//import androidx.compose.ui.layout.onGloballyPositioned
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.semantics.Role
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.ui.window.Dialog
//import com.composables.composetheme.ComposeTheme
//import com.composables.composetheme.base
//import com.composables.composetheme.colors
//import com.composables.composetheme.gray100
//import com.composables.composetheme.gray200
//import com.composables.composetheme.gray500
//import com.composables.composetheme.gray900
//import com.composables.composetheme.round
//import com.composables.composetheme.roundL
//import com.composables.composetheme.shapes
//import com.composables.composetheme.sm
//import com.composables.composetheme.textStyles
//import kotlinx.datetime.DateTimeUnit
//import kotlinx.datetime.DayOfWeek
//import kotlinx.datetime.LocalDate
//import kotlinx.datetime.Month
//import kotlinx.datetime.TimeZone
//import kotlinx.datetime.minus
//import kotlinx.datetime.plus
//import kotlinx.datetime.toLocalDateTime
//import kotlin.time.Clock
//import kotlin.time.ExperimentalTime
//
///**
// * Add the following dependency to your build.gradle.kts:
// *
// * dependencies {
// *      implementation("com.composables:core:1.11.2")
// *      implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")
// * }
// */
//
//@Composable
//fun Calendar(
//    today: LocalDate,
//    selectedDate: LocalDate?,
//    minDate: LocalDate,
//    maxDate: LocalDate,
//    onDateSelected: (date: LocalDate) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    var firstOfSelectedMonth by remember {
//        val selected = selectedDate ?: today
//        mutableStateOf(LocalDate(selected.year, selected.month, 1))
//    }
//
//    val daysOfWeek = remember { DayOfWeek.entries.map { it.name.uppercase().take(1) } }
//    val firstDayOfWeek = firstOfSelectedMonth.dayOfWeek
//
//    val canGoPrevious by derivedStateOf {
//        LocalDate(minDate.year, minDate.month, 1) <= firstOfSelectedMonth.minus(
//            1,
//            DateTimeUnit.MONTH
//        )
//    }
//    val canGoNext by derivedStateOf {
//        firstOfSelectedMonth.plus(1, DateTimeUnit.MONTH) <= LocalDate(
//            maxDate.year,
//            maxDate.month,
//            1
//        )
//    }
//
//    @Composable
//    fun DateText(
//        date: LocalDate,
//        enabled: Boolean,
//        onClick: () -> Unit,
//        color: Color = ComposeTheme.colors.gray900,
//        modifier: Modifier = Modifier
//    ) {
//        val textColor = when {
//            date == selectedDate -> Color.White
//            enabled -> color
//            else -> ComposeTheme.colors.gray100
//        }
//        Box(
//            modifier = modifier
//                .height(40.dp)
//                .clip(ComposeTheme.shapes.round)
//                .clickable(role = Role.Button, enabled = enabled, onClick = onClick),
//            contentAlignment = Alignment.Center
//        ) {
//            if (date == today) {
//                Box(
//                    Modifier.matchParentSize().clip(ComposeTheme.shapes.round)
//                        .background(ComposeTheme.colors.gray100)
//                )
//            }
//            if (date == selectedDate) {
//                Box(Modifier.size(40.dp).clip(CircleShape).background(Color.Black))
//            }
//
//            BasicText(
//                date.dayOfMonth.toString(),
//                style = ComposeTheme.textStyles.sm.copy(color = textColor)
//            )
//        }
//    }
//
//    var componentWidth by remember { mutableStateOf(Dp.Unspecified) }
//    val cellWidth by derivedStateOf { componentWidth / 7 }
//
//    val density = LocalDensity.current
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = modifier.onGloballyPositioned {
//            with(density) {
//                componentWidth = it.size.width.toDp()
//            }
//        }) {
//        Row(
//            horizontalArrangement = Arrangement.SpaceBetween,
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Box(
//                modifier = Modifier.clip(ComposeTheme.shapes.round).size(36.dp)
//                    .clickable(enabled = canGoPrevious) {
//                        firstOfSelectedMonth = firstOfSelectedMonth.minus(1, DateTimeUnit.MONTH)
//                    }, contentAlignment = Alignment.Center
//            ) {
//                Image(
//                    Lucide.ChevronLeft,
//                    contentDescription = "Go to previous month",
//                    colorFilter = ColorFilter.tint(if (canGoPrevious) ComposeTheme.colors.gray500 else ComposeTheme.colors.gray200)
//                )
//            }
//
//            val month =
//                firstOfSelectedMonth.month.name.lowercase().replaceFirstChar { it.uppercase() }
//            val year = firstOfSelectedMonth.year
//
//            BasicText("$month $year", style = ComposeTheme.textStyles.sm)
//
//            Box(
//                modifier = Modifier.clip(ComposeTheme.shapes.round).size(36.dp)
//                    .clickable(enabled = canGoNext) {
//                        firstOfSelectedMonth = firstOfSelectedMonth.plus(1, DateTimeUnit.MONTH)
//                    }, contentAlignment = Alignment.Center
//            ) {
//                Image(
//                    Lucide.ChevronRight,
//                    contentDescription = "Go to next month",
//                    colorFilter = ColorFilter.tint(if (canGoNext) ComposeTheme.colors.gray500 else ComposeTheme.colors.gray200)
//                )
//            }
//        }
//
//        Spacer(Modifier.height(20.dp))
//
//        Row(Modifier.fillMaxWidth()) {
//            daysOfWeek.forEach { day ->
//                Box(Modifier.width(cellWidth), contentAlignment = Alignment.Center) {
//                    BasicText(
//                        day,
//                        style = ComposeTheme.textStyles.sm.copy(color = ComposeTheme.colors.gray500)
//                    )
//                }
//            }
//        }
//
//        Spacer(Modifier.height(8.dp))
//
//        val daysToShow = remember(firstOfSelectedMonth) {
//            val remainingDaysOfPreviousMonth = firstDayOfWeek.ordinal
//            val previousMonth = if (remainingDaysOfPreviousMonth > 0) {
//                val first =
//                    firstOfSelectedMonth.minus(remainingDaysOfPreviousMonth, DateTimeUnit.DAY)
//                val last = firstOfSelectedMonth.minus(1, DateTimeUnit.DAY)
//
//                generateSequence(first) { date ->
//                    if (date < last) date.plus(1, DateTimeUnit.DAY) else null
//                }.toList()
//            } else emptyList()
//
//            val first = LocalDate(firstOfSelectedMonth.year, firstOfSelectedMonth.month, 1)
//            val last = first.plus(1, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY)
//            val currentMonth = generateSequence(first) { date ->
//                if (date < last) date.plus(1, DateTimeUnit.DAY) else null
//            }.toList()
//
//            val nextMonth = run {
//                // always show a set number of days, so that each month has the same height and width
//                val daysPerCalendarMonth = 42
//
//                val firstDayOfNextMonth = firstOfSelectedMonth.plus(1, DateTimeUnit.MONTH)
//                List(daysPerCalendarMonth - previousMonth.size - currentMonth.size) { i ->
//                    firstDayOfNextMonth.plus(i, DateTimeUnit.DAY)
//                }
//            }
//
//            previousMonth + currentMonth + nextMonth
//        }
//
//        Column(Modifier.fillMaxWidth()) {
//            daysToShow.chunked(7).forEach { daysInWeek ->
//                Row {
//                    daysInWeek.forEach { date ->
//                        val isNotInThisMonth =
//                            date.month != firstOfSelectedMonth.month || date.year != firstOfSelectedMonth.year
//                        DateText(
//                            date = date,
//                            enabled = date in minDate..maxDate,
//                            onClick = { onDateSelected(date) },
//                            modifier = Modifier.width(cellWidth),
//                            color = if (isNotInThisMonth) ComposeTheme.colors.gray500 else ComposeTheme.colors.gray900
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalTime::class)
//@Composable
//fun DatePicker() {
//    var selectedDate: LocalDate? by remember { mutableStateOf(null) }
//    val today = remember {
//        val currentMoment = Clock.System.now()
//        currentMoment.toLocalDateTime(TimeZone.currentSystemDefault()).date
//    }
//    val minDate = LocalDate(today.year, Month.JANUARY, 1)
//    val maxDate = LocalDate(today.year, Month.DECEMBER, 31)
//
//    val dialogState = rememberDialogState()
//
//    Dialog(state = dialogState) {
//        Scrim(enter = fadeIn(), exit = fadeOut(), scrimColor = Color.Black.copy(0.3f))
//        DialogPanel(
//            Modifier
//                .systemBarsPadding()
//                .padding(40.dp)
//                .widthIn(max = 320.dp)
//                .shadow(8.dp, ComposeTheme.shapes.round)
//                .clip(ComposeTheme.shapes.round)
//                .border(1.dp, ComposeTheme.colors.gray200, ComposeTheme.shapes.round)
//                .background(Color.White)
//                .padding(20.dp),
//            enter = scaleIn(initialScale = 0.8f) + fadeIn(tween(durationMillis = 250)),
//            exit = scaleOut(targetScale = 0.6f) + fadeOut(tween(durationMillis = 150))
//        ) {
//            var dialogDate by remember { mutableStateOf(selectedDate) }
//
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                BasicText("Select a date", style = ComposeTheme.textStyles.base)
//                Spacer(Modifier.height(20.dp))
//                Calendar(
//                    modifier = Modifier.fillMaxWidth(),
//                    today = today,
//                    minDate = minDate,
//                    maxDate = maxDate,
//                    selectedDate = dialogDate,
//                    onDateSelected = {
//                        dialogDate = it
//                    }
//                )
//                Spacer(Modifier.height(20.dp))
//                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//                    Box(
//                        Modifier.weight(1f).clip(ComposeTheme.shapes.roundL)
//                            .clickable(role = Role.Button) { dialogState.visible = false }
//                            .border(1.dp, Color(0xFFBDBDBD), ComposeTheme.shapes.roundL)
//                            .padding(horizontal = 14.dp, vertical = 10.dp),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        BasicText(
//                            text = "Cancel",
//                            style = TextStyle(
//                                color = Color(0xFF424242),
//                                fontSize = 14.sp,
//                                fontWeight = FontWeight(600)
//                            )
//                        )
//                    }
//                    Box(
//                        Modifier.weight(1f).clip(ComposeTheme.shapes.roundL)
//                            .clickable(role = Role.Button) {
//                                selectedDate = dialogDate
//                                dialogState.visible = false
//                            }.background(Color(0xFF212121))
//                            .padding(horizontal = 14.dp, vertical = 10.dp),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        BasicText(
//                            text = "Set date",
//                            style = TextStyle(
//                                color = Color.White,
//                                fontSize = 14.sp,
//                                fontWeight = FontWeight(600)
//                            )
//                        )
//                    }
//                }
//            }
//        }
//    }
//
//    Box(
//        Modifier
//            .clip(ComposeTheme.shapes.roundL)
//            .clickable(role = Role.Button) { dialogState.visible = true }
//            .border(1.dp, Color(0xFFBDBDBD), ComposeTheme.shapes.roundL)
//            .padding(horizontal = 14.dp, vertical = 8.dp)
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            Image(
//                imageVector = Icons.Default.CalendarViewMonth,
//                contentDescription = null,
//                colorFilter = ColorFilter.tint(Color(0xFF424242))
//            )
//
//            val label = if (selectedDate == null) "Select a date" else {
//                val theDate = requireNotNull(selectedDate)
//                val month = theDate.month.name.lowercase().replaceFirstChar { it.uppercase() }
//                val year = theDate.year
//                val date = theDate.dayOfMonth
//                "$date $month $year"
//            }
//            BasicText(
//                text = label,
//                style = ComposeTheme.textStyles.base.copy(color = Color(0xFF424242))
//            )
//        }
//    }
//}