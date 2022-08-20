package com.example.wikiapp.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wikiapp.R
import com.example.wikiapp.adapters.ArticleListAdapter
import com.example.wikiapp.databinding.FragmentArticlesBinding
import com.example.wikiapp.db.ArticleDatabase
import com.example.wikiapp.model.Article
import com.example.wikiapp.repository.ArticleRepository
import com.example.wikiapp.utils.NetworkUtils
import com.example.wikiapp.viewmodels.ArticlesViewModel
import com.example.wikiapp.viewmodels.ArticlesViewModelFactory
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ArticlesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ArticlesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var articlesViewModel: ArticlesViewModel
    private var _binding: FragmentArticlesBinding? = null
    private val binding get() = _binding!!
    private val noteViewModel by viewModels<ArticlesViewModel>()

    private lateinit var adapter: ArticleListAdapter


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
        _binding = FragmentArticlesBinding.inflate(inflater, container, false)
        adapter = ArticleListAdapter(::onArticleClick)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view?.findViewById<RecyclerView>(R.id.articlesRV)
        val progressBar = view?.findViewById<ProgressBar>(R.id.progressBar)
        progressBar!!.visibility = View.VISIBLE

        if (NetworkUtils.isInternetAvailable(requireActivity().applicationContext)) {
//            Toast.makeText(this.context,"Loading Online",Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(this.context, "Loading Offline", Toast.LENGTH_SHORT).show()

        }


        val adapter = ArticleListAdapter(::onArticleClick)
        var articleList: MutableList<Article>? = null
        binding.articlesRV.layoutManager =
            LinearLayoutManager(context)
        binding.articlesRV.adapter = adapter
        binding.articlesRV.setOnClickListener {
            var intent: Intent = Intent(activity, WebViewActivity::class.java)
            activity?.startActivity(intent)
        }


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

        val database = ArticleDatabase.getDatabase(requireActivity().application)
        val articleRepository = ArticleRepository(database, requireActivity().application)
        articlesViewModel = ViewModelProvider(
            this,
            ArticlesViewModelFactory(articleRepository)
        ).get(ArticlesViewModel::class.java)

        articlesViewModel.articles.observe(viewLifecycleOwner, Observer {

            val mutableList = mutableListOf<Article>()

            it.query.pages.keys.forEach { key ->

                var pageid: Int = key.toInt()
                var title: String? = it.query.pages.get(key)?.title
                var description: String? = it.query.pages.get(key)?.description.toString()
                var imageUrl: String? = it.query.pages.get(key)?.images?.get(0)?.title.toString()

                if (description == "null") {
                    description = title
                }

                if (imageUrl == "null") {
                    imageUrl = R.drawable.wiki_article.toString()
                } else {
                    imageUrl =
                        "https://commons.wikimedia.org/wiki/Special:FilePath/" + imageUrl + "?width=200"
                }

                Log.d("URL ARTICLE IMAGES", imageUrl)

                var article = Article(1, pageid, title, imageUrl, description)
                mutableList.add(article)


            }
            val immutableList = Collections.unmodifiableList(mutableList)

            adapter.submitList(immutableList)

//            adapter.submitList(mutableListOf( ))
            progressBar.visibility = View.INVISIBLE
            Log.d("RES ARTICLE", adapter.currentList.toString())
        })

    }


    private fun onArticleClick(article: Article) {
        val bundle = Bundle()
        bundle.putString("url", article.imgUrl)
        Log.d("LISTENER", "clicked")
        Toast.makeText(context, "Article clicked", Toast.LENGTH_LONG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ArticlesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ArticlesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}