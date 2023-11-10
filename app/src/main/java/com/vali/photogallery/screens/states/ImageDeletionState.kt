package com.vali.photogallery.screens.states

data class DeletionState(
    val isDeleted: Boolean? = null,
    val deletionError: Throwable? = null
)
