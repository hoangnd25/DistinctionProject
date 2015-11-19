package me.hoangnd.swin.distinctionproject.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ToggleButton;


import me.hoangnd.swin.distinctionproject.R;
import windyzboy.github.io.customeeditor.CustomEditText;

public class DescriptionActivity extends AppCompatActivity {

    private String taskId;
    private CustomEditText customEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        customEditor = (CustomEditText) findViewById(R.id.editor);
        ToggleButton boldToggle = (ToggleButton) findViewById(R.id.button_bold);
        ToggleButton italicsToggle = (ToggleButton) findViewById(R.id.button_italic);
        ToggleButton underlinedToggle = (ToggleButton) findViewById(R.id.button_underline);
        customEditor.setBoldToggleButton(boldToggle);
        customEditor.setItalicsToggleButton(italicsToggle);
        customEditor.setUnderlineToggleButton(underlinedToggle);

        customEditor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        customEditor.setEventBack(new CustomEditText.EventBack() {
            @Override
            public void close() {

            }

            @Override
            public void show() {

            }
        });
        customEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Intent intent = getIntent();
        if(intent == null)
            return;
        if(intent.getExtras() == null)
            return;

        taskId = intent.getExtras().getString("id");
        customEditor.setTextHTML(intent.getExtras().getString("description"));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("id", taskId);
        intent.putExtra("description", customEditor.getTextHTML());
        setResult(0, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
