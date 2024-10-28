package com.example.kitaplistesikayitekle;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

public class Kitap {

    //classlara bunları tanımla
    private String kitapAdi,kitapYazari,kitapOzeti;
    private Bitmap kitapResmi;


    // boş bir tane constructor oluştur
    public Kitap(){

    }



    // dolusunu oluştur ve getter setter olustur
    public Kitap(String kitapAdi, String kitapYazari, String kitapOzeti, Bitmap kitapResmi) {
        this.kitapAdi = kitapAdi;
        this.kitapYazari = kitapYazari;
        this.kitapOzeti = kitapOzeti;
        this.kitapResmi = kitapResmi;
    }

    public String getKitapAdi() {
        return kitapAdi;
    }

    public void setKitapAdi(String kitapAdi) {
        this.kitapAdi = kitapAdi;
    }

    public String getKitapYazari() {
        return kitapYazari;
    }

    public void setKitapYazari(String kitapYazari) {
        this.kitapYazari = kitapYazari;
    }

    public String getKitapOzeti() {
        return kitapOzeti;
    }

    public void setKitapOzeti(String kitapOzeti) {
        this.kitapOzeti = kitapOzeti;
    }

    public Bitmap getKitapResmi() {
        return kitapResmi;
    }

    public void setKitapResmi(Bitmap kitapResmi) {
        this.kitapResmi = kitapResmi;
    }


    //geriye liste döndermek için
    static  public ArrayList<Kitap> getData(Context context){
        ArrayList<Kitap> kitaplist= new ArrayList<>();

        //veritabanından almak için
        ArrayList<String>kitapAdiList= new ArrayList<>();
        ArrayList<String>kitapYazariList= new ArrayList<>();
        ArrayList<String>kitapOzetiList= new ArrayList<>();
        ArrayList<Bitmap>kitapResimLList= new ArrayList<>();

        // işlemleri yap tablo ve veri tabanı oluştur
        try{
            SQLiteDatabase database= context.openOrCreateDatabase("Kitaplar",Context.MODE_PRIVATE,null);

            //veri almak için
            Cursor cursor=database.rawQuery("SELECT * FROM Kitaplar",null);

            int kitapadiIndex= cursor.getColumnIndex("kitapAdi");
            int kitapyazariIndex= cursor.getColumnIndex("kitapYazari");
            int kitapozetiIndex= cursor.getColumnIndex("kitapOzeti");
            int kitapresmiIndex= cursor.getColumnIndex("kitapResmi");


            while(cursor.moveToNext()){
                // değerleri doldur
                kitapAdiList.add(cursor.getString(kitapadiIndex));
                kitapYazariList.add(cursor.getString(kitapyazariIndex));
                kitapOzetiList.add(cursor.getString(kitapozetiIndex));

                byte[] gelenresimbyte= cursor.getBlob(kitapresmiIndex);
                Bitmap gelenresim= BitmapFactory.decodeByteArray(gelenresimbyte,0,gelenresimbyte.length);
                kitapResimLList.add(gelenresim);

            }
            cursor.close(); // nesneyi kaapt



            for(int i=0; i<kitapAdiList.size(); i++){

                Kitap kitap= new Kitap();

                kitap.setKitapAdi(kitapAdiList.get(i));
                kitap.setKitapYazari(kitapYazariList.get(i));
                kitap.setKitapOzeti(kitapOzetiList.get(i));
                kitap.setKitapResmi(kitapResimLList.get(i));

                kitaplist.add(kitap);





            }
        }catch (Exception e){

            e.printStackTrace();
        }

        return  kitaplist;

    }
}
