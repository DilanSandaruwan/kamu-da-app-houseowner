package com.dilan.kamuda.houseownerapp.feature.menu

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.dilan.kamuda.houseownerapp.R

class CustomDialogFragmentMenu : DialogFragment() {

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_MESSAGE = "message"
        private const val ARG_POSITIVE_BUTTON_TEXT = "positive_button_text"
        private const val ARG_NEGATIVE_BUTTON_TEXT = "negative_button_text"
        private const val ARG_NEUTRAL_BUTTON_TEXT = "neutral_button_text"
        private const val ARG_CHECKED_ITEMS = "list_data"

        fun newInstance(
            title: String,
            message: String,
            positiveButtonText: String = "OK",
            negativeButtonText: String? = null,
            neutralButtonText: String? = null,
        ): CustomDialogFragmentMenu {
            val fragment = CustomDialogFragmentMenu()
            val args = Bundle().apply {
                putString(ARG_TITLE, title)
                putString(ARG_MESSAGE, message)
                putString(ARG_POSITIVE_BUTTON_TEXT, positiveButtonText)
                putString(ARG_NEGATIVE_BUTTON_TEXT, negativeButtonText)
                putString(ARG_NEUTRAL_BUTTON_TEXT, neutralButtonText)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = requireArguments().getString(ARG_TITLE, "")
        val message = requireArguments().getString(ARG_MESSAGE, "")

        val inflater = requireActivity().layoutInflater
        val customView = inflater.inflate(R.layout.dialog_layout_custom, null)

        customView.findViewById<TextView>(R.id.textViewTitle).text = title
        customView.findViewById<TextView>(R.id.textViewMessage).text = message


        val builder = AlertDialog.Builder(requireContext())
            .setView(customView)
            .setPositiveButton(
                requireArguments().getString(ARG_POSITIVE_BUTTON_TEXT)
            ) { _, _ -> positiveActionListener?.invoke() }
            .setNegativeButton(
                requireArguments().getString(ARG_NEGATIVE_BUTTON_TEXT)
            ) { _, _ -> negativeActionListener?.invoke() }

        requireArguments().getString(ARG_NEUTRAL_BUTTON_TEXT)?.let {
            builder.setNeutralButton(it) { _, _ -> neutralActionListener?.invoke() }
        }

        val alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false) // Prevent dismissing by touching outside the dialog
        alertDialog.setCancelable(false) // Prevent dismissing by pressing the back button

        return alertDialog
    }

//    private fun positiveButtonClickListener(): DialogInterface.OnClickListener =
//        DialogInterface.OnClickListener { _, _ -> }
//
//    private fun negativeButtonClickListener(): DialogInterface.OnClickListener =
//        DialogInterface.OnClickListener { _, _ -> }
//
//    private fun neutralButtonClickListener(): DialogInterface.OnClickListener =
//        DialogInterface.OnClickListener { _, _ -> }

    private var positiveActionListener: (() -> Unit)? = null

    private var negativeActionListener: (() -> Unit)? = null

    private var neutralActionListener: (() -> Unit)? = null

    fun setPositiveActionListener(listener: () -> Unit) {
        positiveActionListener = listener
    }

    fun setNegativeActionListener(listener: () -> Unit) {
        negativeActionListener = listener
    }

    fun setNeutralActionListener(listener: () -> Unit) {
        neutralActionListener = listener
    }
}