const PatientRecordsTest = artifacts.require('./PatientRecordsTest.sol');
const ProtegoCoin = artifacts.require('./ProtegoCoin.sol');
const { BN } = require('@openzeppelin/test-helpers');
const utils = require('./utils.js');

contract('PatientRecordsTest.sol', (accounts) => {
    describe('Function: requestTokens', () => {
        let patientRecords;
        let token;
        
        before(async () => {
            patientRecords = await PatientRecordsTest.deployed();
            const tokenAddr = await patientRecords.token.call();
            token = await ProtegoCoin.at(tokenAddr);
        });
        
        it('should have a valid token address', async () => {
            const tokenAddr = await patientRecords.token.call();
            assert.isNotNull(tokenAddr, 'Token is null');
        });
        
        it('should have all tokens owned by PatientRecords', async () => {
            const tokenAddr = await patientRecords.token.call();
            const token = await ProtegoCoin.at(tokenAddr);
            
            const totalTokens = "1000000000000000000000000000"; // 1e27
            const ownedTokens = await token.balanceOf.call(patientRecords.address);
            
            assert.strictEqual(ownedTokens.toString(), totalTokens.toString(), 'PatientRecordsTest does not 1e27 tokens after deployment');
        });
        
        it('should transfer tokens to accounts[1]', async () => {
            const shouldReceive = "1000000000000000000"; // 1e18
            await patientRecords.requestTokens({from:accounts[1]});
            
            const acc1Tokens = await token.balanceOf.call(accounts[1]);
            assert.strictEqual(acc1Tokens.toString(), shouldReceive.toString(), `accounts[1] has ${shouldReceive} tokens`);
        });
    });
});