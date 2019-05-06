package com.softwarepractice.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeleteInterface {
    Integer deleteWorkerById(Integer id);
    Integer deleteAccommendationById(Integer id);
    Integer deletePushById(Integer id);
}
