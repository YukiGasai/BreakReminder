package com.yukigasai.breakreminder.presentation

import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import com.yukigasai.breakreminder.R
import com.yukigasai.breakreminder.presentation.theme.BreakReminderTheme
import kotlinx.coroutines.delay
import java.time.Instant
import kotlin.math.floor

@Composable
fun formatTime(time: Long): String {
    if (time < 0) {
        return stringResource(id = R.string.loading)
    }
    val minutes = floor(time / 60.0).toLong()
    val seconds = time % 60
    return "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
}

@Composable
fun MainScreen(
    vm: MainScreenViewModel
) {

    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }

    val running = remember { vm.running }
    val timeUntilNextBreak = remember { vm.timeUntilNextBreak }
    val nextBreakTime = remember { vm.nextBreakTime }
    val nextTask = remember { vm.nextTask }
    val breakInterval = remember { vm.breakInterval }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(running.value) {
        vm.updateOnceASecond()
    }

    BreakReminderTheme {
        if (running.value == false) {
            CircularProgressIndicator(
                progress = breakInterval.floatValue / 60,
                modifier = Modifier.fillMaxSize(),
                indicatorColor = MaterialTheme.colors.primary,
                strokeWidth = 8.dp,
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectVerticalDragGestures { change, dragAmount ->
                        if (running.value) return@detectVerticalDragGestures
                        change.consume()
                        breakInterval.floatValue =
                            (breakInterval.floatValue - dragAmount / 4).coerceIn(1F, 60F)
                    }
                }
                .onRotaryScrollEvent  {
                    if (running.value) return@onRotaryScrollEvent false
                    breakInterval.floatValue =
                        (breakInterval.floatValue - it.verticalScrollPixels / 10).coerceIn(1F, 60F)
                    true
                }
                .focusRequester(focusRequester)
                .focusable()
        ) {


            if (running.value) {
                Text(
                    text = stringResource(id = R.string.time_until_next_break),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = formatTime(timeUntilNextBreak.longValue),
                )
                Button(
                    onClick = {vm.onStopClick(context)},
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_stop_24),
                        contentDescription = stringResource(id = R.string.stop)
                    )
                }
                Text(
                    text = nextTask.value,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text(
                    text = stringResource(id = R.string.break_interval),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "${breakInterval.floatValue.toInt()} ${stringResource(id = R.string.minutes)}",
                )
                Button(
                    onClick = {
                        vm.onStartClick(context)
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                        contentDescription = stringResource(id = R.string.start)
                    )
                }
            }
        }
    }
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    MainScreen(MainScreenViewModel(PrefsHelper(LocalContext.current)))
}