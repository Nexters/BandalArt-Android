package com.nexters.bandalart

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.nexters.bandalart.database.getDatabaseBuilder

fun MainViewController() = ComposeUIViewController {
    val dao = remember {
        getDatabaseBuilder().peopleDao()
    }
    App(
        dao,
        prefs = remember {
            createDataStore()
        }
    )
}

