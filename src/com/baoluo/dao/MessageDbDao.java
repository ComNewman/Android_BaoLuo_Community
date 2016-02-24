package com.baoluo.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table MESSAGE_DB.
*/
public class MessageDbDao extends AbstractDao<MessageDb, Long> {

    public static final String TABLENAME = "MESSAGE_DB";

    /**
     * Properties of entity MessageDb.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property MyId = new Property(1, String.class, "myId", false, "MY_ID");
        public final static Property ToId = new Property(2, String.class, "toId", false, "TO_ID");
        public final static Property MsgType = new Property(3, Integer.class, "msgType", false, "MSG_TYPE");
        public final static Property ITime = new Property(4, java.util.Date.class, "iTime", false, "I_TIME");
        public final static Property SetTopTime = new Property(5, java.util.Date.class, "setTopTime", false, "SET_TOP_TIME");
    };


    public MessageDbDao(DaoConfig config) {
        super(config);
    }
    
    public MessageDbDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'MESSAGE_DB' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'MY_ID' TEXT," + // 1: myId
                "'TO_ID' TEXT," + // 2: toId
                "'MSG_TYPE' INTEGER," + // 3: msgType
                "'I_TIME' INTEGER," + // 4: iTime
                "'SET_TOP_TIME' INTEGER);"); // 5: setTopTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'MESSAGE_DB'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, MessageDb entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String myId = entity.getMyId();
        if (myId != null) {
            stmt.bindString(2, myId);
        }
 
        String toId = entity.getToId();
        if (toId != null) {
            stmt.bindString(3, toId);
        }
 
        Integer msgType = entity.getMsgType();
        if (msgType != null) {
            stmt.bindLong(4, msgType);
        }
 
        java.util.Date iTime = entity.getITime();
        if (iTime != null) {
            stmt.bindLong(5, iTime.getTime());
        }
 
        java.util.Date setTopTime = entity.getSetTopTime();
        if (setTopTime != null) {
            stmt.bindLong(6, setTopTime.getTime());
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public MessageDb readEntity(Cursor cursor, int offset) {
        MessageDb entity = new MessageDb( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // myId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // toId
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // msgType
            cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)), // iTime
            cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)) // setTopTime
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, MessageDb entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setMyId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setToId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setMsgType(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setITime(cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)));
        entity.setSetTopTime(cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(MessageDb entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(MessageDb entity) {
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