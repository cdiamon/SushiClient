package net.pdmtrdv.sashimisake.view

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import net.pdmtrdv.sashimisake.R
import net.pdmtrdv.sashimisake.view.kitchen.KitchenFragment
import net.pdmtrdv.sashimisake.view.menu.MenuFragment
import net.pdmtrdv.sashimisake.view.wait.WaitFragment

class MainActivity : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener,
        MenuFragment.OnMenuFragmentListener,
        KitchenFragment.OnKitchenFragmentListener,
        WaitFragment.OnWaitFragmentListener,
        AdminFragment.OnAdminFragmentListener {

    private val TAG = "MainActivity"
    private var menuInsideCategory = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        when {
            menuInsideCategory -> {
                val menuFragment: MenuFragment = supportFragmentManager
                        .findFragmentByTag(MenuFragment::class.java.simpleName) as MenuFragment
                menuInsideCategory = false
                menuFragment.pressBack()
            }
            drawer_layout.isDrawerOpen(GravityCompat.START) -> drawer_layout.closeDrawer(GravityCompat.START)
            else -> super.onBackPressed()
        }
    }


    override fun onMenuFragmentCategoryPressed(inCategory: Boolean) {
        menuInsideCategory = inCategory
        Log.i(TAG, "onMenuFragmentCategoryPressed $menuInsideCategory")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        var fragment: Fragment? = null

        when (item.itemId) {
            R.id.nav_menu -> {
                fragment = MenuFragment.newInstance()
                title = "Меню"
            }
            R.id.nav_kitchen -> {
                fragment = KitchenFragment.newInstance()
                title = "Кухня"
            }
            R.id.nav_wait_screen -> {
                fragment = WaitFragment.newInstance()
                title = "Экран ожидания"
            }
            R.id.nav_administrator -> {
                fragment = AdminFragment.newInstance()
                title = "Администрирование"
            }
            R.id.nav_share -> {
            }
            R.id.nav_send -> {
            }
        }

        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(fragmentContainer.id, fragment, fragment!!::class.java.simpleName).commit()


        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onFragmentInteraction() {
    }
}
