package com.squareapps.a4teen.amigos.Services;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.squareapps.a4teen.amigos.Abstract.BaseTaskService;
import com.squareapps.a4teen.amigos.Common.POJOS.Course;
import com.squareapps.a4teen.amigos.Common.Utils.CourseFetchr;

import java.util.HashMap;
import java.util.List;

import static com.squareapps.a4teen.amigos.Common.Contract.Course.COURSE_CODE;
import static com.squareapps.a4teen.amigos.Common.Contract.Course.CREDITS;
import static com.squareapps.a4teen.amigos.Common.Contract.Course.NUMBER;
import static com.squareapps.a4teen.amigos.Common.Contract.Course.PREFIX;
import static com.squareapps.a4teen.amigos.Common.Contract.Course.TITLE;
import static com.squareapps.a4teen.amigos.Common.Contract.OUTPUT;


public class DowloadService extends BaseTaskService {
    public static final String DOWNLOAD_COMPLETED = "download_completed";
    public static final String DOWNLOAD_ERROR = "download_error";
    private static final String TAG = "MyDowloadService";
    private static final String ACTION_DOWLOAD = "com.squareapps.a4teen.amigos.action.DOWNLOAD";


    public DowloadService() {
        super(TAG);
    }


    public static void startActionDownload(Context context, Bundle bundle) {
        Intent intent = new Intent(context, DowloadService.class);
        intent.setAction(ACTION_DOWLOAD);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    public static IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(DOWNLOAD_COMPLETED);
        filter.addAction(DOWNLOAD_ERROR);
        return filter;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DOWLOAD.equals(action)) {
                handleActionFoo(intent);
            }
        }
    }

    private void handleActionFoo(Intent intent) {
        Bundle bundle = intent.getExtras();
        String prefix = bundle.getString(PREFIX);
        String num = bundle.getString(NUMBER);
        String credits = bundle.getString(CREDITS);
        String title = bundle.getString(TITLE);
        String output = bundle.getString(OUTPUT);

        List<Course> courses = doInBackground(prefix, num, credits, title, output);
        putInFirebase(intent, courses);

    }

    private List<Course> doInBackground(String... params) {
        return new CourseFetchr().fetchCourses(params[0], params[1], params[2], params[3], params[4]);
    }

    private void putInFirebase(Intent intent, List<Course> courses) {
        Bundle bundle = intent.getExtras();
        final String courseCode = bundle.getString(COURSE_CODE);


        for (final Course c : courses) {
            HashMap<String, Object> map = c.courseDetailtoMap(courseCode);
            getDataRef().updateChildren(map);
        }
        broadcastUploadFinished(courseCode, intent);
    }

    private boolean broadcastUploadFinished(String courseCode, Intent i) {
        boolean success = courseCode != null;

        String action = success ? DOWNLOAD_COMPLETED : DOWNLOAD_ERROR;

        Intent broadcast = new Intent(action)
                .putExtra(COURSE_CODE, courseCode);

        return LocalBroadcastManager
                .getInstance(getApplicationContext())
                .sendBroadcast(broadcast);
    }
}
