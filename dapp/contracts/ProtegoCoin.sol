pragma solidity ^0.8.13;

import "@openzeppelin/contracts/utils/math/SafeMath.sol";
import "@openzeppelin/contracts/token/ERC20/ERC20.sol";

using SafeMath for uint;

contract ProtegoCoin is ERC20 {
    constructor(uint256 initialSupply) ERC20("Protego", "PROT") {
        _mint(msg.sender, initialSupply);
    }
}