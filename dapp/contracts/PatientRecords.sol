pragma solidity ^0.8.13;

import "@openzeppelin/contracts/utils/math/SafeMath.sol";
import "@openzeppelin/contracts/token/ERC20/ERC20.sol";

import "./ProtegoCoin.sol";

using SafeMath for uint;

contract PatientRecords {
    uint256 constant public TOKEN_DECIMALS = 18;
    uint256 constant public TOKEN_RATIO = 1 * 10 ** TOKEN_DECIMALS; // 1e18 tokens == 1 full token
    uint256 constant public TOTAL_TOKENS = 1e9; // init with 1 billion big tokens
    uint256 constant public TOTAL_SUPPLY = TOTAL_TOKENS * TOKEN_RATIO; // 1e9 * 1e18 total value supply
    
    uint    constant public INDIVIDUAL_PATIENT_ACCESS_COST = TOKEN_RATIO / 100; // Costs 0.1 PROT to access an individual patient history
    
    uint256 constant public WEI_TOKEN_RATIO = 100; // 1 wei == 100 tokens => 1 eth == 100 big tokens
    
    // struct Info {
    //     bytes32 _date_of_record;
    //     bytes32 _heart_rate; // uint
    //     bytes32 _blood_pressure;
    //     bytes32 _blood_type;
    //     bytes32 _respiratory_rate; // uint
    //     bytes32 _temperature; // uint
    //     bytes32 _height; // uint
    //     bytes32 _weight; // uint
    //     bool valid;
    // }
    
    // TODO: do we want to include notes? might be too personal
    struct Vital {
        string[] field;
        string[] value;
    }
    
    struct Medication {
        uint[] datePrescribed;
        string[] dosage;
        string[] name;
    }
    
    // Alternate method for accessing records in the future:
    // Prompt the patients whenever a doctor needs information and
    // give them permission. This way we can privatize the data even further
    // with a given record salt
    
    struct Record {
        Vital vital;
        Medication medication;
        uint timeCreated; // when creatted on the blockchain
        uint timeReported; // when reported by the patient
    }

    // patient address => their list of all their records
    // Can only manipulate most recently added record, becomes immutable after a new record is added
    mapping (address => Record[]) private patientRecords;
    mapping (address => Record[][]) private doctorAccessedRecords;
    mapping (address => uint) public totalPatientRewards; // total reward attributed to a patient
    address[] public allPatientAddresses; // all patient addresses that will be sorted off-chain
    ProtegoCoin public token;
    
    event _PatientRecordsCreated(address contractAddress);
    event _TokenInit(address tokenAddr);
    event _NewVital(address patient, string field);
    event _NewMedication(address patient, string medicationName);
    event _TokensBought(address buyer, uint256 totalBought);
    event _TokensSold(address seller, uint256 totalSold);
    event _RecordReceived(address receiver, address patient, uint cost);
    event _NewTotalRewardsForPatient(address patient, uint totalRewards);
                
    // TODO: need token addr
    constructor() {
        emit _PatientRecordsCreated(address(this));
    }
    
    function initToken(address tokenAddr) public {
        token = ProtegoCoin(tokenAddr);
        
        emit _TokenInit(tokenAddr);
    }
    
    modifier hasHistory {
        require(patientHasRecords(msg.sender), "No records found for patient");
        
        _;
    }
    
    modifier validField(string memory field) {
        require(bytes(field).length != 0, "Field must be a valid value");
        
        _;
    }
    
    
    
    
    
    // ---------------------------------- PATIENT METHODS ----------------------------------
    
    function getRecordsForSelf() public view returns (Record[] memory records) {
        return patientRecords[msg.sender];
    }
    
    function getAllPatients() public view returns (address[] memory patients) {
        return allPatientAddresses;
    }
    
    // Most recent record becomes the working record for the patient
    function addRecord(uint timeReported) public {
        // The time reported has to be before when the record is created
        // timeReported must be in seconds
        uint currentTime = block.timestamp;
        require(timeReported <= currentTime, "Reported timestamp must be before transaction time");
        
        address currPatientAddr = msg.sender;
        
        // Add patient to all patient list if this si the first time they're adding a record
        if (!patientHasRecords(currPatientAddr)) {
            allPatientAddresses.push(currPatientAddr);
        }        
        
        // Initialize a new Record and push it on the patient's record list
        Record[] storage records = patientRecords[currPatientAddr];
        records.push(Record({
            vital: Vital({
                field: new string[](0),
                value: new string[](0)
            }),
            medication: Medication({
                datePrescribed: new uint[](0),
                dosage: new string[](0),
                name: new string[](0)
            }),
            timeCreated: currentTime,
            timeReported: timeReported
        }));
    }
    
    function addNewVital(string memory field, string memory val) public hasHistory validField(field) {
        // Need to do this everytime in order to access the storage value of an array
        Record[] storage records = patientRecords[msg.sender];
        Record storage record = records[records.length - 1];
        Vital storage vital = record.vital;
        
        vital.field.push(field);
        vital.value.push(val);
                
        emit _NewVital(msg.sender, field);
    }
    
    // TODO: hash result
    function addMedication(uint datePrescribed, string memory dosage, string memory name) public hasHistory {
        // Make sure all fields are non-null
        require(datePrescribed != 0, "Date Prescribed must be non-empty");
        require(bytes(dosage).length != 0, "Dosage must be non-empty");
        require(bytes(name).length != 0, "Name must be non-empty");
        
        Record[] storage records = patientRecords[msg.sender];
        Record storage record = records[records.length - 1];
        Medication storage medication = record.medication;
        
        medication.datePrescribed.push(datePrescribed);
        medication.dosage.push(dosage);
        medication.name.push(name);
                        
        emit _NewMedication(msg.sender, name);
    }
    
    
    
    
    
    
    // ---------------------------------- DOCTOR METHODS ----------------------------------
    
    function requestRecordsForPatient(address patient) public {
        require(patientHasRecords(patient), "Address provided has no records");
        
        uint256 allowance = token.allowance(msg.sender, address(this));

        require(allowance >= INDIVIDUAL_PATIENT_ACCESS_COST, "Provided allowance not enough to get record");
        
        // Pay the patient the cost to access their information
        // Fails if requester does not have enough tokens
        token.transferFrom(msg.sender, patient, INDIVIDUAL_PATIENT_ACCESS_COST);
        emit _RecordReceived(msg.sender, patient, INDIVIDUAL_PATIENT_ACCESS_COST);
        
        totalPatientRewards[patient] += INDIVIDUAL_PATIENT_ACCESS_COST;
        emit _NewTotalRewardsForPatient(patient, totalPatientRewards[patient]);
        
        doctorAccessedRecords[msg.sender].push(patientRecords[patient]);
    }
    
    function getRecordsToDoctor() public view returns (Record[][] memory records) {
        return doctorAccessedRecords[msg.sender];
    }
    
    
    
    
    
    
    // ---------------------------------- BUY/SELL METHODS ----------------------------------
    
    function requestTokens() public {
        token.transfer(msg.sender, TOKEN_RATIO);
    }
    
    function buy() payable public {
        uint256 weiGiven = msg.value;
        uint256 totalTokenBalance = token.balanceOf(address(this));
        uint256 tokensForEthGiven = WEI_TOKEN_RATIO * weiGiven;
        
        require(weiGiven > 0, "You need to send some ether");
        require(tokensForEthGiven <= totalTokenBalance, "Not enough tokens in the reserve");
        
        token.transfer(msg.sender, tokensForEthGiven);
        emit _TokensBought(msg.sender, tokensForEthGiven);
    }
    
    function sell(uint256 sellTokens) payable public {
        require(sellTokens > 0, "Must sell a positive number of tokens.");
        
        uint256 allowance = token.allowance(msg.sender, address(this));
        require(allowance >= sellTokens, "Need to approve more allowance");
        token.transferFrom(msg.sender, address(this), sellTokens);
        
        uint256 ethForTokensGiven = WEI_TOKEN_RATIO * sellTokens / 100;
        payable(msg.sender).transfer(ethForTokensGiven);
        emit _TokensSold(msg.sender, sellTokens);
    }
    
    
    
    
    
    
    
    
    
    // ---------------------------------- HELPER METHODS ----------------------------------
    
    function patientHasRecords(address patient) private view returns (bool hasRecords) {
        return patientRecords[patient].length > 0;
    }
    
    // Precondition: field != 0, salt != 0
    // returns hashed field and salt
    function hash(bytes32 field) private pure returns (bytes32 hashed_result) {
        return keccak256(abi.encodePacked(field));
    }
}