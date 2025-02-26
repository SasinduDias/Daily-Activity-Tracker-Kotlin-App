package com.example.personaldetailsapp

import HomeScreen
import SignInScreen
import SignUpScreen
import SplashScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.personaldetailsapp.ui.theme.PersonalDetailsAppTheme


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
        val authViewModel = AuthViewModel()


        NavHost(
            navController,
            startDestination = Routes.Splash.name
        )
        {
            composable(route = Routes.SignIn.name) {
                SignInScreen(
                    navController = navController,
                    authViewModel = authViewModel
                )
            }
            composable(route = Routes.Splash.name) {
                SplashScreen(
                    navController = navController,
                    authViewModel = authViewModel
                )
            }
            composable(route = Routes.SignUp.name) {
                SignUpScreen(
                    navController = navController,
                    authViewModel = authViewModel
                )
            }
            composable(route = Routes.Home.name) {
                HomeScreen(
                    authViewModel = authViewModel,
                    navController = navController
                )
            }
        }

    }

    enum class Routes(@StringRes val title: Int) {
        SignIn(title = R.string.sign_in_name),
        SignUp(title = R.string.sign_up_name),
        Home(title = R.string.home_name),
        Splash(title = R.string.settings_name),
        Settings(title = R.string.settings_name),
    }
}
