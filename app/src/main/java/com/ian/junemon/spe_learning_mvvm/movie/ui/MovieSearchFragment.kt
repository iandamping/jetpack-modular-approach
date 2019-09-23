package com.ian.junemon.spe_learning_mvvm.movie.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.ian.app.helper.util.gone
import com.ian.app.helper.util.loadWithGlide
import com.ian.app.helper.util.visible
import com.ian.junemon.spe_learning_mvvm.BuildConfig
import com.ian.junemon.spe_learning_mvvm.R
import com.ian.junemon.spe_learning_mvvm.data.ResultToConsume
import com.ian.junemon.spe_learning_mvvm.databinding.ActivitySearchBinding
import com.ian.junemon.spe_learning_mvvm.util.MovieConstant.movieAdapterCallback
import com.ian.recyclerviewhelper.helper.setUpVerticalGridAdapter
import kotlinx.android.synthetic.main.item_movie.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class MovieSearchFragment : Fragment() {
    private val vms: MovieDataViewModel by viewModel()
    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: ActivitySearchBinding = DataBindingUtil.inflate(inflater, R.layout.activity_search, container, false)
        binding.apply {
            searchVm = vms
            lifecycleOwner = this@MovieSearchFragment
            searchData(this)
        }
        return binding.root
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun searchData(binding: ActivitySearchBinding) {
        binding.apply {
            vms.mutableEditText.observe(viewLifecycleOwner, Observer { querry ->
                vms.observeSearchData(querry).observe(viewLifecycleOwner, Observer { result ->
                    when (result.status) {
                        ResultToConsume.Status.SUCCESS -> {
                            progressSearch.gone()
                            rvSearch.setUpVerticalGridAdapter(result.data?.results, movieAdapterCallback, R.layout.item_movie, 2, {
                                ivHomeMovie.loadWithGlide(BuildConfig.imageFormatter + it.poster_path)
                                tvHomeMovieName.text = it.original_title
                            })
                        }
                        ResultToConsume.Status.LOADING -> progressSearch.visible()

                        ResultToConsume.Status.ERROR -> {
                            progressSearch.gone()
                            Snackbar.make(constraintParent, result.message!!, Snackbar.LENGTH_LONG).show()
                        }
                    }

                })
            })
            invalidateAll()
        }
    }


}
