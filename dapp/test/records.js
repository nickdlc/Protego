const PatientRecords = artifacts.require('./PatientRecords.sol');
const ProtegoCoin = artifacts.require('./ProtegoCoin.sol');
const { BN } = require('@openzeppelin/test-helpers');
const utils = require('./utils.js');

contract('PatientRecords.sol', (accounts) => {
    describe('Record Operations', () => {
        let patientRecords;
        let token;
        
        const vitalField = "Heart Rate";
        const vitalValue = "87";
        const medDosage = "87 mg";
        const medName = "Tylenol";
        
        const currentTime = Math.floor(Date.now() / 1000); // convert to seconds
        
        before(async () => {
            patientRecords = await PatientRecords.deployed();
            token = await ProtegoCoin.new(utils.totalSupply);
            await patientRecords.initToken(token.address);
            
            await token.transfer(patientRecords.address, new BN('10000000000000000000000000')); // transfer 1e25 tokens
        });
        
        it('should have empty records for account[1]', async () => {
            const records = await patientRecords.getRecordsForSelf.call({from: accounts[1]});
            assert.isEmpty(records, 'accounts[1] is has no records');
        });
        
        it('should not have accounts[1] in allPatientAddresses', async () => {
            const allPatientAddresses = await patientRecords.getAllPatients.call();
            assert.notInclude(allPatientAddresses, accounts[1], "accounts[1] is not in allPatientAddresses");
        });
        
        it('should give accounts[1] a token balance', async () => {
            // Give accounts[1] 3e18 tokens and verify they have it
            const tokensAdded = '3000000000000000000'
            const tokensForAccount1 = new BN(tokensAdded);
            await token.transfer(accounts[1], tokensForAccount1, { from: accounts[0] });
            const account1Balance = await token.balanceOf.call(accounts[1]);            
            assert.strictEqual(account1Balance.toString(), tokensForAccount1.toString(), `accounts[1] has ${tokensAdded} tokens`);
        });
        
        it('should create a new record', async () => {
            await patientRecords.addRecord(currentTime, { from: accounts[1] });
            const recordsBeforePopulation = await patientRecords.getRecordsForSelf.call({from: accounts[1]});
            assert.isNotEmpty(recordsBeforePopulation, 'accounts[1] does not have any records');
            assert.strictEqual(recordsBeforePopulation[0].timeReported, currentTime.toString(), `Time Reported is ${currentTime}`);
        });
        
        it('should have accounts[1] in allPatientAddresses', async () => {
            const allPatientAddresses = await patientRecords.getAllPatients.call();
            assert.include(allPatientAddresses, accounts[1], "accounts[1] is in allPatientAddresses");
        });
        
        it('should add a new vital to accounts[1] latest record', async () => {
            await patientRecords.addNewVital(vitalField, vitalValue, { from: accounts[1] });
            
            const recordsAfterVitalAdd = await patientRecords.getRecordsForSelf.call({from: accounts[1]});
            assert.isDefined(recordsAfterVitalAdd[0].vital, "Vital is defined");
            
            const gotVital = recordsAfterVitalAdd[0].vital;

            assert.strictEqual(gotVital.field[0], vitalField, `Vital field is ${vitalField}`);
            assert.strictEqual(gotVital.value[0], vitalValue, `Vital value is ${vitalValue}`);
        });
        
        it('should add a new medication to accounts[1] latest record', async () => {
            await patientRecords.addMedication(currentTime, medDosage, medName, { from: accounts[1] });
            
            const recordsAfterMedAdd = await patientRecords.getRecordsForSelf.call({from: accounts[1]});
            assert.isDefined(recordsAfterMedAdd[0].medication, "Medication is defined");
            
            const gotMed = recordsAfterMedAdd[0].medication;
            assert.strictEqual(gotMed.datePrescribed[0], currentTime.toString(), `Date prescribed is ${currentTime}`);
            assert.strictEqual(gotMed.dosage[0], medDosage, `Dosage is ${medDosage}`);
            assert.strictEqual(gotMed.name[0], medName, `Medication name is ${medName}`);
        });
        
        it('should add a new record', async () => {
            const newTime = Math.floor(Date.now() / 1000);
            await patientRecords.addRecord(newTime, { from: accounts[1] });
            const newRecords = await patientRecords.getRecordsForSelf.call({from: accounts[1]});
            assert.isTrue(newRecords.length == 2, 'accounts[1] has 2 records');
            assert.strictEqual(newRecords[1].timeReported, newTime.toString(), `Time Reported is ${newTime}`);
                        
            // Verify the older fields are not defined
            assert.isEmpty(newRecords[1].vital.field, 'Vital field is not defined for new record');
            assert.isEmpty(newRecords[1].vital.value, 'Vital value is not defined for new record');
            assert.isEmpty(newRecords[1].medication.datePrescribed, 'Medication date prescribed is not defined for new record');
            assert.isEmpty(newRecords[1].medication.dosage, 'Medication dosage is not defined for new record');
            assert.isEmpty(newRecords[1].medication.name, 'Medication name is not defined for new record');
        });
    });
});