package com.zdnf.bbs.dao;

import java.util.List;
import com.zdnf.bbs.domain.Plate;
import org.apache.ibatis.annotations.Param;

/**
 * Created by ZDNF on 2017/5/12.
 */
public interface PlateDao {
    public List<Plate> get_all();
    public boolean add(@Param("Plate") Plate Plate);
    public boolean delete(String id);
    public boolean set_name(@Param("id")int id,@Param("name")String name);
    public String namebyid(@Param("id")int id);
}
