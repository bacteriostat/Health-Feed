package kotlindays31.project.healthfeed.Feed

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlindays31.project.healthfeed.R
import kotlindays31.project.healthfeed.databinding.FragmentFeedBinding

class FeedFragment : Fragment() {

    private lateinit var viewModel: FeedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentFeedBinding>(inflater,
            R.layout.fragment_feed,container,false)

        viewModel = ViewModelProvider(this).get(FeedViewModel::class.java)

        val adapter = FeedListAdapter()
        binding.feedList.adapter = adapter

        viewModel.feedList.observe(viewLifecycleOwner, Observer {
            adapter.data = it
            binding.swipeRefresh.isRefreshing = false
        })

        if(viewModel.cache){
            adapter.data = loadCache()
        }else{
            loadDataFromBackend()
        }

        binding.swipeRefresh.setOnRefreshListener {
            invalidateCache()
            loadDataFromBackend()
        }
        return binding.root
    }

    private fun loadDataFromBackend(){
        viewModel.getFeed(context)
    }

    private fun loadCache():List<FeedItem>{
        return viewModel.feedList.value!!
    }

    private fun invalidateCache(){
        viewModel.cache = false
    }

}