package com.example.treeapp.ui.test_page.test_result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.treeapp.R
import kotlinx.android.synthetic.main.fragment_test_result.*
import org.koin.android.ext.android.get

class TestResultFragment : MvpAppCompatFragment(), TestResultView {

    @InjectPresenter
    lateinit var presenter: TestResultPresenter

    @ProvidePresenter
    fun provideTestResultPresenter() = get<TestResultPresenter>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val habitImages = requireArguments().getSerializable("habitImages") as HashMap<Int, String>
        val leafImages = requireArguments().getSerializable("leafImages") as HashMap<Int, String>
        val correctAnswers = requireArguments().getSerializable("correctAnswers") as HashMap<Int, String>
        val wrongAnswers = requireArguments().getSerializable("wrongAnswers") as HashMap<Int, String>

        presenter.setData(habitImages, leafImages, correctAnswers, wrongAnswers)

        passTestAgainButton.setOnClickListener {
            findNavController().navigate(R.id.action_testResultFragment_to_testFragment)
        }
        backToMainPageButton.setOnClickListener {
            findNavController().navigate(R.id.action_testResultFragment_to_mainPageFragment)
        }

    }

    override fun setAdapter(testResultAdapter: TestResultAdapter) {
        testResultRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = testResultAdapter
        }
    }

    override fun displayNumberOfCorrectAnswers(correctAnswers: Int) {
        resultTextView.text = correctAnswers.toString()
    }
}