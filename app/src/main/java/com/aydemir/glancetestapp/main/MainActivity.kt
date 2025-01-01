package com.aydemir.glancetestapp.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aydemir.glancetestapp.R
import com.aydemir.glancetestapp.model.DataSample
import com.aydemir.glancetestapp.model.StandingUisState
import com.aydemir.glancetestapp.model.Team
import com.aydemir.glancetestapp.ui.theme.GlanceTestAppTheme
import com.aydemir.glancetestapp.util.Colors
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GlanceTestAppTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val scope = rememberCoroutineScope()
    val mSelectedId = viewModel.hasSelectedTeamId.collectAsState(0).value
    val stateLoadingSave = viewModel.loadingSave.collectAsState().value
    val stateLoadingDelete = viewModel.loadingDelete.collectAsState().value

    Surface(
        modifier = Modifier.fillMaxSize(), color = Colors.Bg
    ) {
        Box(contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Dropdown(selectedId = mSelectedId, {
                    scope.launch {
                        viewModel.saveSelectedTeam(it.id)
                    }
                }, stateLoadingSave = stateLoadingSave)
                AnimatedVisibility(visible = mSelectedId != 0) {
                    LoadingButton(
                        scope = scope,
                        onClick = { viewModel.deleteSelectedTeam() },
                        stateLoadingDelete = stateLoadingDelete,
                        stateLoadingSave = stateLoadingSave
                    )
                }
            }
        }
    }

}

@Composable
fun Dropdown(selectedId: Int, onSelectedTeam: (Team) -> Unit, stateLoadingSave: StandingUisState) {
    var mExpanded by remember { mutableStateOf(false) }
    val icon = if (mExpanded) Icons.Filled.KeyboardArrowUp
    else Icons.Filled.KeyboardArrowDown

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.onSurface)
            .padding(16.dp)
            .animateContentSize()
            .clickable {
                if (stateLoadingSave is StandingUisState.Loading) return@clickable
                mExpanded = !mExpanded
            }
    ) {
        if (stateLoadingSave is StandingUisState.Loading) {
            Text(
                textAlign = TextAlign.Center,
                text = "Loading..."
            )
        } else {
            Row {
                Text(
                    textAlign = TextAlign.Center,
                    text = DataSample.getListStandings().firstOrNull { it.id == selectedId }?.name
                        ?: "Tuttuğunuz takım?"
                )
                Icon(icon, "")
            }
        }

        DropdownMenu(expanded = mExpanded, onDismissRequest = { mExpanded = false }) {
            DataSample.getListStandings().forEach { team: Team ->
                DropdownMenuItem(text = { Text(text = team.name) }, onClick = {
                    mExpanded = false
                    onSelectedTeam.invoke(team)
                })
            }
        }
    }
}

@Composable
fun LoadingButton(
    scope: CoroutineScope,
    onClick: () -> Unit,
    stateLoadingDelete: StandingUisState,
    stateLoadingSave: StandingUisState
) {
    Button(modifier = Modifier.padding(8.dp), onClick = {
        if (stateLoadingDelete is StandingUisState.Loading) return@Button
        scope.launch {
            onClick.invoke()
        }
    }) {
        if (stateLoadingDelete is StandingUisState.Loading || stateLoadingSave is StandingUisState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = Color.Black
            )
        } else {
            Text(
                textAlign = TextAlign.Center,
                text = stringResource(R.string.delete_team)
            )
        }
    }
}


@Preview
@Composable
fun GreetingPreview() {
    GlanceTestAppTheme {
        MainScreen()
    }
}