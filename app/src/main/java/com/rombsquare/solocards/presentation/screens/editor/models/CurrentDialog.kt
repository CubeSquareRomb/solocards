package com.rombsquare.solocards.presentation.screens.editor.models

sealed class CurrentDialog {
    object TagEditor : CurrentDialog()
    object RenameFile : CurrentDialog()
    object DeleteFile : CurrentDialog()
    object EditCard : CurrentDialog()
    object CreateCard : CurrentDialog()
    object Play : CurrentDialog()
    object PlaySettings : CurrentDialog()
}