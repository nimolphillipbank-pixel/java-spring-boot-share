package com.app.controllers.v1;

import com.app.domain.model.InfoRequest;
import com.app.domain.model.InfoResponse;
import com.app.domain.service.impl.InfoServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/infos")
@Tag(name = "Infos", description = "Info CRUD endpoints")
public class RdbmsController {

    private final InfoServiceImpl infoService;

    public RdbmsController(InfoServiceImpl infoService) {
        this.infoService = infoService;
    }

    @GetMapping()
    @Operation(summary = "List infos")
    public List<InfoResponse> getInfos() {
        return this.infoService.getInfos();
    }

    @GetMapping(value="/{id}")
    @Operation(summary = "Get info by id")
    public InfoResponse getInfo(@PathVariable("id") String id) {
        return this.infoService.getInfo(id);
    }

    @PostMapping
    @Operation(summary = "Create info")
    public ResponseEntity<InfoResponse> postInfo(
        @RequestBody InfoRequest req)
        throws URISyntaxException {

        InfoResponse res = this.infoService.generateInfo(req);
        return ResponseEntity
            .created(new URI("/api/v1/infos/" + res.id()))
            .body(res);
    }

    @PutMapping(value="/{id}")
    @Operation(summary = "Update info")
    public InfoResponse putInfo(
        @PathVariable("id") String id,
        @RequestBody InfoRequest req) {

        return this.infoService.updateInfo(id, req);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete info")
    public Map<String, String> deleteInfo(@PathVariable("id") String id) {
        this.infoService.deleteInfo(id);
        return Collections.singletonMap("message", "ok");
    }
}
