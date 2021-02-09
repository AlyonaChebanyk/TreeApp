package com.example.treeapp.ui.test_page.test_result
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.treeapp.R

class TestResultAdapter(private val data: MutableList<TestResultItem>): RecyclerView.Adapter<TestResultViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestResultViewHolder {
        return TestResultViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.test_result_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TestResultViewHolder, position: Int) {
        holder.bind(data[position], position)
    }

    override fun getItemCount(): Int = data.size

}