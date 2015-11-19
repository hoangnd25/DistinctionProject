package me.hoangnd.swin.distinctionproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import me.hoangnd.swin.distinctionproject.R;
import me.hoangnd.swin.distinctionproject.data.Tag;
import me.hoangnd.swin.distinctionproject.data.Task;
import me.hoangnd.swin.distinctionproject.fragment.LoginFragment;
import me.hoangnd.swin.distinctionproject.fragment.RegisterFragment;
import me.hoangnd.swin.distinctionproject.fragment.TaskListFragment;

public class TagDetailActivity extends AppCompatActivity implements
        TaskListFragment.OnTaskListInteractionListener{

    public static String ID_PARAM = "ID";

    private String tagId;
    private Tag tag;
    private TaskListFragment taskListFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_detail);


        tagId = getIntent().getExtras().getString(ID_PARAM);;
        if(tagId == null)
            return;

        tag = Tag.getById(tagId);
        if(tagId == null)
            return;

        taskListFragment = TaskListFragment.newInstance(tag.getParseId(), null);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_containter, taskListFragment);
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(tag == null)
            tag = Tag.getById(tagId);
        if(tag == null)
            return;

        setTitle("Tag: "+tag.getName());
    }

    @Override
    public void onTaskClicked(Task task) {
        finish();
        Intent intent = new Intent(this, TaskDetailActivity.class);
        if(task.getParseId() != null){
            intent.putExtra(TaskDetailActivity.ID_PARAM, task.getParseId());
        }else{
            intent.putExtra(TaskDetailActivity.ID_PARAM, task.getId());
        }
        startActivity(intent);
    }
}
