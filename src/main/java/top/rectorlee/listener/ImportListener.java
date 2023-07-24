package top.rectorlee.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import top.rectorlee.service.EasyExcelService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description: 导入监听类
 * @author: Lee
 * @date: 2023-07-24 20:23:57
 * @version: 1.0
 */
public class ImportListener extends AnalysisEventListener<Map<Integer, String>> {
    private static final Integer PAGE_SIZE = 10000;

    /**
     * 处理业务逻辑的Service,也可以是Mapper
     */
    private EasyExcelService easyExcelService;

    /**
     * 用于存储读取的数据
     */
    private List<Map<Integer, String>> dataList = new ArrayList<Map<Integer, String>>();

    public ImportListener() {
    }

    public ImportListener(EasyExcelService easyExcelService) {
        this.easyExcelService = easyExcelService;
    }

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        // 数据add进入集合
        dataList.add(data);
        // size是否为100000条:这里其实就是分批.当数据等于10w的时候执行一次插入
        if (dataList.size() >= PAGE_SIZE) {
            // 存入数据库:数据小于1w条使用Mybatis的批量插入即可;
            save();
            // 清理集合便于GC回收
            dataList.clear();
        }
    }

    /**
     * Excel中所有数据解析完毕会调用此方法
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        save();
        dataList.clear();
    }

    /**
     * 保存数据到DB
     */
    private void save() {
        easyExcelService.importExcel(dataList);
        dataList.clear();
    }
}
