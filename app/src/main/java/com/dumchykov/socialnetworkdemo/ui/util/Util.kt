package com.dumchykov.socialnetworkdemo.ui.util

import android.content.Context
import android.view.View
import android.widget.Toast
import com.dumchykov.socialnetworkdemo.data.webapi.ResponseState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

suspend fun handleStandardResponse(
    state: ResponseState,
    context: Context,
    scope: CoroutineScope,
    progressLayout: View,
    onSuccess: suspend () -> Unit,
) {
    when (state) {
        is ResponseState.Error -> {
            progressLayout.visibility = View.GONE
            Toast.makeText(
                context,
                state.errorMessage.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }

        is ResponseState.HttpCode -> {
            Toast.makeText(
                context,
                "${state.code}, ${state.message}",
                Toast.LENGTH_LONG
            ).show()
        }

        ResponseState.Loading -> {
            progressLayout.visibility = View.VISIBLE
        }

        is ResponseState.Success<*> -> {
            scope.launch {
                onSuccess()
            }
        }

        ResponseState.Initial -> {}
    }
}