package top.rectorlee.service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Lee
 * @date: 2023-07-24 18:40:14
 * @version: 1.0
 */
public interface EasyExcelService {
    /**
     * 多线程导入
     * @param response
     */
    void exportExcel(HttpServletResponse response) throws Exception;

    /**
     * 多线程导出
     * @param dataList
     */
    Map<String, Object> importExcel(List<Map<Integer, String>> dataList);
}
