package com.learn.todolistkotlin.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {
    @Update
    fun Update(taskModel: TaskModel)

    @Delete
    fun delete(taskModel: TaskModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(taskModel: TaskModel)

    @Query("SELECT * FROM tableTask Where completed=:completed")
    fun taskAll(completed: Boolean): LiveData<List<TaskModel>>

    @Query("SELECT * FROM tableTask Where completed=:completed AND date=:date")
    fun taskAll(completed: Boolean, date: Long): LiveData<List<TaskModel>>

    @Query("Delete FROM tableTask Where completed=1 ")
    fun deleteCompleted()

    @Query("Delete FROM tableTask")
    fun deleteAll()
}
