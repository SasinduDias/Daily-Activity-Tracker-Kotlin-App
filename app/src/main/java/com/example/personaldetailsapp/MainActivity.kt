package com.example.personaldetailsapp

import HomeScreen
import SettingsScreen
import SignInScreen
import SignUpScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.personaldetailsapp.ui.theme.PersonalDetailsAppTheme
import androidx.navigation.compose.composable


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PersonalDetailsAppTheme {
                MainNavigation()

            }
        }
    }

    @Composable
    fun MainNavigation() {
        val navController = rememberNavController()
        val  authViewModel= AuthViewModel()
        NavHost(
            navController,
            startDestination = Routes.SignIn.name
        )
        {
            composable(route = Routes.SignIn.name) {
                SignInScreen(navController = navController,
                    authViewModel= authViewModel)
            }
            composable(route = Routes.Settings.name) {
                SettingsScreen()
            }
            composable(route = Routes.SignUp.name) {
                SignUpScreen(
                    navController = navController,
                    authViewModel = authViewModel
                )
            }
            composable(route = Routes.Home.name) {
                HomeScreen( authViewModel = authViewModel,
                    navController = navController)
            }
        }

    }

    enum class Routes(@StringRes val title: Int) {
        SignIn(title = R.string.sign_in_name),
        SignUp(title = R.string.sign_up_name),
        Home(title = R.string.home_name),
        Settings(title = R.string.settings_name),
        Details(title = R.string.details_name)
    }
}
