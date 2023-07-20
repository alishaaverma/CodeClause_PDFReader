package com.example.pdfreader;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.Loader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainPage extends AppCompatActivity {
ListView lv_pdf;
public static ArrayList<File>fileList=new ArrayList<>();
PDFAdapter obj_adapter;
public static int REQUEST_PERMISSION=1;
boolean bolean_permission;
File dir;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        lv_pdf=(ListView) findViewById(R.id.listview_pdf);
        dir=new File(Environment.getExternalStorageDirectory().toString());

        permission_fn();

        lv_pdf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(),PdfPage.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });


    }

    private void permission_fn() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)){
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainPage.this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)){

            }
            else {
                ActivityCompat.requestPermissions(MainPage.this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION);
            }
        }
        else {
            bolean_permission=true;
            getfile(dir);
            obj_adapter=new PDFAdapter(getApplicationContext(),fileList);
            lv_pdf.setAdapter(obj_adapter);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==REQUEST_PERMISSION){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                bolean_permission=true;
                getfile(dir);
                obj_adapter=new PDFAdapter(getApplicationContext(),fileList);
                lv_pdf.setAdapter(obj_adapter);
            }
            else {
                Toast.makeText(this,"please allow the permission",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public ArrayList<File>getfile(File dir){
        File listFile[]=dir.listFiles();
        if (listFile!=null && listFile.length>0){
            for (int i=0;i<listFile.length;i++){
                if(listFile[i].isDirectory()){
                    getfile(listFile[i]);
                }
                else {
                    boolean booleanpdf=false;
                    if (listFile[i].getName().endsWith(".pdf")){
                        for (int j=0;j<fileList.size();j++){
                            if (fileList.get(j).getName().equals(listFile[i].getName())){
                                booleanpdf=true;
                            }
                            else {

                            }
                        }
                        if (booleanpdf){
                            booleanpdf=false;
                        }
                        else {
                            fileList.add(listFile[i]);
                        }
                    }
                }
            }
        }
        return fileList;
    }
}