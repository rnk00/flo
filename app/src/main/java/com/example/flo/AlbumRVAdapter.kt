package com.example.flo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemAlbumBinding

class AlbumRVAdapter(private val albumList: ArrayList<Album>)
    :RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>(){

    interface MyItemClickListener{
        fun onItemClick(album:Album)
        //fun onRemoveAlbum(position:Int) 앨범 없애는 거 한 번 해봄
    }//인자값을 넣어서 앨범을 클릭했을 때 값이 변하게 한다
    private lateinit var mItemClickListener:MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }//외부에서 전달받아 mItemClickListener에 저장

    fun addItem(album:Album){
        albumList.add(album)
        notifyDataSetChanged()//데이터가 바뀐 걸 알려줘야함
    }//앨범 목록에 새로운 item 추가
    fun removeItem(position: Int){
        albumList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlbumRVAdapter.ViewHolder {
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)
        return ViewHolder(binding)
    }//ViewHolder 만들기

    override fun onBindViewHolder(holder: AlbumRVAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position])
        holder.itemView.setOnClickListener{mItemClickListener.onItemClick(albumList[position])} //인자값을 넣어서 앨범을 클릭했을 때 값이 변하게 한다22
        //holder.binding.itemAlbumTitleTv.setOnClickListener{mItemClickListener.onRemoveAlbum(position) }   앨범 없애는 거 한 번 해봄
    }//ViewHolder에 데이터 바인딩
    //position값을 갖고 있으므로 클릭할 때 일어나는 일은 여기에서 작성
    //setOnClickListener를 통해 listview에 있는 앨범을 클릭했을 때 화면이 전환되게 함

    override fun getItemCount(): Int =  albumList.size
    //데이터 세트 크기를 알려줌
    inner class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(album:Album){
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumCoverImgIv.setImageResource(album.coverImg!!)
        }
    }
}
//앨범 목록을 관리, 사용자의 상호작용에 따라 적절한 동작을 수행
//RecyclerView와 Adapter를 사용하여 동적인 목록을 구현