package com.dilan.kamuda.houseownerapp.common.util.component

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.DialogFragment
import com.dilan.kamuda.houseownerapp.R

class ResponseHandlingDialogFragment : DialogFragment() {
    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_MESSAGE = "message"
        private const val ARG_POSITIVE_BUTTON_TEXT = "positive_button_text"
        private const val ARG_NEGATIVE_BUTTON_TEXT = "negative_button_text"
        private const val ARG_NEUTRAL_BUTTON_TEXT = "neutral_button_text"
        private const val ARG_TYPE = "type"

        fun newInstance(
            title: String,
            message: String,
            positiveButtonText: String = "OK",
            negativeButtonText: String? = null,
            neutralButtonText: String? = null,
            type: Int,
        ): ResponseHandlingDialogFragment {
            val fragment = ResponseHandlingDialogFragment()
            val args = Bundle().apply {
                putString(ARG_TITLE, title)
                putString(ARG_MESSAGE, message)
                putString(ARG_POSITIVE_BUTTON_TEXT, positiveButtonText)
                putString(ARG_NEGATIVE_BUTTON_TEXT, negativeButtonText)
                putString(ARG_NEUTRAL_BUTTON_TEXT, neutralButtonText)
                putInt(ARG_TYPE, type)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = requireArguments().getString(ARG_TITLE, "")
        val message = requireArguments().getString(ARG_MESSAGE, "")
        val type = requireArguments().getInt(ARG_TYPE, 0)

        val inflater = requireActivity().layoutInflater
        val customView = inflater.inflate(R.layout.dialog_layout_response_handling, null)

        when (type) {

            0 -> {
                customView.findViewById<ImageView>(R.id.ivLogo)
                    .setImageDrawable(getDrawable(requireContext(), R.drawable.ic_info_24dp))
            }

            1 -> {
                customView.findViewById<ImageView>(R.id.ivLogo).setImageDrawable(
                    getDrawable(
                        requireContext(),
                        R.drawable.ic_check_circle_green_24dp
                    )
                )
            }

            2 -> {
                customView.findViewById<ImageView>(R.id.ivLogo)
                    .setImageDrawable(getDrawable(requireContext(), R.drawable.ic_cancel_24dp))
            }
        }
        customView.findViewById<TextView>(R.id.tvTitle).text = title
        customView.findViewById<TextView>(R.id.tvMessage).text = message

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