package com.raveendran.mvvmtodo.util

sealed class UiEvents {
    object PopBackStack : UiEvents()
    data class Navigate(val route: String) : UiEvents()
    data class ShowSnackBar(
        val message: String,
        val action: String? = null
    ) : UiEvents()
}
