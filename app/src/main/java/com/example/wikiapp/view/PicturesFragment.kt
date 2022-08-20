package com.example.wikiapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wikiapp.R
import com.example.wikiapp.adapters.PicturesListAdapter
import com.example.wikiapp.databinding.FragmentPicturesBinding
import com.example.wikiapp.model.ImageDetails
import com.example.wikiapp.repository.PicturesRepository
import com.example.wikiapp.utils.NetworkUtils
import com.example.wikiapp.viewmodels.PicturesViewModel
import com.example.wikiapp.viewmodels.PicturesViewModelFactory
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PicturesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PicturesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var picturesViewModel: PicturesViewModel
    private var _binding: FragmentPicturesBinding? = null
    private val binding get() = _binding!!
    private val noteViewModel by viewModels<PicturesViewModel>()

    private lateinit var adapter: PicturesListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPicturesBinding.inflate(inflater, container, false)
        adapter = PicturesListAdapter()
        return binding.root
    }


    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view?.findViewById<RecyclerView>(R.id.imagesRV)
        val progressBar = view?.findViewById<ProgressBar>(R.id.progressBar)
        progressBar!!.visibility = View.VISIBLE

        if (NetworkUtils.isInternetAvailable(requireActivity().applicationContext)) {
//            Toast.makeText(this.context,"Loading Online",Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(this.context, "Loading Offline", Toast.LENGTH_SHORT).show()

        }


        val adapter = PicturesListAdapter()
        var pictureList: MutableList<ImageDetails>? = null
        binding.imagesRV.layoutManager =
            LinearLayoutManager(context)
        binding.imagesRV.adapter = adapter
//    binding.imagesRV.setOnClickListener {
//        var intent : Intent = Intent(activity,WebViewActivity::class.java)
//        activity?.startActivity(intent)
//    }


        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.adapter = adapter

//        var a1 = Article(1,1,"Hello Article","","aricle")
//        var a2 = Article(2,2,"Hello Article2","","aricle")
//        var a3 = Article(3,3,"Hello Article23","","aricle")
//        var a4 = Article(4,4,"Hello Article434","","aricle")
//        var a5 = Article(5,5,"Hello Article43","","aricle")
//        adapter.submitList(listOf(a1,a2,a3,a4,a5))
//        adapter.submitList(listOf(Article(1,1,"Hello Article","","aricle")))

//    val database = ArticleDatabase.getDatabase(requireActivity().application)
        val pictureRepository = PicturesRepository()
        picturesViewModel = ViewModelProvider(
            this,
            PicturesViewModelFactory(pictureRepository)
        ).get(PicturesViewModel::class.java)

        Log.d("PIC FAG", adapter.currentList.toString())
        var str = ""

        picturesViewModel.pictures.observe(viewLifecycleOwner, {
            val mutableList = mutableListOf<ImageDetails>()

            var keys = it.query.pages.keys
            keys.forEach { key ->
                var imageTitle =
                    it.query.pages.get(key)?.title.toString().replace("File:", "").toString() + "\n"

                var picture = ImageDetails(imageTitle)
                mutableList.add(picture)
                str += it.query.pages.get(key)?.title.toString().replace("File:", "")
                    .toString() + "\n"
            }

            val immutableList = Collections.unmodifiableList(mutableList)
            adapter.submitList(immutableList)

            progressBar.visibility = View.INVISIBLE
            Log.d("RESTT IMAGE", adapter.currentList.toString())

        })
        Log.d("REST", str)


/*
         val wikiApiService by lazy {
            WikiApiService.create("https://commons.wikimedia.org/w/")
        }
        wikiApiService.getImages(
            "query",
            "json",
            "categorymembers",
            "file",
            "imageinfo",
            "timestamp|Cuser|url",
            "content",
            "Category:Featured_pictures_on_Wikimedia_Commons",
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    var str = ""
                    var keys = result.query.pages.keys
                    keys.forEach {
                            key ->   str +=
                        result.query.pages.get(key)?.title.toString().replace("File:","").toString() + "\n"
                    }
                    Log.d("IMAGE REPO",str.toString())
//                    Toast.makeText(this.context, result.toString(), Toast.LENGTH_SHORT).show()

                    result.query.pages.keys.forEach { key ->
                    }
                },
                { error ->
                    Log.d("IMAGE LIST ERROR", error.message.toString())
                }
            )
*/
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PicturesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PicturesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}