package com.by_syk.lib.nanoiconpack.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.github.promeg.pinyinhelper.Pinyin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * Created by By_syk on 2017-01-23.
 */

public class ExtraUtil {

    public static boolean isFromLauncherPick(Intent intent) {
        if (intent == null) {
            return false;
        }

        String action = intent.getAction();
        return "com.novalauncher.THEME".equals(action) // Nova
                // Apex: No such funtion
                || "org.adw.launcher.icons.ACTION_PICK_ICON".equals(action) // ADW
                /*|| "com.phonemetra.turbo.launcher.icons.ACTION_PICK_ICON".equals(action) // Turbo
                || Intent.ACTION_PICK.equals(action)
                || Intent.ACTION_GET_CONTENT.equals(action)*/;
    }

    @NonNull
    public static String[] getPinyinForSorting(String[] textArr) {
        if (textArr == null) {
            return new String[0];
        }

        String[] resultArr = new String[textArr.length];
        for (int i = 0, len = textArr.length; i < len; ++i) {
            resultArr[i] = Pinyin.toPinyin(textArr[i], "").toLowerCase();
        }

        return resultArr;
    }

    public static boolean saveIcon(Context context, Drawable drawable, String name) {
        if (context == null || drawable == null || TextUtils.isEmpty(name)) {
            return false;
        }

        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        if (bitmap == null) {
            return false;
        }

        // Create a path where we will place our picture
        // in the user's public pictures directory.
        File picDir = new File(Environment.getExternalStoragePublicDirectory(Environment
                .DIRECTORY_PICTURES), "Icons");
        // Make sure the Pictures directory exists.
        picDir.mkdirs();
        File targetFile = new File(picDir, "ic_" + name + "_"
                + bitmap.getByteCount() + ".png");

        boolean result = false;
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(targetFile);
            result = bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (!result) {
            targetFile.delete();
            return false;
        }

        record2Gallery(context, targetFile, false);
        return true;
    }

    /**
     * 记录新增图片文件到媒体库，这样可迅速在系统图库看到
     *
     * @param context
     * @param newlyPicFile
     * @return
     */
    private static boolean record2Gallery(Context context, File newlyPicFile, boolean allInDir) {
        if (context == null || newlyPicFile == null || !newlyPicFile.exists()) {
            return false;
        }

        Log.d(C.LOG_TAG, "record2Gallery(): " + newlyPicFile + ", " + allInDir);

        if (C.SDK >= 19) {
            String[] filePaths;
            if (allInDir) {
                filePaths = newlyPicFile.getParentFile().list();
            } else {
                filePaths = new String[]{newlyPicFile.getPath()};
            }
            MediaScannerConnection.scanFile(context, filePaths, null, null);
        } else {
            if (allInDir) {
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                        Uri.fromFile(newlyPicFile.getParentFile())));
            } else {
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(newlyPicFile)));
            }
        }

        return true;
    }




    public static boolean sendIcon2HomeScreen(Context context, int iconId, String appName,
                                              String pkgName, String launcherName) {
        if (context == null || iconId == 0 || TextUtils.isEmpty(appName)
                || TextUtils.isEmpty(pkgName) || TextUtils.isEmpty(launcherName)) {
            return false;
        }

        Intent shortcutIntent = new Intent(Intent.ACTION_VIEW);
        shortcutIntent.setClassName(pkgName, launcherName);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        final String ACTION_ADD_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";

        Intent addIntent = new Intent();
        addIntent.setAction(ACTION_ADD_SHORTCUT);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(context, iconId));
        addIntent.putExtra("duplicate", false);
        context.sendBroadcast(addIntent);

        return true;
    }

    @NonNull
    public static String purifyIconName(String iconName) {
        if (TextUtils.isEmpty(iconName)) {
            return "";
        }
        if (iconName.matches(".+?_\\d+")) {
            return iconName.substring(0, iconName.lastIndexOf('_'));
        }
        return iconName;
    }
}
