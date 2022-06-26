package hu.unimiskolc.iit.tutorapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import hu.unimiskolc.iit.framework.db.TutorAppDatabase
import hu.unimiskolc.iit.framework.db.datasource.RoomCourseDataSource
import hu.unimiskolc.iit.framework.db.mapper.CourseMapper
import hu.unimiskolc.iit.tutorapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import hu.unimiskolc.iit.tutorapp.databinding.TutorMainFragmentBinding

class TutorMainFragment: Fragment() {
    private var _binding: TutorMainFragmentBinding? = null

    private val binding get() = _binding!!

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private lateinit var courseDataSource: RoomCourseDataSource

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TutorMainFragmentBinding.inflate(inflater, container, false)

        val db = Room.databaseBuilder(
            this.requireContext(),
            TutorAppDatabase::class.java, "tutorApp.db"
        ).build()

        courseDataSource = RoomCourseDataSource(db.courseDao(), CourseMapper())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addNewCourseButton.setOnClickListener {
            uiScope.launch {
                findNavController()
                    .navigate(
                        R.id.action_tutorMainFragment_to_AddCourseFragment
                    )
            }
        }
        binding.courseListView.setOnClickListener{
            uiScope.launch {
                findNavController()
                    .navigate(
                        R.id.action_tutorMainFragment_to_CourseFragment
                    )
            }
        }
        binding.studentListView.setOnClickListener{
            findNavController()
                .navigate(
                    R.id.action_tutorMainFragment_to_studentFragment
                )
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}