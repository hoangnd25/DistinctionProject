package me.hoangnd.swin.distinctionproject.data;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import me.hoangnd.swin.distinctionproject.filter.TaskFilter;

public class Task {

    public static String TABLE_NAME = "Task";

    private ParseObject parseObject;

    public static Task newInstance(){
        Task task = new Task();
        task.parseObject = new ParseObject(TABLE_NAME);
        task.setId(UUID.randomUUID().toString());
        return task;
    }

    public static Task newInstance(ParseObject parseObject){
        Task task = new Task();
        task.parseObject = parseObject;
        task.setId(UUID.randomUUID().toString());
        return task;
    }

    public static Task getById(String id){
        List<ParseObject> results = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_NAME);
        query.fromPin();
        query.whereEqualTo("objectId", id);
        try{
            results  = query.find();
        }catch (ParseException ex){}

        if(results != null){
            if(results.size() > 0)
                return Task.newInstance(results.get(0));
        }

        query = ParseQuery.getQuery(TABLE_NAME);
        query.fromPin();
        query.whereEqualTo("localId", id);

        try{
            results  = query.find();
        }catch (ParseException ex){}

        if(results != null){
            if(results.size() > 0)
                return Task.newInstance(results.get(0));
        }

        return null;
    }

    public static void getAll(FindCallback<ParseObject> callback, boolean local){
        getAll(null, callback, local);
    }

    public static void getAll(TaskFilter filter, FindCallback<ParseObject> callback, boolean local){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_NAME);
        if(local){
            query.fromPin();
        }
        query.include("tags");
        query.whereEqualTo("owner", ParseUser.getCurrentUser());
        if(filter != null){
            if(filter.getTagId() != null){
                Tag tag = Tag.getById(filter.getTagId());
                query.whereEqualTo("tags", tag.getParseObject());
            }
        }
        query.orderByDescending("dueDate");
        query.findInBackground(callback);
    }

    public Task delete(){
        parseObject.unpinInBackground();
        parseObject.deleteEventually();
        return this;
    }

    public Task save(){
        setUser();
        parseObject.pinInBackground();
        parseObject.saveEventually();
        return this;
    }

    protected void setUser(){
        parseObject.put("owner", ParseUser.getCurrentUser());
    }

    public String getParseId(){
        return parseObject.getObjectId();
    }

    public String getId() {
        return parseObject.getString("localId");
    }

    public Task setId(String id) {
        parseObject.put("localId", id);
        return this;
    }

    public String getName() {
        return parseObject.getString("name");
    }

    public Task setName(String name) {
        parseObject.put("name", name);
        return this;
    }
    public String getDescription() {
        return parseObject.getString("description");
    }

    public Task setDescription(String description) {
        parseObject.put("description", description);
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
        List<ParseObject> objects = new ArrayList<>();
        for(Tag tag : tags){
            objects.add(tag.getParseObject());
        }
        parseObject.remove("tags");
        parseObject.addAll("tags", objects);
        return this;
    }

    public List<Tag> getTags(){
        List<ParseObject> tags = parseObject.getList("tags");
        List<Tag> results = new ArrayList<>();

        if(tags == null){
            return results;
        }

        for (ParseObject obj : tags){
            results.add(Tag.newInstance(obj));
        }
        return  results;
    }

    @Override
    public String toString() {
        return getName();
    }
}
