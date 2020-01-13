package com.rupeeredee.app;

import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoackActivity extends AppCompatActivity {
    private static int CODE_AUTHENTICATION_VERIFICATION = 241;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.mobile_number_activity);
        KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        if (km.isKeyguardSecure()) {
            Intent i = km.createConfirmDeviceCredentialIntent("Unlock RupeeRedee", "Confirm your phone screen lock pattern");
            startActivityForResult(i, CODE_AUTHENTICATION_VERIFICATION);
        } else
        {
            Intent goToLogin = new Intent(LoackActivity.this, MainActivity.class);
            startActivity(goToLogin);
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CODE_AUTHENTICATION_VERIFICATION) {
            Intent goToLogin = new Intent(LoackActivity.this, MainActivity.class);
            startActivity(goToLogin);
            finish();
        } else {
            finish();
        }
    }

}
