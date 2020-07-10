/*
 * Copyright 2017 By_syk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.by_syk.lib.nanoiconpack.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.util.Locale;

/**
 * Created by By_syk on 2017-02-15.
 */

public class PkgUtil {

    public static String getAppVer(Context context, String format) {
        if (context == null || TextUtils.isEmpty(format)) {
            return "";
        }

        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return String.format(Locale.US, format, packageInfo.versionName, packageInfo.versionCode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }


    /**
     * Get Activity icon
     */
    public static Drawable getIcon(PackageManager pkgManager, String pkgName, String activity) {
        if (pkgManager  == null || TextUtils.isEmpty(pkgName) || TextUtils.isEmpty(activity)) {
            return null;
        }

        Intent intent = new Intent();
        intent.setClassName(pkgName, activity);
        ResolveInfo resolveInfo = pkgManager.resolveActivity(intent, 0);
        if (resolveInfo != null) {
            return resolveInfo.loadIcon(pkgManager);
        }
        return null;
    }

}
