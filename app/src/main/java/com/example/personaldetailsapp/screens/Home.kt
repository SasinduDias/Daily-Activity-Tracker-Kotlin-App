import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.NoteAlt
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.personaldetailsapp.AuthState
import com.example.personaldetailsapp.AuthViewModel
import com.example.personaldetailsapp.MainActivity
import com.example.personaldetailsapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage

@Composable
fun HomeScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val authState = authViewModel.authState.observeAsState()

    // Redirect to Sign-In screen if user is not authenticated
    LaunchedEffect(authState.value) {
        if (authState.value is AuthState.Unauthenticated) {
            navController.navigate(MainActivity.Routes.SignIn.name)
        }
    }

    var selectedItem by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedItemIndex = selectedItem,
                onItemSelected = { selectedItem = it }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (selectedItem) {
                0 -> HomeContent(authViewModel)  // ✅ Fix: No recursion issue
                1 -> SettingsScreen()
                2 -> SettingsContent(authViewModel)
            }
        }
    }
}

@Composable
fun HomeContent(authViewModel: AuthViewModel) {
    var expenseName by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp, 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = stringResource(id = R.string.add_expense_name),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displaySmall
        )

        Spacer(modifier = Modifier.padding(8.dp))

        Text(
            text = stringResource(id = R.string.add_expense_name),
            style = MaterialTheme.typography.bodyLarge
        )

        ExpensesTextField(
            modifier = Modifier.fillMaxWidth(),
            value = expenseName,
            label = "",
            onValueChange = { expenseName = it },
            placeholder = "Please enter expense",
            leadingIcon = {
                Icon(Icons.Default.NoteAlt, contentDescription = "Email")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )

        Demo_ExposedDropdownMenuBox()


//        Text(text = "🏠 Home Page", fontSize = 32.sp)
//        TextButton(onClick = { authViewModel.signout() }) {
//            Text(text = "Sign out")
        //}
    }
}

@Composable
fun ExpensesTextField(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon:@Composable (() -> Unit)? = null,
    label:String,
) {

    OutlinedTextField(
        modifier = modifier,
        value = value,
        keyboardOptions = keyboardOptions,
        leadingIcon = leadingIcon,
        onValueChange = onValueChange,
        trailingIcon = trailingIcon,
    )
}




@Composable
fun SettingsContent(authViewModel: AuthViewModel) {

    var imageUrl by remember { mutableStateOf(authViewModel.firebaseUser?.photoUrl?.toString()) }
    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                uploadImageToFirebase(uri) { downloadUrl ->
                    imageUrl = downloadUrl
                    updateUserProfile(downloadUrl)
                }
            }
        }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create Your profile", fontSize = 18.sp, fontFamily = FontFamily.SansSerif)
        Spacer(Modifier.height(10.dp))
        Text(
            authViewModel.firebaseUser?.email.toString(),
            fontSize = 15.sp,
            fontFamily = FontFamily.SansSerif
        )
        Image(
            painter = rememberAsyncImagePainter(imageUrl ?: "https://via.placeholder.com/150"),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .clickable { launcher.launch("image/*") } // Open Image Picker on Click
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Tap to Change Profile Picture", fontSize = 16.sp, color = Color.Gray)
    }
    //  Text(authViewModel.firebaseUser?., fontSize = 15.sp, fontFamily = FontFamily.SansSerif)
}

fun updateUserProfile(imageUrl: String) {
    val user = FirebaseAuth.getInstance().currentUser
    val profileUpdates = UserProfileChangeRequest.Builder()
        .setPhotoUri(Uri.parse(imageUrl))
        .build()

    user?.updateProfile(profileUpdates)
        ?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Firebase", "Profile updated successfully!")
            } else {
                Log.e("Firebase", "Profile update failed: ${task.exception}")
            }
        }

}

fun uploadImageToFirebase(uri: Uri, onSuccess: (String) -> Unit) {
    val storageRef = FirebaseStorage.getInstance().reference
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val imageRef = storageRef.child("profile_images/$userId.jpg")

    imageRef.putFile(uri)
        .addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                onSuccess(downloadUri.toString()) // Pass URL back
            }
        }
        .addOnFailureListener { e ->
            Log.e("Firebase", "Upload Failed: ${e.message}")
        }
}

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    containerColor: Color = NavigationBarDefaults.containerColor,
    contentColor: Color = MaterialTheme.colorScheme.contentColorFor(containerColor),
    tonalElevation: Dp = NavigationBarDefaults.Elevation,
    windowInsets: WindowInsets = NavigationBarDefaults.windowInsets,
    selectedItemIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    NavigationBar(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        windowInsets = windowInsets
    ) {
        val items = listOf("Home", "Summary", "Settings")
        val selectedIcons = listOf(Icons.Filled.Home, Icons.Filled.NoteAlt, Icons.Filled.Settings)
        val unselectedIcons =
            listOf(Icons.Outlined.Home, Icons.Outlined.NoteAlt, Icons.Outlined.Settings)

        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (selectedItemIndex == index) selectedIcons[index] else unselectedIcons[index],
                        contentDescription = item
                    )
                },
                label = { Text(item) },
                selected = selectedItemIndex == index,
                onClick = { onItemSelected(index) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    // Mocking HomeScreen for preview (without NavController)
}
