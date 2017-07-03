package com.square.apps.amigos.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.square.apps.amigos.Contract;
import com.square.apps.amigos.CourseFetchr;
import com.square.apps.amigos.R;
import com.square.apps.amigos.common.common.course.Course;

import java.util.HashMap;
import java.util.List;


public class SearchCourseFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static String TAG = "searCourseFragment";

    Spinner prefix_spinner;
    Spinner department_spinner;
    Spinner college_spinner;
    Spinner level_spinner;
    Spinner institution_spinner;

    ArrayAdapter<CharSequence> prefixSpinnerAdapter;

    Button submitButton;
    Button resetFormButton;

    EditText titleET;

    @Nullable
    private Callbacks callbacks;

    @Override
    public void onAttach(Context activity){
        super.onAttach(activity);
        callbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach(){
        super.onDetach();
        callbacks = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.add_course_search_form, container, false);

        titleET = (EditText) view.findViewById(R.id.search_form_title_tv);

        prefix_spinner = (Spinner) view.findViewById(R.id.prefix_spinner);
        department_spinner = (Spinner) view.findViewById(R.id.department_spinner);
        college_spinner = (Spinner) view.findViewById(R.id.college_spinner);
        level_spinner = (Spinner) view.findViewById(R.id.level_spinner);
        institution_spinner = (Spinner) view.findViewById(R.id.institution_spinner);

        submitButton = (Button) view.findViewById(R.id.search_form_search_buttom);
        resetFormButton = (Button) view.findViewById(R.id.search_form_reset_button);
        submitButton.setOnClickListener(this);
        resetFormButton.setOnClickListener(this);

        // Create ArrayAdapters using the string array and a default spinner layout
        ArrayAdapter<CharSequence> levelSpinnerdapter = ArrayAdapter.createFromResource(getActivity(), R.array.Level_Item_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> institutionSpinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.Institution_Item_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> collegeSpinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.College_Item_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> departmentSpinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.Department_Item_array, android.R.layout.simple_spinner_item);
        prefixSpinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.prefix_Item_array, android.R.layout.simple_spinner_item);

        // Apply the adapter to the spinner
        prefix_spinner.setAdapter(prefixSpinnerAdapter);
        department_spinner.setAdapter(departmentSpinnerAdapter);
        college_spinner.setAdapter(collegeSpinnerAdapter);
        level_spinner.setAdapter(levelSpinnerdapter);
        institution_spinner.setAdapter(institutionSpinnerAdapter);

        //prefix_spinner.setOnItemSelectedListener(this);

        //department_spinner.setOnItemSelectedListener(this);
        //college_spinner.setOnItemSelectedListener(this);
        //level_spinner.setOnItemSelectedListener(this);
        //institution_spinner.setOnItemSelectedListener(this);

        return view;
    }

    @Override
    public void onClick(View v){


        if (v.getId() == R.id.search_form_search_buttom) {
            String selectedPrefix = prefix_spinner.getSelectedItem().toString();
            String selectedDepartment = department_spinner.getSelectedItem().toString();
            String selectedCollege = college_spinner.getSelectedItem().toString();
            String selectedlevel = level_spinner.getSelectedItem().toString();
            String SelectedInstitution = institution_spinner.getSelectedItem().toString();

            HashMap<String, String> hashables = Contract.getHashMaps();
            String dept = hashables.get(selectedDepartment);
            String lvl = hashables.get(selectedlevel);
            String coll = hashables.get(selectedCollege);
            String title = titleET.getText().toString().trim();


            String[] strings = {selectedPrefix, dept, coll, lvl, title};
            new FetchItemsTask().execute(strings);
        }
        if (v.getId() == R.id.search_form_reset_button) {
            prefix_spinner.setSelection(0);
            department_spinner.setSelection(0);
            college_spinner.setSelection(0);
            institution_spinner.setSelection(0);
            titleET.setText("");
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
        //Toast.makeText(parent.getContext(), "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent){

    }


    public interface Callbacks {
        void onSubmitQuery(List<Course> courses);
    }

    private class FetchItemsTask extends AsyncTask<String, Void, List<Course>> {

        @Override
        protected List<Course> doInBackground(String... params){
            return new CourseFetchr().fetchCourses(params[0], params[1], params[2], params[3], params[4]);
        }

        @Override
        protected void onPostExecute(List<Course> courses){
            for (Course c : courses)
                Log.d(TAG, c.getTitle());
            callbacks.onSubmitQuery(courses);
        }
    }


}




