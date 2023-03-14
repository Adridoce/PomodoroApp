package com.adridev.pomodoro

import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.adridev.pomodoro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var countDownTimer: CountDownTimer

    private var timeLeft: Long = 0
    private var isPaused = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setClickListeners()
    }

    private fun setClickListeners() {
        binding.fabAddTask.setOnClickListener { showDialog() }
        binding.btnStop.setOnClickListener { stopCountDownTimer(countDownTimer) }
        binding.btnPlay.setOnClickListener { pauseCountDownTimer(countDownTimer) }
    }

    private fun pauseCountDownTimer(countDownTimer: CountDownTimer) {

        if (binding.btnPlay.tag == null || binding.btnPlay.tag == R.drawable.ic_play){
            binding.btnPlay.setImageResource(R.drawable.ic_pause)
            binding.btnPlay.tag = R.drawable.ic_pause
        }
        else {
            binding.btnPlay.setImageResource(R.drawable.ic_play)
            binding.btnPlay.tag = R.drawable.ic_play
        }

        isPaused = if (!isPaused){
            countDownTimer.cancel()
            true
        } else {
            startPomodoro(timeLeft)
            false
        }
    }

    private fun showDialog() {

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_task)

        val etTaskName: EditText = dialog.findViewById(R.id.etDialogTask)
        val etWorkingTime: EditText = dialog.findViewById(R.id.etDialogWorkingTime)
        val etRestTime: EditText = dialog.findViewById(R.id.etDialogRestTime)
        val btnAccept: Button = dialog.findViewById(R.id.btnDialogAddTask)

        btnAccept.setOnClickListener {

            val name = etTaskName.text.toString()
            val workingTime = etWorkingTime.text.toString()
            val restTime = etRestTime.text.toString()

            timeLeft = (workingTime.toLong() * 60) * 1000

            if (name.isNotEmpty() && workingTime.isNotEmpty() && restTime.isNotEmpty()) {
                startPomodoro(timeLeft)
                binding.tvTask.text = name
                dialog.hide()
            }
            else {
                Toast.makeText(this, "Debes completar todos los campos", Toast.LENGTH_LONG)
                    .show()
            }
        }
        dialog.show()
    }

    private fun startPomodoro(time: Long) {

        binding.btnPlay.show()

        countDownTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(workingMinutesMillis: Long) {

                timeLeft = workingMinutesMillis
                updateCountDownTimerText()
            }

            override fun onFinish() {
                runAlarm()
            }
        }.start()
    }

    private fun updateCountDownTimerText() {

        val minutes = (timeLeft/1000) / 60
        val seconds = (timeLeft / 1000) % 60

        binding.tvMins.text = minutes.toString().padStart(2, '0')
        binding.tvSeconds.text = seconds.toString().padStart(2, '0')
    }


    private fun stopCountDownTimer(countDownTimer: CountDownTimer) {

        countDownTimer.cancel()
        timeLeft = 0

        binding.tvMins.text = "00"
        binding.tvSeconds.text = "00"
        binding.btnPlay.hide()

        Toast.makeText(this@MainActivity, "Cuenta atras cancelada", Toast.LENGTH_LONG).show()
    }

    private fun runAlarm() {
        Toast.makeText(this@MainActivity, "Cuenta atras finalizada", Toast.LENGTH_LONG).show()
    }
}