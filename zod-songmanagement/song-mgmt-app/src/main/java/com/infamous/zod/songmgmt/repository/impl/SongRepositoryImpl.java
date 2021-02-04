package com.infamous.zod.songmgmt.repository.impl;

import com.infamous.framework.logging.ZodLogger;
import com.infamous.framework.logging.ZodLoggerUtil;
import com.infamous.zod.base.rest.entity.UploadResult;
import com.infamous.zod.songmgmt.converter.SongConverter;
import com.infamous.zod.songmgmt.model.Song;
import com.infamous.zod.songmgmt.model.SongVO;
import com.infamous.zod.songmgmt.repository.SongDAO;
import com.infamous.zod.songmgmt.repository.SongRepository;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class SongRepositoryImpl implements SongRepository {

    private static final ZodLogger LOGGER = ZodLoggerUtil.getLogger(SongRepositoryImpl.class, "song.mgmt");

    private SongDAO m_songDAO;
    private SongConverter m_songConverter;

    @Autowired
    public SongRepositoryImpl(SongDAO songDAO, SongConverter songConverter) {
        this.m_songDAO = songDAO;
        this.m_songConverter = songConverter;
    }

    @Override
    public boolean upload(SongVO song) {
        try {
            Long dbId = saveToDB(song).orElseThrow(() -> new RuntimeException("DB Id isn't present"));
            song.setId(dbId);
            return true;
        } catch (Exception e) {
            LOGGER.error("Error while saving Song [" + song + "]", e);
            return false;
        }
    }


    private Optional<Long> saveToDB(SongVO song) {
        Song songEntity = m_songConverter.toEntity(song);
        boolean res = m_songDAO.persist(songEntity);

        return res ? Optional.of(songEntity.getId()) : Optional.empty();
    }

    @Override
    public UploadResult<SongVO> upload(Collection<SongVO> songs) {
        UploadResult<SongVO> res = new UploadResult<>();
        List<SongVO> data = new LinkedList<>();

        songs.forEach(s -> {
            try {
                if (upload(s)) {
                    data.add(s);
                }
            } catch (Exception e) {
                LOGGER.error("Error while uploading song [" + s.getTitle() + "]", e);
            }
        });
        res.setData(data);
        if (data.size() == songs.size()) {
            res.setStatus("success");
        } else {
            res.setStatus("failure");
        }
        return res;
    }

    @Override
    public boolean update(SongVO song) {
        return m_songDAO.merge(m_songConverter.toEntity(song));
    }

    @Override
    public void update(Collection<SongVO> songs) {
        songs.forEach(this::upload);
    }

    @Override
    public List<SongVO> list() {
        return m_songConverter.toDTO(m_songDAO.findAll().parallelStream());
    }
}
