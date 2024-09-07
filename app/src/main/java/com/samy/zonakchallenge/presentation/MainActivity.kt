package com.samy.zonakchallenge.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.samy.zonakchallenge.domain.model.NewsResponse
import com.samy.zonakchallenge.presentation.detail_screen.DetailsScreen
import com.samy.zonakchallenge.presentation.news_screen.NewsScreen
import com.samy.zonakchallenge.ui.theme.ZonakChallengeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZonakChallengeTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "news_screen"
                ) {

                    composable("news_screen") {
                        NewsScreen(
                            navController
                        )
                    }
                    composable(
                        "details_screen/{sourceJson}",
                        arguments = listOf(navArgument("sourceJson") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val sourceJson = backStackEntry.arguments?.getString("sourceJson")
                        val source = Gson().fromJson(sourceJson, NewsResponse.Source::class.java)
                        DetailsScreen(
                            navController = navController,
                            data = source
                        )
                    }

                }


            }
        }
    }

}


