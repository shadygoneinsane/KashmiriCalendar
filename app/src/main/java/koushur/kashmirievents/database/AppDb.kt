package vikesh.dass.lockmeout.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import vikesh.dass.lockmeout.local.dao.RunningLockProfileDao
import vikesh.dass.lockmeout.local.dao.ScheduleLockProfileDao
import vikesh.dass.lockmeout.local.entity.RunningLockProfile
import vikesh.dass.lockmeout.local.entity.ScheduleLockProfile

/****
 * Application Database
 **/
@Database(entities = [ScheduleLockProfile::class, RunningLockProfile::class],
        version = 2, exportSchema = false)
abstract class AppDb : RoomDatabase() {

    abstract fun scheduleLockProfileDao(): ScheduleLockProfileDao

    abstract fun runningLockProfileDao(): RunningLockProfileDao
}