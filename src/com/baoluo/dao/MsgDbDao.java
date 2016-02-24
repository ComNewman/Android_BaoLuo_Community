package com.baoluo.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table MSG_DB.
*/
public class MsgDbDao extends AbstractDao<MsgDb, Long> {

    public static final String TABLENAME = "MSG_DB";

    /**
     * Properties of entity MsgDb.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property MyId = new Property(1, String.class, "myId", false, "MY_ID");
        public final static Property FromId = new Property(2, String.class, "fromId", false, "FROM_ID");
        public final static Property ToId = new Property(3, String.class, "toId", false, "TO_ID");
        public final static Property IsOut = new Property(4, Integer.class, "isOut", false, "IS_OUT");
        public final static Property MsgType = new Property(5, Integer.class, "msgType", false, "MSG_TYPE");
        public final static Property ContentType = new Property(6, Integer.class, "contentType", false, "CONTENT_TYPE");
        public final static Property Itime = new Property(7, java.util.Date.class, "itime", false, "ITIME");
        public final static Property Body = new Property(8, String.class, "body", false, "BODY");
        public final static Property IsRead = new Property(9, Integer.class, "isRead", false, "IS_READ");
        public final static Property ShowTimed = new Property(10, Integer.class, "showTimed", false, "SHOW_TIMED");
    };


    public MsgDbDao(DaoConfig config) {
        super(config);
    }
    
    public MsgDbDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'MSG_DB' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'MY_ID' TEXT NOT NULL ," + // 1: myId
                "'FROM_ID' TEXT," + // 2: fromId
                "'TO_ID' TEXT NOT NULL ," + // 3: toId
                "'IS_OUT' INTEGER," + // 4: isOut
                "'MSG_TYPE' INTEGER," + // 5: msgType
                "'CONTENT_TYPE' INTEGER," + // 6: contentType
                "'ITIME' INTEGER," + // 7: itime
                "'BODY' TEXT," + // 8: body
                "'IS_READ' INTEGER," + // 9: isRead
                "'SHOW_TIMED' INTEGER);"); // 10: showTimed
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'MSG_DB'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, MsgDb entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getMyId());
 
        String fromId = entity.getFromId();
        if (fromId != null) {
            stmt.bindString(3, fromId);
        }
        stmt.bindString(4, entity.getToId());
 
        Integer isOut = entity.getIsOut();
        if (isOut != null) {
            stmt.bindLong(5, isOut);
        }
 
        Integer msgType = entity.getMsgType();
        if (msgType != null) {
            stmt.bindLong(6, msgType);
        }
 
        Integer contentType = entity.getContentType();
        if (contentType != null) {
            stmt.bindLong(7, contentType);
        }
 
        java.util.Date itime = entity.getItime();
        if (itime != null) {
            stmt.bindLong(8, itime.getTime());
        }
 
        String body = entity.getBody();
        if (body != null) {
            stmt.bindString(9, body);
        }
 
        Integer isRead = entity.getIsRead();
        if (isRead != null) {
            stmt.bindLong(10, isRead);
        }
 
        Integer showTimed = entity.getShowTimed();
        if (showTimed != null) {
            stmt.bindLong(11, showTimed);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public MsgDb readEntity(Cursor cursor, int offset) {
        MsgDb entity = new MsgDb( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // myId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // fromId
            cursor.getString(offset + 3), // toId
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // isOut
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // msgType
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // contentType
            cursor.isNull(offset + 7) ? null : new java.util.Date(cursor.getLong(offset + 7)), // itime
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // body
            cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9), // isRead
            cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10) // showTimed
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, MsgDb entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setMyId(cursor.getString(offset + 1));
        entity.setFromId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setToId(cursor.getString(offset + 3));
        entity.setIsOut(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setMsgType(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setContentType(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setItime(cursor.isNull(offset + 7) ? null : new java.util.Date(cursor.getLong(offset + 7)));
        entity.setBody(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setIsRead(cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9));
        entity.setShowTimed(cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(MsgDb entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(MsgDb entity) {
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