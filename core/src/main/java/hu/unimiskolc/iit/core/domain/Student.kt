package hu.unimiskolc.iit.core.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Student(
    val neptunId: Int,
    var name: String,
    var birthDate: Date,
    var course: Course?
): Parcelable
