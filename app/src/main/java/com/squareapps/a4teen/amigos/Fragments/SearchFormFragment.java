package com.squareapps.a4teen.amigos.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.squareapps.a4teen.amigos.Common.Utils.CourseFetchr;
import com.squareapps.a4teen.amigos.Common.POJOS.Course;
import com.squareapps.a4teen.amigos.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SearchFormFragment extends Fragment implements View.OnClickListener {

    public final static String SEARCH = "search";
    public final static String DETAIL = "detail";
    private final static String OUTPUTTYPE = "outputType";

    @Nullable
    private Callbacks callbacks;

    @BindView(R.id.prefix_spinner)
    Spinner prefix_spinner;

    @BindView(R.id.search_form_search_buttom)
    Button submitButton;

    @BindView(R.id.search_form_reset_button)
    Button resetFormButton;

    @BindView(R.id.search_form_toolbar)
    Toolbar toolbar;

    @BindView(R.id.search_form_title_tv)
    EditText titleET;

    @BindView(R.id.search_form_number_tv)
    EditText numberET;

    @BindView(R.id.search_form_credits_tv)
    EditText creditsET;

    @BindView(R.id.search_form_collapsingTB)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.search_form_progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.search_form)
    View searchForm;

    public static SearchFormFragment newInstance(@OutputType String outputType) {
        Bundle args = new Bundle();
        SearchFormFragment fragment = new SearchFormFragment();
        args.putSerializable(OUTPUTTYPE, outputType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        callbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_form, container, false);
        ButterKnife.bind(this, view);

        collapsingToolbarLayout.setTitle("Search");
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


        submitButton.setOnClickListener(this);
        resetFormButton.setOnClickListener(this);

        // Create ArrayAdapters using the string array and a default spinner layout
        ArrayAdapter<CharSequence> prefixSpinnerAdapter = ArrayAdapter.createFromResource(getActivity()
                , R.array.prefix_Item_array
                , android.R.layout.simple_spinner_item);

        // Apply the adapter to the spinner
        prefix_spinner.setAdapter(prefixSpinnerAdapter);


        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.search_form_search_buttom) {

            searchForm.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            String selectedPrefix = prefix_spinner.getSelectedItem().toString();

            String output = getArguments().getString(OUTPUTTYPE);

            String[] strings = {selectedPrefix, getText(numberET), getText(creditsET), getText(titleET), output};
            new FetchItemsTask().execute(strings);
        }

        if (v.getId() == R.id.search_form_reset_button) {
            prefix_spinner.setSelection(0);
            clearTexts(titleET, numberET, creditsET);
        }

    }

    @NonNull
    private String getText(EditText text) {
        return text.getText().toString().trim();
    }

    private void clearTexts(EditText... texts) {
        for (EditText text : texts) {
            text.setText("");
        }

    }

    private class FetchItemsTask extends AsyncTask<String, Void, List<Course>> {

        @Override
        protected List<Course> doInBackground(String... params) {
            return new CourseFetchr().fetchCourses(params[0], params[1], params[2], params[3], params[4]);
        }

        @Override
        protected void onPostExecute(List<Course> courses) {
            searchForm.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            callbacks.onSubmitQuery(courses);
        }
    }

    @StringDef({
            SEARCH,
            DETAIL,
    })

    @Retention(RetentionPolicy.SOURCE)
    @interface OutputType {
    }

    public interface Callbacks {
        void onSubmitQuery(List<Course> courses);
    }


}




