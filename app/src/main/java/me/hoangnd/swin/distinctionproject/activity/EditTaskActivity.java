package me.hoangnd.swin.distinctionproject.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.hoangnd.swin.distinctionproject.R;
import me.hoangnd.swin.distinctionproject.data.Task;

public class EditTaskActivity extends AppCompatActivity {

    Task task;

    EditText nameInput;
    EditText dateInput;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        task = Task.newInstance();

        nameInput = (EditText) findViewById(R.id.name_input);
        dateInput = (EditText) findViewById(R.id.due_date_input);
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

        task
                .setName(name)
                .setDueDate(date)
                .save(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null)
                            finish();
                    }
                });
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
