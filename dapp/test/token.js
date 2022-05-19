const PatientRecords = artifacts.require('./PatientRecords.sol');
const ProtegoCoin = artifacts.require('./ProtegoCoin.sol');
const { BN } = require('@openzeppelin/test-helpers');
const utils = require('./utils.js');

contract('PatientRecords.sol', (accounts) => {
    describe('Token Operations', () => {
        let patientRecords;
        let token;
        
        before(async () => {
            patientRecords = await PatientRecords.deployed();
            token = await ProtegoCoin.new(utils.totalSupply);
            await patientRecords.initToken(token.address);
            
            await token.transfer(patientRecords.address, new BN('10000000000000000000000000')); // transfer 1e25 tokens
            await token.transfer(accounts[1], new BN('3000000000000000000')); // transfer 3e18 to accounts[1]
        });
        
        it('should have tokens for account[1]', async () => {
            const tokensForAccount0 = new BN('3000000000000000000');
            const actualTokens = await token.balanceOf.call(accounts[1]);
            
            assert.strictEqual(actualTokens.toString(), tokensForAccount0.toString(), 'accounts[1] does not have 3e18 tokens');
        });
        
        it('should not have tokens for account[2]', async () => {
            const tokensForAccount0 = new BN('0');
            const actualTokens = await token.balanceOf.call(accounts[2]);
            
            assert.strictEqual(actualTokens.toString(), tokensForAccount0.toString(), 'accounts[2] does not have 0 tokens');
        });
    });
});