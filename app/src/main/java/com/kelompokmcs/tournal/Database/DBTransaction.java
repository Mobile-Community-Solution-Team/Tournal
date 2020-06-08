package com.kelompokmcs.tournal.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kelompokmcs.tournal.Model.Agenda;
import com.kelompokmcs.tournal.Model.Announcement;
import com.kelompokmcs.tournal.Model.Group;
import com.kelompokmcs.tournal.Model.Symbol;

import java.util.ArrayList;

public class DBTransaction {
    private DBHelper dbHelper;

    public DBTransaction(Context context){
        dbHelper = new DBHelper(context);
    }

    public ArrayList<Group> getGroupList(){
        ArrayList<Group> groups = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(dbHelper.TABLE_GROUP, null,null,null,null,null,null);
        while (cursor.moveToNext()){
            int groupId = cursor.getInt(cursor.getColumnIndex(dbHelper.COL_GROUP_ID));
            String groupCode = cursor.getString(cursor.getColumnIndex(dbHelper.COL_GROUP_CODE));
            String groupName = cursor.getString(cursor.getColumnIndex(dbHelper.COL_GROUP_NAME));
            String groupLocation = cursor.getString(cursor.getColumnIndex(dbHelper.COL_GROUP_LOCATION));
            String startDate = cursor.getString(cursor.getColumnIndex(dbHelper.COL_START_DATE));
            String endDate = cursor.getString(cursor.getColumnIndex(dbHelper.COL_END_DATE));
            String groupPass = cursor.getString(cursor.getColumnIndex(dbHelper.COL_GROUP_PASS));

            groups.add(new Group(groupId,groupCode,groupName,groupLocation,startDate,endDate,groupPass));
        }
        db.close();
        return groups;
    }

    public ArrayList<Agenda> getAllAgenda(){
        ArrayList<Agenda> agendaArrayList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + dbHelper.TABLE_AGENDA +
                " JOIN " + dbHelper.TABLE_GROUP +
                " ON " + dbHelper.TABLE_AGENDA + "." + dbHelper.COL_GROUP_ID + "=" +
                dbHelper.TABLE_GROUP + "." + dbHelper.COL_GROUP_ID,null);

