pragma solidity ^0.5.0;
pragma experimental ABIEncoderV2;

contract Housing{
    struct House{
        uint256 id;
        uint256 value;
        address payable owner;
        bool forSale;
    }
    
    struct Valuator{
        address addr;
        bool approved;
    }
    
    event housesForSale(uint256[] hArr);
    event curHouseId(uint256 hId);

    address payable owner;
    uint256 houseId;

    mapping(uint256 => House) houses;
    mapping(address => Valuator) valuators;

    constructor() payable public{
        owner = msg.sender;
    }

    //=====modifiers=====

    modifier ownerOnly(){
        require (msg.sender == owner);
        _;
    }

    modifier houseOwned(uint256 id){
        require (houses[id].owner == msg.sender);
        _;
    }

    modifier houseForSale(uint256 id){
        require (houses[id].forSale);
        _;
    }

    modifier valuatorOnly(){
        require (valuators[msg.sender].approved);
        _;
    }

    modifier houseValue(uint256 id){
        require(houses[id].value > 0);
        _;
    }

    modifier enoughValue(uint256 amt){
        require(msg.value >= amt);
        _;
    }

    //=====owner=====

    function stealHouse(uint256 id) ownerOnly public{
        House memory h = houses[id];
        h.owner = owner;
        h.forSale = true;
    }

    function stealHouseKindly(uint256 id) ownerOnly enoughValue(houses[id].value * 80 / 100) payable public {
        House memory h = houses[id];
        h.owner.transfer(houses[id].value * 80 / 100);
        h.owner = owner;
        h.forSale = true;
    }

    function approveValuator(address vAddr) ownerOnly public{
        valuators[vAddr].approved = true;
    }

    //=====valuator=====

    function applyForValuator() public{
        valuators[msg.sender].addr = msg.sender;
    }

    function assignValue(uint256 id, uint256 v) valuatorOnly public{
        houses[id].value = v;
    }

    //=====user=====

    function registerHome() public{
        houses[houseId] = House(houseId, 0, msg.sender, false);
        houseId++;
        emit curHouseId(houseId);
    }

    function getAllHousesForSale() view public returns(uint256[] memory arr){
        uint256[] memory hArr = new uint256[](houseId);
        for(uint256 x = 0; x <= houseId - 1; x++){
            if(houses[x].forSale){
                hArr[x] = houses[x].value;
            }
        }
        return hArr;
        // emit housesForSale(hArr);
    }

    function sellHouse(uint256 id) houseOwned(id) houseValue(id) public{
        houses[id].forSale = true;
    }

    function buyHouse(uint256 id) houseForSale(id) enoughValue(houses[id].value) payable public{
        houses[id].owner.transfer(msg.value);
        houses[id].owner = msg.sender;
        houses[id].forSale = false;
    }
}