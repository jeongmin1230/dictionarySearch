package com.example.dictionarysearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.appbar.*

class MainActivity : AppCompatActivity() {

    private val TAG : String = "jeongmin"

    private val db = FirebaseFirestore.getInstance()    // Firestore 인스턴스 선언
    private val itemList = arrayListOf<ListLayout>()    // 리스트 아이템 배열
    private val adapter = ListAdapter(itemList)         // 리사이클러 뷰 어댑터

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        load()

        rvList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvList.adapter = adapter

        search()
    }

/* --------------------기능함수-------------------- */

    // 엡이 시작되자마자 FireStore database 의 데이터를 불러오기 위한 함수
    private fun load() {
        db.collection("dictionary") // 작업할 컬렉션
            .get() // 문서 가져오기
            .addOnSuccessListener { result ->
                // 성공할 경우
                itemList.clear()
                for(document in result) { // 가져온 문서들은 result 에 들어감
                    val item = ListLayout(document["word"] as String, document["mean"] as String)
                    itemList.add(item)
                }
                adapter.notifyDataSetChanged() // 리사이클러 뷰 갱신
            }
            .addOnFailureListener { exception ->
                // 실패할 경우
                Log.d(TAG, "Error getting documents : $exception")
            }
    }
    private fun search() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean { // 검색 버튼 누를 때 호출(이번 예제에서는 실시간 검색을 하기 때문에 사용하지 않음)
                Log.d(TAG, "search")
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean { // 검색창에서 글자의 변경이 일어 날 때마다 호출
                Log.d(TAG, "$p0")
                db.collection("dictionary") // 작업할 컬렉션
                    .get() // 문서 가져오기
                    .addOnSuccessListener { result ->
                        // 성공할 경우
                        itemList.clear()
                        for(document in result) { // 가져온 문서들은 result 에 들어감
                            val item = ListLayout(document["word"] as String, document["mean"] as String)
                            Log.d(TAG, "for 문 안 p0 입력 값 : $p0")
                                if((document["word"] as String).contains("$p0")) {
                                    val inputItem = ListLayout(document["word"] as String, document["mean"] as String)
                                    itemList.add(inputItem)
                            }
                        }
                        adapter.notifyDataSetChanged() // 리사이클러 뷰 갱신
                    }
                    .addOnFailureListener { exception ->
                        // 실패할 경우
                        Log.d(TAG, "Error getting documents : $exception")
                    }
                return true
            }

        })
    }
}