        while (cursor.moveToNext()){
            int agendaId = cursor.getInt(cursor.getColumnIndex(dbHelper.COL_AGENDA_ID));
            int groupId = cursor.getInt(cursor.getColumnIndex(dbHelper.COL_GROUP_ID));
            String groupName = cursor.getString(cursor.getColumnIndex(dbHelper.COL_GROUP_NAME));
            String agendaTitle = cursor.getString(cursor.getColumnIndex(dbHelper.COL_AGENDA_TITLE));
            String agendaDesc = cursor.getString(cursor.getColumnIndex(dbHelper.COL_AGENDA_DESC));
            double startLat = cursor.getDouble(cursor.getColumnIndex(dbHelper.COL_START_LAT));
            double startLng = cursor.getDouble(cursor.getColumnIndex(dbHelper.COL_START_LNG));
            double startAlt = cursor.getDouble(cursor.getColumnIndex(dbHelper.COL_START_ALT));
            double endLat = cursor.getDouble(cursor.getColumnIndex(dbHelper.COL_END_LAT));
            double endLng = cursor.getDouble(cursor.getColumnIndex(dbHelper.COL_END_LNG));
            double endAlt = cursor.getDouble(cursor.getColumnIndex(dbHelper.COL_END_ALT));
            String startTime = cursor.getString(cursor.getColumnIndex(dbHelper.COL_START_TIME));
            String endTime = cursor.getString(cursor.getColumnIndex(dbHelper.COL_END_TIME));

            agendaArrayList.add(new Agenda(agendaId,groupId,groupName,agendaTitle,agendaDesc,startLat,startLng,startAlt,endLat,endLng,endAlt,startTime,endTime));
        }
        db.close();
        return agendaArrayList;
    }

    public Group getGroupDataById(int groupId){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = "group_id = ?";
        String[] selectionArgs = new String[]{String.valueOf(groupId)};

        Cursor cursor = db.query(dbHelper.TABLE_GROUP, null,selection,selectionArgs,null,null,null);

        //data yang didapatkan pasti 1, jadi tidak perlu looping
        cursor.moveToNext();
        String groupCode = cursor.getString(cursor.getColumnIndex(dbHelper.COL_GROUP_CODE));
        String groupName = cursor.getString(cursor.getColumnIndex(dbHelper.COL_GROUP_NAME));
        String groupLocation = cursor.getString(cursor.getColumnIndex(dbHelper.COL_GROUP_LOCATION));
        String startDate = cursor.getString(cursor.getColumnIndex(dbHelper.COL_START_DATE));
        String endDate = cursor.getString(cursor.getColumnIndex(dbHelper.COL_END_DATE));
        String groupPass = cursor.getString(cursor.getColumnIndex(dbHelper.COL_GROUP_PASS));

        db.close();
        return new Group(groupId,groupCode,groupName,groupLocation,startDate,endDate,groupPass);
    }

    public ArrayList<Announcement> getAnnouncementById(int groupId){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<Announcement> announcementArrayList = new ArrayList<>();
        String selection = "group_id = ?";
        String[] selectionArgs = new String[]{String.valueOf(groupId)};

        Cursor cursor = db.query(dbHelper.TABLE_ANNOUNCEMENT, null,selection,selectionArgs,null,null,dbHelper.COL_DATE_AND_TIME +" DESC");

        while(cursor.moveToNext()){
            int announcementId = cursor.getInt(cursor.getColumnIndex(dbHelper.COL_ANNOUNCEMENT_ID));
            String announcementTitle = cursor.getString(cursor.getColumnIndex(dbHelper.COL_ANNOUNCEMENT_TITLE));
            String announcementDesc = cursor.getString(cursor.getColumnIndex(dbHelper.COL_ANNOUNCEMENT_DESC));
            String dateAndTime = cursor.getString(cursor.getColumnIndex(dbHelper.COL_DATE_AND_TIME));
            String userName = cursor.getString(cursor.getColumnIndex(dbHelper.COL_USER_NAME));
            String userEmail = cursor.getString(cursor.getColumnIndex(dbHelper.COL_USER_EMAIL));
            String userPhoto = cursor.getString(cursor.getColumnIndex(dbHelper.COL_USER_PHOTO));

            announcementArrayList.add(new Announcement(announcementId,groupId,announcementTitle,announcementDesc,dateAndTime,userName,userEmail,userPhoto));
        }
        db.close();
        return announcementArrayList;
    }

    public ArrayList<Agenda> getAgendaById(int groupId){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<Agenda> agendaArrayList = new ArrayList<>();
        String selection = "group_id = ?";
        String[] selectionArgs = new String[]{String.valueOf(groupId)};

        Cursor cursor = db.query(dbHelper.TABLE_AGENDA, null,selection,selectionArgs,null,null,dbHelper.COL_START_TIME);

        while(cursor.moveToNext()){
            int agendaId = cursor.getInt(cursor.getColumnIndex(dbHelper.COL_AGENDA_ID));
            String agendaTitle = cursor.getString(cursor.getColumnIndex(dbHelper.COL_AGENDA_TITLE));
            String agendaDesc = cursor.getString(cursor.getColumnIndex(dbHelper.COL_AGENDA_DESC));
            double startLat = cursor.getDouble(cursor.getColumnIndex(dbHelper.COL_START_LAT));
            double startLng = cursor.getDouble(cursor.getColumnIndex(dbHelper.COL_START_LNG));
            double startAlt = cursor.getDouble(cursor.getColumnIndex(dbHelper.COL_START_ALT));
            double endLat = cursor.getDouble(cursor.getColumnIndex(dbHelper.COL_END_LAT));
            double endLng = cursor.getDouble(cursor.getColumnIndex(dbHelper.COL_END_LNG));
            double endAlt = cursor.getDouble(cursor.getColumnIndex(dbHelper.COL_END_ALT));
            String startTime = cursor.getString(cursor.getColumnIndex(dbHelper.COL_START_TIME));
            String endTime = cursor.getString(cursor.getColumnIndex(dbHelper.COL_END_TIME));

            agendaArrayList.add(new Agenda(agendaId,groupId,"",agendaTitle,agendaDesc,startLat,startLng,startAlt,endLat,endLng,endAlt,startTime,endTime));
        }
        db.close();
        return agendaArrayList;
    }

    public ArrayList<Symbol> getSymbolById(int groupId){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<Symbol> symbolArrayList = new ArrayList<>();
        String selection = "group_id = ?";
        String[] selectionArgs = new String[]{String.valueOf(groupId)};

        Cursor cursor = db.query(dbHelper.TABLE_SYMBOL, null,selection,selectionArgs,null,null,null);

        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(dbHelper.COL_ID));
            int symbolId = cursor.getInt(cursor.getColumnIndex(dbHelper.COL_SYMBOL_ID));
            double lat = cursor.getDouble(cursor.getColumnIndex(dbHelper.COL_LAT));
            double lng = cursor.getDouble(cursor.getColumnIndex(dbHelper.COL_LNG));
            double alt = cursor.getDouble(cursor.getColumnIndex(dbHelper.COL_ALT));

            symbolArrayList.add(new Symbol(id,groupId,lat,lng,alt,symbolId));
        }
        db.close();
        return symbolArrayList;
    }

    public void addNewGroup(Group group){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dbHelper.COL_GROUP_ID, group.getGroupId());
        cv.put(dbHelper.COL_GROUP_CODE, group.getGroupCode());
        cv.put(dbHelper.COL_GROUP_NAME, group.getGroupName());
        cv.put(dbHelper.COL_GROUP_LOCATION,group.getGroupLocation());
        cv.put(dbHelper.COL_START_DATE,group.getStartDate());
        cv.put(dbHelper.COL_END_DATE, group.getEndDate());
        cv.put(dbHelper.COL_GROUP_PASS,group.getGroupPass());

        db.insert(dbHelper.TABLE_GROUP,null,cv);
        db.close();
    }


    public void addGroupDatas(ArrayList<Group> groupArrayList){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (Group group : groupArrayList) {
            ContentValues cv = new ContentValues();
            cv.put(dbHelper.COL_GROUP_ID, group.getGroupId());
            cv.put(dbHelper.COL_GROUP_CODE, group.getGroupCode());
            cv.put(dbHelper.COL_GROUP_NAME, group.getGroupName());
            cv.put(dbHelper.COL_GROUP_LOCATION,group.getGroupLocation());
            cv.put(dbHelper.COL_START_DATE,group.getStartDate());
            cv.put(dbHelper.COL_END_DATE, group.getEndDate());
            cv.put(dbHelper.COL_GROUP_PASS,group.getGroupPass());

            db.insert(dbHelper.TABLE_GROUP,null,cv);
        }
        db.close();
    }

    public void addAnnouncementDatas(ArrayList<Announcement> announcementArrayList){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (Announcement announcement : announcementArrayList) {
            ContentValues cv = new ContentValues();
            cv.put(dbHelper.COL_ANNOUNCEMENT_ID, announcement.getAnnouncementId());
            cv.put(dbHelper.COL_GROUP_ID, announcement.getGroupId());
            cv.put(dbHelper.COL_ANNOUNCEMENT_TITLE, announcement.getAnnouncementTitle());
            cv.put(dbHelper.COL_ANNOUNCEMENT_DESC, announcement.getAnnouncementDesc());
            cv.put(dbHelper.COL_DATE_AND_TIME,announcement.getDateAndTime());
            cv.put(dbHelper.COL_USER_NAME, announcement.getUserName());
            cv.put(dbHelper.COL_USER_EMAIL, announcement.getUserEmail());
            cv.put(dbHelper.COL_USER_PHOTO, announcement.getUserPhoto());

            db.insert(dbHelper.TABLE_ANNOUNCEMENT,null,cv);
        }
        db.close();
    }

    public void addAgendaDatas(ArrayList<Agenda> agendaArrayList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (Agenda agenda : agendaArrayList) {
            ContentValues cv = new ContentValues();
            cv.put(dbHelper.COL_AGENDA_ID, agenda.getAgendaId());
            cv.put(dbHelper.COL_GROUP_ID, agenda.getGroupId());
            cv.put(dbHelper.COL_AGENDA_TITLE, agenda.getAgendaTitle());
            cv.put(dbHelper.COL_AGENDA_DESC, agenda.getAgendaDesc());
            cv.put(dbHelper.COL_START_LAT, agenda.getStartLat());
            cv.put(dbHelper.COL_START_LNG, agenda.getStartLng());
            cv.put(dbHelper.COL_START_ALT, agenda.getStartAlt());
            cv.put(dbHelper.COL_END_LAT, agenda.getEndLat());
            cv.put(dbHelper.COL_END_LNG, agenda.getEndLng());
            cv.put(dbHelper.COL_END_ALT, agenda.getEndAlt());
            cv.put(dbHelper.COL_START_TIME,agenda.getStartTime());
            cv.put(dbHelper.COL_END_TIME,agenda.getEndTime());

            db.insert(dbHelper.TABLE_AGENDA,null,cv);
        }
        db.close();
    }

    public void addSymbolDatas(ArrayList<Symbol> symbolArrayList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (Symbol symbol : symbolArrayList) {
            ContentValues cv = new ContentValues();
            cv.put(dbHelper.COL_ID, symbol.getId());
            cv.put(dbHelper.COL_GROUP_ID, symbol.getGroupid());
            cv.put(dbHelper.COL_LAT, symbol.getLat());
            cv.put(dbHelper.COL_LNG, symbol.getLng());
            cv.put(dbHelper.COL_ALT, symbol.getAlt());
            cv.put(dbHelper.COL_SYMBOL_ID, symbol.getSymbolId());

            db.insert(dbHelper.TABLE_SYMBOL,null,cv);
        }
        db.close();
    }

    public void deleteAllTable(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(dbHelper.TABLE_GROUP,null,null);
        db.delete(dbHelper.TABLE_AGENDA,null,null);
        db.delete(dbHelper.TABLE_ANNOUNCEMENT,null,null);
        db.delete(dbHelper.TABLE_SYMBOL,null,null);

        db.close();
    }

    public void deleteAgendaTable(int groupId){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = dbHelper.COL_GROUP_ID+" = ?";
        String[] whereArgs = new String[]{String.valueOf(groupId)};

        db.delete(dbHelper.TABLE_AGENDA,whereClause,whereArgs);
        db.close();
    }

    public void deleteAnnouncementTable(int groupId){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = dbHelper.COL_GROUP_ID+" = ?";
        String[] whereArgs = new String[]{String.valueOf(groupId)};

        db.delete(dbHelper.TABLE_ANNOUNCEMENT,whereClause,whereArgs);
        db.close();
    }
}
