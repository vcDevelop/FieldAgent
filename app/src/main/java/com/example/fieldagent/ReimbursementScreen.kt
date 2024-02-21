package com.example.fieldagent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow

class ReimbursementScreen : AppCompatActivity() {
    private lateinit var tableLayout: TableLayout
    private var rowCount = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reimbursement_screen)

        tableLayout = findViewById(R.id.tableLayout)
        addRow(findViewById(R.id.addRowButton))
    }

    fun addRow(view: android.view.View) {
        // Create a new row
        val tableRow = TableRow(this)
        tableRow.layoutParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.WRAP_CONTENT
        )
        for (col in 0 until 4) { // Adjust the number of columns as needed
            val editText = EditText(this)
            editText.hint = "Enter data"
            editText.gravity = Gravity.CENTER
            val params = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 8, 8, 8)
            editText.layoutParams = params
            tableRow.addView(editText)
        }
        tableLayout.addView(tableRow, rowCount++)
    }
}
