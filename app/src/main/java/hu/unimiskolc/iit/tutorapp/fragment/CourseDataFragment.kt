package hu.unimiskolc.iit.tutorapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import hu.unimiskolc.iit.core.domain.Course
import hu.unimiskolc.iit.framework.db.TutorAppDatabase
import hu.unimiskolc.iit.framework.db.datasource.RoomCourseDataSource
import hu.unimiskolc.iit.framework.db.datasource.RoomStudentDataSource
import hu.unimiskolc.iit.framework.db.mapper.CourseMapper
import hu.unimiskolc.iit.framework.db.mapper.StudentMapper
import hu.unimiskolc.iit.tutorapp.CourseListAdapter
import hu.unimiskolc.iit.tutorapp.R
import hu.unimiskolc.iit.tutorapp.StudentListAdapter
import hu.unimiskolc.iit.tutorapp.databinding.CourseDataFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import hu.unimiskolc.iit.tutorapp.databinding.CourseFragmentBinding

class CourseDataFragment: Fragment() {
    private var _binding: CourseDataFragmentBinding? = null

    private val binding get() = _binding!!

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private lateinit var courseDataSource: RoomCourseDataSource

    private lateinit var studentDataSource:RoomStudentDataSource

    private lateinit var course:Course

    private val fragment = this

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CourseDataFragmentBinding.inflate(inflater, container, false)

        val db = Room.databaseBuilder(
            this.requireContext(),
            TutorAppDatabase::class.java, "tutorApp.db"
        ).build()

        courseDataSource = RoomCourseDataSource(db.courseDao(), CourseMapper())
        studentDataSource = RoomStudentDataSource(db.studentDao(), StudentMapper() )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val courseId = arguments?.get("courseId") as Int

        uiScope.launch {
            val courses = courseDataSource.fetchById(courseId)
            val students = studentDataSource.fetchByCourse(courseId)

            if (courses != null) {
                binding.courseIdData.text = courses.id.toString()
                binding.idcoursetitle.text = courses.title
                binding.idcoursedescription.text = courses.description
                binding.idcoursemaxnumber.text = courses.maxNumber.toString()
                binding.idcreditvalue.text= courses.creditValue.toString()
                binding.studentListView.adapter = StudentListAdapter(fragment,courses,students)
            }

        }

        binding.addStudentButton.setOnClickListener{
            uiScope.launch {
                findNavController()
                    .navigate(
                        R.id.action_courseDataFragment_to_AddStudentFragment,
                        bundleOf(Pair("courseId", courseId))
                    )
            }
        }

        binding.updateButton.setOnClickListener{
            uiScope.launch {
                findNavController()
                    .navigate(
                        R.id.action_courseDataFragment_to_courseDataUpdateFragment,
                        bundleOf(Pair("courseId", courseId))
                    )
            }
        }

        binding.backtomain.setOnClickListener{
            uiScope.launch {
                findNavController()
                    .navigate(
                        R.id.action_courseDataFragment_to_tutorMainFragment,
                    )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}