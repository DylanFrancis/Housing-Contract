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
    
    //=====owner=====
    
    function stealHouse(uint256 id) ownerOnly public{
        House memory h = houses[id];
        h.owner = owner;
        h.forSale = true;
    }
    
    function stealHouseKindly(uint256 id) ownerOnly payable public {
        House memory h = houses[id];
        uint256 pay = houses[id].value * 8 / 100;
        h.owner.transfer(pay);
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
        houseId++;
        houses[houseId] = House(houseId, 0, msg.sender, false);
    }
    
    function getAllHousesForSale() public returns(House[] memory h){
        House[] memory hArr = new House[](houseId);
        for(uint256 x = 0; x <= houseId; x++){
            if(houses[x].forSale)
                hArr[x] = houses[x];
        }
        return hArr;
    }
    
    function sellHouse(uint256 id) houseOwned(id) public{
        houses[id].forSale = true;
    }
    
    function buyHouse(uint256 id) houseForSale(id) payable public{
        if(msg.value != houses[id].value)
            return;
        houses[id].owner.transfer(msg.value);
        houses[id].owner = msg.sender;
        houses[id].forSale = false;
    }
}