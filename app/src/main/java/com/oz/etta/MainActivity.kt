package com.oz.etta

import android.app.ActionBar
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.view.*

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    var gameIsStarted = false
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

//        viewUserScore.text = ""
//        editUserScore.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
//            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
//                buttonAddToScore.callOnClick()
//                return@OnKeyListener true
//            }
//            false
//        })


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
        }

        buttonAddUser.setOnClickListener {
            val newUser = User(editUserName.text.toString(), Integer(0))
            val newButton = UserButton(this, newUser)
            buttonList.add(newButton)
            newButton.updateText()

            val lp = ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
            userGridLayout.addView(newButton, lp)
            editUserName.text.clear()
        }

        start_stop_button.setOnClickListener {
            if (gameIsStarted) {
                start_stop_button.text = "STOP GAME"
                gameIsStarted = false
//                editUserScore.setBackgroundColor(Color.WHITE)
                changeLayoutState(addNewUserLayout, gameIsStarted)
                changeLayoutState(addScoreLayout, !gameIsStarted)
            } else {
                if (!buttonList.isEmpty()) {
                    start_stop_button.text = "START GAME"
                    gameIsStarted = true
                    changeLayoutState(addNewUserLayout, gameIsStarted)
                    changeLayoutState(addScoreLayout, !gameIsStarted)
                    userIterator = buttonList.iterator()
                    getNextUser()

                    // set focus to edit score
                    editUserScore.requestFocus()
//                    editUserScore.setBackgroundColor(Color.RED)
//                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                    imm.showSoftInput(editUserScore, InputMethodManager.SHOW_IMPLICIT)
                }
            }
        }
        // disable adding score by default
        changeLayoutState(addScoreLayout, false)

    }

    private fun getNextUser() {
        if (currentUserButton != null) {
            currentUserButton!!.setBackgroundColor(Color.GRAY)
        }
        if (!userIterator!!.hasNext()) {
            userIterator = buttonList.iterator()
        }
        currentUserButton = userIterator!!.next()

        currentUserButton!!.setBackgroundColor(Color.GREEN)

        updateEditForm()
    }

    private fun updateEditForm() {
        textViewUserName.setText(currentUserButton!!.user.toString())
//        viewUserScore.setText(currentUserButton!!.user.score.toString())
    }

    private fun changeLayoutState(layout: ViewGroup, state: Boolean) {
        for (i in 0 until layout.childCount) {
            val child = layout.getChildAt(i)
            child.isEnabled = state
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
