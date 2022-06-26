package hu.unimiskolc.iit.tutorapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import hu.unimiskolc.iit.core.domain.Course
import hu.unimiskolc.iit.framework.db.TutorAppDatabase
import hu.unimiskolc.iit.framework.db.datasource.RoomCourseDataSource
import hu.unimiskolc.iit.framework.db.mapper.CourseMapper
import hu.unimiskolc.iit.tutorapp.R
import hu.unimiskolc.iit.tutorapp.databinding.AddCourseFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AddCourseFragment: Fragment() {
    private var _binding: AddCourseFragmentBinding? = null

    private val binding get() = _binding!!

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private lateinit var courseDataSource: RoomCourseDataSource

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddCourseFragmentBinding.inflate(inflater, container, false)

        val db = Room.databaseBuilder(
            this.requireContext(),
            TutorAppDatabase::class.java, "tutorApp.db"
        ).build()

        courseDataSource = RoomCourseDataSource(db.courseDao(), CourseMapper())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.submitButton.setOnClickListener {
            uiScope.launch {
                val title: String = binding.courseTitle.text.toString()
                val description: String = binding.description.text.toString()
                val maxNumber: String = binding.maxNumber.text.toString()
                val creditValue: String = binding.creditValue.text.toString()

                if(
                    title == "" ||
                    description == "" ||
                    maxNumber == "" ||
                    creditValue == ""

                ) {
                    Toast.makeText(
                        requireContext(),
                        "Missing form data.",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@launch
                }

                val course = Course(
                    0,
                    title,
                    description,
                    maxNumber.toInt(),
                    creditValue.toInt(),
                    listOf()
                )

                courseDataSource.add(course)

                Toast.makeText(
                    requireContext(),
                    "New course added successfully.",
                    Toast.LENGTH_SHORT
                ).show()

                findNavController()
                    .navigate(
                        R.id.action_AddCourseFragment_to_CourseFragment
                    )
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}