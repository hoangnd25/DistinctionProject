package me.hoangnd.swin.distinctionproject.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.hoangnd.swin.distinctionproject.R;
import me.hoangnd.swin.distinctionproject.component.TagsCompletionView;
import me.hoangnd.swin.distinctionproject.data.Tag;
import me.hoangnd.swin.distinctionproject.data.Task;

public class EditTaskActivity extends AppCompatActivity {

    public static String ID_PARAM = "ID";

    boolean isEditing = false;
    Task task;
    String taskId;
    String receivedDescription = null;

    EditText nameInput;
    EditText dateInput;
    TextView descriptionText;
    TagsCompletionView tagInput;
    ArrayAdapter<Tag> tagAdapter;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        Button editDescriptionButton = (Button)findViewById(R.id.button_edit_description);
        editDescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent descriptionIntent = new Intent(EditTaskActivity.this, DescriptionActivity.class);
                descriptionIntent.putExtra("id", taskId);
                String description = task.getDescription() == null ? "" : task.getDescription();
                descriptionIntent.putExtra("description", description);
                startActivityForResult(descriptionIntent, 0);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("taskId", taskId);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        taskId = savedInstanceState.getString("taskId");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != 0 || data == null)
            return;

        if(data.getExtras() == null)
            return;

        taskId = data.getExtras().getString("id");
        receivedDescription = data.getExtras().getString("description");
    }

    @Override
    protected void onStart() {
        super.onStart();

        taskId = null;
        final Intent intent = getIntent();
        if(intent != null & intent.getExtras() != null){
            taskId = intent.getExtras().getString(ID_PARAM);
        }

        if(taskId == null){
            task = Task.newInstance();
        }else{
            task = Task.getById(taskId);
            setTitle(getResources().getString(R.string.title_activity_edit_task));
            isEditing = true;
        }

        nameInput = (EditText) findViewById(R.id.name_input);
        dateInput = (EditText) findViewById(R.id.due_date_input);

        nameInput.setText(task.getName());
        dateInput.setText(dateFormat.format(task.getDueDate()));

        descriptionText = (TextView)findViewById(R.id.label_description);
        receivedDescription = receivedDescription == null ? task.getDescription() : receivedDescription;
        receivedDescription = receivedDescription == null ? "" : receivedDescription;
        descriptionText.setText(Html.fromHtml(
                receivedDescription == null ? task.getDescription() : receivedDescription
        ));
        task.setDescription(receivedDescription);

        tagAdapter = new ArrayAdapter<Tag>(this, android.R.layout.simple_list_item_1);
        tagInput = (TagsCompletionView)findViewById(R.id.tag_input);
        tagInput.setAdapter(tagAdapter);

        Tag.getAll(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                tagAdapter.clear();
                for (ParseObject obj : objects) {
                    tagAdapter.add(Tag.newInstance(obj));
                }
            }
        }, true);

        tagInput.clear();
        for (Tag tag : task.getTags()){
            tagInput.addObject(tag);
        }
    }

    public void showDatePicker(View v) {
        DialogFragment datePickerFragment = DatePickerFragment.newInstance(task.getDueDate(), dateInput, dateFormat);
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void saveTask() {
        boolean cancel = false;
        Date date = new Date();
        String name = nameInput.getText().toString();

        if(name.trim().length() < 1){
            nameInput.setError(getResources().getString(R.string.error_field_required));
            cancel = true;
        }

        try{
            date = dateFormat.parse(dateInput.getText().toString());
        }catch (java.text.ParseException e){
            dateInput.setError(getResources().getString(R.string.error_invalid_date));
            cancel = true;
        }

        if(cancel)
            return;

        List<Tag> tags = tagInput.getObjects();
        for (Tag tag : tags){
            tag.save();
        }

        task
                .setName(name)
                .setDueDate(date)
                .setTags(tags)
                .save();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            saveTask();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        Date date;
        TextView dateInput;
        SimpleDateFormat dateFormat;

        public static DatePickerFragment newInstance(Date date, TextView dateInput, SimpleDateFormat format) {
            DatePickerFragment fragment = new DatePickerFragment();
            fragment.date = date;
            fragment.dateInput = dateInput;
            fragment.dateFormat = format;
            return fragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            c.setTime(date);
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            dateInput.setText(dateFormat.format(new Date(year, month, day)));
        }
    }
}
