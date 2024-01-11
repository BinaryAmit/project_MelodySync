package com.example.music20

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.provider.MediaStore

class MainActivity : AppCompatActivity() {
    private lateinit var buttom:BottomNavigationView

    companion object{
        var mediaplayer:MediaPlayer?=null
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttom=findViewById(R.id.bottomNavigation)

        if (mediaplayer==null){
            mediaplayer=MediaPlayer()

        }


        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_MEDIA_AUDIO)==PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
            //BAAD ME AAUNGA

        }else{
            requestPermission()
        }
        replaceFragment(local_music())


        buttom.setOnItemSelectedListener {
            if (it.itemId==R.id.local){
                replaceFragment(local_music())
              //  true
            }
            else if (it.itemId==R.id.online){
                replaceFragment(online())
             //   true
            }else{
                replaceFragment(download())
              //  true
            }
            true

        }


    }
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.container,fragment).commit()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestPermission(){
        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.READ_MEDIA_AUDIO,android.Manifest.permission.READ_EXTERNAL_STORAGE),1)


    }

}

