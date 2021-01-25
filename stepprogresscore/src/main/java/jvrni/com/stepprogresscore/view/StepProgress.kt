package jvrni.com.stepprogresscore.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import jvrni.com.stepprogresscore.R
import jvrni.com.stepprogresscore.model.StepProgressModel
import jvrni.com.stepprogresscore.model.TypeStepProgress
import jvrni.com.stepprogresscore.model.ViewType
import kotlinx.android.synthetic.main.step_progress.view.*

class StepProgress : FrameLayout {

    constructor(context: Context) : super(context) {
        setupView()
    }

    constructor(
        context: Context,
        attrs: AttributeSet
    ) : super(context, attrs) {
        setupView(attrs)
    }

    private val list by lazy {
        mutableListOf<StepProgressModel>().apply {
            for (position in FIRST..this@StepProgress.itemCount) {
                when (position) {
                    FIRST -> add(StepProgressModel(TypeStepProgress.BIG, colorSelected))
                    else -> add(StepProgressModel(TypeStepProgress.LITTLE, colorUnSelected))
                }
            }
        }
    }

    private var itemCount: Int = VALUE_ZERO

    private var spanCount: Int = VALUE_ONE

    private var colorSelected: Int = R.color.default_selected

    private var colorUnSelected: Int = R.color.default_un_selected

    private var viewType: Int = DEFAULT_VIEW_TYPE
        set(value) {
            field = value
            setAdapter()
        }

    private fun setupView(attrs: AttributeSet? = null) {
        View.inflate(context, R.layout.step_progress, this)
        attrs?.let { attributeSet ->
            context.obtainStyledAttributes(
                attributeSet,
                R.styleable.StepProgress,0,0
            ).run {
                itemCount = getInt(R.styleable.StepProgress_item_count, 0)
                spanCount = getInt(R.styleable.StepProgress_span_count, 0)
                colorSelected = getResourceId(R.styleable.StepProgress_color_selected, R.color.default_selected)
                colorUnSelected = getResourceId(R.styleable.StepProgress_color_un_selected, R.color.default_un_selected)
                viewType = getInteger(R.styleable.StepProgress_view_type, 0)
                recycle()
            }
        }
    }

    private fun setAdapter() {
        rvStepProgress.layoutManager =
            GridLayoutManager(context, spanCount)
        rvStepProgress.adapter = StepProgressAdapter(context, list, viewType.mapper())
    }

    fun next() {
        val positionSelected = list.indexOf(list.first { stepProgressModel -> stepProgressModel.typeStepProgress == TypeStepProgress.BIG })
        val listSaved = mutableListOf<StepProgressModel>().apply { addAll(list) }
        var color = colorSelected

        list.clear()
        list.addAll(
            when (positionSelected) {
                listSaved.size.minus(VALUE_ONE) -> listSaved
                else -> listSaved.mapIndexed { index, _ ->
                    when {
                        index.minus(positionSelected) == VALUE_ONE -> {
                            color = colorUnSelected
                            StepProgressModel(TypeStepProgress.BIG, colorSelected)
                        }
                        else -> StepProgressModel(TypeStepProgress.LITTLE, color)
                    }
                }
            }
        )

        rvStepProgress.adapter = StepProgressAdapter(context, list, viewType.mapper())
        (rvStepProgress.adapter as StepProgressAdapter).notifyDataSetChanged()
    }

    fun back() {
        val positionSelected = list.indexOf(list.first { stepProgressModel -> stepProgressModel.typeStepProgress == TypeStepProgress.BIG })
        val listSaved = mutableListOf<StepProgressModel>().apply { addAll(list) }
        var color = colorSelected

        list.clear()
        list.addAll(
            when (positionSelected) {
                VALUE_ZERO -> listSaved
                else -> listSaved.mapIndexed { index, _ ->
                    when(index) {
                        positionSelected - VALUE_ONE -> {
                            color = colorUnSelected
                            StepProgressModel(TypeStepProgress.BIG, colorSelected)
                        }
                        else -> StepProgressModel(TypeStepProgress.LITTLE, color)
                    }
                }
            }
        )

        rvStepProgress.adapter = StepProgressAdapter(context, list, viewType.mapper())
        (rvStepProgress.adapter as StepProgressAdapter).notifyDataSetChanged()
    }

    private fun Int.mapper(): ViewType =
        when (this) {
            VIEW_TYPE_LINE -> ViewType.LINE
            else -> ViewType.ROUNDED
        }

    companion object {
        private const val DEFAULT_VIEW_TYPE = 0
        private const val VIEW_TYPE_LINE = 0
        private const val VIEW_TYPE_ROUNDED = 1
        private const val FIRST = 1
        private const val VALUE_ZERO = 0
        private const val VALUE_ONE = 1
    }
}