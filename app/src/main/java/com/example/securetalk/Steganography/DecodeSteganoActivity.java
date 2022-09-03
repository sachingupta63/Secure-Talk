package com.example.securetalk.Steganography;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextDecodingCallback;
import com.ayush.imagesteganographylibrary.Text.ImageSteganography;
import com.ayush.imagesteganographylibrary.Text.TextDecoding;
import com.example.securetalk.R;

import java.io.IOException;

public class DecodeSteganoActivity extends AppCompatActivity implements TextDecodingCallback {

    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "Image Decode Class";

    private TextView textView;
    private ImageView imageView;
    private TextView message;
    private EditText secret_key;
    private Uri filepath;
    //Bitmap
    private Bitmap original_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decode_stegano);

        textView = findViewById(R.id.whether_decoded);

        imageView = findViewById(R.id.imageview);

        message = findViewById(R.id.message);
        secret_key = findViewById(R.id.secret_key);

        Button choose_image_button = findViewById(R.id.choose_image_button);
        Button decode_button = findViewById(R.id.decode_button);

        //Choose Image Button
        choose_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageChooser();
            }
        });

        //Decode Button
        decode_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filepath != null) {

                    //Making the ImageSteganography object
                    ImageSteganography imageSteganography = new ImageSteganography(secret_key.getText().toString(),
                            original_image);

                    //Making the TextDecoding object
                    TextDecoding textDecoding = new TextDecoding(DecodeSteganoActivity.this, DecodeSteganoActivity.this);

                    //Execute Task
                    textDecoding.execute(imageSteganography);
                }
            }
        });


    }

    // Choosing image from gallery
    private void ImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    //gallery choose result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Image set to imageView
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filepath = data.getData();
            try {
                original_image = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);

                imageView.setImageBitmap(original_image);
            } catch (IOException e) {
                Log.d(TAG, "Error : " + e);
            }
        }

    }

    @Override
    public void onStartTextEncoding() {
        //Whatever you want to do by the start of textDecoding
        Toast.makeText(this, "Encoding start", Toast.LENGTH_SHORT).show();
    }

    //Completion
    @Override
    public void onCompleteTextEncoding(ImageSteganography result) {

        //By the end of textDecoding

        if (result != null) {
            if (!result.isDecoded())
                textView.setText("No message found");
            else {
                if (!result.isSecretKeyWrong()) {
                    textView.setText("Decoded");
                    message.setText("" + result.getMessage());
                } else {
                    textView.setText("Wrong secret key");
                }
            }
        } else {
            textView.setText("Select Image First");
        }


    }
}