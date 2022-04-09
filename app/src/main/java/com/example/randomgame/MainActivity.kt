package com.example.randomgame

import android.app.ActionBar
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.lang.NumberFormatException
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    var randomNumber = Random.nextInt(0, 20)
    var shotCounter = 0
    val maxShotNumber = 10
    var pointsCounter = 0
    val scoringTable = intArrayOf(9999, 5, 3, 3, 3, 2, 2, 1, 1, 1, 1)

    fun setRecord() {
        val sharedScore = this.getSharedPreferences("com.example.myapplication.shared",0)
        val edit = sharedScore.edit()
        edit.putInt("score", pointsCounter)
        edit.apply()
    }

    fun getRecord() {
        val sharedScore = this.getSharedPreferences("com.example.myapplication.shared",0)
        pointsCounter = sharedScore.getInt("score", 0)
        findViewById<TextView>(R.id.scoreTv).text = "Aktualny wynik: " + pointsCounter
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        setTitle("Randomowa gra")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getRecord()

        val shotsInput = findViewById<EditText>(R.id.numberInput)
        val shotsTv = findViewById<TextView>(R.id.shotsTv)
        val scoreTv = findViewById<TextView>(R.id.scoreTv)
        val shotBtn = findViewById<Button>(R.id.shotBtn)
        val newGameBtn = findViewById<Button>(R.id.newGameBtn)

        fun validateShot(shot: Int): Boolean {
            if (shot > 20 || shot < 0) {
                Toast.makeText(
                    this,
                    "Liczba spoza zakresu! [0:20]",
                    Toast.LENGTH_LONG
                ).show()
                return false
            }
            return true
        }

        fun updateShotTv() {
            shotsTv.text = "Oddane strzały: " + shotCounter
        }

        fun updateScoreTv() {
            scoreTv.text = "Aktualny wynik: " + pointsCounter
            setRecord()
        }

        fun newGame() {
            randomNumber = Random.nextInt(0, 20)
            shotCounter = 0
            updateShotTv()
            Toast.makeText(this, "Rozpoczęto nową grę!", Toast.LENGTH_SHORT).show()
        }

        fun resetPoints() {
            pointsCounter = 0
            newGame()
            updateScoreTv()
            Toast.makeText(this, "Zresetowano punkty!", Toast.LENGTH_SHORT).show()

        }

        fun checkShot(shot: Int): Boolean {
            shotCounter += 1

            if (shot > randomNumber) {
                Toast.makeText(this, "Za dużo", Toast.LENGTH_SHORT).show()
                return false
            } else if (shot < randomNumber) {
                Toast.makeText(this, "Za mało!", Toast.LENGTH_SHORT).show()
                return false
            }
            return true
        }

        fun showAlert(title: String, message: String) {
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle(title)
            builder.setMessage(message)

            builder.setPositiveButton("OK") { _: DialogInterface, _: Int -> }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        fun makeShot() {
            try {
                val shot = shotsInput.text.toString().toInt()
                validateShot(shot)

                if (checkShot(shot)) {
                    pointsCounter += scoringTable[shotCounter]
                    updateScoreTv()
                    showAlert("Koniec gry!", "Wygrałeś! Brawo!")
                    shotCounter = 0
                    newGame()
                } else if (shotCounter == maxShotNumber) {
                    showAlert("Koniec gry!", "Przegrałeś! Możesz zagrać jeszcze raz...")
                    newGame()
                }
                updateShotTv()
                shotsInput.text = null
            } catch (e: NumberFormatException) {
                showAlert("Niepoprawna wartość!", "Wprowadz poprawną liczbę w pole strzału.")
            }


        }

        shotBtn.setOnClickListener {
            makeShot()
        }
        newGameBtn.setOnClickListener {
            newGame()
        }


    }
}