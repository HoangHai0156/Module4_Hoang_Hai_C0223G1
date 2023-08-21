package com.cg.service.video;

import com.cg.model.Video;
import com.cg.service.IGeneralService;

import java.util.List;

public interface IVideoService extends IGeneralService<Video, Long> {
    List<Video> findAllByDeletedFalse();

}
