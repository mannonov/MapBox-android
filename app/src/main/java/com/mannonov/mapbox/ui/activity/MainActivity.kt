package com.mannonov.mapbox.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mannonov.mapbox.ui.fragment.MapFragment
import com.mannonov.mapbox.R
import com.mannonov.mapbox.ui.utils.setFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.setFragment(fragment = MapFragment(), layout = R.id.container_view)

    }

}