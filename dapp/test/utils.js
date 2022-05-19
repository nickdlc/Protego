const { BN } = require('@openzeppelin/test-helpers');

module.exports = {
    tokenDeciamls: 18,
    tokenRatio: new BN('1000000000000000000'), // 1e18
    totalTokens: new BN('1000000000'), // 1e9
    totalSupply: new BN('1000000000000000000000000000'), // 1e27
    individualPatientAccessCost: new BN('10000000000000000'), // 1e16
    weiTokenRatio: 100
}