package com.example.querizz_app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuizModel (
    val question: String,
    val aAnswer: String,
    val bAnswer: String,
    val cAnswer: String,
    val dAnswer: String,
    val correctAnswer: String
    ) : Parcelable