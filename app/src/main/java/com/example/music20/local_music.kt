package com.example.music20

import SongDataModel
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.text.Layout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.lang.Exception


class local_music : Fragment(),SongInterface {

    private lateinit var rv:RecyclerView
    private lateinit var songData:ArrayList<SongDataModel>
    private lateinit var dialog: BottomSheetBehavior<CardView>
    private lateinit var play:ImageView
    private lateinit var seekbar: SeekBar
    private lateinit var pref:SharedPreferences
    private lateinit var next:ImageView
    private lateinit var title:TextView
    private lateinit var prev:ImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

     val view= inflater.inflate(R.layout.fragment_local_music, container, false)
     rv=view.findViewById(R.id.Rview)
        play=view.findViewById(R.id.play)
        seekbar=view.findViewById(R.id.seekbar)
        next=view.findViewById(R.id.next)
        title=view.findViewById(R.id.songTitle)
        prev=view.findViewById(R.id.prev)
        rv.layoutManager = LinearLayoutManager(requireActivity())
        pref=requireActivity().getSharedPreferences("Status",Context.MODE_PRIVATE)


         dialog=BottomSheetBehavior.from(view.findViewById(R.id.card))
        dialog.peekHeight=130


        dialog.state=BottomSheetBehavior.STATE_COLLAPSED

        songData= ArrayList()

        play.setOnClickListener {
            if (MainActivity.mediaplayer!!.isPlaying) {
                pauseSong()
                play.setImageResource(R.drawable.play2icon) // Set the play icon
            } else {
                playSong()
                play.setImageResource(R.drawable.pause222) // Set the pause icon
            }
        }

        next.setOnClickListener {
            if (MainActivity.mediaplayer!=null){
                nextSong()

            }
        }

        prev.setOnClickListener {
            if (MainActivity.mediaplayer!=null){
                prevSong()

            }
        }



        getSongData()


        return view



    }

    private fun getSongData(){
        val path= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection= MediaStore.Audio.Media.IS_MUSIC + "!=0"
        val projection= arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM)
        val cursor= requireActivity().contentResolver.query(path,projection,selection,null,null)

        if (cursor!=null){
            while (cursor.moveToNext()){
                val song=SongDataModel(cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                )
               songData.add(song)
            }
            cursor.close()
        }
        val adapter=SongAdapter(songData,this)
        rv.adapter=adapter
    }

    override fun onClick(position: Int) {
       val path= songData[position].path
        startSong(path,songData[position].title)
        val editor=pref.edit()
        editor.putString("title",songData[position].title)
        editor.putString("path",songData[position].path)
        editor.putString("duration",songData[position].duration)
        editor.putString("artist",songData[position].artist)
        editor.putString("album",songData[position].album)
        editor.putInt("position",position)
        editor.apply()
        dialog.state = BottomSheetBehavior.STATE_EXPANDED


    }
    private fun startSong(path:String,title1:String){
        MainActivity.mediaplayer!!.reset()
        MainActivity.mediaplayer!!.setDataSource(path)
        MainActivity.mediaplayer!!.prepare()
        MainActivity.mediaplayer!!.start()

        setSeekBar()
         title.text=title1
    }

    private fun playSong(){
        MainActivity.mediaplayer!!.start()

    }

    private fun pauseSong(){
        MainActivity.mediaplayer!!.pause()
    }
    private fun setSeekBar(){
        seekbar.max= MainActivity.mediaplayer!!.duration

        val handler = Handler()
        handler.postDelayed(object :Runnable{
            override fun run() {
                try {
                    seekbar.progress=MainActivity.mediaplayer!!.currentPosition
                    if (MainActivity.mediaplayer!!.duration==MainActivity.mediaplayer!!.currentPosition){
                        nextSong()
                    }
                    handler.postDelayed(this,1000)
                }catch (e:Exception){
                   seekbar.progress=0

                }

            }
        },0)
    }

    private fun nextSong(){
        val position=pref.getInt("position",0)
        val nextPosition=position+1
        val editor=pref.edit()
        editor.putInt("position",nextPosition)
        editor.putString("title",songData[nextPosition].title)
        editor.apply()
        val path=songData[nextPosition].path
        startSong(path,songData[position].title)
    }
    private fun prevSong(){
        val position=pref.getInt("position",0)
        val prevPosition=position-1
        val editor=pref.edit()
        editor.putInt("position",prevPosition)
        editor.putString("title",songData[prevPosition].title)
        editor.apply()
        val path=songData[prevPosition].path
        startSong(path,songData[position].title)
    }

}