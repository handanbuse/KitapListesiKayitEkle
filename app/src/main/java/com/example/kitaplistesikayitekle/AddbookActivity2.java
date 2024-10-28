package com.example.kitaplistesikayitekle;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kitaplistesikayitekle.databinding.ActivityMainBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Permission;

public class AddbookActivity2 extends AppCompatActivity {

    private EditText editkitap,edityazar,editozet;
    private ImageView imgkitap;
    private String kitapismi,kitapyazari,kitapozeti;

    private int imgizinalmakodu =0, izinverme=1;
  private   Bitmap secilenresim,kücültülenresim,enbastakiresim;
  private Button btnkayıt;


    private ActivityResultLauncher<String> permissionLauncher;
    private ActivityResultLauncher<Intent> activityResultLauncher;



    private void init(){
        editkitap=findViewById(R.id.kitapİsmi_edit);
        edityazar=findViewById(R.id.kitapYazar_edit2);
        editozet=findViewById(R.id.kitapözet_edit3);
        imgkitap = findViewById(R.id.addbook_img);
        enbastakiresim= BitmapFactory.decodeResource(this.getResources(),R.drawable.ic_launcher_background);
        imgkitap.setImageBitmap(enbastakiresim);
        btnkayıt=findViewById(R.id.bookactivity_btn);


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_addbook2);
        init();

        // İzin talebi için launcher'ı tanımlayın
        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        // İzin verildiğinde resmi seçme işlemini başlat

                    } else {
                        // İzin reddedildi
                        Toast.makeText(this, "İzin reddedildi!", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Resim seçim işlemi için launcher'ı tanımlayın

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        // Seçilen resmi ImageView'de göster
                        imgkitap.setImageURI(imageUri);
                    }
                }
        );




    }



    public void kitapKaydet(View v){

        kitapismi=editkitap.getText().toString();
        kitapyazari=edityazar.getText().toString();
        kitapozeti= editozet.getText().toString();


        if(!TextUtils.isEmpty(kitapismi)){
            if (!TextUtils.isEmpty(kitapyazari)){
                if (!TextUtils.isEmpty(kitapozeti)){

                    //kaydet
                    ByteArrayOutputStream outputStream= new ByteArrayOutputStream();
                    kücültülenresim= resimkücült(secilenresim);
                    kücültülenresim.compress(Bitmap.CompressFormat.PNG,75,outputStream);
                    byte[] kayitedilenresim= outputStream.toByteArray(); // bu şekilde resmin boyutlarını ayarladık ki çok yer kaplamasın


                    //  veri tabanı oluştur
                    try {
                        SQLiteDatabase database= this.openOrCreateDatabase("kitaplar", MODE_PRIVATE,null);
                        database.execSQL("CREATE TABLE IF NOT EXISTS Kitaplar (id INTEGER PRIMARY KEY, kitapadi VARCHAR,kitapyazari VARCHAR,kitapozeti VARCHAR,kitapresim BLOB )");
                        String sqlsorgusu="INSERT INTO Kitaplar(kitapadi,kitapyazari,kitapozeti,kitapresim)VALUES (?,?,?,?)";
                        SQLiteStatement statement= database.compileStatement(sqlsorgusu);
                        statement.bindString(1,kitapismi);
                        statement.bindString(2,kitapyazari);
                        statement.bindString(3,kitapozeti);
                        statement.bindBlob(4,kayitedilenresim);
                        statement.execute();

                        Toast.makeText(getApplicationContext(),"kayıt başarıyla eklendi",Toast.LENGTH_SHORT).show();
                        nesnetemizle();
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                } else Toast.makeText(getApplicationContext(),"kitap özeti boş olamaz",Toast.LENGTH_SHORT).show();
            } else Toast.makeText(getApplicationContext(),"yazar adı boş olamaz",Toast.LENGTH_SHORT).show();
        } else Toast.makeText(getApplicationContext(),"kitap adı  boş olamaz",Toast.LENGTH_SHORT).show();
    }

    private Bitmap resimkücült(Bitmap resim){
        return Bitmap.createScaledBitmap(resim,120,150,true);
    }

    private void nesnetemizle(){
        editkitap.setText("");
        edityazar.setText("");
        editozet.setText("");
        imgkitap.setBackgroundResource(R.drawable.ic_launcher_background);
        btnkayıt.setEnabled(false);



    }



    public void resimsec(View v) {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, imgizinalmakodu);
        } else {
            Intent resimiAl = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
           activityResultLauncher.launch(resimiAl);





        }


    }
// galeriye gitmesi için izin işlemlerini yaptık
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==imgizinalmakodu){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Intent resimiAl = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(resimiAl);

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // galeriden alması için


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==izinverme){
            if(resultCode==RESULT_OK&& data != null){
                Uri resimUri=data.getData();

                try {
                    if(Build.VERSION.SDK_INT>=28){
                        // 28 ve sonrası sürüm için gelen güncellemeymiş kişinin kullandığı telefon yüksekse
                        ImageDecoder.Source resimSource= ImageDecoder.createSource(this.getContentResolver(),resimUri);
                        secilenresim= ImageDecoder.decodeBitmap(resimSource);
                        imgkitap.setImageBitmap(secilenresim);


                    }else{
                        secilenresim=MediaStore.Images.Media.getBitmap(this.getContentResolver(),resimUri);
                        imgkitap.setImageBitmap(secilenresim);
                    }

                    btnkayıt.setEnabled(true);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

