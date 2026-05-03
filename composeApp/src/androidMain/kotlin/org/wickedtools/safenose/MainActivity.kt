package org.wickedtools.safenose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import org.koin.androidx.compose.koinViewModel
import org.wickedtools.safenose.feature_authentication.presentation.viewmodels.AuthViewModel
import org.wickedtools.safenose.screens.HomeScreen
import org.wickedtools.safenose.screens.LoginScreen
import org.wickedtools.safenose.screens.SignUpScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AuthRoot()
        }
    }
}

@Composable
private fun AuthRoot() {

    val authViewModel: AuthViewModel = koinViewModel()
    val uiState by authViewModel.uiState.collectAsState()
    var showSignUp by remember { mutableStateOf(false) }

    when {
        !uiState.isSessionChecked -> Box(Modifier.fillMaxSize())
        uiState.currentUser != null -> HomeScreen()
        showSignUp -> SignUpScreen(
            uiState = uiState,
            onSignUpClicked = authViewModel::onSignUpClicked,
            onNavigateToLogin = { showSignUp = false }
        )
        else -> LoginScreen(
            uiState = uiState,
            onLoginClicked = authViewModel::onLoginClicked,
            onNavigateToSignUp = { showSignUp = true }
        )
    }
}
