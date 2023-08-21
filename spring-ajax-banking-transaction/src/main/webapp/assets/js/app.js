class App {
    static DOMAIN_SERVER = window.origin;
    static API_SERVER = this.DOMAIN_SERVER+'/api';
    static  API_LOCATION = "https://vapi.vnappmob.com/api/province/"

    static API_CUSTOMER = this.API_SERVER + '/customers';
    static API_DEPOSIT = this.API_SERVER + '/deposits';
    static API_WITHDRAW = this.API_SERVER + '/withdraws';
    static API_TRANSFER = this.API_SERVER + '/transfers';
    static API_PROVINCE = this.API_LOCATION;
    static API_DISTRICT = this.API_LOCATION +"district";
    static API_WARD = this.API_LOCATION +"ward";

    static showDeleteConfirmDialog() {
      Swal.fire({
        title: 'Are you sure?',
        text: "You won't be able to revert this!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, delete it!'
      });
    }
  
    static showSuccessAlert(t) {
      Swal.fire({
        icon: 'success',
        title: t,
        position: 'top-end',
        showConfirmButton: false,
        timer: 1500,
      });
    }
  
    static showErrorAlert(t) {
      Swal.fire({
        icon: 'error',
        title: 'Warning',
        text: t,
      });
    }
  }
  
  class Customer {
    constructor(id, fullName, email, phone, balance, locationRegion, deleted) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.balance = balance;
        this.locationRegion = locationRegion;
        this.deleted = deleted;
    }
  }

  class LocationRegion{
    constructor(id, provinceId, provinceName, districtId, districtName, wardId, wardName, address) {
        this.id = id;
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        this.districtId = districtId;
        this.districtId = districtId;
        this.districtName = districtName;
        this.wardId = wardId;
        this.wardName = wardName;
        this.address = address;
    }
}
  
  class Deposit {
    constructor(id, customerId, transactionAmount) {
      this.id = id;
      this.customerId = customerId;
      this.transactionAmount = transactionAmount;
    }
  }

  class Withdraw {
    constructor(id, customerId, transactionAmount) {
      this.id = id;
      this.customerId = customerId;
      this.transactionAmount = transactionAmount;
    }
  }

  class Transfer {
    constructor(id, senderId, recipientId, transferAmount, transactionAmount, feeAmount){
        this.id = id;
        this.senderId = senderId,
        this.recipientId = recipientId,
        this.transferAmount = transferAmount,
        this.transactionAmount = transactionAmount,
        this.feeAmount = feeAmount
    }
  }
  
  // $(function() {
  //     $(".num-space").number(true, 0, ',', ' ');
  //     $(".num-point").number(true, 0, ',', '.');
  //     $(".num-comma").number(true, 0, ',', ',');
  
  //     $('[data-toggle="tooltip"]').tooltip();
  // });