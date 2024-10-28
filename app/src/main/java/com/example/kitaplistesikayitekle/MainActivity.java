package com.example.kitaplistesikayitekle;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mrecyclerview;
    private kitapAdapter adapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //tıklayınca başka sayfaya gitsin

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.addBook_addmenu){
            //ıntent sayfası olacak
            Intent addbookIntent= new Intent(this,AddbookActivity2.class);
            startActivity(addbookIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mrecyclerview=findViewById(R.id.mainActivity_reyclerView);
        adapter= new kitapAdapter(Kitap.getData(this),this);

        mrecyclerview.setHasFixedSize(true);

        // nesneleri sırayla ekle
        LinearLayoutManager manager= new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mrecyclerview.setLayoutManager(manager);
        mrecyclerview.setAdapter(adapter);

        adapter.setOnItemClickListener(new kitapAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Kitap kitap) {
                Toast.makeText(getApplicationContext(),kitap.getKitapYazari(),Toast.LENGTH_SHORT).show();
            }
        });


    }

}