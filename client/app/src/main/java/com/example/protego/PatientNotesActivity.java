package com.example.protego;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class PatientNotesActivity extends AppCompatActivity {
    public static class NotesInfo {
        private final String title;
        private final String date;
        private final boolean visibility;
        private final String details;

        public NotesInfo(String title, String date, boolean visibility, String details) {
            this.title = title;
            this.date = date;
            this.visibility = visibility;
            this.details = details;
        }
    }

    NotesInfo[] patientNotesData = new NotesInfo[]{
            new NotesInfo("test 1","2/24/2022",true,"This is a test note"),
            new NotesInfo("test 2","2/23/2022",false,"This is a test to see how the page handles longer notes and the visibility being set to private."),
            new NotesInfo("test 3","2/23/2022",true,"Test to see if scrollview works")};

    private void getPatientNotes(){
        LinearLayout patientDataLayout =(LinearLayout)findViewById(R.id.patientNotesScrollviewLayout);

        for(NotesInfo info: patientNotesData){
            TextView noteTitle = new TextView(getBaseContext());
            noteTitle.setText(info.title);
            noteTitle.setTextSize(28);
            noteTitle.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

            TextView noteDate = new TextView(getBaseContext());
            noteDate.setText(info.date);
            noteDate.setTextSize(28);
            noteDate.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

            TextView noteVisibility = new TextView(getBaseContext());
            noteVisibility.setText("Visibility: "+new Boolean(info.visibility).toString());
            noteVisibility.setTextSize(28);
            noteVisibility.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

            TextView dataDetails = new TextView(getBaseContext());
            dataDetails.setText(info.details);
            dataDetails.setTextSize(28);
            dataDetails.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

            LinearLayout newLayout = new LinearLayout(getBaseContext());
            newLayout.setOrientation(LinearLayout.VERTICAL);
            newLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
            newLayout.addView(noteTitle);
            newLayout.addView(noteDate);
            newLayout.addView(noteVisibility);
            newLayout.addView(dataDetails);

            CardView newCardView = new CardView(getBaseContext());
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            cardParams.setMargins(0,0,0,50);
            newCardView.setLayoutParams(cardParams);
            newCardView.addView(newLayout);
            newCardView.setElevation(15);
            newCardView.setContentPadding(15,0,0,5);
            newCardView.setRadius(10);
            newCardView.setBackgroundColor(ContextCompat.getColor(this, R.color.notes_color));
            patientDataLayout.addView(newCardView);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_notes);

        getPatientNotes();
    }
}
