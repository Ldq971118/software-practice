package com.softwarepractice.dao;

import com.softwarepractice.entity.Admin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SelectInterface {
    Admin SelectAdmin(Integer w_id);
}
