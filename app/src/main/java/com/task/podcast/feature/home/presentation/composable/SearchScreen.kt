package com.task.podcast.feature.home.presentation.composable

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.task.podcast.feature.home.domain.entity.ContentEntity
import com.task.podcast.feature.home.presentation.model.SearchUiEvent
import com.task.podcast.feature.home.presentation.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    onItemClicked: (ContentEntity) -> Unit = {}
) {
    val viewModel: SearchViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is SearchUiEvent.ShowError ->
                    Toast.makeText(context, context.getString(event.messageResId), Toast.LENGTH_SHORT).show()
            }
        }
    }

    SearchScreenContent(
        uiState = uiState,
        onIntent = { intent -> viewModel.processIntent(intent) },
        onItemClicked = onItemClicked
    )
}