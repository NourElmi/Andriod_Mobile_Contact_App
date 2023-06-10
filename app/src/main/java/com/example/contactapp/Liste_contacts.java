package com.example.contactapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactapp.Model.Contact;
import com.example.contactapp.adapters.MyAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Liste_contacts extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton fab_add;
    RecyclerView contactsRecycler;
    EditText barreRecherche;
    TextView servicee;

//    SearchView barreRecherche;
    ImageView menuButton;
    ImageView favorisButton;
    ImageView logoutButton;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    List<Contact> contacts;
    List<Contact> filteredList;
    MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_contacts);
        contactsRecycler=(RecyclerView) findViewById(R.id.list_contacts);
        barreRecherche= (EditText) findViewById(R.id.search);
        menuButton=(ImageView) findViewById(R.id.menu_button);
        favorisButton=(ImageView) findViewById(R.id.favoris_button);
        logoutButton=(ImageView) findViewById(R.id.logout_button);
        fab_add=(FloatingActionButton) findViewById(R.id.fab_add);
        fab_add.setOnClickListener(this);
        menuButton.setOnClickListener(this);
        favorisButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        contacts= new ArrayList<Contact>();
        getContacts();
        Log.i("beffff", contacts.toString());
        addTextListener();
    }
    public void addTextListener() {
        barreRecherche.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence query, int start, int before, int count) {
                // query contient text qu'on a ecrit dans barre de recherche
                if (query.toString().isEmpty()) {
                    // if query is vide, all contacts vont s'afficher
                    MyAdapter myAdapter = new MyAdapter(contacts, Liste_contacts.this);
                    contactsRecycler.setAdapter(myAdapter);
                } else {
                    // else we are going to show just the contacts that conferms to the query
                    List<Contact> filteredContacts = new ArrayList<>();
                    for (Contact c : contacts) {
                        if (c.getNomContact().toLowerCase().contains(query.toString().toLowerCase()) ||
                                c.getPrenomContact().toLowerCase().contains(query.toString().toLowerCase()) ||
                                c.getServiceContact().toLowerCase().contains(query.toString().toLowerCase())) {
                            filteredContacts.add(c);
                        }
                    }
                    MyAdapter myAdapter = new MyAdapter(filteredContacts, Liste_contacts.this);
                    contactsRecycler.setAdapter(myAdapter);
                }
            }
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.fab_add){
            Intent myintent= new Intent(this, Ajouter_contact.class);
            startActivity(myintent);
        }else if(view.getId()==R.id.menu_button){

        }else if(view.getId()==R.id.favoris_button){
            Intent myintent= new Intent(this, Liste_favoris.class);
            startActivity(myintent);
        }
        else if(view.getId()==R.id.logout_button){
            mAuth.signOut();
            Intent intent = new Intent(this, MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }


//   void getContacts(){
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        DocumentReference docRef = db.collection("user").document(currentUser.getEmail());
//        docRef.collection("contact").get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Contact c= new Contact(document.get("nom").toString(),document.get("prenom").toString(),document.get("service").toString(),document.get("email").toString(),document.get("Tel").toString(),document.get("url").toString());
//                                contacts.add(c);
//                            }
//                            // use this setting to improve performance if you know that changes
//                            // in content do not change the layout size of the RecyclerView
//                            Log.i("mesConactssss", contacts.toString());
//                            contactsRecycler.setHasFixedSize(true);
//                            myAdapter = new MyAdapter(contacts, Liste_contacts.this);
//                            contactsRecycler.setAdapter(myAdapter);
//                            LinearLayoutManager layoutManager = new LinearLayoutManager(Liste_contacts.this);
//                            contactsRecycler.setLayoutManager(layoutManager);
//
//                        } else {
//                            Log.d("not ok", "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//    }
void getContacts() {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    DocumentReference docRef = db.collection("user").document(currentUser.getEmail());
    docRef.collection("contact").get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Contact c= new Contact(document.get("nom").toString(),document.get("prenom").toString(),document.get("service").toString(),document.get("email").toString(),document.get("Tel").toString(),document.get("url").toString());
                            contacts.add(c);
                        }
                        // use this setting to improve performance if you know that changes
                        // in content do not change the layout size of the RecyclerView
                        Log.i("mesConactssss", contacts.toString());
                        contactsRecycler.setHasFixedSize(true);
                        myAdapter = new MyAdapter(contacts, Liste_contacts.this);
                        contactsRecycler.setAdapter(myAdapter);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(Liste_contacts.this);
                        contactsRecycler.setLayoutManager(layoutManager);
                    } else {
                        Log.d("not ok", "Error getting documents: ", task.getException());
                    }
                }
            });
}



}



