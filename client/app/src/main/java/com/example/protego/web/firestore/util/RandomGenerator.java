package com.example.protego.web.firestore.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomGenerator {
    public static List<String> randomApprovedDoctors;
    public static Integer randNum() {
        return (int)Math.floor(Math.random() * 40) + 60;
    }


    public static Integer randNumInRange(Integer min, Integer max) {  //the min and max values are inclusive
        Random random = new Random();
        Integer aboveMax = max + 1;
        int randint = random.nextInt(aboveMax);

        while (randint < min || randint > aboveMax) {
            if (randint < min) {
                randint = randint + min;
            } else if (randint > aboveMax) {
                randint = randint % aboveMax;
            }
        }
        return randint;
    }

    public static String randomSource(){
        int num = randNumInRange(0, 1);
        String[] sourceTypes = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

        String source = "";
        if(num == 0){
            source = "I";
        }
        else if(num == 1){
            source = "Dr. " + sourceTypes[randNumInRange(0, sourceTypes.length - 1)];
        }
        else{
            source = "I";
        }
        return source;
    }



    public static List<String> randomApprovedDoctors(){

        int rand = randNumInRange(1, 9);
        String[] sourceTypes = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        List<String> result = new ArrayList<String>();
        String source;

        for(int i = 0; i < rand; i++){
            result.add("Dr. " + sourceTypes[i]);
        }

        return result;
    }



    public static String randomMessage(){

        int rand = randNumInRange(0, 5);
        String[] messages = {
                "I have intense back pain",
                "I have an intense headache",
                "I had a bad reaction to my medication",
                "I have a new rash throughout my body",
                "My blood pressure is higher than usual",
                "My temperature is very high"
        };

        return messages[rand];
    }

    public static String randomVisibility(){
        String visibility;
        int rand = randNumInRange(0, 1);
        if(rand == 0){
            visibility = "Public";
        }
        else{
            visibility = "Private";
        }

        return visibility;
    }


    public static List<String> randomMedication(List<String> approvedDoctors){
        String[] name = {"Atorvastatin", "Omeprazole", "Amlodipine", "Lisinopril", "Levothyroxine", "Metformin"};
        int randNameIndex = randNumInRange(0, name.length - 1);
        String nameSelected = name[randNameIndex];
        int randDoctorsIndex = randNumInRange(0, approvedDoctors.size() - 1);
        List<String> dosages = new ArrayList<String>();
        String dosage = "";
        List<String> medicationInfo = new ArrayList<String>();
        String units = "mg";

        if(nameSelected == name[0]){
            dosages.clear();
            Collections.addAll(dosages, "10", "20", "40", "80");
            units = "mg";

        } else if(nameSelected == name[1]){
            dosages.clear();
            Collections.addAll(dosages, "10", "20", "40");
            units = "mg";


        } else if(nameSelected == name[2]){
            dosages.clear();
            Collections.addAll(dosages, "2.5", "5", "10");
            units = "mg";



        } else if(nameSelected == name[3]){
            dosages.clear();
            Collections.addAll(dosages, "2.5", "5", "10", "20", "30", "40");
            units = "mg";

        }
        else if(nameSelected == name[4]){
            dosages.clear();
            Collections.addAll(dosages, "25", "50", "75", "88", "100", "112", "125", "137", "150", "175", "200", "300");
            units = "mcg";

        }
        else if(nameSelected == name[5]){
            dosages.clear();
            Collections.addAll(dosages, "500", "850", "1000");
            units = "mg";

        }

        int randDosageIndex = randNumInRange(0, dosages.size() - 1);
        dosage = dosages.get(randDosageIndex);

        String prescriber = approvedDoctors.get(randDoctorsIndex);

        Collections.addAll(medicationInfo, nameSelected, dosage + units, prescriber);

        return medicationInfo;
    }
}
