const PatientRecords = artifacts.require('./PatientRecords.sol');

module.exports = (deployer) => {
    deployer.deploy(PatientRecords);
}