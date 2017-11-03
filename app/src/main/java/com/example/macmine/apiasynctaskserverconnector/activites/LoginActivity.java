package com.example.macmine.apiasynctaskserverconnector.activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.macmine.apiasynctaskserverconnector.R;
import com.example.macmine.apiasynctaskserverconnector.async.ServerConnector;
import com.example.macmine.apiasynctaskserverconnector.utils.AppUtils;

import org.json.JSONObject;

import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener , ServerConnector.onAsyncTaskComplete{

    Button btnSubmit;
    EditText etPhone, etPassword;
    ProgressDialog progressDialog;
    String device_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etPhone = (EditText) findViewById(R.id.et_phone);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnSubmit = (Button) findViewById(R.id.btn_submit);

        btnSubmit.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {

        execute();
    }

    //=============Input Validation CHeck ---------

    private void execute() {
        String mbile = etPhone.getText().toString();
        String pass = etPassword.getText().toString();

        if (mbile.equals("")) {
            etPhone.setError("Enter mobile no");
            etPhone.requestFocus();
        } else if (pass.equals("")) {
            etPassword.setError("Enter password");
            etPassword.requestFocus();
        } else {
            if (AppUtils.isConnectingToInternet(getApplicationContext())) {
                Login(mbile, pass);
            } else {
                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }


//=====================  Setting Parameters  for API -----------


    private void Login(String phone, String password) {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        String urlParameters;
        try {

            urlParameters =
                    "phone=" + URLEncoder.encode(phone, "UTF-8") +
                            "&password=" + URLEncoder.encode(password, "UTF-8") +
                            "&device_type=" + URLEncoder.encode("a", "UTF-8") +
                            "&device_id=" + URLEncoder.encode(device_id, "UTF-8");

            Log.d("", "urlParameters " + urlParameters);

            ServerConnector serverConnector = new ServerConnector(LoginActivity.this, urlParameters);
            serverConnector.setDataDowmloadListner(this);

            //**************  here you define
            serverConnector.execute(AppUtils.Login);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //=====================  method of interface ServerConnector.onAsycTaskComplete =====================


    public void OnSucess(String string) {
        Log.d("", "string " + string);
        if (progressDialog != null)
            progressDialog.dismiss();

        try {
            JSONObject jsonObject = new JSONObject(string);


            boolean status = jsonObject.getBoolean("status");
            if (status) {
                JSONObject user_details = jsonObject.getJSONObject("user_details");
                String Fname = user_details.getString("Fname");
                String email = user_details.getString("email");
                String accessToken = user_details.getString("accessToken");
                String data = user_details.getString("data");
                String userID = user_details.getString("userID");
                String userPhone = user_details.getString("userPhone");


                Toast.makeText(LoginActivity.this, Fname + " " + email + " " + userPhone + " ", Toast.LENGTH_LONG).show();


                // Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);


            } else {
                String message = jsonObject.getString("message");
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
