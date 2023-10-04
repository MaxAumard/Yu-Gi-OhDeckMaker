package fr.uha.hassenforder.team

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class TeamFileProvider : FileProvider(
    R.xml.file_paths
) {
    companion object {

        fun getImageUri(context: Context): Uri {
            val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "images")
            directory.mkdirs()
            val filename = createUniqueName("photo", "jpg")
            val photoFile = File(directory, filename)
            val authority = context.packageName + ".fileprovider"
            return getUriForFile(context, authority, photoFile)
        }

        private fun createUniqueName(prefix: String, extension: String?): String {
            val tmp = StringBuilder()
            tmp.append(prefix)
            tmp.append("-")
            val formatter = SimpleDateFormat("yyyyMMdd_HHmmss")
            tmp.append(formatter.format(Calendar.getInstance().time))
            if (extension != null) {
                tmp.append(".")
                tmp.append(extension)
            }
            return tmp.toString()
        }

    }
}