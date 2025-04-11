package com.example.financetracker.view.dashboard_UI

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.financetracker.model.repositories.FinanceRepository
import com.example.financetracker.R
import com.example.financetracker.view.viewmodel.BalanceViewModel
import com.example.financetracker.view.viewModelFactory.FinanceViewModelFactory
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate


/**
 * A simple [Fragment] subclass.
 * Use the [BalanceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class BalanceFragment : Fragment() {

    private lateinit var viewModel: BalanceViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_balance, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // important declarations
        val repository = FinanceRepository()
        // viewModel
        val viewModel = ViewModelProvider(
            this,
            FinanceViewModelFactory(repository)
        ).get(BalanceViewModel::class.java)
        // observe the viewModel for the balance updates
        viewModel.balance.observe(viewLifecycleOwner) { balance ->
            view.findViewById<TextView>(R.id.currentBalance).text = "₹ " + balance
            // color of the balance
            if (balance != null) {
                if(balance.toDouble() < 0){
                    view.findViewById<TextView>(R.id.currentBalance).setTextColor(Color.parseColor("#E53935"))
                } else {
                    view.findViewById<TextView>(R.id.currentBalance).setTextColor(Color.parseColor("#4CAF50"))
                }
            }
        }



        /* next part of the application*/

        // for pie chart
        val pieChart = view.findViewById<PieChart>(R.id.pieChart)
        // setup pie chart
        setupPieChart(pieChart)


        // recycler view to display the list of transactions
        // inflate the recycler view
        val transactionRecyclerView = view.findViewById<RecyclerView>(R.id.transactionRecyclerView)
        transactionRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        // data for the transaction history
        val transactionHistory = listOf(
            Transaction("Groceries", 40.0, "2023-08-01", "Expense"),
            Transaction("Groceries", 140.0, "2023-08-01", "Expense"),
            Transaction("Rent", 1200.0, "2023-08-05", "Expense"),
            Transaction("Utilities", 100.0, "2023-08-10", "Expense"),
            Transaction("Utilities", 10.0, "2023-08-10", "Expense"),
            Transaction("Utilities", 500.0, "2023-08-10", "Expense"),

            )

        val adapter = TransactionAdapter(transactionHistory)
        transactionRecyclerView.adapter = adapter

        val view1 = view

        /* other part of the application */
        // for adding the expense to the database when add expense button is clicked
        val addExpenseButton = view1.findViewById<Button>(R.id.addExpenseButton)
        addExpenseButton.setOnClickListener {
            // open the bottomsheetdialog
            val bottomSheet = FragmentAddExpenseBottomSheet()
            bottomSheet.show(parentFragmentManager, "BottomSheetDialog")
        }


    }


    private fun setupPieChart(pieChart: PieChart) {
        // Sample Data (Replace with actual finance data)
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(40f, "Groceries"))
        entries.add(PieEntry(25f, "Rent"))
        entries.add(PieEntry(15f, "Entertainment"))
        entries.add(PieEntry(10f, "Utilities"))
        entries.add(PieEntry(10f, "Savings"))

        // Dataset
        val dataSet = PieDataSet(entries, "Expenses")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 14f

        // Data
        val data = PieData(dataSet)

        // Configure PieChart
        pieChart.data = data
        pieChart.description.isEnabled = false
        pieChart.setUsePercentValues(true)
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.TRANSPARENT)
        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.animateY(1000)

        // highlight the saving
        val highlightIndex = 4 // "Others" is at index 4
        pieChart.highlightValue(highlightIndex.toFloat(), 0)


        // Refresh the chart
        pieChart.invalidate()

    }


    // data class to represent a transaction
    data class Transaction(
        val title: String,
        val amount: Double,
        val date: String,
        val type: String // "Income" or "Expense"
    )
}