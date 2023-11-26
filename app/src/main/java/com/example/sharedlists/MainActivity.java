package com.example.sharedlists;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    private GridLayout gridLayout;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridLayout = findViewById(R.id.gridLayout);

        databaseReference = getDatabaseReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Limpiar cualquier vista existente
                gridLayout.removeAllViews();

                // Obtener el número total de columnas en el GridLayout
                int numColumnas = gridLayout.getColumnCount();

                // Iterar sobre todas las listas en la base de datos
                for (DataSnapshot listaSnapshot : dataSnapshot.getChildren()) {
                    // Obtener la lista actual
                    ListDb list = listaSnapshot.getValue(ListDb.class);

                    // Crear un CardView para la lista
                    CardView cardView = createCardView(list);

                    // Obtener el número total de elementos ya presentes
                    int totalElementos = gridLayout.getChildCount();

                    // Calcular la fila y columna para el nuevo CardView
                    int row = totalElementos / numColumnas;
                    int column = totalElementos % numColumnas;

                    // Configurar los parámetros del GridLayout para el nuevo CardView
                    GridLayout.Spec filaSpec = GridLayout.spec(row);
                    GridLayout.Spec columnaSpec = GridLayout.spec(column);
                    GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(filaSpec, columnaSpec);
                    cardView.setLayoutParams(layoutParams);

                    // Agregar el CardView al GridLayout
                    gridLayout.addView(cardView);
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error al recuperar datos de la base de datos", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private DatabaseReference getDatabaseReference(){
        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://sharedlists-cd7f3-default-rtdb.europe-west1.firebasedatabase.app");
        database.setPersistenceEnabled(true);


        databaseReference = database.getReference("ListDb");

        return databaseReference;
    }

    private CardView createCardView(ListDb list) {
        CardView cardView = new CardView(this);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(16, 16, 16, 16);

        TextView titleTextView = new TextView(this);
        titleTextView.setText(list.getTitle());
        titleTextView.setTextSize(18);
        titleTextView.setTypeface(null, Typeface.BOLD);

        TextView contentTextView = new TextView(this);
        contentTextView.setText(list.getContent());
        contentTextView.setTextSize(14);

        linearLayout.addView(titleTextView);
        linearLayout.addView(contentTextView);

        cardView.addView(linearLayout);

        cardView.setOnClickListener(v -> {
            // Al hacer clic en el CardView, abrir el nuevo Activity con el título y contenido
            abrirDetalleActivity(list);
        });

        return cardView;
    }

    private void abrirDetalleActivity(ListDb list) {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("TITLE", list.getTitle());
        intent.putExtra("CONTENT", list.getContent());
        startActivity(intent);
    }
}