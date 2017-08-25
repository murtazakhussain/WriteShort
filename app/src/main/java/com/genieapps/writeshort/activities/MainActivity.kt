package com.genieapps.writeshort.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import com.genieapps.writeshort.NoteEntity
import com.genieapps.writeshort.R
import com.genieapps.writeshort.helpers.FirebaseManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.wang.avi.AVLoadingIndicatorView


class MainActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var etNote:EditText? = null
    private var progress:AVLoadingIndicatorView? = null

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        val navigationView = findViewById(R.id.nav_view) as NavigationView
        etNote = findViewById(R.id.etNote) as EditText
        progress = findViewById(R.id.progress) as AVLoadingIndicatorView

        setSupportActionBar(toolbar)

        /* val fab = findViewById(R.id.fab) as FloatingActionButton
         fab.setOnClickListener({ view ->
             Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                     .setAction("Action", null).show()
         })*/

        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)

        val btnSave = findViewById(R.id.btnSave) as Button
        btnSave.setOnClickListener({
            saveNote()
        })
    }

    private fun saveNote() {
        val noteText = etNote!!.text

        if (noteText.isEmpty())
            return

        progress?.smoothToShow()
        val note = NoteEntity(noteText.toString())

        val manager = FirebaseManager(this)
                .insertANote(note,
                        DatabaseReference.CompletionListener
                        { _, _ -> clearBoard() })

    }

    private fun clearBoard() {
        etNote?.setText("")
        progress?.smoothToHide()
    }

    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu:Menu?):Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item:MenuItem?):Boolean {
        val id = item?.itemId
        when (id) {
            R.id.action_logout -> {
                FirebaseAuth.getInstance().signOut();
                startActivity(Intent(this, Splash::class.java))
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item:MenuItem):Boolean {
        val id = item.itemId

        when (id) {
            R.id.note_list -> {
                startActivity(Intent(this, NoteListActivity::class.java))
            }
        }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}
