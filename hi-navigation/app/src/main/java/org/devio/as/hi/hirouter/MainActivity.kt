package org.devio.`as`.hi.hirouter

import android.net.Uri
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDestinationDsl
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import org.devio.`as`.hi.nav_annotation.Destination

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

//        navView.setupWithNavController(navController)
//        navController.navigate(R.id.navigation_notifications)
//        navController.navigate(R.id.navigation_notifications, Bundle.EMPTY)
//        navController.navigate(Uri.parse("www.imooc.com"))
//
//        navController.navigateUp()//回退上个
//        navController.popBackStack(/*目标页面id*/R.id.navigation_dashboard,/*是否将目标页面也回退*/true)





//        val appBarConfiguration = AppBarConfiguration(setOf(
//            R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
//        setupActionBarWithNavController(navController, appBarConfiguration)

        NavUtil.builderNavGraph(this,navController,R.id.nav_host_fragment)
        NavUtil.buildBottomBar(bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            navController.navigate(item.itemId)
            true }


    }
}