package com.example.cardstackview

import android.content.Context
import android.content.res.Resources
import android.graphics.BlurMaskFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.cardstackview.cardstack.CardStackView
import com.example.cardstackview.cardstack.StackAdapter
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import com.example.cardstackview.databinding.ListCardItemBinding
import kotlin.math.roundToInt

class TestStackAdapter(context: Context) : StackAdapter<Int>(context) {
    override fun onCreateView(parent: ViewGroup, viewType: Int): CardStackView.ViewHolder {
        return ColorItemViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_card_item, parent, false
        ))
        //val view: View
        /*return when (viewType) {
            R.layout.list_card_item_larger_header -> {
                view = layoutInflater.inflate(R.layout.list_card_item_larger_header, parent, false)
                ColorItemLargeHeaderViewHolder(view);
            }
            R.layout.list_card_item_with_no_header -> {
                view = layoutInflater.inflate(R.layout.list_card_item_with_no_header, parent, false)
                ColorItemWithNoHeaderViewHolder(view);
            }
            else -> {
                *//*view = layoutInflater.inflate(R.layout.list_card_item, parent, false)
                ColorItemViewHolder(view);*//*
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.list_card_item, parent, false
                )
            }
        }*/
    }

    override fun bindView(
        dataList: MutableList<Int>,
        data: Int,
        position: Int,
        holder: CardStackView.ViewHolder
    ) {
        if (holder is ColorItemLargeHeaderViewHolder) {
            val h: ColorItemLargeHeaderViewHolder =
                holder as ColorItemLargeHeaderViewHolder
            h.onBind(data, position)
        }
        if (holder is ColorItemWithNoHeaderViewHolder) {
            val h: ColorItemWithNoHeaderViewHolder =
                holder as ColorItemWithNoHeaderViewHolder
            h.onBind(data, position)
        }
        if (holder is ColorItemViewHolder) {
            val h: ColorItemViewHolder =
                holder as ColorItemViewHolder
            h.onBind(dataList, data, position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.list_card_item
        /*return if (position == 6) { //TODO TEST LARGER ITEM
            R.layout.list_card_item_larger_header
        } else if (position == 10) {
            R.layout.list_card_item_with_no_header
        } else {
            R.layout.list_card_item
        }*/
    }

    internal class ColorItemViewHolder(private val binding: ListCardItemBinding) : CardStackView.ViewHolder(binding.root) {
        val mainHandler = Handler(Looper.getMainLooper())
        var secondsLeft = 10
        var timerProgress = 0

        fun minusOneSecond() {
            if (secondsLeft > 0) {
                secondsLeft -= 1
                timerProgress += 1
                binding.progressBar.progress = timerProgress * 10
                binding.timerTextview.text = secondsLeft.toString()
            } else {
                //show try again button and remove progressbar
                binding.progressBar.visibility = View.GONE
                binding.timerTextview.visibility = View.GONE
                binding.openDoorButton.text = "TAP TO TRY AGAIN"
                binding.openDoorButton.visibility = View.VISIBLE
                mainHandler.removeCallbacksAndMessages(null)
                secondsLeft = 10
                timerProgress = 0
            }
        }

        override fun onItemExpand(b: Boolean) {
            binding.containerListContent.visibility = if (b) View.VISIBLE else View.GONE
            if (binding.containerListContent.visibility == View.VISIBLE) {
                binding.openDoorButton.text = "TAP TO UNLOCK DOOR"
                binding.openDoorButton.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                binding.timerTextview.visibility = View.GONE
            } else {
                mainHandler.removeCallbacksAndMessages(null)
                secondsLeft = 10
                timerProgress = 0
            }
        }

        fun onBind(dataList: MutableList<Int>, data: Int, position: Int) {
            with(binding) {
                clickListener = createOnClickListener()
                binding.cardImage.setImageResource(data)
                binding.textListCardTitle.text = position.toString()
            }

        }

        private fun createOnClickListener(): View.OnClickListener {
            return View.OnClickListener {
                when (it.id) {
                    R.id.openDoorButton -> {
                        //send door open command, hide door open button and show progressbar
                        binding.openDoorButton.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                        binding.timerTextview.visibility = View.VISIBLE
                        //
                        secondsLeft = 10
                        timerProgress = 0
                        binding.progressBar.progress = 0
                        binding.timerTextview.text = "10"
                        mainHandler.removeCallbacksAndMessages(null)
                        mainHandler.post(object : Runnable {
                            override fun run() {
                                minusOneSecond()
                                mainHandler.postDelayed(this, 1000)
                            }
                        })
                    }
                }
            }
        }
    }

    internal class ColorItemWithNoHeaderViewHolder(view: View) :
        CardStackView.ViewHolder(view) {
        var mLayout: View
        var mTextTitle: TextView

        init {
            mLayout = view.findViewById(R.id.frame_list_card_item)
            mTextTitle = view.findViewById<View>(R.id.text_list_card_title) as TextView
        }

        override fun onItemExpand(b: Boolean) {}
        fun onBind(data: Int?, position: Int) {
            mLayout.background.setColorFilter(
                ContextCompat.getColor(context, data!!),
                PorterDuff.Mode.SRC_IN
            )
            mTextTitle.text = position.toString()
        }
    }

    internal class ColorItemLargeHeaderViewHolder(view: View) :
        CardStackView.ViewHolder(view) {
        var mLayout: View
        var mContainerContent: View
        var mTextTitle: TextView

        init {
            mLayout = view.findViewById(R.id.frame_list_card_item)
            mContainerContent = view.findViewById(R.id.container_list_content)
            mTextTitle = view.findViewById<View>(R.id.text_list_card_title) as TextView
        }

        override fun onItemExpand(b: Boolean) {
            mContainerContent.visibility = if (b) View.VISIBLE else View.GONE
        }

        override fun onAnimationStateChange(state: Int, willBeSelect: Boolean) {
            super.onAnimationStateChange(state, willBeSelect)
            if (state == CardStackView.ANIMATION_STATE_START && willBeSelect) {
                onItemExpand(true)
            }
            if (state == CardStackView.ANIMATION_STATE_END && !willBeSelect) {
                onItemExpand(false)
            }
        }

        fun onBind(data: Int, position: Int) {
            mLayout.background.setColorFilter(
                ContextCompat.getColor(context, data),
                PorterDuff.Mode.SRC_IN
            )
            mTextTitle.text = position.toString()
            itemView.findViewById<TextView>(R.id.text_view)
                .setOnClickListener(View.OnClickListener {
                    (itemView.parent as CardStackView).performItemClick(this@ColorItemLargeHeaderViewHolder)
                })
        }
    }


}