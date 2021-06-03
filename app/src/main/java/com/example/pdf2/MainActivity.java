package com.example.pdf2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        try {
            if (!SharedPreferencesManager.getSomeBooleanValue("PERMISOS")) {
                Intent intent;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                    intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivity(intent);
                    SharedPreferencesManager.setSomeBooleanValue("PERMISOS", true);
                }
            }
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
        }

    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private void requestPermission2() {
//        if (this.checkSelfPermission(
//                WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
//                this.checkSelfPermission(
//                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//            Log.i("TAG", "requestPermission2: ok");
//        } else {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                requestPermissions(new String[]{
//                        WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
//                        Manifest.permission.READ_EXTERNAL_STORAGE,}, 1);
//            }
//        }
//    }
//
//    private void requestPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            try {
//                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                intent.addCategory("android.intent.category.DEFAULT");
//                intent.setData(Uri.parse(String.format("package:%s", this.getPackageName())));
//                startActivityForResult(intent, 2296);
//            } catch (Exception e) {
//                Intent intent = new Intent();
//                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
//                startActivityForResult(intent, 2296);
//            }
//        } else {
//            //below android 11
//            ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE},
//                    2);
//        }
//    }




    public void createPDF(View view){
        EditText txt=(EditText) findViewById(R.id.txt_input);
        Document doc=new Document();
        String outPath= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+ "ejemplo.pdf";
        try{
            PdfWriter.getInstance(doc,new FileOutputStream(outPath));
            doc.open();
            doc.add(new Paragraph(txt.getText().toString()));
            doc.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

}