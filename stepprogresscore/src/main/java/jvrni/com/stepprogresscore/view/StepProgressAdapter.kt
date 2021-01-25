package jvrni.com.stepprogresscore.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import jvrni.com.stepprogresscore.R
import jvrni.com.stepprogresscore.model.StepProgressModel
import jvrni.com.stepprogresscore.model.TypeStepProgress
import jvrni.com.stepprogresscore.model.ViewType

class StepProgressAdapter(
    private val context: Context,
    private val list: List<StepProgressModel>,
    private val viewType: ViewType
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class HolderBig(view: View) : RecyclerView.ViewHolder(view) {
        val step: CardView = view.findViewById(R.id.step_item)
    }

    class HolderLittle(view: View) : RecyclerView.ViewHolder(view) {
        val step: CardView = view.findViewById(R.id.step_item)
    }

    override fun getItemViewType(position: Int): Int =
        when (list[position].typeStepProgress) {
            TypeStepProgress.BIG -> VIEW_TYPE_BIG
            else -> VIEW_TYPE_LITTLE
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            VIEW_TYPE_BIG -> HolderBig(
                LayoutInflater.from(context).inflate(
                    when (this@StepProgressAdapter.viewType) {
                        ViewType.LINE -> R.layout.item_step_progress_line_big
                        else -> R.layout.item_step_progress_rounded_big
                    }, parent, false
                )
            )
            else -> HolderLittle(
                LayoutInflater.from(context).inflate(
                    when (this@StepProgressAdapter.viewType) {
                        ViewType.LINE -> R.layout.item_step_progress_line_little
                        else -> R.layout.item_step_progress_rounded_little
                    }, parent, false
                )
            )
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_BIG -> viewBig(holder as HolderBig, position)
            else -> viewLittle(holder as HolderLittle, position)
        }
    }

    override fun getItemCount(): Int = list.size

    private fun viewBig(holder: HolderBig, position: Int) {
        val item = list[position]
        holder.step.backgroundTintList = ContextCompat.getColorStateList(context, item.colorRes)
    }

    private fun viewLittle(holder: HolderLittle, position: Int) {
        val item = list[position]
        holder.step.backgroundTintList = ContextCompat.getColorStateList(context, item.colorRes)
    }

    companion object {
        private const val VIEW_TYPE_BIG = 0
        private const val VIEW_TYPE_LITTLE = 1
    }
}