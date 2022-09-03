package com.example.securetalk.Algo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.securetalk.R;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DESActivity extends AppCompatActivity {


    EditText input_text, password_text;
    TextView output_text;
    Button enc, dec,clear,reset,send;
    String outputstring;

    private static final String UNICODE_FORMAT="UTF8";
    public static final String DES_ENCRYPTION_SCHEME="DES";
    private KeySpec myKeySpec;
    private SecretKeyFactory mySecretKeyFactory;
    private Cipher cipher;
    byte[] KeyAsBytes;
    private String myEncryptionKey;
    private String getMyEncryptionScheme;
    SecretKey key;
    String myEncKey="This is Key";

    String ans="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desactivity);


        input_text = findViewById(R.id.input_text);

        output_text = findViewById(R.id.output_text);
        enc = findViewById(R.id.encrypt);
        dec = findViewById(R.id.decrypt);
        clear = findViewById(R.id.clear_button);
        reset = findViewById(R.id.reset);
        send = findViewById(R.id.send);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //initialising key generation part
        myEncryptionKey=myEncKey;
        getMyEncryptionScheme=DES_ENCRYPTION_SCHEME;
        try {
            KeyAsBytes=myEncryptionKey.getBytes(UNICODE_FORMAT); //converting into unicode_format
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            myKeySpec=new DESKeySpec(KeyAsBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            mySecretKeyFactory=SecretKeyFactory.getInstance(getMyEncryptionScheme);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            cipher=Cipher.getInstance(getMyEncryptionScheme);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            key=mySecretKeyFactory.generateSecret(myKeySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }


        //Encrption button
        enc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String temp=input_text.getText().toString();
                ans=encrypt(temp);
                Log.d("SACHIN", "encrypt key:" + ans);
                output_text.setText(ans);

            }
        });

        //decryption button
        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp=input_text.getText().toString();
                ans=decrypt(temp);
                Log.d("SACHIN", "decrypt key:" + ans);
                output_text.setText(ans);
            }
        });

        //send button
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ans.length()>0) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, ans);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
                else
                {
                    Toast.makeText(DESActivity.this,"empty output",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //clear button
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    input_text.setText(" ");
                    output_text.setText("");
                    //  Toast.makeText( this,"Cleared Successfully!!!",Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

    }


    public String encrypt(String unencryptedString)
    {
        String encryptedString =null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText); //Encoding

            encryptedString= Base64.encodeToString(encryptedText, Base64.DEFAULT); //Encoding to base 64
        }
        catch (InvalidKeyException|UnsupportedEncodingException| IllegalBlockSizeException | BadPaddingException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        return encryptedString;
    }

    public String decrypt(String encrytedString)
    { String decryptedText = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedText = Base64.decode(encrytedString, Base64.DEFAULT);  //Pehle decode kar byte main
            byte[] plainText = cipher.doFinal(encryptedText); //Decryptinng
            decryptedText = bytes2String(plainText);
        }catch(InvalidKeyException|IllegalBlockSizeException|BadPaddingException e) {

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
        return decryptedText;
    }

    //convert a byte[] into a string
    private static String bytes2String(byte[] bytes)
    {
        StringBuilder stringBuffer=new StringBuilder();
        for(int i=0;i<bytes.length;i++)
        {
            stringBuffer.append((char) bytes[i]);

        }
        return stringBuffer.toString();
    }

    // Speech Recognition
    public void getspeechinput(View view) {

        Intent intent =new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        try {
            startActivityForResult(intent, 1001);
        }
        catch (Exception e) {
            Toast.makeText(DESActivity.this, " " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //Speech result will come here
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case 1001:
                if(resultCode==RESULT_OK&&data!=null)
                {
                    ArrayList<String> res=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    input_text.setText(res.get(0));
                }
                break;
        }
    }
}