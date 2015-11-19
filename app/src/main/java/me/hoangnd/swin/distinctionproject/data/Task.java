package me.hoangnd.swin.distinctionproject.data;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Date;
import java.util.List;

public class Task {

    public static String TABLE_NAME = "Task";

    private ParseObject parseObject;

    public static Task newInstance(){
        Task task = new Task();
        task.parseObject = new ParseObject(TABLE_NAME);
        return task;
    }

    public static Task newInstance(ParseObject parseObject){
        Task task = new Task();
        task.parseObject = parseObject;
        return task;
    }

    public static void getAll(FindCallback<ParseObject> callback, boolean local){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_NAME);
        if(local)
            query.fromPin(TABLE_NAME);
        query.whereEqualTo("owner", ParseUser.getCurrentUser());
        query.orderByDescending("dueDate");
        query.findInBackground(callback);
    }

    public Task save(){
        setUser();
        parseObject.pinInBackground(TABLE_NAME);
        parseObject.saveEventually();
        return this;
    }

    protected void setUser(){
        parseObject.put("owner", ParseUser.getCurrentUser());
    }

    public String getName() {
        return parseObject.getString("name");
    }

    public Task setName(String name) {
        parseObject.put("name", name);
        return this;
    }

    public Date getDueDate() {
        Date date = parseObject.getDate("dueDate");
        if (date == null){
            date = new Date();
            setDueDate(date);
        }
        return date;
    }

    public Task setDueDate(Date dueDate) {
        parseObject.put("dueDate", dueDate);
        return this;
    }

    public Task setTags(List<Tag> tags){
        ParseRelation<ParseObject> relation = parseObject.getRelation("tags");
        for(Tag tag : tags){
            relation.add(tag.getParseObject());
        }

        return this;
    }

    @Override
    public String toString() {
        return getName();
    }
}
