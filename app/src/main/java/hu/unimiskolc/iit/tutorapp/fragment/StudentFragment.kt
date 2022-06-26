package hu.unimiskolc.iit.tutorapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.room.Room
import hu.unimiskolc.iit.core.domain.Student
import hu.unimiskolc.iit.framework.db.TutorAppDatabase
import hu.unimiskolc.iit.framework.db.datasource.RoomStudentDataSource
import hu.unimiskolc.iit.framework.db.mapper.StudentMapper
import hu.unimiskolc.iit.tutorapp.AllStudentListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import hu.unimiskolc.iit.tutorapp.databinding.StudentFragmentBinding

class StudentFragment: Fragment() {
    private var _binding: StudentFragmentBinding? = null

    private val binding get() = _binding!!

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private lateinit var studentDataSource: RoomStudentDataSource

    private val fragment = this

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = StudentFragmentBinding.inflate(inflater, container, false)

        val db = Room.databaseBuilder(
            this.requireContext(),
            TutorAppDatabase::class.java, "tutorApp.db"
        ).build()

        studentDataSource = RoomStudentDataSource(db.studentDao(), StudentMapper())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        uiScope.launch {
            val students = studentDataSource.fetchAll()
            binding.studentListView.adapter = AllStudentListAdapter(fragment, students)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}