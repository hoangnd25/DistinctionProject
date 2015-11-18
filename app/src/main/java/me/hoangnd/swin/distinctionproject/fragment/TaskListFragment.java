package me.hoangnd.swin.distinctionproject.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import me.hoangnd.swin.distinctionproject.R;
import me.hoangnd.swin.distinctionproject.data.Task;

public class TaskListFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener{
    private static final String DUE_DATE = "due_date";
    private static final String TAG_ID = "tag_id";

    private Date dueDate;
    private String tagId;

    private ArrayAdapter<Task> adapter;

    private OnFragmentInteractionListener mListener;

    SwipeRefreshLayout refreshLayout;

    // TODO: Rename and change types of parameters
    public static TaskListFragment newInstance(String tagId, Date dueDate) {
        TaskListFragment fragment = new TaskListFragment();
        Bundle args = new Bundle();
        if(dueDate != null)
            args.putLong(DUE_DATE, dueDate.getTime());
        if(tagId != null)
            args.putString(TAG_ID, tagId);
        fragment.setArguments(args);
        return fragment;
    }

    public TaskListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            long date = getArguments().getLong(DUE_DATE);
            if(date > 0){
                if(dueDate == null)
                    dueDate = new Date();
                dueDate.setTime(date);
            }else {
                dueDate = null;
            }
            tagId = getArguments().getString(TAG_ID);
        }

        adapter = new ArrayAdapter<Task>(getActivity(),
                R.layout.row_task_item, R.id.text1){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(R.id.text1);
                TextView text2 = (TextView) view.findViewById(R.id.text2);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                text1.setText(adapter.getItem(position).getName());
                text2.setText(sdf.format(adapter.getItem(position).getDueDate()));
                return view;
            }
        };;

        setListAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        reloadFromLocalData();
    }

    protected void reloadFromLocalData(){
        Task.getAll(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null)
                    return;

                adapter.clear();
                for (ParseObject obj : objects) {
                    adapter.add(Task.newInstance(obj));
                }
            }
        }, true);
    }

    @Override
    public void onRefresh() {
        Task.getAll(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null)
                    return;
                final List<ParseObject> retrievedObjects = objects;

                ParseObject.unpinAllInBackground(Task.TABLE_NAME, new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        ParseObject.pinAllInBackground(Task.TABLE_NAME, retrievedObjects, new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                reloadFromLocalData();
                                refreshLayout.setRefreshing(false);
                            }
                        });
                    }
                });
            }
        }, false);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem == 0 && visibleItemCount > 0 && getListView().getChildAt(0).getTop() >= 0) {
            refreshLayout.setEnabled(true);
        }
        else {
            refreshLayout.setEnabled(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup listContainer = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);
        refreshLayout = new SwipeRefreshLayout(inflater.getContext());
        refreshLayout.addView(listContainer);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        return refreshLayout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setOnScrollListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
//            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String id);
    }

}
