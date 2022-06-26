package hu.unimiskolc.iit.core.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Course(
    val id: Int,
    var title: String,
    var description: String,
    var maxNumber: Int,
    var creditValue:Int,
    val students: List<Student>
    ): Parcelable
