package net.flow9.androidmvvm.activity.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import net.flow9.androidmvvm.R
import net.flow9.androidmvvm.activity.base.DiffCallback
import net.flow9.androidmvvm.databinding.DialogFindSchoolBinding
import net.flow9.androidmvvm.databinding.ItemFindSchoolBinding
import net.flow9.androidmvvm.repository.model.response.School
import net.flow9.androidmvvm.repository.model.response.SchoolResponse
import net.flow9.androidmvvm.viewmodel.FindSchoolViewModel

class FindSchoolDialog(val callback: (school: School?) -> Unit): DialogFragment() {

    private val viewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FindSchoolViewModel::class.java)
    }
    private val binding: DialogFindSchoolBinding by lazy {
        DataBindingUtil.inflate<DialogFindSchoolBinding>(LayoutInflater.from(context), R.layout.dialog_find_school, null, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onItemClickCallback = {
            this.dismiss()
            this.callback(it)
        }

        binding.apply {
            lifecycleOwner = this@FindSchoolDialog
            vm = viewModel
            recyclerSchool.adapter = FindSchoolAdapter(viewModel)
            btnClose.setOnClickListener {
                dismiss()
            }
        }
    }
}

class FindSchoolAdapter(private val viewModel: FindSchoolViewModel): ListAdapter<School, FindSchoolAdapter.Holder>(
    DiffCallback<School>()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_find_school, parent, false)
        val binding: ItemFindSchoolBinding = DataBindingUtil.bind(view)!!
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class Holder(private val binding: ItemFindSchoolBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: School) {
            binding.item = item
            binding.vm = viewModel
        }
    }
}

@BindingAdapter("bind_school_response")
fun bindRecyclerView(recyclerView: RecyclerView, item: SchoolResponse?){
    item?.let { response ->
        val adapter = recyclerView.adapter as FindSchoolAdapter
        adapter.submitList(response.data.content)
    }
}

