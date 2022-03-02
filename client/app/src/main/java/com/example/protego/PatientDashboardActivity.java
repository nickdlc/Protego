package com.example.protego;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageButton;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import android.view.ViewGroup.LayoutParams;

        import java.util.ArrayList;

public class PatientDashboardActivity extends AppCompatActivity{
    //input fields
    private Button button;
    private ImageButton imageButton;
    private Button btnNotifications;
    private TextView tvNotifications;
    private static String Name;


    public static class PatientInfo {
        private final String title;
        private final String details;

        public PatientInfo(String title,String details) {
            this.title = title;
            this.details = details;
        }

        public String getTitle() {
            return title;
        }

        public String getDetails() {
            return details;
        }
    }

    ArrayList<PatientInfo> patientData = new ArrayList<>();

    private void setUpPatientInfo(){
        patientData.add( new PatientInfo("Heart Rate:", "87 Bpm"));
        patientData.add( new PatientInfo("Blood Pressure:", "87 Bpm"));
        patientData.add( new PatientInfo("Temperature:", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_dashboard);


        RecyclerView recyclerView = findViewById(R.id.patientDataRecyclerView);

        // update the user's name based on their profile information
        //TODO: update this name according to the database to get the patient's first name
        Name = "Example";

        setUpPatientInfo();

        PatientDashboardRecyclerViewAdapter adapter = new PatientDashboardRecyclerViewAdapter(this,patientData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //TODO : update the connection, the View Doctors button is connected to the View Notes Activity to test it.
        connectButtonToActivity(R.id.viewDoctorsButton, PatientNotesActivity.class);

        connectImageButtonToActivity(R.id.qrCodeButton, PatientQRCodeDisplay.class);

        //connectNotificationMenuToActivity(R.id.btnNotifications, PatientNotifications.class);

    }

    // navigate to next activity
    private void connectButtonToActivity(Integer buttonId, Class nextActivityClass) {

        button = findViewById(buttonId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), nextActivityClass);
                startActivity(i);
                finish();
            }
        });
    }

    private void connectImageButtonToActivity(Integer buttonId, Class nextActivityClass) {

        imageButton = findViewById(buttonId);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), nextActivityClass);
                startActivity(i);
                finish();
            }
        });
    }

    private void connectNotificationMenuToActivity(Integer buttonId, Class nextActivityClass) {

        btnNotifications = findViewById(buttonId);
        btnNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), nextActivityClass);
                startActivity(i);
                finish();
            }
        });
    }

    //function to call from the navbar fragment class to update the navbar according to the patients first name
    public static String getName(){
        return Name;
    }

}
