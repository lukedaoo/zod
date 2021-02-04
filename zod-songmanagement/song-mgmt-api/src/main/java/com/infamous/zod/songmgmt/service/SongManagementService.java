package com.infamous.zod.songmgmt.service;

import com.infamous.zod.base.rest.entity.UploadResult;
import com.infamous.zod.songmgmt.model.SongVO;
import com.infamous.zod.storage.model.StorageFileVO;
import java.util.List;

public interface SongManagementService {

    SongVO upload(SongVO songVO, StorageFileVO file);

    UploadResult<SongVO> multipleUpload(List<SongVO> songs, List<StorageFileVO> files);

    List<SongVO> findAll();
}
