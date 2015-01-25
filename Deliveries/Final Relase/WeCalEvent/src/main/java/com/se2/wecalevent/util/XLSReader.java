/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.util;

import com.se2.wecalevent.entities.Event;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 *
 * @author Mert
 */
public class XLSReader {

    /**
     * column list of the input file
     */
    private static final String[] columnList = new String[]{"Name", "Description", "City",
        "Privacy","Type","Desired weather", "Starting Date", "Ending Date"};

    private static Date covertDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        try {
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+1"));
            return sdf.parse(date);
        } catch (ParseException ex) {
            Logger.getLogger(XLSReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static List<Event> readXLS(InputStream fis) {
        List<Event> events = new ArrayList<Event>();
        try {
            POIFSFileSystem fs = new POIFSFileSystem(fis);
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);
            HSSFRow row;
            HSSFCell cell;

            int rows; // No of rows
            rows = sheet.getPhysicalNumberOfRows();

            int cols = 0; // No of columns
            int tmp = 0;
            for (int r = 0; r < rows; r++) {
                row = sheet.getRow(r);
                if (row != null) {
                    //if it is the first row
                    cols = row.getPhysicalNumberOfCells();
                    if (r == 0) {
                        for (int c = 0; c < cols; c++) {
                            cell = row.getCell((short) c);
                            //check input file format
                            if (!columnList[c].equals(cell.getStringCellValue())) {
                                return null;
                            }
                        }
                    } else {
                        Event newEvent = new Event();
                        //Event Name
                        cell = row.getCell(0);
                        newEvent.setEventName(cell.getStringCellValue());
                        //Event Description
                        cell = row.getCell(1);
                        newEvent.setEventDescription(cell.getStringCellValue());
                        //Event Location
                        cell = row.getCell(2);
                        newEvent.setLocationCity(cell.getStringCellValue());
                        //Event Privacy
                        cell = row.getCell(3);
                        newEvent.setVisibility(cell.getStringCellValue());
                        //Event Type
                        cell = row.getCell(4);
                        newEvent.setEventType(cell.getStringCellValue());
                        //Event Privacy
                        cell = row.getCell(5);
                        newEvent.setDesiredWeather(cell.getStringCellValue());
                        //Event Starting date
                        cell = row.getCell(6);
                        newEvent.setStartingDate(covertDate(cell.getStringCellValue()));
                        //Event Ending date
                        cell = row.getCell(7);
                        newEvent.setEndingDate(covertDate(cell.getStringCellValue()));
                        events.add(newEvent);
                    }
                }
            }
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }

        return events;
    }

}
