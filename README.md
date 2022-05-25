# Protego

Protego is an Android healthcare app which improves communication between patients and their doctors by offering real-time access to health and fitness data. Our QR Code technology gives patients greater control to securely share their data. Doctors can scan a patient’s QR code to begin a connection with the patient, which includes access to viewing and adding to the patient’s data, and this connection must be approved and maintained by the patient. Patients can also choose to anonymously monetize their information on the blockchain; researchers can then pay these patients for access to this information.

[Final Engineering Report](https://github.com/nickdlc/Protego/files/8767308/Final.Engineering.Report.pdf)

## Setup
### Firebase Setup
1. Create a [Firebase project](https://firebase.google.com/).
2. Create a Firebase app by navigating to Firebase Overview.
3. On your app's dashboard, set up Firebase Authentication and Firestore.
   * You will need to allow read and write permissions for all in your Firestore settings.
4. Download the `google-services.json` from your app in Project Settings.
   * Place the file in `client/app`

### Android Studio Setup
1. Download [Android Studio](https://developer.android.com/studio) and open `Protego/client` as a project.
2. Connect a physical Android device or create an [Android Virtual Device](https://developer.android.com/studio/run/managing-avds).
   * If you are using an AVD, click on "Show Advanced Settings" on the "Verify Configuration" page and set the back camera to `Webcam0` if you have a webcam. This will allow you to scan QR codes using the AVD.

## Running Protego
1. Open Android Studio and build and run Protego.
2. Create new patients and doctors on the registration page.
   * You will receive a confirmation email from Firebase Authentication. Click on the provided link to verify the account(s).
3. Login with your desired user.

## QR Code Scanning and Connecting Patients to Doctors
1. While logged in as a doctor, click the "Scan QR Code" button to open the camera.
2. On a separate device and while logged in as a patient, click on the QR code icon in the bottom right of the dashboard to view your QR code.
3. Use the doctor's camera to scan this QR code.
   * If you are using an AVD for a doctor, you will need to save a copy of the patient's QR code on a separate device and scan it using the webcam.
4. On the patient's device, swipe up on the dashboard to bring up the notification menu and click on the new connection request from the desired doctor. 

## Blockchain
### Ganache Setup
1. Download [Ganache](https://trufflesuite.com/ganache/).
2. Open Ganache and click "New Workspace", then "Add Project", and add `Protego/dapp`.
3. Click on the "Server" tab and change the port number to `8545` and click on "Save Workspace".

### Deployment
1. Open [Remix](https://remix.ethereum.org/#optimize=false&runs=200&evmVersion=null&version=soljson-v0.8.13+commit.abaa5c0e.js).
2. Upload `Protego/dapp/contracts/ProtegoCoin.sol` and `Protego/dapp/contracts/PatientRecords.sol` to Remix.
3. In Remix, go to "Deploy & run transactions" and change the environment to "Ganache Provider".
4. Navigate to "File explorers" and select `ProtegoCoin.sol`.
5. Navigate to "Solidity compiler" and compile the contract.
6. Repeat 4-5 for `PatientRecords.sol`.
7. Return to "Deploy & run transactions" and, under "Contracts", select `ProtegoCoin`.
8. Next to the "Deploy" button, enter `1000000000000000000000000000` (1e27) for the `initialSupply`.
9. Click "Deploy".
10. Return to "Deploy & run transactions" and, under "Contracts", select `PatientRecords`.
11. Click "Deploy".
12. Under "Deployed Contracts", copy the deployed `ProtegoCoin` address.
13. Under the deployed `PatientRecords` contract, select `initToken()` and enter the `ProtegoCoin` address and run.

**NOTE**: Account 0 deployed the contracts and is not a user.

### Usage
1. Select an account (not Account 0) to serve as a patient uploader.
2. Call `addRecord()` with the timestamp in Unix time for the deployed `PatientRecords` contract.
3. Call `addNewVital()` and `addNewMedication()` for as many vitals and medications you want to add to the desired record.
   * If you create another record, any previous records are archived.
4. Select another account that isn't Account 0 or the patient uploader to serve as a researcher.
5. For initial testing, call `requestTokens()` to have an initial balance.
6. As the researcher, call `requestRecordsForPatient()` and supply the patient account address you used.
7. To retrieve the records, call `getRecordsToDoctor()`.
