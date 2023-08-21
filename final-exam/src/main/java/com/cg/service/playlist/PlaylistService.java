package com.cg.service.playlist;

import com.cg.model.Playlist;
import com.cg.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlaylistService implements IPlaylistService{

    @Autowired
    private PlaylistRepository playlistRepository;

    @Override
    public List<Playlist> findAll() {
        return null;
    }

    @Override
    public Optional<Playlist> findById(Long id) {
        return playlistRepository.findById(id);
    }

    @Override
    public Playlist save(Playlist playlist) {
        return null;
    }

    @Override
    public void delete(Playlist playlist) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
