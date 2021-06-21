package com.example.pdf2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.pdf2.Inconsistencia.Inconsistencia;
import com.example.pdf2.Interfaz.InterfazAPI;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.List;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        requestPermission2();
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

    private void requestPermission2() {
        if (this.checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.i("TAG", "requestPermission2: ok");
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                requestPermissions(new String[]{
                        WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,}, 1);
            } else {
            }
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", this.getPackageName())));
                startActivityForResult(intent, 2296);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 2296);
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE},
                    2);
        }
    }

    public void createPDF2(View view) {
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        File filePDF = new File(directory, "ejemploPDF.pdf");
        BufferedWriter bw;
        EditText txt = findViewById(R.id.txt_input);

        if ((filePDF.exists())) {
            if (filePDF.delete()) {
                Log.i("Existe", "se ha eliminado el archivo .pff exitosamente");
            } else
                Log.e("Crear crearPDF", "No se ha eliminado el archivo .pdf ");
        }

        try {
            if (filePDF.createNewFile()) {
                Log.i("CaPro", "archivo creado");
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error al crear el PDF", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        try {
            bw = new BufferedWriter(new FileWriter(filePDF));
            bw.write(txt.getText().toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createPDF(View view) {
        EditText txt = findViewById(R.id.txt_input);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.censospanama.pa/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        InterfazAPI interfazAPI = retrofit.create(InterfazAPI.class);
        Call<Inconsistencia> call = interfazAPI.find(txt.getText().toString());
        call.enqueue(new Callback<Inconsistencia>() {
            @SuppressLint("SdCardPath")
            @Override
            public void onResponse(Call<Inconsistencia> call, Response<Inconsistencia> response) {
                if (response.isSuccessful()) {
                    Inconsistencia p = response.body();
                    Document doc = new Document();
                    String outPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                            + "/" + "ejemplo.pdf";
                    try {
                        PdfWriter.getInstance(doc, new FileOutputStream(outPath));
                        doc.open();
                        doc.add(new Paragraph(txt.getText().toString()));
                        doc.close();
                        Toast.makeText(MainActivity.this, "PDF creado", Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error al crear el PDF", Toast.LENGTH_SHORT).show();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }

                }

            }

            public void onFailure(Call<Inconsistencia> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error de conexion",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 || requestCode == 2) {
            if (grantResults.length != 0)
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permiso concedido.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permiso Denegado.", Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }
}