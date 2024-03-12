package ru.wwerlosh.vktestcase.handlers.albums;

import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.wwerlosh.vktestcase.handlers.validation.markers.PatchValidationGroup;
import ru.wwerlosh.vktestcase.handlers.validation.markers.PostValidationGroup;
import ru.wwerlosh.vktestcase.handlers.validation.markers.PutValidationGroup;

@RestController
@RequestMapping("/api")
public class AlbumController {

    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping("/albums")
    public List<AlbumDTO> getAllAlbums() {
        return albumService.getAllAlbums();
    }

    @GetMapping("/albums/{id}")
    public AlbumDTO getAlbumById(@PathVariable Long id) {
        return albumService.getAlbumById(id);
    }

    @PostMapping("/albums")
    public AlbumDTO addNewAlbum(@Validated(PostValidationGroup.class) @RequestBody AlbumDTO albumDTO) {
        return albumService.save(albumDTO);
    }

    @DeleteMapping("/albums/{id}")
    public Long deleteAlbumById(@PathVariable Long id) {
        return albumService.deleteAlbumById(id);
    }

    @PutMapping("/albums/{id}")
    public AlbumDTO updateAlbumById(@PathVariable Long id,
                                  @Validated(PutValidationGroup.class) @RequestBody AlbumDTO albumDTO) {
        return albumService.updateAlbumById(id, albumDTO);
    }

    @PatchMapping("/albums/{id}")
    public AlbumDTO updateAlbumFieldsById(@PathVariable Long id,
                                        @Validated(PatchValidationGroup.class) @RequestBody AlbumDTO albumDTO) {
        return albumService.updateAlbumFieldsById(id, albumDTO);
    }
}
