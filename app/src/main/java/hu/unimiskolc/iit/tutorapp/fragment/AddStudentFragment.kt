package hu.unimiskolc.iit.tutorapp.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import hu.unimiskolc.iit.core.domain.Course
import hu.unimiskolc.iit.core.domain.Student
import hu.unimiskolc.iit.framework.db.TutorAppDatabase
import hu.unimiskolc.iit.framework.db.datasource.RoomCourseDataSource
import hu.unimiskolc.iit.framework.db.datasource.RoomStudentDataSource
import hu.unimiskolc.iit.framework.db.mapper.CourseMapper
import hu.unimiskolc.iit.framework.db.mapper.StudentMapper
import hu.unimiskolc.iit.tutorapp.R
import hu.unimiskolc.iit.tutorapp.databinding.AddCourseFragmentBinding
import hu.unimiskolc.iit.tutorapp.databinding.AddStudentFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddStudentFragment: Fragment() {
    private var _binding: AddStudentFragmentBinding? = null

    private val binding get() = _binding!!

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var birthDateCalendar: Calendar = Calendar.getInstance()

    private lateinit var studentDataSource: RoomStudentDataSource

    private lateinit var courseDataSource:RoomCourseDataSource

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddStudentFragmentBinding.inflate(inflater, container, false)

        val db = Room.databaseBuilder(
            this.requireContext(),
            TutorAppDatabase::class.java, "tutorApp.db"
        ).build()

        studentDataSource = RoomStudentDataSource(db.studentDao(), StudentMapper())
        courseDataSource = RoomCourseDataSource(db.courseDao(), CourseMapper())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.US)

            birthDateCalendar.set(Calendar.YEAR, year)
            birthDateCalendar.set(Calendar.MONTH, monthOfYear)
            birthDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            binding.birthDateValue.text = formatter.format(birthDateCalendar.time)
        }
        binding.birthDate.setOnClickListener {
            DatePickerDialog(
                this.requireContext(),
                dateSetListener,
                birthDateCalendar.get(Calendar.YEAR),
                birthDateCalendar.get(Calendar.MONTH),
                birthDateCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.submitButton.setOnClickListener {
            val courseId = arguments?.get("courseId") as Int

            uiScope.launch {
                val name: String = binding.name.text.toString()
                val birthdate: String = binding.birthDateValue.toString()

                if(
                    name == "" ||
                    birthdate == ""

                ) {
                    Toast.makeText(
                        requireContext(),
                        "Missing form data.",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@launch
                }

                val courses = courseDataSource.fetchById(courseId)

                val student = Student(
                    0,
                    name,
                    birthDate = birthDateCalendar.time,
                    courses,
                )

                studentDataSource.add(student)

                Toast.makeText(
                    requireContext(),
                    "New student added successfully.",
                    Toast.LENGTH_SHORT
                ).show()

                findNavController()
                    .navigate(
                        R.id.action_AddStudentFragment_to_courseDataFragment,
                        bundleOf(Pair("courseId", courseId))
                    )
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}