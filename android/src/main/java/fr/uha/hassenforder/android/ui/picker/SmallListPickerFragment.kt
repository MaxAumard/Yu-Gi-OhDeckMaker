package fr.uha.hassenforder.android.ui.picker
/*
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

abstract class SmallListPickerFragment : DialogFragment(), DialogInterface.OnClickListener {

    protected abstract fun getRequestKey(): String
    protected abstract fun getTitle(): String?
    protected abstract fun buildInputs(): Array<String>
    protected abstract fun buildOutput(position: Int): String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getTitle())
                .setItems(buildInputs(), this)
                .setNegativeButton(android.R.string.cancel, this)
        return builder.create()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        val requestKey = getRequestKey()
        val result = Bundle()
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> {
            }
            DialogInterface.BUTTON_NEGATIVE -> result.putString(NAME, buildOutput(-1))
            DialogInterface.BUTTON_NEUTRAL -> {
            }
            else -> result.putString(NAME, buildOutput(which))
        }
        parentFragmentManager.setFragmentResult(requestKey, result)
    }

    companion object {
        const val NAME: String = "name"
    }
}

 */