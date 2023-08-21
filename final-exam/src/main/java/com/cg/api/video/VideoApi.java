package com.cg.api.video;

import com.cg.model.Playlist;
import com.cg.model.Video;
import com.cg.model.VideoCreReqDTO;
import com.cg.service.playlist.IPlaylistService;
import com.cg.service.video.IVideoService;
import com.cg.utils.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/videos")
public class VideoApi {

    @Autowired
    private IVideoService videoService;

    @Autowired
    private IPlaylistService playlistService;

    @GetMapping
    public ResponseEntity<?> getAll(){
        List<Video> videos = videoService.findAllByDeletedFalse();

        return new ResponseEntity<>(videos,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody VideoCreReqDTO videoCreReqDTO){
        Long[] playlistIds = videoCreReqDTO.getPlaylist();
        List<String> playlists = new ArrayList<>();
        for (Long playlistId : playlistIds){
            Optional<Playlist> playlistOptional = playlistService.findById(playlistId);
            if (playlistOptional.isEmpty()){
                Map<String, String> data = new HashMap<>();
                data.put("message", "Không thể tạo phiếu khám cho lịch khám trong hoặc trước ngày hôm nay");
                return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
            }
            String playlist = playlistOptional.get().getName();
            playlists.add(playlist);
        }

        String newPlaylist = playlists.stream().map(String::valueOf).collect(Collectors.joining(", "));
        Video video = videoCreReqDTO.toVideo(newPlaylist);
        Video newVideo = videoService.save(video);

        return new ResponseEntity<>(newVideo,HttpStatus.CREATED);
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity<?> delete(@PathVariable("videoId") String videoIdStr ){

        if (!ValidateUtil.isNumberValid(videoIdStr)){
            Map<String, String> data = new HashMap<>();
            data.put("message", "ID video không đúng định dạng");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        Long videoId = Long.parseLong(videoIdStr);
        Optional<Video> videoOptional = videoService.findById(videoId);
        if (videoOptional.isEmpty()){
            Map<String, String> data = new HashMap<>();
            data.put("message", "Video không tồn tại");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }
        Video video = videoOptional.get();
        video.setId(videoId);
        video.setDeleted(true);
        videoService.save(video);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{videoId}")
    public ResponseEntity<?> update(@PathVariable("videoId") String videoIdStr,
                                    @RequestBody VideoCreReqDTO videoCreReqDTO){

        if (!ValidateUtil.isNumberValid(videoIdStr)){
            Map<String, String> data = new HashMap<>();
            data.put("message", "ID video không đúng định dạng");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        Long videoId = Long.parseLong(videoIdStr);
        Optional<Video> videoOptional = videoService.findById(videoId);
        if (videoOptional.isEmpty()){
            Map<String, String> data = new HashMap<>();
            data.put("message", "Video không tồn tại");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        Long[] playlistIds = videoCreReqDTO.getPlaylist();
        List<String> playlists = new ArrayList<>();
        for (Long playlistId : playlistIds){
            Optional<Playlist> playlistOptional = playlistService.findById(playlistId);
            if (playlistOptional.isEmpty()){
                Map<String, String> data = new HashMap<>();
                data.put("message", "Không thể tạo phiếu khám cho lịch khám trong hoặc trước ngày hôm nay");
                return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
            }
            String playlist = playlistOptional.get().getName();
            playlists.add(playlist);
        }

        String newPlaylist = playlists.stream().map(String::valueOf).collect(Collectors.joining(","));
        Video video = videoCreReqDTO.toVideo(newPlaylist);
        video.setId(videoId);
        videoService.save(video);

        return new ResponseEntity<>(video,HttpStatus.OK);
    }

    @GetMapping("/{videoId}")
    public ResponseEntity<?> getVideo(@PathVariable ("videoId") String videoIdStr){
        if (!ValidateUtil.isNumberValid(videoIdStr)){
            Map<String, String> data = new HashMap<>();
            data.put("message", "ID video không đúng định dạng");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        Long videoId = Long.parseLong(videoIdStr);
        Optional<Video> videoOptional = videoService.findById(videoId);
        if (videoOptional.isEmpty()){
            Map<String, String> data = new HashMap<>();
            data.put("message", "Video không tồn tại");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }
        Video video = videoOptional.get();

        return new ResponseEntity<>(video,HttpStatus.OK);
    }
}
