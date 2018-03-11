package com.squareapps.a4teen.amigos.Abstract;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

import com.squareapps.a4teen.amigos.Common.Utils.AppPreferences;

/**
 * Created by y-pol on 1/21/2018.s
 */

public class GroupNamePreference extends EditTextPreference {

    private String DEFAULT_VALUE;

    public GroupNamePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        GroupSettingsCallback callbacks = (GroupSettingsCallback) context;
        DEFAULT_VALUE = AppPreferences.getPrefGroupName(getContext(), callbacks.getGroupId());
        setSummary(DEFAULT_VALUE);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setText(restoreValue ? DEFAULT_VALUE : (String) defaultValue);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }


}