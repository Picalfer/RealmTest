package com.example.realmtest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realmtest.models.Address
import com.example.realmtest.models.Course
import com.example.realmtest.models.Student
import com.example.realmtest.models.Teacher
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val realm = App.realm

    val courses = realm
        .query<Course>(
            "teacher.address.fullName CONTAINS $0",
            "Sam"
        )
        .asFlow()
        .map { results ->
            results.list.toList()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    init {
        //createSampleEntries()
    }

    private fun createSampleEntries() {
        viewModelScope.launch {
            realm.write {
                val address1 = Address().apply {
                    fullName = "Mark Man"
                    street = "Mark Man Street"
                    houseNumber = 22
                    zip = 23425
                    city = "Mark City"
                }
                val address2 = Address().apply {
                    fullName = "Sam Man"
                    street = "Sam Man Street"
                    houseNumber = 42
                    zip = 22345
                    city = "Sam City"
                }

                val course1 = Course().apply {
                    name = "Kotlin"
                }
                val course2 = Course().apply {
                    name = "Android"
                }
                val course3 = Course().apply {
                    name = "Java"
                }

                val teacher1 = Teacher().apply {
                    address = address1
                    courses = realmListOf(course1, course2)
                }
                val teacher2 = Teacher().apply {
                    address = address2
                    courses = realmListOf(course3)
                }

                course1.teacher = teacher1
                course2.teacher = teacher1
                course3.teacher = teacher2

                address1.teacher = teacher1
                address2.teacher = teacher2

                val student1 = Student().apply {
                    name = "Jack Junior"
                }
                val student2 = Student().apply {
                    name = "Lech Junior"
                }

                course1.enrolledStudents.add(student1)
                course2.enrolledStudents.add(student2)
                course3.enrolledStudents.addAll(listOf(student1, student2))

                copyToRealm(teacher1, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(teacher2, updatePolicy = UpdatePolicy.ALL)

                copyToRealm(course1, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(course3, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(course2, updatePolicy = UpdatePolicy.ALL)

                copyToRealm(student1, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(student2, updatePolicy = UpdatePolicy.ALL)
            }
        }
    }
}