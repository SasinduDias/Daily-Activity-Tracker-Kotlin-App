import android.text.TextUtils
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.personaldetailsapp.AuthViewModel
import com.example.personaldetailsapp.MainActivity
import com.example.personaldetailsapp.R

@Composable
fun SettingScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var selectedEmailAddress by rememberSaveable { mutableStateOf("") }
    var context = LocalContext.current
    val networkObserver = remember { NetworkObserver(context) }
    val isConnected by networkObserver.isConnected.observeAsState(false)

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxHeight()
            .fillMaxWidth()
    ) {

        Image(
            modifier = Modifier
                .clip(shape = CircleShape)
                .width(100.dp)
                .height(100.dp),
            painter = painterResource(R.drawable.happy_cook),
            contentDescription = ""
        )

        Spacer(Modifier.width(25.dp))

        Text(
            text = "Forgot Your Password?", color = Color.Black, fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

        Spacer(Modifier.width(20.dp))

        Text(
            text = "Enter your email address and we will send you \n instructions to reset your password",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(10.dp),
            color = Color.Blue,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = selectedEmailAddress,
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
            ),
            leadingIcon = {
                Icon(
                    Icons.Default.Email,
                    contentDescription = "forget password mail"
                )
            },
            onValueChange = { selectedEmailAddress = it },
            placeholder = { Text("Email") },
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                if (TextUtils.isEmpty(selectedEmailAddress)) {
                    Toast.makeText(context, "Please add your email !", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    if (isConnected) {
                        authViewModel.sendEmailVerification(selectedEmailAddress, context)
                    } else {
                        Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show()
                    }

                }

            },

            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // on below line we are adding text for our button
            Text(text = "Continue", fontSize = 18.sp, modifier = Modifier.padding(8.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        TextButton(
            onClick = {
                navController.navigate(MainActivity.Routes.SignIn.name)
            }
        ) {
            Text(text = "Back to the login screen", fontSize = 18.sp, color = Color.Black)
        }
    }

}