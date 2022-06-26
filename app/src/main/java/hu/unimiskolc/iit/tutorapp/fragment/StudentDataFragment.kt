package hu.unimiskolc.iit.tutorapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import hu.unimiskolc.iit.framework.db.TutorAppDatabase
import hu.unimiskolc.iit.framework.db.datasource.RoomStudentDataSource
import hu.unimiskolc.iit.framework.db.mapper.StudentMapper
import hu.unimiskolc.iit.tutorapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import hu.unimiskolc.iit.tutorapp.databinding.StudentDataFragmentBinding
import java.text.SimpleDateFormat

class StudentDataFragment: Fragment() {
    private var _binding: StudentDataFragmentBinding? = null

    private val binding get() = _binding!!

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private lateinit var studentDataSource:RoomStudentDataSource

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = StudentDataFragmentBinding.inflate(inflater, container, false)

        val db = Room.databaseBuilder(
            this.requireContext(),
            TutorAppDatabase::class.java, "tutorApp.db"
        ).build()

        studentDataSource = RoomStudentDataSource(db.studentDao(), StudentMapper() )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val neptunId = arguments?.get("neptunId") as Int

        val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.US)

        uiScope.launch {
            val students = studentDataSource.fetchById(neptunId)

            if (students != null) {
                binding.idstudentname.text = students.name
                binding.idbirthday.text = formatter.format(students.birthDate)
            }

        }
        binding.back.setOnClickListener {
            uiScope.launch {
                findNavController()
                    .navigate(
                        R.id.action_studentDataFragment_to_tutorMainFragment,
                        bundleOf(
                            Pair("neptunId", neptunId),
                        )
                    )
            }
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}