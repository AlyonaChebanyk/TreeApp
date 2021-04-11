package com.example.treeapp.ui.test_page.test_passing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.bumptech.glide.Glide
import com.example.treeapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_test.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class TestFragment : MvpAppCompatFragment(), TestView {

    @InjectPresenter
    lateinit var presenter: TestPresenter

    @ProvidePresenter
    fun provideTestPresenter() = get<TestPresenter>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        habitTextView.setOnClickListener {
            Glide.with(view).load(presenter.currentHabitImage).into(plantImageView)
        }
        leafTextView.setOnClickListener {
            if (presenter.currentLeafImage.isNotEmpty())
                Glide.with(view).load(presenter.currentLeafImage).into(plantImageView)
        }
        answer1Button.setOnClickListener {
            GlobalScope.launch {
                presenter.nextQuestion(answer1Button.text.toString())
            }
        }
        answer2Button.setOnClickListener {
            GlobalScope.launch {
                presenter.nextQuestion(answer2Button.text.toString())
            }
        }
        answer3Button.setOnClickListener {
            GlobalScope.launch {
                presenter.nextQuestion(answer3Button.text.toString())
            }
        }
        answer4Button.setOnClickListener {
            GlobalScope.launch {
                presenter.nextQuestion(answer4Button.text.toString())
            }
        }
    }

    override fun displayData(imageUrl: String?, variantsList: List<String>, questionNumber: Int) {
        Glide.with(this).load(imageUrl).into(plantImageView)
        questionNumberTextView.text = questionNumber.toString()
        answer1Button.text = variantsList[0]
        answer2Button.text = variantsList[1]
        answer3Button.text = variantsList[2]
        answer4Button.text = variantsList[3]
    }

    override fun goToResultScreen(bundle: Bundle) {
        findNavController().navigate(R.id.action_testFragment_to_testResultFragment, bundle)
    }
}