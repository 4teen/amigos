package com.square.apps.amigos.common.common.course;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.square.apps.amigos.Contract;
import com.square.apps.amigos.common.common.db.DataProvider;

import java.util.ArrayList;

/**
 *
 */
public class CourseLab {

    @SuppressLint("StaticFieldLeak")
    private static CourseLab courseLab;
    private final Context context;

    private CourseLab(Context mAppContext) {
        ArrayList<Course> tempCourses = new ArrayList<>();
        context = mAppContext;
    }

    public static CourseLab get(@NonNull Context context) {
        if (courseLab == null) {
            courseLab = new CourseLab(context.getApplicationContext());
        }
        return courseLab;
    }

    @NonNull
    public ArrayList<Course> getCourses() {
        Cursor cursor = context.getContentResolver().query(DataProvider.CONTENT_URI_COURSES, null, null, null, null);
        return toArray(cursor);
    }

    @Nullable
    public Course getCourse(String CourseID) {
        Cursor cursor = context.getContentResolver().query(Uri.withAppendedPath(DataProvider.CONTENT_URI_COURSES, CourseID), null, null, null, null);
        if ((cursor != null ? cursor.getCount() : 0) > 0) {
            cursor.moveToFirst();
            return toCourseObject(cursor);
        } else
            return null;

    }

    @NonNull
    private ArrayList<Course> toArray(@NonNull Cursor cursor) {
        ArrayList<Course> courses = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                courses.add(toCourseObject(cursor));
            }
        }
        return courses;
    }

    @NonNull
    private Course toCourseObject(@NonNull Cursor cursor) {
        Course course = new Course();
        course.setDepartment(cursor.getString(cursor.getColumnIndex(Contract.DEPARTMENT)));
        course.setCampus(cursor.getString(cursor.getColumnIndex(Contract.CAMPUS)));
        course.setCollege(cursor.getString(cursor.getColumnIndex(Contract.COLLEGE)));
        course.setTitle(cursor.getString(cursor.getColumnIndex(Contract.TITLE)));
        course.setSubjectCRS(cursor.getString(cursor.getColumnIndex(Contract.SUBJECT_CRS)));
        course.setSection(cursor.getString(cursor.getColumnIndex(Contract.SECTION)));
        course.setInstructor(cursor.getString(cursor.getColumnIndex(Contract.INSTRUCTOR)));
        course.setCourseID(cursor.getString(cursor.getColumnIndex(Contract.PRIMARY_KEY)));
        course.setDays(cursor.getString(cursor.getColumnIndex(Contract.DAYS)));
        course.setClassTime(cursor.getString(cursor.getColumnIndex(Contract.TIME)));
        course.setRoom(cursor.getString(cursor.getColumnIndex(Contract.ROOM)));
        course.setBuilding(cursor.getString(cursor.getColumnIndex(Contract.BUILDING)));
        course.setCRN(cursor.getString(cursor.getColumnIndex(Contract.CRN)));
        return course;
    }

}
