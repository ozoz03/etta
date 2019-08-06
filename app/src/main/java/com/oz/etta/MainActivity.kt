package com.oz.etta

import android.app.ActionBar
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.*

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.widget.Toast
import android.content.DialogInterface


class MainActivity : AppCompatActivity() {

    val ACTIVE_BUTTON_COLOR = Color.GREEN
    val INACTIVE_BUTTON_COLOR = Color.GRAY

    var isGameStarted = false
    var buttonList = LinkedHashSet<UserButton>()
    var currentUserButton: UserButton? = null
    var userIterator: Iterator<UserButton>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val scoreRepository = ScoreRepository()
        AlertDialog.Builder(this)
                    .setTitle("Loading the previous result")
                    .setMessage("Do you really want to load results of previous game?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes,
                        DialogInterface.OnClickListener { dialog, whichButton ->
                            mapToUsers(scoreRepository.readFromFile(applicationContext))
                        })
                    .setNegativeButton(android.R.string.no, null).show()

        buttonAddToScore.setOnClickListener {
            if (editUserScore.text.trim().isBlank() || !editUserScore.text.toString().get(0).isDigit()) {
                return@setOnClickListener
            }
            val newTurnScore = Integer.parseInt(editUserScore.text.toString())
            val newScore = newTurnScore + currentUserButton!!.user.score.toInt()
            editUserScore.text = null
            currentUserButton!!.user.score = Integer(newScore)
            currentUserButton!!.updateText()

            getNextUser()
            scoreRepository.writeToFile(usersToMap(), applicationContext)
        }

        buttonAddUser.setOnClickListener {
            val newUser = User(editUserName.text.toString(), Integer(0))
            val newButton = UserButton(this, newUser)
            buttonList.add(newButton)
            newButton.updateText()
            newButton.setOnClickListener {
                makeMyUserActive(newButton)
            }

            val lp = ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
            userGridLayout.addView(newButton, lp)
            editUserName.text.clear()
        }

        start_stop_button.setOnClickListener {
            if (isGameStarted) {
//                AlertDialog.Builder(this)
//                    .setTitle("Title")
//                    .setMessage("Do you really want to whatever?")
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .setPositiveButton(android.R.string.yes,
//                        DialogInterface.OnClickListener { dialog, whichButton ->
//                            Toast.makeText(
//                                this@MainActivity,
//                                "Yaay",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        })
//                    .setNegativeButton(android.R.string.no, null).show()

                start_stop_button.text = "START GAME"
                isGameStarted = false
                changeLayoutState(addNewUserLayout, enabled = true)
                changeLayoutState(addScoreLayout, enabled = false)
            } else {
                if (!buttonList.isEmpty()) {
                    start_stop_button.text = "STOP GAME"
                    isGameStarted = true
                    changeLayoutState(addNewUserLayout, enabled = false)
                    changeLayoutState(addScoreLayout, enabled = true)
                    userIterator = buttonList.iterator()
                    getNextUser()

                    // set focus to edit score
                    editUserScore.requestFocus()
                }
            }
        }
        // disable adding score by default
        changeLayoutState(addScoreLayout, false)
    }

    private fun mapToUsers(userScoreMap: Map<String, Integer>) {
        for (entry in userScoreMap) {
            val newUser = User(entry.key, entry.value)
            val newButton = UserButton(this, newUser)
            newButton.setOnClickListener {
                makeMyUserActive(newButton)
            }
            buttonList.add(newButton)
            val lp = ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
            userGridLayout.addView(newButton, lp)
        }
    }

    private fun makeMyUserActive(thisButton: UserButton) {
        if (isGameStarted) {
            currentUserButton!!.setBackgroundColor(INACTIVE_BUTTON_COLOR)
            currentUserButton = thisButton
            currentUserButton!!.setBackgroundColor(ACTIVE_BUTTON_COLOR)
            updateEditForm()
            // reset iterator
            userIterator = buttonList.iterator()
        }
    }

    private fun usersToMap(): Map<String, Integer> {
        val userMap = HashMap<String, Integer>()
        for (userButton in buttonList) {
            userMap.put(userButton.user.name, userButton.user.score)
        }
        return userMap
    }

    private fun getNextUser() {
        if (currentUserButton != null) {
            currentUserButton!!.setBackgroundColor(INACTIVE_BUTTON_COLOR)
        }
        if (!userIterator!!.hasNext()) {
            userIterator = buttonList.iterator()
        }
        currentUserButton = userIterator!!.next()

        currentUserButton!!.setBackgroundColor(ACTIVE_BUTTON_COLOR)

        updateEditForm()
    }

    private fun updateEditForm() {
        textViewUserName.setText(currentUserButton!!.user.toString())
    }

    private fun changeLayoutState(layout: ViewGroup, enabled: Boolean) {
        for (i in 0 until layout.childCount) {
            val child = layout.getChildAt(i)
            child.isEnabled = enabled
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
