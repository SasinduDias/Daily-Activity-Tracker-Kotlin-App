import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.personaldetailsapp.AuthState
import com.example.personaldetailsapp.AuthViewModel
import com.example.personaldetailsapp.MainActivity
import com.example.personaldetailsapp.R


@Composable
fun SignUpScreen(navController:NavController, authViewModel: AuthViewModel) {
    val scrollState = rememberScrollState()
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.happy_cook),
            contentDescription = "background image",
            modifier = Modifier
                .fillMaxSize()
                .blur(7.dp),
            contentScale = ContentScale.Crop
        )
    }

    Column(
        modifier = Modifier
            .padding(10.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Box(
            modifier = Modifier
                .padding(28.dp)
                .alpha(0.7f)
                .clip(
                    CutCornerShape(
                        topEnd = 10.dp,
                        topStart = 10.dp,
                        bottomStart = 10.dp,
                        bottomEnd = 10.dp
                    )
                )
                .background(MaterialTheme.colorScheme.background)
                .wrapContentHeight()
        ){
            Column(
                modifier = Modifier.padding(48.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                val email = remember { mutableStateOf("") }
                val password = remember { mutableStateOf("") }
                val authState = authViewModel.authState.observeAsState()
                val context = LocalContext.current

                LaunchedEffect(authState.value) {
                    when (authState.value) {
                        is AuthState.Authenticated -> navController.navigate("home")
                        is AuthState.Error -> Toast.makeText(
                            context,
                            (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT
                        ).show()

                        else -> Unit
                    }
                }
                SignUpTitle()
                Spacer(modifier = Modifier.height(20.dp))
                SignUpFields(
                    email.value,
                    password.value,
                    onEmailChanged = {
                        email.value = it
                    },
                    onPasswordChanged = {
                        password.value = it
                    },

                )
                SignUpFooter(
                    onSignUpClick = {
                        authViewModel.signup(email.value,password.value)
                      //  navController.navigate(MainActivity.Routes.Home.name)
                    },
                    onSignInClick = {
                        navController.navigate(MainActivity.Routes.SignIn.name)
                    }
                )
            }
        }

    }
}

@Composable
fun SignUpFooter(onSignUpClick:() -> Unit,
                onSignInClick: () -> Unit) {

    Spacer(Modifier.height(20.dp))
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = onSignUpClick, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Sign Up")
        }
        TextButton(onClick = onSignInClick) {
            Text(text = "You already have an account, Click here")
        }

    }
}

@Composable
fun SignUpTitle() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Welcome", fontSize = 36.sp, fontWeight = FontWeight.ExtraBold)
        Text(text = "Sign up to continue", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun SignUpTextField(
    value: String,
    label: String,
    placeholder: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        label = { Text(text = label) },
        placeholder = { Text(text = placeholder) },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        onValueChange = onValueChange,
    )
}

@Composable
fun SignUpFields(
    email: String,
    password: String,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
) {

    Column {
        SignUpTextField(
            value = email,
            label = "Email",
            placeholder = "Enter your email address",
            onValueChange = onEmailChanged,
            leadingIcon = {
                Icon(Icons.Default.Email, contentDescription = "Email")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                //imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        SignUpTextField(
            value = password,
            label = "Password",
            placeholder = "Enter your password",
            onValueChange = onPasswordChanged,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                // imeAction = ImeAction.Go
            ),
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = "Password")
            }
        )


    }

}







