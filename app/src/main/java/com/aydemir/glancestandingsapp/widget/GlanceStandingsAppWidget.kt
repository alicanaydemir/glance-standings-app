package com.aydemir.glancestandingsapp.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.material3.ColorProviders
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.aydemir.glancestandingsapp.R
import com.aydemir.glancestandingsapp.data.StandingsRepositoryImp
import com.aydemir.glancestandingsapp.main.MainActivity
import com.aydemir.glancestandingsapp.model.StandingsUiState
import com.aydemir.glancestandingsapp.model.StandingsState
import com.aydemir.glancestandingsapp.model.Team
import com.aydemir.glancestandingsapp.ui.theme.DarkColorScheme
import com.aydemir.glancestandingsapp.ui.theme.LightColorScheme
import com.aydemir.glancestandingsapp.ui.theme.Primary
import com.aydemir.glancestandingsapp.util.Colors
import com.aydemir.glancestandingsapp.worker.DeleteDataWorker
import com.aydemir.glancestandingsapp.worker.GetDataWorker

class GlanceStandingsAppWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val repository = StandingsRepositoryImp.get(context)

        provideContent {
            GlanceTheme(
                colors = ColorProviders(
                    light = LightColorScheme,
                    dark = DarkColorScheme,
                ),
            ) {
                WidgetScreen(repository = repository)
            }
        }
    }

    @Composable
    private fun WidgetScreen(repository: StandingsRepositoryImp) {

        val stateStandings =
            repository.getStandingsState().collectAsState(initial = StandingsState.Empty).value

        val stateLoading = repository.loading.collectAsState().value

        Column(
            modifier = GlanceModifier.fillMaxSize().background(Colors.Bg),
        ) {
            Header()
            Box(
                contentAlignment = Alignment.Center, modifier = GlanceModifier.fillMaxSize()
            ) {
                if (stateLoading == StandingsUiState.Loading) {
                    CircularProgressIndicator(color = ColorProvider(color = Primary))
                } else {
                    when (stateStandings) {
                        is StandingsState.Empty -> {
                            Text(
                                style = TextStyle(textAlign = androidx.glance.text.TextAlign.Center),
                                text = LocalContext.current.getString(R.string.no_standings_data)
                            )
                        }

                        is StandingsState.Error -> {
                            Text(text = LocalContext.current.getString(R.string.error))
                        }

                        is StandingsState.Success -> {
                            List(stateStandings.data)
                        }

                        is StandingsState.NoSelectedTeam -> {
                            Button(
                                text = LocalContext.current.getString(R.string.choose_a_team),
                                onClick = actionStartActivity<MainActivity>()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Header() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = GlanceModifier.fillMaxWidth().padding(start = 12.dp, end = 12.dp).height(32.dp)
            .background(Colors.Yellow)
    ) {
        Text(
            text = LocalContext.current.getString(R.string.standings), style = TextStyle(
                fontWeight = FontWeight.Bold, fontSize = 18.sp, color = ColorProvider(Color.Black)
            ), modifier = GlanceModifier.clickable(
                actionStartActivity<MainActivity>()
            )
        )
        Spacer(modifier = GlanceModifier.defaultWeight())
        Image(
            provider = ImageProvider(R.drawable.ic_refresh),
            contentDescription = null,
            modifier = GlanceModifier.clickable(
                onClick = actionRunCallback<RefreshAction>(
                    actionParametersOf(
                        RefreshAction.keySelectedTeamId to 6
                    )
                )
            )
        )
    }
}

@Composable
fun List(list: List<Team>) {
    LazyColumn(
        modifier = GlanceModifier.fillMaxSize()
    ) {
        items(list) { item ->
            Box(
                modifier = GlanceModifier.background(
                    if (item.position % 2 == 0) Color.White
                    else Color.LightGray
                ).padding(start = 12.dp, end = 12.dp).height(24.dp).fillMaxWidth(),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = GlanceModifier.fillMaxSize()
                ) {
                    Text(
                        "${item.position}. ${item.name}", style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = if (item.selected) ColorProvider(Colors.Red) else ColorProvider(
                                Color.Black
                            )
                        )
                    )
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Text(
                        text = "${item.point}", style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            color =
                            if (item.selected) ColorProvider(Colors.Red) else ColorProvider(Color.Black)
                        )
                    )
                }
            }
        }
    }
}

//-----------------------------------------------------------------------//
fun runGetDataWorker(context: Context) {
    val getDataWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<GetDataWorker>().build()
    WorkManager.getInstance(context).enqueue(getDataWorkRequest)
}

fun runDeleteDataWorker(context: Context) {
    val deleteDataWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<DeleteDataWorker>().build()
    WorkManager.getInstance(context).enqueue(deleteDataWorkRequest)
}

class RefreshAction : ActionCallback {
    companion object {
        val keySelectedTeamId = ActionParameters.Key<Int>(
            "selectedTeamId"
        )
    }

    override suspend fun onAction(
        context: Context, glanceId: GlanceId, parameters: ActionParameters
    ) {
        runGetDataWorker(context)
    }
}
//-----------------------------------------------------------------------//