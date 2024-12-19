package org.soulasphyxia.webcourseproject.user_actions.writer;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.soulasphyxia.webcourseproject.entity.UserAction;
import org.soulasphyxia.webcourseproject.user_actions.LogType;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class XlsxUserActionsWriter implements UserActionsWriter {

    @Override
    public InputStream write(List<UserAction> userActions) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("log");
            sheet.setColumnWidth(0, 6000);
            sheet.setColumnWidth(1, 4000);
            int rowIndex = 0;
            XSSFRow header = sheet.createRow(rowIndex++);
            Cell idCellStart = header.createCell(0);
            idCellStart.setCellValue("ID");
            Cell dateCellStart = header.createCell(1);
            dateCellStart.setCellValue("Дата");
            Cell actionCellStart = header.createCell(2);
            Cell userIpCellStart = header.createCell(3);
            userIpCellStart.setCellValue("Ip пользователя");
            actionCellStart.setCellValue("Действие");
            for (UserAction userAction : userActions) {
                XSSFRow row = sheet.createRow(rowIndex++);
                Cell idCell = row.createCell(0);
                idCell.setCellValue(userAction.getId());
                Cell dateCell = row.createCell(1);
                dateCell.setCellValue(userAction.getDateTime().toString());
                Cell actionCell = row.createCell(2);
                actionCell.setCellValue(userAction.getAction());
                Cell ipCell = row.createCell(3);
                ipCell.setCellValue(userAction.getUserIp());

            }
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            workbook.write(os);
            return new ByteArrayInputStream(os.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public LogType getLogType() {
        return LogType.XLSX;
    }
}
