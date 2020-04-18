package koushur.kashmirievents.database.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import vikesh.dass.lockmeout.local.entity.RunningLockProfile


/**
 * File Description
 * Author: Vikesh Dass
 * Created on: 30-03-2020
 */
@Dao
interface RunningLockProfileDao {
    @Query("SELECT * FROM RunningLockProfile WHERE profileId = (SELECT MAX(profileId)  FROM RunningLockProfile)")
    fun selectProfile(): Single<RunningLockProfile>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createProfile(profile: RunningLockProfile): Single<Long>

    @Update
    fun updateProfile(profile: RunningLockProfile): Completable

    @Query("DELETE FROM RunningLockProfile")
    fun deleteRunningProfiles(): Completable
}