package com.pureblacksoft.gradinfo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pureblacksoft.gradinfo.NavMainDirections
import com.pureblacksoft.gradinfo.data.Grad
import com.pureblacksoft.gradinfo.databinding.CardGradBinding
import com.pureblacksoft.gradinfo.dialog.CriteriaDialog
import com.pureblacksoft.gradinfo.fragment.GradFragment
import java.util.*

class GradAdapter(private val gradList: MutableList<Grad>) : RecyclerView.Adapter<GradAdapter.ViewHolder>(), Filterable
{
    private lateinit var context: Context
    private var filteredGradList = gradList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        val layoutInflater = LayoutInflater.from(context)
        val binding = CardGradBinding.inflate(layoutInflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val grad = filteredGradList[position]
        holder.bind(grad)
    }

    override fun getItemCount(): Int = filteredGradList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val resultList = mutableListOf<Grad>()
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    resultList.clear()
                } else {
                    for (grad in gradList) {
                        if (CriteriaDialog.currentCriteriaId == 1) {
                            if (grad.number.toString().contains(charSearch)) {
                                resultList.add(grad)
                            }
                        } else {
                            if (grad.name.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                                resultList.add(grad)
                            }
                        }
                    }
                }
                filteredGradList = resultList

                val filterResults = FilterResults()
                filterResults.values = filteredGradList

                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredGradList = results?.values as MutableList<Grad>
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(private val binding: CardGradBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(grad: Grad) {
            binding.txtNumberGC.text = grad.number.toString()
            binding.txtNameGC.text = grad.name
            binding.txtDegreeGC.text = grad.degree
            binding.txtYearGC.text = grad.year
            Glide.with(context).load(grad.image).into(binding.imgGradGC)

            binding.root.setOnClickListener {
                GradFragment.accessedGrad = filteredGradList[adapterPosition]

                val action = NavMainDirections.actionGlobalGradFragment()
                it.findNavController().navigate(action)
            }
        }
    }
}

//PureBlack Software / Murat BIYIK