package com.cg.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class VideoCreReqDTO {

    private String title;

    private String description;

    private Long[] playlist;

    private String url;

    public Video toVideo(String playlist){
        return new Video()
                .setId(null)
                .setTitle(title)
                .setDescription(description)
                .setPlaylist(playlist)
                .setUrl(url);
    }
}
