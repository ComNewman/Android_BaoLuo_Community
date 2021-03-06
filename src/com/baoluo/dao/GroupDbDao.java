package com.baoluo.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table GROUP_DB.
*/
public class GroupDbDao extends AbstractDao<GroupDb, Long> {

    public static final String TABLENAME = "GROUP_DB";

    /**
     * Properties of entity GroupDb.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property MyId = new Property(1, String.class, "myId", false, "MY_ID");
        public final static Property GroupType = new Property(2, Integer.class, "groupType", false, "GROUP_TYPE");
        public final static Property GroupId = new Property(3, String.class, "groupId", false, "GROUP_ID");
        public final static Property GroupName = new Property(4, String.class, "groupName", false, "GROUP_NAME");
        public final static Property GroupAvatar = new Property(5, String.class, "groupAvatar", false, "GROUP_AVATAR");
        public final static Property Describe = new Property(6, String.class, "describe", false, "DESCRIBE");
        public final static Property Owner = new Property(7, String.class, "owner", false, "OWNER");
        public final static Property Dnded = new Property(8, Integer.class, "dnded", false, "DNDED");
    };


    public GroupDbDao(DaoConfig config) {
        super(config);
    }
    
    public GroupDbDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'GROUP_DB' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'MY_ID' TEXT," + // 1: myId
                "'GROUP_TYPE' INTEGER," + // 2: groupType
                "'GROUP_ID' TEXT," + // 3: groupId
                "'GROUP_NAME' TEXT," + // 4: groupName
                "'GROUP_AVATAR' TEXT," + // 5: groupAvatar
                "'DESCRIBE' TEXT," + // 6: describe
                "'OWNER' TEXT," + // 7: owner
                "'DNDED' INTEGER);"); // 8: dnded
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'GROUP_DB'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, GroupDb entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String myId = entity.getMyId();
        if (myId != null) {
            stmt.bindString(2, myId);
        }
 
        Integer groupType = entity.getGroupType();
        if (groupType != null) {
            stmt.bindLong(3, groupType);
        }
 
        String groupId = entity.getGroupId();
        if (groupId != null) {
            stmt.bindString(4, groupId);
        }
 
        String groupName = entity.getGroupName();
        if (groupName != null) {
            stmt.bindString(5, groupName);
        }
 
        String groupAvatar = entity.getGroupAvatar();
        if (groupAvatar != null) {
            stmt.bindString(6, groupAvatar);
        }
 
        String describe = entity.getDescribe();
        if (describe != null) {
            stmt.bindString(7, describe);
        }
 
        String owner = entity.getOwner();
        if (owner != null) {
            stmt.bindString(8, owner);
        }
 
        Integer dnded = entity.getDnded();
        if (dnded != null) {
            stmt.bindLong(9, dnded);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public GroupDb readEntity(Cursor cursor, int offset) {
        GroupDb entity = new GroupDb( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // myId
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // groupType
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // groupId
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // groupName
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // groupAvatar
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // describe
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // owner
            cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8) // dnded
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, GroupDb entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setMyId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setGroupType(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setGroupId(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setGroupName(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setGroupAvatar(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setDescribe(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setOwner(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setDnded(cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(GroupDb entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(GroupDb entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
