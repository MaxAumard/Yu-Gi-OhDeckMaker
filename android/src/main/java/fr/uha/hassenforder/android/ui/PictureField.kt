package fr.uha.hassenforder.android.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import fr.uha.hassenforder.android.R

class TakePictureWithUriContract : ActivityResultContract<Uri, Pair<Boolean, Uri>>() {

    private lateinit var imageUri: Uri

    @CallSuper
    override fun createIntent(context: Context, input: Uri): Intent {
        imageUri = input
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, input)
    }

    override fun getSynchronousResult(
        context: Context,
        input: Uri
    ): SynchronousResult<Pair<Boolean, Uri>>? = null

    @Suppress("AutoBoxing")
    override fun parseResult(resultCode: Int, intent: Intent?): Pair<Boolean, Uri> {
        return (resultCode == Activity.RESULT_OK) to imageUri
    }
}

@Composable
fun PictureField(
    value : Uri? = null,
    onValueChange: (Uri?) -> Unit,
    newImageUriProvider : (Context) -> Uri,
    modifier : Modifier = Modifier,
    @StringRes labelId: Int? = null,
    @StringRes errorId : Int? = null,
) {
    val context = LocalContext.current

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> onValueChange(uri) }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = TakePictureWithUriContract(),
        onResult = { success -> if (success.first) onValueChange(success.second) }
    )

    Column (
        modifier = modifier
            .padding(top = 4.dp, bottom = 4.dp)
            .fillMaxWidth()
            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground), MaterialTheme.shapes.extraSmall)
            .padding(start = 16.dp),
    ) {
        val color = if (errorId == null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
        if (labelId != null) {
            Text(
                text = stringResource(id = labelId),
                color = color
            )
        }
        Row() {
            AsyncImage(
                model = value,
                modifier = Modifier.size(128.dp),
                contentDescription = "Selected image",
                error = rememberVectorPainter(Icons.Outlined.Error),
                placeholder = rememberVectorPainter(Icons.Outlined.Casino),
            )
            Column() {
                Button(onClick = { onValueChange(null) }) {
                    Icon(imageVector = Icons.Outlined.Delete, contentDescription = "delete picture")
                }
                Row (horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Button(onClick = { imagePicker.launch("image/*") }) {
                        Icon(
                            imageVector = Icons.Outlined.PhotoLibrary,
                            contentDescription = "picture gallery"
                        )
                    }
                    Button(
                        onClick = {
                            cameraLauncher.launch(newImageUriProvider(context))
                        }
                    ) {
                        Icon(imageVector = Icons.Outlined.Camera, contentDescription = "take picture")
                    }
                }
            }
        }
        if (errorId != null){
            Text(
                text = stringResource(id = errorId),
                color = color,
            )
        }
    }
}
