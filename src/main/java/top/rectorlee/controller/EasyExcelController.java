package top.rectorlee.controller;

import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.rectorlee.listener.ImportListener;
import top.rectorlee.service.EasyExcelService;

import javax.servlet.http.HttpServletResponse;

/**
 * @description: 接口层
 * @author: Lee
 * @date: 2023-07-24 18:37:50
 * @version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class EasyExcelController {
    @Autowired
    private EasyExcelService easyExportService;

    @RequestMapping("/export")
    public void exportExcel(HttpServletResponse response) throws Exception {
        easyExportService.exportExcel(response);
    }

    @RequestMapping("/import")
    @ResponseBody
    public String importExcel(MultipartFile file) throws Exception {
        log.info("多线程导入开始");
        EasyExcel.read(file.getInputStream(), new ImportListener(easyExportService)).doReadAll();
        log.info("多线程导入结束");

        return "success";
    }
}
