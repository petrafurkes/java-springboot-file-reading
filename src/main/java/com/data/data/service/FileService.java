package com.data.data.service;

import com.data.data.model.FileData;
import com.data.data.model.FileContent;
import com.data.data.repository.FileContentRepository;
import com.data.data.repository.FileRepository;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class FileService {

    FileRepository fileRepository;
    FileContentRepository fileContentRepository;

    public FileService(FileRepository fileRepository, FileContentRepository fileContentRepository) {
        this.fileRepository = fileRepository;
        this.fileContentRepository = fileContentRepository;
    }

    public void importFile(MultipartFile file, List<FileContent> fileContentList) throws IOException {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String contentType = FilenameUtils.getExtension(file.getOriginalFilename());
        byte[] dataByte = file.getBytes();

        FileData data = new FileData(fileName, contentType, dataByte);

        File newFile = new File("src/main/resources/"+fileName);
        file.transferTo(newFile.toPath());

        String path = newFile.getAbsolutePath();

        FileInputStream fileInputStream = new FileInputStream(new File(path));

        fileRepository.save(data);


        if(contentType.equals("xlsx")){
            readFileXlsx(fileInputStream, fileContentList);
        }else if(contentType.equals("pdf")){
            readFilePdf(fileInputStream, fileContentList);
        }else if(contentType.equals("docx")){
            readFileDocx(fileInputStream, fileContentList);
        }else{
            System.out.println("no file supported");
        }

        newFile.delete();
    }

    private void readFileDocx(FileInputStream fileInputStream, List<FileContent> fileContentList) throws IOException {
        System.out.println("readFileDocx method executed");

        XWPFDocument document = new XWPFDocument(fileInputStream);
        List<XWPFParagraph> paragraphs = document.getParagraphs();

        for (XWPFParagraph para : paragraphs) {
            System.out.println(para.getText());
        }

        fileInputStream.close();
    }

    private void readFilePdf(FileInputStream fileInputStream, List<FileContent> fileContentList) throws IOException {
        System.out.println("readFilePdf method executed");


        fileInputStream.close();
    }

    private void readFileXlsx(FileInputStream fileInputStream, List<FileContent> fileContentList) throws IOException {

            XSSFWorkbook wb=new XSSFWorkbook(fileInputStream);
            Sheet sh = wb.getSheetAt(0);
            Row topRow = sh.getRow(0);
            Cell cell1 = topRow.getCell(0);
            String cellName1 = cell1.getStringCellValue();
            Cell cell2 = topRow.getCell(1);
            String cellName2 = cell2.getStringCellValue();
            Cell cell3 = topRow.getCell(2);
            String cellName3 = cell3.getStringCellValue();
            System.out.println("cell1 "+cellName1+" cell2 "+cellName2+" cell3 "+cellName3);


            Iterator itr=sh.iterator();
            while(itr.hasNext())
            {
                ArrayList<Object> data =new ArrayList();
                Row r=(Row) itr.next();

                Iterator cellitr=r.cellIterator();
                while(cellitr.hasNext())
                {
                    Cell celldata=(Cell) cellitr.next();

                    switch(celldata.getCellType()) {
                        case STRING:
                            data.add(celldata.getStringCellValue());
                            break;
                        case NUMERIC:
                            data.add(celldata.getNumericCellValue());
                            break;
                    }
                }
                FileContent fileContent = new FileContent();
                if(data.get(0).equals(cellName1)){
                    continue;
                }else{
                    fileContent.setName((String)data.get(0));
                }
                if(data.get(1).equals(cellName2)){
                    continue;
                }else{
                    fileContent.setAddress((String)data.get(1));
                }
                if(data.get(2).equals(cellName3)){
                    continue;
                }else{
                    fileContent.setPosition((String)data.get(2));
                }

                fileContentList.add(fileContent);
                fileContentRepository.save(fileContent);
                fileInputStream.close();
            }

    }


}
