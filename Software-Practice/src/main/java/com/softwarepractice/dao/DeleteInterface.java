package com.softwarepractice.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeleteInterface {
    Integer DeleteWorkerById(Integer id);
    Integer DeleteAccommendation(Integer id);
}
