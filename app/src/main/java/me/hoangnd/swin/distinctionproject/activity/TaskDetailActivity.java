package me.hoangnd.swin.distinctionproject.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import me.hoangnd.swin.distinctionproject.R;
import me.hoangnd.swin.distinctionproject.data.Tag;
import me.hoangnd.swin.distinctionproject.data.Task;

public class TaskDetailActivity extends AppCompatActivity {

    public static String ID_PARAM = "ID";

    private String taskId;
    private Task task;
    private TextView dueDateLabel;
    private TextView tagLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        taskId = getIntent().getExtras().getString(ID_PARAM);;
        if(taskId == null)
            return;

        task = Task.getById(taskId);
        if(task == null)
            return;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(task == null)
            task = Task.getById(taskId);
        if(task == null)
            return;

        setTitle(task.getName());

        dueDateLabel = (TextView)findViewById(R.id.label_due_date_value);
        tagLabel = (TextView)findViewById(R.id.label_tag_value);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        dueDateLabel.setText(sdf.format(task.getDueDate()));

        List<Tag> tags = task.getTags();
        String tagString = "";
        for (int i = 0; i < tags.size(); i++) {
            tagString += tags.get(i).getName();
            tagString += i == tags.size() - 1 ? "  " : ",  ";
        }
        tagLabel.setText(tagString);
    }

    protected void deleteTask(){
        if(task == null)
            return;

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.action_delete)
                .setMessage(R.string.message_confirm_delete_task)
                .setPositiveButton(R.string.action_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        task.delete();
                        finish();
                        TaskDetailActivity.this.finish();
                    }
                })
                .setNegativeButton(R.string.action_no, null)
                .show();
    }

    protected void editTask(){
        Intent intent = new Intent(this, EditTaskActivity.class);
        if(task.getParseId() != null){
            intent.putExtra(TaskDetailActivity.ID_PARAM, task.getParseId());
        }else{
            intent.putExtra(TaskDetailActivity.ID_PARAM, task.getId());
        }
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete) {
            deleteTask();
            return true;
        }else if(id == R.id.action_edit){
            editTask();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
