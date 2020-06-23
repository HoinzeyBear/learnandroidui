package com.example.learnandroidui.animation

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.learnandroidui.R
import kotlinx.android.synthetic.main.activity_animation.*

class AnimationActivity : AppCompatActivity(), Animator.AnimatorListener {

    var rotateAnimator: Animator? = null
    var scaleAnimator: Animator? = null
    var translateAnimator: Animator? = null
    var fadeAnimator: ObjectAnimator? = null // Object Animator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation)
    }

    fun rotateAnimation(view: View) {
        rotateAnimator = AnimatorInflater.loadAnimator(this, R.animator.rotate)
        rotateAnimator?.apply {
            setTarget(targetImage)
            addListener(this@AnimationActivity)
            start()
        }
    }

    fun scaleAnimation(view: View) {
        scaleAnimator = AnimatorInflater.loadAnimator(this, R.animator.scale)
        scaleAnimator?.apply {
            setTarget(targetImage)
            addListener(this@AnimationActivity)
            start()
        }
    }

    fun translateAnimation(view: View) {
        translateAnimator = AnimatorInflater.loadAnimator(this, R.animator.translate)
        translateAnimator?.apply {
            setTarget(targetImage)
            addListener(this@AnimationActivity)
            start()
        }
    }

    fun fadeAnimation(view: View) {
        fadeAnimator = ObjectAnimator.ofFloat(targetImage,"alpha",1.0f,0.0f)
        fadeAnimator?.apply {
            duration = 1500
            repeatCount = 1
            repeatMode = ValueAnimator.REVERSE
            addListener(this@AnimationActivity)
            start()
        }
    }

    override fun onAnimationRepeat(animation: Animator?) {
        Toast.makeText(this,"Animation Repeated", Toast.LENGTH_SHORT).show()
    }

    override fun onAnimationEnd(animation: Animator?) {
        Toast.makeText(this,"Animation Ended", Toast.LENGTH_SHORT).show()
    }

    override fun onAnimationCancel(animation: Animator?) {
        Toast.makeText(this,"Animation Cancelled", Toast.LENGTH_SHORT).show()
    }

    override fun onAnimationStart(animation: Animator?) {
        if(animation == rotateAnimator) {
            Toast.makeText(this,"Rotate Animation Started", Toast.LENGTH_SHORT).show()
        }
    }

    fun setFromXML(view: View) {
        val animator = AnimatorInflater.loadAnimator(this, R.animator.set)
        animator.apply {
            setTarget(targetImage)
            start()
        }
    }

    fun setFromCode(view: View) {

    }

    companion object {
        const val TAG = "animation_activity"
    }

}
