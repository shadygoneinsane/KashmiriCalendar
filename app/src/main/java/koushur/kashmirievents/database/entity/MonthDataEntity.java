package koushur.kashmirievents.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "monthData")
public class MonthDataEntity {
    @NonNull
    @PrimaryKey
    public String date = "abc";
    @NonNull
    public String events = "abc";
}