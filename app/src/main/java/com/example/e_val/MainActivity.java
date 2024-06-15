package com.example.e_val;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowId;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView tv_Login;
    DATABASE_HELPER dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DATABASE_HELPER(this);

        String AdminUserName = "Admin";
        String AdminPassword = "AdminPass";

        boolean AdminExists = dbHelper.checkAdminIfExists(AdminUserName, AdminPassword);

        if (AdminExists==false){
            dbHelper.insertADMIN(AdminUserName, AdminPassword);
        } else {
        }


        tv_Login = (TextView) findViewById (R.id.tvLogin);
        tv_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, StudentLoginPage.class);
                startActivity(i);
            }
        });

    }


    public void GoBack(View view) {
    }
}