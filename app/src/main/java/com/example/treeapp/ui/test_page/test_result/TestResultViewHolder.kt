package com.example.treeapp.ui.test_page.test_result

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.test_result_item.view.*

class TestResultViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    fun bind(item: TestResultItem, position: Int){
        with(view){
            questionNumberTextView.text = (position + 1).toString()
            if (item.habitImageUrl != "")
                Picasso.get().load(item.habitImageUrl).into(habitTestResultItemImageView)
            if (item.leafImageUrl != "")
                Picasso.get().load(item.leafImageUrl).into(leafTestResultItemImageView)
            if (item.wrongAnswer.isNullOrEmpty())
                incorrectAnswerLayout.visibility = View.GONE
            else
                wrongAnswerTextView.text = item.wrongAnswer
            correctAnswerTextView.text = item.correctAnswer
        }
    }
}