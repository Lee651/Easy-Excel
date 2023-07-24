package top.rectorlee.mapper;

import org.springframework.stereotype.Repository;
import top.rectorlee.entity.Area;

import java.util.List;

@Repository
public interface EasyExcelMapper {
    Integer selectCount();

    List<Area> selectByPageSizeAndPageIndex(Integer pageIndex, Integer pageSize);

    void save(List<Area> list);
}
