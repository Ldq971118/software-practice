package com.softwarepractice.dao;

import com.softwarepractice.entity.Admin;
import com.softwarepractice.entity.Worker;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface SelectInterface {
    Admin SelectAdmin(Integer w_id);
    List<Worker> FindWorkerAll();
}
