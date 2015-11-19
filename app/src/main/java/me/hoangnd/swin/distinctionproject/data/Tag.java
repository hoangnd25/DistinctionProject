package me.hoangnd.swin.distinctionproject.data;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class Tag {

    public static String TABLE_NAME = "Tag";

    private ParseObject parseObject;

    public static Tag newInstance(String name){
        Tag tag = new Tag();
        tag.parseObject = new ParseObject(TABLE_NAME);
        tag.parseObject.put("owner", ParseUser.getCurrentUser());
        tag.setName(name);
        return tag;
    }

    public static Tag newInstance(ParseObject parseObject){
        Tag tag = new Tag();
        tag.parseObject = parseObject;
        tag.parseObject.put("owner", ParseUser.getCurrentUser());
        return tag;
    }

    public Tag save(){
        parseObject.pinInBackground();
        parseObject.saveEventually();
        return this;
    }

    public static void getAll(FindCallback<ParseObject> callback, boolean local){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_NAME);
        if(local)
            query.fromPin();
        query.whereEqualTo("owner", ParseUser.getCurrentUser());
        query.orderByAscending("name");
        query.findInBackground(callback);
    }

    public static Tag getByName(String name){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_NAME);
        query.fromPin();
        query.whereEqualTo("owner", ParseUser.getCurrentUser());
        query.whereEqualTo("name", name);
        List<ParseObject> results = null;
        try{
            results = query.find();
            if(results.size() > 0)
                return Tag.newInstance(results.get(0));
            return null;
        }catch (ParseException ex){
        }

        return null;
    }

    public String getName() {
        return parseObject.getString("name");
    }

    public Tag setName(String name) {
        parseObject.put("name", name);
        return this;
    }

    public ParseObject getParseObject(){
        return parseObject;
    }

    @Override
    public String toString() {
        return getName();
    }
}
