package me.hoangnd.swin.distinctionproject.filter;

public class TaskFilter{
    public String tagId;

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public TaskFilter(String tagId) {
        this.tagId = tagId;
    }
}
