const PatientRecordsTest = artifacts.require('./PatientRecordsTest.sol');

module.exports = (deployer) => {
    deployer.deploy(PatientRecordsTest);
}