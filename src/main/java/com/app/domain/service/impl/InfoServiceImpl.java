package com.app.domain.service.impl;

import com.app.common.exception.BadRequest;
import com.app.common.exception.Conflict;
import com.app.common.exception.InternalServerError;
import com.app.domain.model.InfoRequest;
import com.app.domain.model.InfoResponse;
import com.app.domain.service.InfoService;
import com.app.infrastrucuture.entity.Info;
import com.app.infrastrucuture.persistent.InfoPersistentRepository;
import java.util.ArrayList;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InfoServiceImpl implements InfoService {

    private final InfoPersistentRepository infoRepository;

    public InfoServiceImpl(InfoPersistentRepository infoRepository) {
        this.infoRepository = infoRepository;
    }

    @Override
    public List<InfoResponse> getInfos() {
        List<Info> infoEntities;
        try {
            infoEntities = this.infoRepository.findAll();
        } catch (Exception ex) {
            throw new InternalServerError(ex);
        }

        List<InfoResponse> res = new ArrayList<>();
        for (Info i : infoEntities) {
            res.add(new InfoResponse(i.getId(), i.getTitle(), i.getDescription()));
        }
        return res;
    }

    @Override
    public InfoResponse getInfo(String id) {
        Info info;
        try {
            info = this.infoRepository.findById(id);
        } catch (Exception ex) {
            throw new InternalServerError(ex);
        }
        return new InfoResponse(info.getId(), info.getTitle(), info.getDescription());
    }

    @Override
    public InfoResponse updateInfo(String id, InfoRequest req) {
        Info info;
        try {
            info = this.infoRepository.findById(id);
        } catch (Exception ex) {
            throw new BadRequest();
        }

        info = req.convert(info.getId());
        try {
            info = this.infoRepository.insert(info);
        } catch (Exception ex) {
            throw new InternalServerError(ex);
        }

        return new InfoResponse(info.getId(),info.getTitle(), info.getDescription());
    }

    @Override
    public InfoResponse generateInfo(InfoRequest request) {
        Info info;
        try {
            info = this.infoRepository.insert(request.convert(this.infoRepository.newId()));
        } catch (Conflict ex) {
            throw new Conflict(ex);
        } catch (Exception ex) {
            throw new InternalServerError(ex);
        }
        return new InfoResponse(info.getId(), info.getTitle(), info.getDescription());
    }

    @Override
    public void deleteInfo(String id) {
        try {
            this.infoRepository.removeById(id);
        } catch (Exception ex) {
            throw new InternalServerError(ex);
        }
    }
}
