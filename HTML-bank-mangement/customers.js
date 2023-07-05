class customer{
    id = 0;
    name = "";
    email = "";
    address = "";
    phone = "";
    balance = 0;
    constructor(id, name, email, address, phone, balance){
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.balance = balance;
    }
}
let customers = [
    new customer(
        1,
        "alabama nguyen",
        "alabama@gmail.com",
        "Namek",
        "0123456789",
        200
    ),
    new customer(
        2,
        "obama tran",
        "obama@usa.com",
        "trai dat",
        "0987654321",
        300
    )
];
showCustomers(customers);
function showCustomers(customers){
    let tableBodyDiv = document.querySelector(".customer-table-body");
    tableBodyDiv.innerHTML = "";
    console.log("show customer");
    for(let customer of customers){
        console.log("show cus");
        var id = customer.id;
        tableBodyDiv.innerHTML += `
        <tr>
            <th scope="row" class="customer-id">${id}</th>
            <td class="customer-name">${customer.name}</td>
            <td class="customer-email"> ${customer.email} </td>
            <td class="customer-address">${customer.address} </td>
            <td class="customer-phone">${customer.phone} </td>
            <td class="customer-balance">${customer.balance} </td>
            <td>
                <button onclick="showInfo(${id})" data-toggle="modal"
                data-target="#infoCustomer" class="btn btn-outline-info fas fa-question"></button>

                <button onclick="showEdit(${id})" data-toggle="modal"
                data-target="#editCustomer" class="btn btn-outline-dark fas fa-pencil-alt"></button>

                <button onclick="showDeposit(${id})" data-toggle="modal"
                data-target="#deposit" class="btn btn-outline-success fas fa-plus"></button>

                <button onclick="showWithdraw(${id})" data-toggle="modal"
                data-target="#withdraw" class="btn btn-outline-secondary fas fa-minus"></button>

                <button onclick="showTransfer(${id})" data-toggle="modal"
                data-target="#transfer" class="btn btn-outline-primary fas fa-exchange-alt"></button>

                <button class="btn btn-outline-danger fas fa-ban"
                        onclick="return confirm('Chắc chắn muốn xóa KH này?')">
                </button>
            <td>
        </tr>
        `;
    }
}
function findMaxId(customers){
    var maxId = 0;
    for (let customer of customers){
        if(customer.id > maxId){
            maxId = customer.id;
        }
    }
    return maxId + 1;
}
function doCreate(){
    var inpName = document.querySelector("#name").value;
    var inpEmail = document.querySelector("#email").value;
    var inpAddress = document.querySelector("#address").value;
    var inpPhone = document.querySelector("#phone").value;

    let requires = [];
    if(inpName == "") requires.push("Tên không được để trống");
    if(inpEmail == "") requires.push("Email không được để trống");
    if(inpAddress == "") requires.push("Địa chỉ không được để trống");
    if(inpPhone == "") requires.push("Phone không được để trống");

    let createDiv = document.querySelector("#create-result");
    createDiv.innerHTML = "";

    if(requires.length > 0){
        createDiv.innerHTML += '<span class="alert alert-danger">';
        for(var i = 0; i < requires.length; i++){
            createDiv.innerHTML += `
            <p>${requires[i]}</p>
            `;
        }
        createDiv.innerHTML += '</span>';
    } else{
        createDiv.innerHTML += `
            <p class="alert alert-success">Tạo mới thành công</p>
            `;

        customers.push(new customer(
            findMaxId(customers),
            inpName,
            inpEmail,
            inpAddress,
            inpPhone,
            0
        ));
    
        document.querySelector("#name").value = "";
        document.querySelector("#email").value = "";
        document.querySelector("#address").value = "";
        document.querySelector("#phone").value = "";
        showCustomers(customers);
    }
    
}
function showInfo(id){
    var customerInfo = customers.find(function (value){
        return value.id == id;
    })

    document.querySelector("#name-info").value = customerInfo.name;
    document.querySelector("#email-info").value = customerInfo.email;
    document.querySelector("#address-info").value = customerInfo.address;
    document.querySelector("#phone-info").value = customerInfo.phone;
    document.querySelector("#balance-info").value = customerInfo.balance;
}
function showEdit(id){
    var customerEdit = customers.find(function (value){
        return value.id == id;
    })

    document.querySelector("#id-edit").value = id;
    document.querySelector("#name-edit").value = customerEdit.name;
    document.querySelector("#email-edit").value = customerEdit.email;
    document.querySelector("#address-edit").value = customerEdit.address;
    document.querySelector("#phone-edit").value = customerEdit.phone;
}
function doEdit(){
    var idEdit = document.querySelector("#id-edit").value;

    var customerEdited = customers.find(function (value){
        return value.id == idEdit;
    })

    customerEdited.name = document.querySelector("#name-edit").value;
    customerEdited.email = document.querySelector("#email-edit").value;
    customerEdited.address = document.querySelector("#address-edit").value;
    customerEdited.phone = document.querySelector("#phone-edit").value;

    showCustomers(customers);
}
function showDeposit(id){
    var customerDeposit = customers.find(function (value){
        return value.id == id;
    })

    document.querySelector("#id-deposit").value = id;
    document.querySelector("#name-deposit").value = customerDeposit.name;
    document.querySelector("#email-deposit").value = customerDeposit.email;
    document.querySelector("#address-deposit").value = customerDeposit.address;
    document.querySelector("#phone-deposit").value = customerDeposit.phone;
    document.querySelector("#balance-deposit").value = customerDeposit.balance;

}
function doDeposit(){
    var idDeposit = document.querySelector("#id-deposit").value;

    var customerDeposit = customers.find(function (value){
        return value.id == idDeposit;
    })

    var transAmount = +document.querySelector("#trans-amount-deposit").value;
    var balance = +customerDeposit.balance;
    customerDeposit.balance = balance + transAmount;

    showCustomers(customers);
}
function showWithdraw(id){
    var customerWithdraw = customers.find(function (value){
        return value.id == id;
    })

    document.querySelector("#id-withdraw").value = id;
    document.querySelector("#name-withdraw").value = customerWithdraw.name;
    document.querySelector("#email-withdraw").value = customerWithdraw.email;
    document.querySelector("#address-withdraw").value = customerWithdraw.address;
    document.querySelector("#phone-withdraw").value = customerWithdraw.phone;
    document.querySelector("#balance-withdraw").value = customerWithdraw.balance;
}
function doWithdraw(){
    var idWithdraw = document.querySelector("#id-withdraw").value;

    var customerDeposit = customers.find(function (value){
        return value.id == idWithdraw;
    })

    var transAmount = +document.querySelector("#trans-amount-withdraw").value;
    var balance = +customerDeposit.balance;
    customerDeposit.balance = balance - transAmount;

    showCustomers(customers);
}
function calculateTotal(amount){
    let amountNum = parseFloat(amount);
    document.getElementById('total-transfer').value = amountNum + (amountNum * 0.1);
}
function showTransfer(id){
    var sender = customers.find(function (value){
        return value.id == id;
    })

    var recipients = customers.filter(function (value){
        return value.id != id;
    })

    var recipientSelect = document.querySelector("#recipient-transfer-id");
    recipientSelect.innerHTML = "";

    for(let recipient of recipients){
        recipientSelect.innerHTML += `
        <option value="${recipient.id} ">${recipient.name} </option>
        `
    }

    document.querySelector("#id-transfer").value = id;
    document.querySelector("#name-transfer").value = sender.name;
    document.querySelector("#email-transfer").value = sender.email;
    document.querySelector("#address-transfer").value = sender.address;
    document.querySelector("#phone-transfer").value = sender.phone;
    document.querySelector("#balance-transfer").value = sender.balance;
}
function doTransfer(){
    var senderId = document.querySelector("#id-transfer").value;
    var recipientId = document.querySelector("#recipient-transfer-id").value;

    var sender = customers.find(function (value){
        return value.id == senderId;
    });

    var recipient = customers.find(function (value){
        return value.id == recipientId;
    });

    var transAmount = +document.querySelector("#trans-amount-transfer").value;
    var totalTransAmount = transAmount*1.1;

    sender.balance -= totalTransAmount;
    recipient.balance += transAmount;

    showCustomers(customers);
}