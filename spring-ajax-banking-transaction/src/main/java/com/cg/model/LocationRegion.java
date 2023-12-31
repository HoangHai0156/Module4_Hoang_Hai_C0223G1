package com.cg.model;

import com.cg.model.dto.location.LocationRegionCreResDTO;
import com.cg.model.dto.location.LocationRegionResDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "location_region")
@Accessors(chain = true)
@Entity
public class LocationRegion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "province_id")
    private String provinceId;

    @Column(name = "province_name")
    private String provinceName;

    @Column(name = "district_id")
    private String districtId;

    @Column(name = "district_name")
    private String districtName;

    @Column(name = "ward_id")
    private String wardId;

    @Column(name = "ward_name")
    private String wardName;

    private String address;

    public LocationRegionResDTO toLocationRegionResDTO(){
        return new LocationRegionResDTO()
                .setId(id)
                .setProvinceId(provinceId)
                .setProvinceName(provinceName)
                .setDistrictId(districtId)
                .setDistrictName(districtName)
                .setWardId(wardId)
                .setWardName(wardName)
                .setAddress(address);
    }

    public LocationRegionCreResDTO toLocationRegionCreResDTO(){
        return new LocationRegionCreResDTO()
                .setProvinceName(provinceName)
                .setDistrictName(districtName)
                .setWardName(wardName)
                .setAddress(address)
                ;
    }

}
