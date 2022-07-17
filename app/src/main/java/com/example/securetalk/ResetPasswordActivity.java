package com.example.securetalk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText old,newpwd,cnfpwd;
    Button save;
    String oldpassword,newpassword,cnfpassword;
    String pwd=AESActivity.pwdtext.toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        old=(EditText) findViewById(R.id.old);


        newpwd=(EditText) findViewById(R.id.newp);


        cnfpwd=(EditText) findViewById(R.id.cnfpwd);


        save=(Button) findViewById(R.id.save);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldpassword=old.getText().toString();
                newpassword=newpwd.getText().toString();
                cnfpassword=cnfpwd.getText().toString();
                if(pwd.equals(oldpassword))
                {

                    if(newpassword.equals(cnfpassword))
                    {
                        AESActivity.pwdtext=newpassword;
                        System.out.println("updated successfully");
                        Toast.makeText(ResetPasswordActivity.this,"Updated Successfully!!",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(ResetPasswordActivity.this," password does not match",Toast.LENGTH_SHORT).show();
                        System.out.println("new password match ni karre");
                    }
                }
                else
                {
                    System.out.println(pwd);
                    Toast.makeText(ResetPasswordActivity.this," password does not match",Toast.LENGTH_SHORT).show();
                    System.out.println("purane password match ni karre");
                    System.out.println(oldpassword);
                }
            }
        });

    }
}