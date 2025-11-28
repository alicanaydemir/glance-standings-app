package com.aydemir.glancestandingsapp.ui.main

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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aydemir.glancestandingsapp.R
import com.aydemir.glancestandingsapp.model.DataSample
import com.aydemir.glancestandingsapp.model.StandingsUiState
import com.aydemir.glancestandingsapp.model.Team
import com.aydemir.glancestandingsapp.ui.theme.GlanceStandingsAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GlanceStandingsAppTheme {
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
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    text = LocalContext.current.getString(R.string.what_club_u_support)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Dropdown(selectedId = mSelectedId, {
                    scope.launch {
                        viewModel.saveSelectedTeam(it.id)
                    }
                }, stateLoadingDelete = stateLoadingDelete, stateLoadingSave = stateLoadingSave)
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
fun Dropdown(
    selectedId: Int,
    onSelectedTeam: (Team) -> Unit,
    stateLoadingDelete: StandingsUiState,
    stateLoadingSave: StandingsUiState
) {
    var mExpanded by remember { mutableStateOf(false) }
    val icon = if (mExpanded) Icons.Filled.KeyboardArrowUp
    else Icons.Filled.KeyboardArrowDown

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(16.dp)
            .animateContentSize()
            .clickable {
                if (stateLoadingSave is StandingsUiState.Loading || stateLoadingDelete is StandingsUiState.Loading) return@clickable
                mExpanded = !mExpanded
            }
    ) {
        if (stateLoadingSave is StandingsUiState.Loading) {
            Text(
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                text = LocalContext.current.getString(R.string.loading)
            )
        } else {
            Row {
                Text(
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    text = DataSample.getListStandings().firstOrNull { it.id == selectedId }?.name
                        ?: LocalContext.current.getString(R.string.choose_a_team)
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
    stateLoadingDelete: StandingsUiState,
    stateLoadingSave: StandingsUiState
) {
    Button(modifier = Modifier.padding(8.dp), onClick = {
        if (stateLoadingDelete is StandingsUiState.Loading || stateLoadingSave is StandingsUiState.Loading) return@Button
        scope.launch {
            onClick.invoke()
        }
    }) {
        if (stateLoadingDelete is StandingsUiState.Loading || stateLoadingSave is StandingsUiState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = Color.White
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
    GlanceStandingsAppTheme {
        MainScreen()
    }
}