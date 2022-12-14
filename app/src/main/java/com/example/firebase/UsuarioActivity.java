package com.example.firebase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UsuarioActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();

        TextView nombre = findViewById(R.id.nombre);
        TextView email =findViewById(R.id.email);
        TextView phone_number =findViewById(R.id.phone_number);
        TextView uid =findViewById(R.id.uid);
        Button cerrar =findViewById(R.id.btn_cerrar_sesion);

        uid.setText(usuario.getUid());
        nombre.setText(usuario.getDisplayName());
        email.setText(usuario.getEmail());
        phone_number.setText(usuario.getPhoneNumber());

        // Inicialización Volley (Hacer solo una vez en Singleton o Applicaction)
        RequestQueue colaPeticiones = Volley.newRequestQueue(this);
        ImageLoader lectorImagenes = new ImageLoader(colaPeticiones,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap> cache =
                            new LruCache<String, Bitmap>(10);
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }
                });


        Uri urlImagen = usuario.getPhotoUrl();
        if (urlImagen != null) {
            NetworkImageView foto = (NetworkImageView) findViewById(R.id.imagen);
            foto.setImageUrl(urlImagen.toString(), lectorImagenes);
        }


    }


    public void cerrarSesion(View view) {
        AuthUI.getInstance().signOut(getApplicationContext())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent i = new Intent(
                                getApplicationContext (),LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                });
    }
}