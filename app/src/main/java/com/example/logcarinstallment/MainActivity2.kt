package com.example.logcarinstallment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.*
import kotlinx.android.synthetic.main.activity_main2.*
import java.text.NumberFormat

class MainActivity2 : AppCompatActivity() {

    private lateinit var  mEditPrice: EditText
    private lateinit var mEditDown: EditText
    private lateinit var mRadioGroup: RadioGroup
    private lateinit var mEditRate: EditText
    private lateinit var mSpinner: Spinner
    private lateinit var mTextResult: TextView
    private lateinit var mReset: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        mEditPrice = findViewById(R.id.editText_price)
        mEditDown = findViewById(R.id.editText_down)
        mEditRate = findViewById(R.id.editText_rate)
        mRadioGroup = findViewById(R.id.radioGroup)
        mRadioGroup.check(R.id.radio_percent)
        mReset = findViewById(R.id.button_reset)

        mSpinner = findViewById<Spinner>(R.id.spinner)
        val items = MutableList(6){"${it + 1} ปี"}
        items.add(0,"ระยะเวลาการส่งงวด")
        val adapter = ArrayAdapter(baseContext, android.R.layout.simple_spinner_dropdown_item, items)

        mSpinner.adapter = adapter

        findViewById<Button>(R.id.button).apply {setOnClickListener{calculate()}}

        mTextResult = findViewById(R.id.textView_result)

        button_reset.setOnClickListener {
            editText_price.setText("")
            editText_down.setText("")
            editText_rate.setText("")
            textView_result.setText("")

        }

    }

    private fun calculate() {
        val price = mEditPrice.text.trim().toString().toInt()
        if (price <= 0){
            toast("ราคาไม่ถูกต้อง")
            return
        }
        var down = mEditDown.text.trim().toString().toDouble()
        if (radioGroup.checkedRadioButtonId == R.id.radio_percent){
            down = price * (down/100)
        }
        if (down <= 0 || down > price){
            toast("เงินดาวน์ไม่ถูกต้อง ($down)")
            return
        }
        var rate = mEditRate.text.trim().toString().toDouble()
        rate /= 100
        rate /= 12
        if (rate <= 0){
            toast("อัตราดอกเบี้ยไม่ถูกต้อง")
            return
        }
        val finance = price - down
        if (mSpinner.selectedItemPosition == 0){
            toast("กรุณาเลือกระยะเวลาการส่งงวด")
            return
        }
        val months = mSpinner.selectedItemPosition * 12

        val interest = finance * months.toDouble() * rate

        val total = finance + interest

        val installment = total / months

        val format = NumberFormat.getNumberInstance()
        format.maximumFractionDigits = 2
        val result = format.format(installment)
        mTextResult.text = "ส่งงวดเดือนละ $result บาท"
        mEditPrice.onEditorAction(EditorInfo.IME_ACTION_DONE)
    }
    private fun toast(msg: String){
        val t = Toast.makeText(this, msg, Toast.LENGTH_LONG)
        t.show()

    }




}

