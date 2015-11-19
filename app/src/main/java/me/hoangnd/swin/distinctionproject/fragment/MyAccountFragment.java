package me.hoangnd.swin.distinctionproject.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import me.hoangnd.swin.distinctionproject.R;
import me.hoangnd.swin.distinctionproject.activity.LoginActivity;
import me.hoangnd.swin.distinctionproject.activity.MainActivity;

public class MyAccountFragment extends Fragment {

    private TextView emailTextView;
    private Button logoutButton;

    public static MyAccountFragment newInstance() {
        MyAccountFragment fragment = new MyAccountFragment();
        return fragment;
    }

    public MyAccountFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        ParseUser user = ParseUser.getCurrentUser();
        if(emailTextView != null && user != null)
            emailTextView.setText(user.getEmail());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);
        emailTextView = (TextView)view.findViewById(R.id.label_email_value);
        logoutButton = (Button)view.findViewById(R.id.button_logout);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        Activity activity = getActivity();
                        if(activity instanceof MainActivity){
                            ((MainActivity) activity).openLoginPage();
                        }
                    }
                });
            }
        });

        return view;
    }


}
