package hu.unimiskolc.iit.tutorapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import hu.unimiskolc.iit.core.domain.Course
import hu.unimiskolc.iit.framework.db.TutorAppDatabase
import hu.unimiskolc.iit.framework.db.datasource.RoomCourseDataSource
import hu.unimiskolc.iit.framework.db.datasource.RoomStudentDataSource
import hu.unimiskolc.iit.framework.db.mapper.CourseMapper
import hu.unimiskolc.iit.framework.db.mapper.StudentMapper
import hu.unimiskolc.iit.tutorapp.R
import hu.unimiskolc.iit.tutorapp.databinding.CourseDataUpdateFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CourseDataUpdateFragment: Fragment() {
    private var _binding: CourseDataUpdateFragmentBinding? = null

    private val binding get() = _binding!!

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private lateinit var courseDataSource: RoomCourseDataSource

    private var course:Course? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CourseDataUpdateFragmentBinding.inflate(inflater, container, false)

        val db = Room.databaseBuilder(
            this.requireContext(),
            TutorAppDatabase::class.java, "tutorApp.db"
        ).build()

        courseDataSource = RoomCourseDataSource(db.courseDao(), CourseMapper())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val courseId = arguments?.get("courseId") as Int

        uiScope.launch {
            course = courseDataSource.fetchById(courseId)

            binding.idcoursetitle.setText(course?.title)
            binding.idcoursedescription.setText(course?.description)
            binding.idcoursemaxnumber.setText(course?.maxNumber.toString())
            binding.idcreditvalue.setText(course?.creditValue.toString())
        }

        binding.buttonUpdate.setOnClickListener{
            uiScope.launch {
                val title:String = binding.idcoursetitle.text.toString()
                val description:String = binding.idcoursedescription.text.toString()
                val maxNumber: Int? = binding.idcoursemaxnumber.text.toString().toIntOrNull()
                val creditValue: Int?= binding.idcreditvalue.text.toString().toIntOrNull()

                course?.title = if(course?.title != title && title !="") title else course?.title!!
                course?.description = if(course?.description != description && description !="") description else course?.description!!
                course?.maxNumber = if(course?.maxNumber != maxNumber && maxNumber !=null) maxNumber else course?.maxNumber!!
                course?.creditValue = if(course?.creditValue != creditValue && creditValue !=null) creditValue else course?.creditValue!!

                courseDataSource.update(course!!)

                Toast.makeText(
                    requireContext(),
                    "Your changes has been saved.",
                    Toast.LENGTH_SHORT
                ).show()

                findNavController()
                    .navigate(
                        R.id.action_courseDataUpdateFragment_to_courseDataFragment,
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