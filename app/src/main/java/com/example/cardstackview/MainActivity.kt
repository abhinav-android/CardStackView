package com.example.cardstackview

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.cardstackview.cardstack.AllMoveDownAnimatorAdapter
import com.example.cardstackview.cardstack.CardStackView
import com.example.cardstackview.cardstack.UpDownAnimatorAdapter
import com.example.cardstackview.cardstack.UpDownStackAnimatorAdapter

class MainActivity : AppCompatActivity(), CardStackView.ItemExpendListener {

    var TEST_DATAS = arrayOf<Int>(
        R.drawable.grey_card,
        R.drawable.black_card,
        R.drawable.dark_grey_card,
        R.drawable.darker_grey_card,
        R.drawable.plum_card,
        R.drawable.dark_plum_card
    )

    private lateinit var mStackView: CardStackView
    private lateinit var mActionButtonContainer: LinearLayout
    private lateinit var mTestStackAdapter: TestStackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mStackView = findViewById(R.id.stackview_main)
        //mActionButtonContainer = findViewById(R.id.button_container)
        mStackView.setItemExpendListener(this)
        mTestStackAdapter = TestStackAdapter(this)
        mStackView.setAdapter(mTestStackAdapter)

        Handler(Looper.getMainLooper()).postDelayed({
            mTestStackAdapter.updateData(TEST_DATAS.toList())
        }, 200)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_actions, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_all_down -> mStackView.setAnimatorAdapter(
                AllMoveDownAnimatorAdapter(
                    mStackView
                )
            )
            R.id.menu_up_down -> mStackView.setAnimatorAdapter(UpDownAnimatorAdapter(mStackView))
            R.id.menu_up_down_stack -> mStackView.setAnimatorAdapter(
                UpDownStackAnimatorAdapter(
                    mStackView
                )
            )
        }
        return super.onOptionsItemSelected(item)
    }

    fun onPreClick(view: View?) {
        mStackView.pre()
    }

    fun onNextClick(view: View?) {
        mStackView.next()
    }

    override fun onItemExpend(expend: Boolean) {
        //mActionButtonContainer.setVisibility(if (expend) View.VISIBLE else View.GONE)
    }
}