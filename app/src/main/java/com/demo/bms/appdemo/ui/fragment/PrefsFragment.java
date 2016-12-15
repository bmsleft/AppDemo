package com.demo.bms.appdemo.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.bms.appdemo.BmsZhihuApp;
import com.demo.bms.appdemo.R;
import com.demo.bms.appdemo.support.Check;
import com.demo.bms.appdemo.support.Constants;

/**
 * Created by bms on 16-12-14.
 */

public class PrefsFragment extends PreferenceFragment
        implements Preference.OnPreferenceClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
        findPreference("about").setOnPreferenceClickListener(this);

//        if (!Check.isAppInstalled()) {
//            ((PreferenceCategory) findPreference("settings_settings")).removePreference(findPreference("using_client?"));
//        }
//
//        if (!BmsZhihuApp.getSharedPreferences()
//            .getBoolean(Constants.SharedPreferencesKeys.KEY_SHOULD_ENABLE_ACCELERATE_SERVER, false)) {
//            ((PreferenceScreen) findPreference("preference_screen"))
//                    .removePreference(findPreference("settings_network_settings"));
//        }

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals("about")) {
            showApacheLicenseDialog();
            return true;
        }
        return false;
    }

    private void showApacheLicenseDialog() {
        final Dialog apacheLicenseDialog = new Dialog(getActivity());
        apacheLicenseDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        apacheLicenseDialog.setCancelable(true);
        apacheLicenseDialog.setContentView(R.layout.dialog_apche_license);

        TextView textView = (TextView) apacheLicenseDialog.findViewById(R.id.dialog_text);
        textView.setText(getString(R.string.my_test));

        Button closeDialogButton = (Button) apacheLicenseDialog.findViewById(R.id.close_dialog_button);
        closeDialogButton.setOnClickListener(view -> apacheLicenseDialog.dismiss());

        closeDialogButton.setOnLongClickListener(view -> {
            apacheLicenseDialog.dismiss();
            Toast.makeText(getActivity(),
                    getActivity().getString(R.string.accelerate_server_unlock),
                    Toast.LENGTH_SHORT).show();

            PreferenceManager.getDefaultSharedPreferences(getActivity())
                    .edit()
                    .putBoolean(Constants.SharedPreferencesKeys.KEY_SHOULD_ENABLE_ACCELERATE_SERVER, true)
                    .apply();
            return true;
        });
        apacheLicenseDialog.show();
    }


}




