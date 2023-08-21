class App {
    static DOMAIN_SERVER = window.origin;
    static API_SERVER = this.DOMAIN_SERVER + '/api';

    static API_USER = this.API_SERVER + '/auth';
    static API_CUSTOMER = this.API_SERVER + '/customers';
    static API_DOCTOR = this.API_SERVER + '/doctors';
    static API_APPOINTMENT = this.API_SERVER + '/appointments';
    static API_MEDICAL_BILL = this.API_SERVER + '/medical-bills';
    static API_ROOM = this.API_SERVER + '/rooms';
    static API_SPECIALITY = this.API_SERVER + '/specialities';

    static API_LOCATION_REGION = 'https://vapi.vnappmob.com/api/province'
    static API_ROM = this.API_SERVER + '/rooms';
    static API_SPECIALITY = this.API_SERVER + '/specialities';

    static API_LOCATION_REGION = 'https://vapi.vnappmob.com/api/province'
    static API_JOB = "https://api-119.medpro.com.vn:5000/profession-mongo/get-all-by-partner"
    static API_ETHNIC = "https://api-119.medpro.com.vn:5000/nation-mongo/get-all-by-partner"


    static formatDate = (date) => {
        let d = new Date(date),
            month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear();

        if (month.length < 2)
            month = '0' + month;
        if (day.length < 2)
            day = '0' + day;

        return [day, month, year].join('/');
    }

    static formatCurrency = (cost) => {
        return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(cost);
    }

    static showDeleteConfirmDialog() {
        return Swal.fire({
            icon: 'warning',
            text: 'Are you sure you want to delete the selected data ?',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it !',
            cancelButtonText: 'Cancel',
        });
    }
    static showSuccessNotification(t){
        const Toast = Swal.mixin({
            toast: true,
            position: 'top-end',
            showConfirmButton: false,
            timer: 1500,
            timerProgressBar: true,
            didOpen: (toast) => {
                toast.addEventListener('mouseenter', Swal.stopTimer)
                toast.addEventListener('mouseleave', Swal.resumeTimer)
            }
        })

        Toast.fire({
            icon: 'success',
            title: t
        })
    }
    static showSuccessAlert(t) {
        Swal.fire({
            position: 'top-end',
            icon: 'success',
            title: t,
            showConfirmButton: false,
            timer: 1500
        })
    }

    static showErrorAlert(t) {
        Swal.fire({
            icon: 'error',
            title: 'Warning',
            text: t,
        });
    }

    static showConfirmCheckout() {
        return Swal.fire({
            icon: 'warning',
            text: 'Bạn muốn thanh toán tất cả phiếu khám ?',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Có, thanh toán !',
            cancelButtonText: 'Thôi',
        });
    }

    static showErrorNotification(t){
        const Toast = Swal.mixin({
            toast: true,
            position: 'top-end',
            showConfirmButton: false,
            timer: 1500,
            timerProgressBar: true,
            didOpen: (toast) => {
                toast.addEventListener('mouseenter', Swal.stopTimer)
                toast.addEventListener('mouseleave', Swal.resumeTimer)
            }
        })

        Toast.fire({
            icon: 'error',
            title: t
        })
    }
}

class LocationRegion {
    constructor(id, provinceId, provinceName, districtId, districtName, wardId, wardName, address) {
        this.id = id;
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        this.districtId = districtId;
        this.districtName = districtName;
        this.wardId = wardId;
        this.wardName = wardName;
        this.address = address;
    }
}


class Doctor {
    constructor(id, fullName, email, nameGender, phone, birthDay, identityNumber, ethnic, locationRegion, userId, specialityId, levelName) {
        this.id = id;

        this.fullName = fullName;

        this.email = email;

        this.nameGender = nameGender;

        this.phone = phone;

        this.birthDay = birthDay;

        this.identityNumber = identityNumber;

        this.ethnic = ethnic;

        this.locationRegion = locationRegion;

        this.userId = userId;

        this.specialityId = specialityId;

        this.levelName = levelName;
    }

}

class Speciality {
    constructor(id, codeName, name) {
        this.id = id;
        this.codeName = codeName;
        this.name = name;
    }
}

class Room {
    constructor(id, specialityName, name, isAvailable) {
        this.id = id;
        this.specialityName = specialityName;
        this.name = name;
        this.isAvailable = isAvailable;
    }
}

class User {
    constructor(id,username,password,roleName) {
        this.id=id;
        this.username=username;
        this.password=password;
        this.roleName=roleName;


    }
}