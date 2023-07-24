package top.rectorlee.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.enums.BooleanEnum;
import com.alibaba.excel.enums.poi.BorderStyleEnum;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @description: 实体类
 * @author: Lee
 * @date: 2023-07-24 18:38:32
 * @version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ContentRowHeight(value = 30) // 设置行高
@HeadFontStyle(fontName = "Time New Roman", color = 6, italic = BooleanEnum.TRUE) // 设置标题字体
@ContentFontStyle(fontName = "Time New Roman") // 设置单元格内容字体
@ContentStyle(
        horizontalAlignment = HorizontalAlignmentEnum.CENTER,
        borderTop = BorderStyleEnum.THIN,
        borderBottom = BorderStyleEnum.THIN,
        borderLeft = BorderStyleEnum.THIN,
        borderRight = BorderStyleEnum.THIN
) // 内容居中
public class Area {
    @ExcelIgnore // 忽略该字段
    private Long id;

    @ExcelProperty("姓名")
    @ColumnWidth(value = 20)
    private String name;

    @ExcelProperty("电话号码")
    @ColumnWidth(value = 20)
    private Integer phone;

    @ExcelProperty("地址")
    @ColumnWidth(value = 50)
    private String address;

    @ExcelProperty("日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ColumnWidth(value = 20)
    private Date date;

    @ExcelProperty("说明")
    @ColumnWidth(value = 50)
    private String remark;
}
