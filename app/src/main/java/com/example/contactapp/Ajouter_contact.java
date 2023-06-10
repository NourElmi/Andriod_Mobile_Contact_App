package com.example.contactapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Ajouter_contact extends AppCompatActivity implements View.OnClickListener{

    Button btt_add_Contact_img;
    Button btt_add_contact;
    ImageView contact_photo;
    EditText urlImg_Contact;
    EditText tel_Contact;
    EditText nom_Contact;
    EditText prenom_Contact;
    EditText email_Contact;
    EditText service_Contact;
    ImageView backButton;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_contact);

        btt_add_Contact_img=(Button) findViewById(R.id.btt_add_Contact_img);
        btt_add_contact=(Button) findViewById(R.id.btt_add_contact);
        contact_photo=(ImageView) findViewById(R.id.contact_photo);
        urlImg_Contact=(EditText) findViewById(R.id.urlImg_Contact);
        nom_Contact=(EditText) findViewById(R.id.nom_Contact);
        prenom_Contact=(EditText) findViewById(R.id.prenom_Contact);
        tel_Contact=(EditText) findViewById(R.id.tel_Contact);
        email_Contact=(EditText) findViewById(R.id.email_contact);
        service_Contact=(EditText) findViewById(R.id.service_contact);
        backButton = (ImageView) findViewById(R.id.back_button);

        btt_add_Contact_img.setOnClickListener(this);
        btt_add_contact.setOnClickListener(this);
        backButton.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();
    }

    void insertNewContact(String nomContact, String prenomContact, String emailContact, String serviceContact, String telContact, String urlImgContact){
        Map<String, String> contactAttributs =new HashMap<>();
        contactAttributs.put("nom", nomContact.trim() );
        contactAttributs.put("prenom", prenomContact.trim() );
        contactAttributs.put("Tel", telContact.trim() );
        contactAttributs.put("email", emailContact.trim() );
        contactAttributs.put("service", serviceContact.trim() );
        contactAttributs.put("url", "gs://contactapp-b7ba8.appspot.com/photos/nour1234@gmail.com.jpeg" );

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        CollectionReference docRef = db.collection("user").document(currentUser.getEmail()).collection("contact");
        docRef.document(telContact).set(contactAttributs);
        }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btt_add_Contact_img) {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
        } else if (view.getId()==R.id.btt_add_contact) {
            String nomContact = nom_Contact.getText().toString();
            String prenomContact = prenom_Contact.getText().toString();
            String emailContact = email_Contact.getText().toString();
            String serviceContact = service_Contact.getText().toString();
            String telContact = tel_Contact.getText().toString();
            String urlImgContact = urlImg_Contact.getText().toString();
            insertNewContact(nomContact, prenomContact, emailContact,serviceContact, telContact, urlImgContact );
            Intent myintent= new Intent(this, Liste_contacts.class);
            startActivity(myintent);
        }else if(view.getId()==R.id.back_button){
            onBackPressed(); // gérer l'événement de clic sur la flèche arrière
        }

    }
}