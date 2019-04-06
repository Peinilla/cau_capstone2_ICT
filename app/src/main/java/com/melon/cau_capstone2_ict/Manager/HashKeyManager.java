package com.melon.cau_capstone2_ict.Manager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;

public class HashKeyManager {
    @Nullable
    public static String getHashKey(Context context) {

        final String TAG = "KeyHash";

        String keyHash = null;

        try {

            PackageInfo info =
                    context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {

                MessageDigest md;

                md = MessageDigest.getInstance("SHA");

                md.update(signature.toByteArray());

                keyHash = new String(Base64.encode(md.digest(), 0));

                Log.d(TAG, keyHash);

            }

        } catch (Exception e) {

            Log.e("name not found", e.toString());

        }

        if (keyHash != null) {

            return keyHash;

        } else {

            return null;

        }

    }
}
