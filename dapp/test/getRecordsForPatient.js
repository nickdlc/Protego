const PatientRecords = artifacts.require('./PatientRecords.sol');
const ProtegoCoin = artifacts.require('./ProtegoCoin.sol');
const { BN } = require('@openzeppelin/test-helpers');
const utils = require('./utils.js');

contract('PatientRecords.sol', (accounts) => {
    describe('Getting Records From Patients', () => {
        [owner, patient1, patient2, doctor] = accounts;
        
        let patientRecords;
        let token;
        
        const vitalField = "Heart Rate";
        const vitalValue = "87";
        const medDosage = "87 mg";
        const medName = "Tylenol";
        
        const patient1InitVal = '1000000000000000000'; // 1e18
        const patient2InitVal = '0'; // 0
        const doctorInitVal = '3000000000000000000'; // 3e18
        
        const allowance = '1000000000000000000'; // 1e18
        const reward = '10000000000000000'; // 1e16
        
        const currentTime = Math.floor(Date.now() / 1000); // convert to seconds
        
        before(async () => {
            patientRecords = await PatientRecords.deployed();
            token = await ProtegoCoin.new(utils.totalSupply);
            await patientRecords.initToken(token.address);
            
            // Send tokens
            await token.transfer(patientRecords.address, new BN('10000000000000000000000000')); // transfer 1e25 tokens
        });
        
        it('should transfer initial token balances for patients and doctors', async () => {
            await token.transfer(patient1, new BN(patient1InitVal));
            await token.transfer(patient2, new BN(patient2InitVal));
            await token.transfer(doctor, new BN(doctorInitVal));
            
            const actualInitValP1 = await token.balanceOf.call(patient1);
            const actualInitValP2 = await token.balanceOf.call(patient2);
            const actualInitValD = await token.balanceOf.call(doctor);
            
            assert.strictEqual(actualInitValP1.toString(), patient1InitVal, `Patient 1 has init balance of ${patient1InitVal}`);
            assert.strictEqual(actualInitValP2.toString(), patient2InitVal, `Patient 2 has init balance of ${patient2InitVal}`);
            assert.strictEqual(actualInitValD.toString(), doctorInitVal, `Doctor has init balance of ${doctorInitVal}`);
            
            await token.approve(patientRecords.address, new BN(allowance), { from: doctor });
            const doctorContractAllowance = await token.allowance(doctor, patientRecords.address, { from: doctor });
            assert.strictEqual(doctorContractAllowance.toString(), allowance, `Contract has ${allowance} from doctor`);
        });
        
        it('should create a record for patient 1 and 2 and populate it', async () => {
            await patientRecords.addRecord(currentTime, { from: patient1 });
            await patientRecords.addRecord(currentTime, { from: patient2 });
            const recordsBeforePopulation1 = await patientRecords.getRecordsForSelf.call({from: patient1});
            const recordsBeforePopulation2 = await patientRecords.getRecordsForSelf.call({from: patient2});
            assert.isNotEmpty(recordsBeforePopulation1, 'Patient 1 has records');
            assert.isNotEmpty(recordsBeforePopulation2, 'Patient 2 has records');
            
            await patientRecords.addNewVital(vitalField, vitalValue, { from: patient1 });
            await patientRecords.addNewVital(vitalField+'1', vitalValue+'1', { from: patient2 });
            
            const recordsAfterVitalAdd1 = await patientRecords.getRecordsForSelf.call({from: patient1});
            const recordsAfterVitalAdd2 = await patientRecords.getRecordsForSelf.call({from: patient2});
            assert.isDefined(recordsAfterVitalAdd1[0].vital, "Vital is defined for Patient 1");
            assert.isDefined(recordsAfterVitalAdd2[0].vital, "Vital is defined for Patient 2");
            
            await patientRecords.addMedication(currentTime, medDosage, medName, { from:patient1 });
            await patientRecords.addMedication(currentTime+1, medDosage+'1', medName+'1', { from:patient2 });
            
            const recordsAfterMedAdd1 = await patientRecords.getRecordsForSelf.call({from: patient1});
            const recordsAfterMedAdd2 = await patientRecords.getRecordsForSelf.call({from: patient2});
            assert.isDefined(recordsAfterMedAdd1[0].medication, "Medication is defined for Patient 1");
            assert.isDefined(recordsAfterMedAdd1[0].medication, "Medication is defined for Patient 2");
        });
        
        it('should have the doctor access Patient 1 and Patient 2 records', async () => {
            await patientRecords.requestRecordsForPatient(patient1, { from: doctor });
            await patientRecords.requestRecordsForPatient(patient2, { from: doctor });
            const allPatientRecords = await patientRecords.getRecordsToDoctor.call({from: doctor});
            assert.isNotEmpty(allPatientRecords, "All Patient Records should be size 2");
            
            const pat1Records = allPatientRecords[0];
            const pat2Records = allPatientRecords[1];
            
            assert.isNotEmpty(pat1Records, "Patient 1 has records to be accessed");
            assert.isNotEmpty(pat2Records, "Patient 2 has records to be accessed");
            
            assert.strictEqual(pat1Records[0].vital.field[0], vitalField, `Patient 1 has field name ${vitalField} for doctor`);
            assert.strictEqual(pat2Records[0].vital.field[0], vitalField+'1', `Patient 2 has field name ${vitalField+'1'} for doctor`);
            
            assert.strictEqual(pat1Records[0].medication.dosage[0], medDosage, `Patient 1 has field name ${medDosage} for doctor`);
            assert.strictEqual(pat2Records[0].medication.dosage[0], medDosage+'1', `Patient 2 has field name ${medDosage+'1'} for doctor`);
        });
        
        it('should have 2 patients in allPatientAddresses', async () => {
            const allPatientAddresses = await patientRecords.getAllPatients.call();
            assert.strictEqual(allPatientAddresses.length.toString(), '2', 'There are only 2 patients in allPatientAddresses');
            assert.include(allPatientAddresses, patient1, "Patient 1 is in allPatientAddresses");
            assert.include(allPatientAddresses, patient2, "Patient 2 is in allPatientAddresses");
        });
        
        it('should set token values correctly', async () => {
            const pat1balance = await token.balanceOf.call(patient1);
            const pat2balance = await token.balanceOf.call(patient2);
            
            const rewardBN = new BN(reward);
            const p1initBN = new BN(patient1InitVal);
            const p2initBN = new BN(patient2InitVal);
            
            assert.strictEqual(pat1balance.toString(), p1initBN.add(rewardBN).toString(), 'Patient 1 should have +0.1 PROT to init balance');
            assert.strictEqual(pat2balance.toString(), p2initBN.add(rewardBN).toString(), 'Patient 2 should have +0.1 PROT to init balance');
        });
        
        it('should have patient 1 have more tokens than patient 2', async () => {
            await patientRecords.requestRecordsForPatient(patient1, { from: doctor });
            
            const pat1balance = await token.balanceOf.call(patient1);
            const pat2balance = await token.balanceOf.call(patient2);
            
            const rewardBN = new BN(reward);
            const p1initBN = new BN(patient1InitVal);
            const p2initBN = new BN(patient2InitVal);
            
            assert.strictEqual(pat1balance.toString(), p1initBN.add(rewardBN).add(rewardBN).toString(), 'Patient 1 should have +0.2 PROT to init balance');
            assert.strictEqual(pat2balance.toString(), p2initBN.add(rewardBN).toString(), 'Patient 2 should have +0.1 PROT to init balance');
        });
    });
});