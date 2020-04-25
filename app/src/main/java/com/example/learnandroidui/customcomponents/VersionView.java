package com.example.learnandroidui.customcomponents;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class VersionView extends androidx.appcompat.widget.AppCompatTextView {


    public VersionView(Context context) {
        super(context);
        setVersion();
    }

    public VersionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setVersion();
    }

    public VersionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setVersion();
    }

    private void setVersion() {
        try {
            PackageInfo packageInfo = getContext()
                    .getPackageManager()
                    .getPackageInfo(getContext().getPackageName(),0);

            setText(packageInfo.versionName);
        }catch (Exception e){

        }
    }
}
