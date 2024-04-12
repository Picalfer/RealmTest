package com.example.realmtest

import android.app.Application
import com.example.realmtest.models.Address
import com.example.realmtest.models.Course
import com.example.realmtest.models.Student
import com.example.realmtest.models.Teacher
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class App : Application() {

    companion object {
        lateinit var realm: Realm
    }

    override fun onCreate() {
        super.onCreate()
        realm = Realm.open(
            configuration = RealmConfiguration.create(
                schema = setOf(
                    Address::class,
                    Teacher::class,
                    Course::class,
                    Student::class
                )
            )
        )
    }
}