package com.example.timerapp

import android.content.DialogInterface
import android.media.RingtoneManager
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var timerText: TextView
    private var countDownTimer: CountDownTimer? = null
    private var timerType: String = ""
    private var isTimerRunning: Boolean = false
    private var remainingTimeMillis: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerText = findViewById(R.id.timer_text)

        val btnMilk: Button = findViewById(R.id.btn_milk)
        val btnWaterMotor: Button = findViewById(R.id.btn_water_motor)
        val btnToast: Button = findViewById(R.id.btn_toast)
        val btnCancel: Button = findViewById(R.id.btn_cancel)
        val btnPass: Button = findViewById(R.id.btn_pass)
        val btnResume: Button = findViewById(R.id.btn_resume)

        btnMilk.setOnClickListener {
            startTimer(5 * 60 * 1000, "Milk") // 5 minutes
        }

        btnWaterMotor.setOnClickListener {
            startTimer(20 * 60 * 1000, "Water Motor") // 20 minutes
        }

        btnToast.setOnClickListener {
            startTimer(7 * 60 * 1000, "Toast") // 7 minutes
        }

        btnCancel.setOnClickListener {
            cancelTimer()
        }

        btnPass.setOnClickListener {
            // Implement pass logic here
            skipToNextTimer()
        }

        btnResume.setOnClickListener {
            // Implement resume logic here
            resumeTimer()
        }
    }

    private fun startTimer(timeInMillis: Long, type: String) {
        countDownTimer?.cancel() // cancel any existing timer
        timerType = type

        countDownTimer = object : CountDownTimer(timeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = (millisUntilFinished / 1000) % 60
                timerText.text = String.format("%02d:%02d", minutes, seconds)
                isTimerRunning = true
                remainingTimeMillis = millisUntilFinished
            }

            override fun onFinish() {
                timerText.text = "00:00"
                showAlertDialog(timerType)
                playAlarm()
                isTimerRunning = false
            }
        }.start()
    }

    private fun cancelTimer() {
        countDownTimer?.cancel()
        timerText.text = "00:00"
        isTimerRunning = false
    }

    private fun skipToNextTimer() {
        countDownTimer?.cancel()

    }

    private fun resumeTimer() {
        if (isTimerRunning && remainingTimeMillis > 0) {
            startTimer(remainingTimeMillis, timerType)
        }
    }

    private fun showAlertDialog(timerType: String) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Timer Ended")
        alertDialogBuilder.setMessage("$timerType timer ended!")
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun playAlarm() {
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val ringtone = RingtoneManager.getRingtone(applicationContext, notification)
        ringtone.play()
    }
}
