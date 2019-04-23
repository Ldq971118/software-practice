package com.softwarepractice.dao;

import com.softwarepractice.entity.Worker;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InsertInterface {
    Integer InsertWorker(Worker worker);
}
