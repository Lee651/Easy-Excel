package top.rectorlee.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.rectorlee.entity.Area;
import top.rectorlee.mapper.EasyExcelMapper;
import top.rectorlee.service.EasyExcelService;
import top.rectorlee.utils.JDBCDruidUtils;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description: 业务层
 * @author: Lee
 * @date: 2023-07-24 18:40:14
 * @version: 1.0
 */
@Slf4j
@Service
public class EasyExcelServiceImpl implements EasyExcelService {
    private static final Integer PAGE_SIZE = 10000;

    @Autowired
    private EasyExcelMapper easyExcelMapper;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    private DataSource dataSource;

    @Override
    public void exportExcel(HttpServletResponse response) throws Exception {
        log.info("多线程导出开始");

        OutputStream outputStream = response.getOutputStream();
        // 设置响应内容
        response.setContentType("application/vnd.ms-excel");
        // 防止下载的文件名字乱码
        response.setCharacterEncoding("UTF-8");
        try {
            // 文件以附件形式下载
            String fileName = URLEncoder.encode("Area", "utf-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 获取总数据量
            int count = easyExcelMapper.selectCount();
            // 必须放到循环外，否则会刷新流
            ExcelWriter excelWriter = EasyExcel.write(outputStream).build();

            // 1.单线程按照页号顺序写入sheet页
            /*for (int i = 0; i < (count / PAGE_SIZE); i++) {
                List<Area> exportList = easyExcelMapper.selectByPageSizeAndPageIndex(i * PAGE_SIZE, PAGE_SIZE);
                WriteSheet sheet = EasyExcel.writerSheet(i + 1, "第" + (i + 1) + "页").head(Area.class).build();
                excelWriter.write(exportList, sheet);
            }*/

            // 2.多线程不能按照页号顺序写入sheet页
            List<CompletableFuture> completableFutures = new ArrayList<>();
            for (int i = 0; i < (count / PAGE_SIZE) + 1; i++) {
                int pageIndex = i;
                CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
                    List<Area> exportList = easyExcelMapper.selectByPageSizeAndPageIndex(pageIndex * PAGE_SIZE, PAGE_SIZE);
                    if (!CollectionUtils.isEmpty(exportList)) {
                        WriteSheet writeSheet = EasyExcel.writerSheet(pageIndex, "第" + (pageIndex + 1) + "页").head(Area.class).build();
                        synchronized(excelWriter) {
                            excelWriter.write(exportList, writeSheet);
                        }
                    }
                }, threadPoolExecutor);
                completableFutures.add(completableFuture);
            }
            for (CompletableFuture completableFuture : completableFutures) {
                completableFuture.join();
            }
            // 刷新流
            excelWriter.finish();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        outputStream.flush();
        response.getOutputStream().close();

        log.info("多线程导出结束");
    }

    @Override
    public Map<String, Object> importExcel(List<Map<Integer, String>> dataList) {
        HashMap<String, Object> result = new HashMap<>();
        // 结果集中数据为0时,结束方法.进行下一次调用
        if (dataList.size() == 0) {
            result.put("empty", "0000");
            return result;
        }
        // JDBC分批插入+事务操作完成对10w数据的插入
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = dataSource.getConnection();
            // 控制事务:默认不提交
            conn.setAutoCommit(false);
            String sql = "insert into area (name, phone, address, date, remark) values (?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            // 循环结果集:这里循环不支持"烂布袋"表达式
            for (Map<Integer, String> item : dataList) {
                ps.setString(1, item.get(0));
                ps.setString(2, item.get(1));
                ps.setString(3, item.get(2));
                ps.setString(4, item.get(3));
                ps.setString(5, item.get(4));
                /*ps.setString(6, item.get(5));*/

                // 将一组参数添加到此PreparedStatement对象的批处理命令中
                ps.addBatch();
            }
            // 执行批处理
            ps.executeBatch();
            // 手动提交事务
            conn.commit();
            result.put("success", "1111");
        } catch (Exception e) {
            result.put("exception", "0000");
            e.printStackTrace();
        } finally {
            // 关连接
            JDBCDruidUtils.close(conn, ps);
        }
        return result;
    }
}
