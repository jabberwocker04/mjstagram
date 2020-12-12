package com.jkh.mjstagram

import android.os.Bundle
import android.os.RecoverySystem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HomeFragment: Fragment() {
    lateinit var listRv: RecyclerView

    lateinit var homeAdapter: HomeAdapter
    lateinit var postlist: ArrayList<Post>

    lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_home, container, false)
        listRv = view.findViewById(R.id.list_Rv)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
        postlist = ArrayList<Post>()
        homeAdapter = HomeAdapter(activity!!, postlist)

        listRv.adapter = homeAdapter
        listRv.layoutManager = LinearLayoutManager(activity)

        firestore.collection("post")
                .orderBy("uploadDate", Query.Direction.ASCENDING)
                .addSnapshotListener() { value, error ->
                    if (value != null) {
                        for (dc in value.documentChanges) {
                            if (dc.type == DocumentChange.Type.ADDED) {
                                var post = dc.document.toObject(Post::class.java)
                                postlist.add(0, post)
                            }
                        }
                        homeAdapter.notifyDataSetChanged()
                    }
                }
    }
}