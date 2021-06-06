package com.technorapper.hiltsample.utils

import android.app.Activity
import android.app.Dialog
import android.content.ContextWrapper
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.technorapper.hiltsample.R
import com.technorapper.hiltsample.data.model.RequestPost
import com.technorapper.hiltsample.databinding.NewPostViewBinding
import java.util.*

class BottomUpDialogs {
    companion object {
        var dialog: BottomSheetDialog? = null


        fun showAddDialog(
            context: Activity?,
            callback: (Any) -> Unit,
        ): Dialog? {
            if (dialog != null && dialog!!.isShowing) dismissWithCheck(dialog)
            dialog = BottomSheetDialog(context!!)
            val binding: NewPostViewBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.new_post_view,
                null,
                false
            )
            dialog!!.setContentView(binding.getRoot())
            dialog!!.setCancelable(true)
            dialog!!.setCanceledOnTouchOutside(true)
            dialog!!.window!!.attributes.windowAnimations = R.style.DialogAnimationBottom
            dialog!!.window!!.setGravity(Gravity.BOTTOM)
            dialog!!.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            binding.saveBtn.setOnClickListener {
                dismissWithCheck(dialog)
                callback.invoke(
                    RequestPost(
                        binding.title.text.toString(),
                        binding.description.text.toString(),
                        binding.uri.text.toString(),
                        if (binding.uri.text.isBlank()) "Text" else "video"
                    )
                )
            }
            Objects.requireNonNull(dialog!!.window)!!
                .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            try {
                dialog!!.show()
            } catch (e: Exception) {
                //LogUtils.ERRORLOG(e.message)
            }
            return dialog
        }


        private fun dismissWithCheck(dialog: Dialog?) {
            var dialog = dialog
            if (dialog != null) {
                if (dialog.isShowing) {

                    //get the Context object that was used to great the dialog
                    val context = (dialog.context as ContextWrapper).baseContext

                    // if the Context used here was an activity AND it hasn't been finished or destroyed
                    // then dismiss it
                    if (context is Activity) {

                        // Api >=17
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            if (!context.isFinishing && !context.isDestroyed) {
                                dialog.dismiss()
                            }
                        } else {

                            // Api < 17. Unfortunately cannot check for isDestroyed()
                            if (!context.isFinishing) {
                                dialog.dismiss()
                            }
                        }
                    } else  // if the Context used wasn't an Activity, then dismiss it too
                        dialog.dismiss()
                }
                dialog = null
            }
        }

    }


}