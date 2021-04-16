package net.flow9.androidmvvm.activity.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.scopes.ActivityScoped

import net.flow9.androidmvvm.R
import net.flow9.androidmvvm.activity.base.DiffCallback
import net.flow9.androidmvvm.databinding.DialogUserBinding
import net.flow9.androidmvvm.databinding.ItemUserBinding
import net.flow9.androidmvvm.repository.model.response.GithubUser
import net.flow9.androidmvvm.viewmodel.GithubUserViewModel
import javax.inject.Inject


class UserDialog @Inject constructor(
    var viewModel:GithubUserViewModel
) : DialogFragment() {

    private val binding: DialogUserBinding by lazy {
        DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_user, null, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = this@UserDialog
            vm = viewModel
            recyclerSchool.adapter = UserAdapter(viewModel)
            btnClose.setOnClickListener {
                dismiss()
            }
        }
    }
}

class UserAdapter(private val viewModel: GithubUserViewModel): ListAdapter<GithubUser, UserAdapter.Holder>(
    DiffCallback<GithubUser>()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        val binding: ItemUserBinding = DataBindingUtil.bind(view)!!
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class Holder(private val binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GithubUser) {
            binding.item = item
            binding.vm = viewModel
        }
    }
}

@BindingAdapter("bind_user_response")
fun bindRecyclerView(recyclerView: RecyclerView, item: List<GithubUser>?){
    item?.let { users ->
        val adapter = recyclerView.adapter as UserAdapter
        adapter.submitList(users)
    }
}

