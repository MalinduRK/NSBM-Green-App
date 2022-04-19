package com.example.mygreenapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mygreenapp.databinding.ActivityAddMeetingBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_meeting.*
import java.text.SimpleDateFormat
import java.util.*

class AddMeetingActivity : AppCompatActivity() {

    //Database variable declaration
    private lateinit var binding: ActivityAddMeetingBinding
    private lateinit var fStore: FirebaseFirestore
    private lateinit var txtMeetingDate: TextView

    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMeetingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.title = "Add New Meeting"

        //init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        txtMeetingDate = findViewById(R.id.txtMeetingDate)

        val myCalendar = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel(myCalendar)
        }

        //Meeting Time Text View pops up the Time Picker to select the Meeting Date
        txtMeetingTime.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                //set time to Text View
                txtMeetingTime.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(
                this,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }

        //Meeting Date Text View pops up the calendar to select the Meeting Date
        txtMeetingDate.setOnClickListener {
            DatePickerDialog(
                this,
                datePicker,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Firebase fireStore auth

        fStore = FirebaseFirestore.getInstance()

        val firebaseUser = firebaseAuth.currentUser
        val email = firebaseUser!!.email
        val userId = firebaseUser.uid

        binding.btnAddMeetingDb.setOnClickListener {
            val meetingTitle = binding.txtMeetingTitle.text.toString()
            val meetingDesc = binding.txtMeetingDesc.text.toString()
            val meetingVenue = binding.txtVenue.text.toString()
            val meetingDate = binding.txtMeetingDate.text.toString()
            val meetingTime = binding.txtMeetingTime.text.toString()

            // Getting user's "headOf" value
            fStore.collection("users").document(userId).get()
                .addOnCompleteListener { task: Task<DocumentSnapshot> ->
                    val document = task.result
                    val clubName = document.get("headOf").toString()

                    val meeting = Meeting(
                        meetingTitle,
                        meetingDesc,
                        meetingVenue,
                        meetingDate,
                        meetingTime,
                        clubName
                    )
                    fStore.collection("meeting").document().set(meeting)
                }

            binding.txtMeetingTitle.text.clear()
            binding.txtMeetingDesc.text.clear()
            binding.txtVenue.text.clear()
            binding.txtMeetingDate.text = ""
            binding.txtMeetingTime.text = ""

            Toast.makeText(this@AddMeetingActivity, "Meeting successfully added", Toast.LENGTH_SHORT).show()

        }
    }

    private fun updateLabel(myCalendar: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        txtMeetingDate.text = sdf.format(myCalendar.time)
    }
}

