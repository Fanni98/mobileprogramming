package hu.unimiskolc.iit.tutorapp

import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import hu.unimiskolc.iit.core.domain.Student
import hu.unimiskolc.iit.framework.db.TutorAppDatabase
import hu.unimiskolc.iit.framework.db.datasource.RoomStudentDataSource
import hu.unimiskolc.iit.framework.db.mapper.StudentMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AllStudentListAdapter(private val context: Fragment, private val students: List<Student>)
    : ArrayAdapter<Student>(context.requireActivity(), R.layout.list_student, students.toTypedArray()){

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.list_student, null, true)

        val db = Room.databaseBuilder(
            context.requireContext(),
            TutorAppDatabase::class.java, "tutorApp.db"
        ).build()

        val studentDataSource = RoomStudentDataSource(db.studentDao(), StudentMapper())

        rowView.setOnClickListener {
            context.findNavController()
                .navigate(
                    R.id.action_studentFragment_to_studentDataFragment,
                    bundleOf(
                        Pair("neptunId", students[position].neptunId),

                    )
                )
        }

        val removeButton = rowView.findViewById<ImageButton>(R.id.removeStudentButton)

        removeButton.setOnClickListener {
            uiScope.launch {
                studentDataSource.remove(students[position])

                val parentFragmentManager = context.parentFragmentManager

                parentFragmentManager
                    .beginTransaction()
                    .detach(context)
                    .commitNow()
                parentFragmentManager
                    .beginTransaction()
                    .attach(context)
                    .commitNow()
            }
        }

        val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.US)

        val nameText = rowView.findViewById(R.id.studentNameListItem) as TextView
        val birthDateText = rowView.findViewById(R.id.studentBirthDateListItem) as TextView

        nameText.text = students[position].name
        birthDateText.text = formatter.format(students[position].birthDate)

        return rowView
    }
}