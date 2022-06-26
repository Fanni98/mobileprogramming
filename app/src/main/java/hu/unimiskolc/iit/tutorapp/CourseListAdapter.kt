package hu.unimiskolc.iit.tutorapp

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import hu.unimiskolc.iit.core.domain.Course
import hu.unimiskolc.iit.framework.db.TutorAppDatabase
import hu.unimiskolc.iit.framework.db.datasource.RoomCourseDataSource
import hu.unimiskolc.iit.framework.db.mapper.CourseMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CourseListAdapter(private val context: Fragment,  private val courses: List<Course>)
    : ArrayAdapter<Course>(context.requireActivity(), R.layout.list_course, courses.toTypedArray()){

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.list_course, null, true)

        val db = Room.databaseBuilder(
            context.requireContext(),
            TutorAppDatabase::class.java, "tutorApp.db"
        ).build()

        val courseDataSource = RoomCourseDataSource(db.courseDao(), CourseMapper())

        rowView.setOnClickListener {
            context.findNavController()
                .navigate(
                    R.id.action_CourseFragment_to_courseDataFragment,
                    bundleOf(
                        Pair("courseId", courses[position].id),
                    )
                )
        }

        val removeButton = rowView.findViewById<ImageButton>(R.id.removeCourseButton)

        removeButton.setOnClickListener {
            uiScope.launch {
                courseDataSource.remove(courses[position])

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

        val titleText = rowView.findViewById(R.id.courseTitleListItem) as TextView
        val descriptionText = rowView.findViewById(R.id.courseDescriptionListItem) as TextView

        titleText.text = courses[position].title
        descriptionText.text = courses[position].description

        return rowView
    }
}