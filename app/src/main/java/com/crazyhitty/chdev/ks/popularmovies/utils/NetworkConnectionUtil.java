package com.crazyhitty.chdev.ks.popularmovies.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.crazyhitty.chdev.ks.popularmovies.R;

/**
 * Created by Kartik_ch on 12/17/2015.
 * This util class uses a 3rd party library named Material Dialogs ( https://github.com/afollestad/material-dialogs )
 * It also uses a error icon, which you can create manually.
 */
public class NetworkConnectionUtil {
    private static final String NO_INTERNET_CONN = "No Internet Available";
    private static final String NO_INTERNET_CONN_DESC = "Unable to detect an internet connection. Please turn it on in your network settings.";
    private static final String SETTINGS = "Settings";
    private static final String DISMISS = "Dismiss";

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static void showNetworkUnavailableDialog(final Context context) {
        MaterialDialog networkUnavailableDialog = new MaterialDialog.Builder(context)
                .title(NO_INTERNET_CONN)
                .content(NO_INTERNET_CONN_DESC)
                .iconRes(R.drawable.ic_error_24dp)
                .positiveText(SETTINGS)
                .negativeText(DISMISS)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        Intent wifiSettingsIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        context.startActivity(wifiSettingsIntent);
                    }
                }).build();
        networkUnavailableDialog.show();
    }
}
