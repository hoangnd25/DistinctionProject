package me.hoangnd.swin.distinctionproject.data;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Date;

public class Task {

    private ParseObject parseObject;
    private String name;
    private Date dueDate;

    public static Task newInstance(){
        Task task = new Task();
        task.parseObject = new ParseObject("Task");
        return task;
    }

    public Task save(){
        setUser();
        parseObject.saveEventually();
        return this;
    }

    public Task save(SaveCallback callback){
        setUser();
        parseObject.saveEventually(callback);
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
}
