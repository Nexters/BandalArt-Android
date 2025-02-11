package com.nexters.bandalart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import com.nexters.bandalart.database.getDatabaseBuilder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao = getDatabaseBuilder(applicationContext).peopleDao()
        setContent {
            App(
                dao,
                prefs = remember {
                    createDataStore(applicationContext)
                }
            )
        }
    }
}
