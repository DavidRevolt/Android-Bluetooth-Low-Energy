package com.davidrevolt.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.davidrevolt.core.designsystem.components.AppFabButton
import com.davidrevolt.core.designsystem.components.LoadingWheel
import com.davidrevolt.core.designsystem.components.isSyncing.IsSyncing
import com.davidrevolt.core.designsystem.components.isSyncing.rememberIsSyncingState
import com.davidrevolt.core.designsystem.drawable.homeBanner
import com.davidrevolt.core.designsystem.icons.AppIcons


@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.homeUiState.collectAsStateWithLifecycle()
    val onSaveClick = viewModel::onSaveClick
    val showHomeDialog = remember { mutableStateOf(false) }
    val onMainFabClick = { showHomeDialog.value = true }


    if (showHomeDialog.value)
        HomeDialog(
            showHomeDialog = showHomeDialog,
            onButtonClick = {}
        )

    Scaffold(
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                AppFabButton(onFabClick = onMainFabClick, icon = AppIcons.Add)
            }

        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = homeBanner), contentDescription = "home banner",
                modifier = Modifier
                    .padding(top = 30.dp)
                    .size(200.dp)
            )
            when (uiState) {
                is HomeUiState.Data -> {
                    val data = (uiState as HomeUiState.Data)
                    if (data.stringsData.isNotEmpty())
                        HomeScreenContent(data.isSyncing, data.stringsData)
                    else
                        Text("Nothing to show here...yet", color = Color.White)
                }

                is HomeUiState.Loading -> LoadingWheel()
            }
        }
    }
}

@Composable
private fun HomeScreenContent(isSyncing: Boolean, stringsData:List<String>) {

    IsSyncing(
        state = rememberIsSyncingState(isRefreshing = isSyncing),
        onRefresh = {},
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Home Content
            LazyColumn {
                item {
                    stringsData.forEach{string -> Text(text=string) }
                }
            }
        }
    }
}



@Composable
fun HomeDialog(
    showHomeDialog: MutableState<Boolean>,
    onButtonClick: () -> Unit,
) {

    Dialog(
        onDismissRequest = { showHomeDialog.value = false },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Box(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(8.dp))
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Dialog Content
                Text("Test")
                Button(onClick = {
                    onButtonClick()
                    showHomeDialog.value = false
                }) {
                    Text("onButtonClick")
                }
            }
        }
    }
}