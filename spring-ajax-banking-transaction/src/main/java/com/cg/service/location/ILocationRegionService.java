package com.cg.service.location;

import com.cg.model.LocationRegion;
import com.cg.service.IGeneralService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


public interface ILocationRegionService extends IGeneralService<LocationRegion, Long> {
}
