const PatientRecords = artifacts.require('./PatientRecords.sol');
const ProtegoCoin = artifacts.require('./ProtegoCoin.sol');
const { BN } = require('@openzeppelin/test-helpers');
const utils = require('./utils.js');

contract('PatientRecords.sol', () => {
    describe('Function: constructor', () => {
        let patientRecords;
        let token;
        
        before(async () => {
            patientRecords = await PatientRecords.deployed();
            token = await ProtegoCoin.new(utils.totalSupply);
            await patientRecords.initToken(token.address);
            
            await token.transfer(patientRecords.address, new BN('10000000000000000000000000')); // transfer 1e25 tokens
        });
        
        it('should have a valid token address', async () => {
            const tokenAddr = await patientRecords.token.call();
            assert.isNotNull(tokenAddr, 'Token is null');
        });
        
        it('should have all tokens owned by PatientRecords', async () => {
            const tokenAddr = await patientRecords.token.call();
            const token = await ProtegoCoin.at(tokenAddr);
            
            const totalTokens = "10000000000000000000000000"; // 1e25
            const ownedTokens = await token.balanceOf.call(patientRecords.address);
            
            assert.strictEqual(ownedTokens.toString(), totalTokens.toString(), 'PatientRecords does not 1e25 tokens after deployment');
        });
    });
});