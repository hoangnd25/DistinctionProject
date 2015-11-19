package me.hoangnd.swin.distinctionproject.component;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokenautocomplete.TokenCompleteTextView;

import me.hoangnd.swin.distinctionproject.R;
import me.hoangnd.swin.distinctionproject.data.Tag;

public class TagsCompletionView extends TokenCompleteTextView<Tag> {

    public TagsCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);

        char[] splitChar = {',', ';', ' '};
        setSplitChar(splitChar);
        setThreshold(1);
        performBestGuess(false);
    }

    @Override
    protected View getViewForObject(Tag tag) {

        LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = (LinearLayout)l.inflate(R.layout.tag_token, (ViewGroup)TagsCompletionView.this.getParent(), false);
        ((TextView)view.findViewById(R.id.name)).setText(tag.getName());

        return view;
    }

    @Override
    protected Tag defaultObject(String s) {
        return Tag.newInstance(s);
    }
}
