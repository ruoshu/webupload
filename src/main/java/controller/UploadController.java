package controller;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class UploadController{

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void upload(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException { 
        DiskFileItemFactory factory = new DiskFileItemFactory();  
        ServletFileUpload sfu = new ServletFileUpload(factory);  
        sfu.setHeaderEncoding("utf-8");  
          
        String savePath = request.getContextPath();
        String folad = "uploads";  
        savePath = savePath + "\\"+folad+"\\";  
          
        String fileMd5 = null;  
        String chunk = null;  
          
        try {  
            List<FileItem> items = sfu.parseRequest(request);  
              
            for(FileItem item:items){  
                if(item.isFormField()){  
                    String fieldName = item.getFieldName();  
                    if(fieldName.equals("fileMd5")){  
                        fileMd5 = item.getString("utf-8");  
                    }  
                    if(fieldName.equals("chunk")){  
                        chunk = item.getString("utf-8");  
                    }  
                }else{  
                    File file = new File(savePath+"/"+fileMd5);  
                    if(!file.exists()){  
                        file.mkdir();  
                    }  
                    File chunkFile = new File(savePath+"/"+fileMd5+"/"+chunk);  
                    FileUtils.copyInputStreamToFile(item.getInputStream(), chunkFile);  
                      
                }  
            }  
              
        } catch (FileUploadException e) {  
            e.printStackTrace();  
        }
    }
    
    
    public void combine(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException { 
        String savePath = request.getContextPath();
        String folad = "uploads";  
        savePath = savePath + "\\"+folad+"\\";  
          
        String action = request.getParameter("action");  
          
        if(action.equals("mergeChunks")){  
            //合并文件  
            //需要合并的文件的目录标记  
            String fileMd5 = request.getParameter("fileMd5");  
              
            //读取目录里的所有文件  
            File f = new File(savePath+"/"+fileMd5);  
            File[] fileArray = f.listFiles(new FileFilter(){  
                //排除目录只要文件   
                public boolean accept(File pathname) {  
                    // TODO Auto-generated method stub  
                    if(pathname.isDirectory()){  
                        return false;  
                    }  
                    return true;  
                }  
            });  
              
            //转成集合，便于排序  
            List<File> fileList = new ArrayList<File>(Arrays.asList(fileArray));  
            Collections.sort(fileList,new Comparator<File>() {   
                public int compare(File o1, File o2) {  
                    // TODO Auto-generated method stub  
                    if(Integer.parseInt(o1.getName()) < Integer.parseInt(o2.getName())){  
                        return -1;  
                    }  
                    return 1;  
                }  
            });  
            //UUID.randomUUID().toString()-->随机名  
            File outputFile = new File(savePath+"/"+fileMd5+".mp4");  
            //创建文件  
            outputFile.createNewFile();  
            //输出流  
            @SuppressWarnings("resource")
            FileChannel outChnnel = new FileOutputStream(outputFile).getChannel();  
            //合并  
            FileChannel inChannel;  
            for(File file : fileList){  
                inChannel = new FileInputStream(file).getChannel();  
                inChannel.transferTo(0, inChannel.size(), outChnnel);  
                inChannel.close();  
                //删除分片  
                file.delete();  
            }  
            outChnnel.close();  
            //清除文件夹  
            File tempFile = new File(savePath+"/"+fileMd5);  
            if(tempFile.isDirectory() && tempFile.exists()){  
                tempFile.delete();  
            }  
            System.out.println("合并成功");  
        }else if(action.equals("checkChunk")){  
            //检查当前分块是否上传成功  
            String fileMd5 = request.getParameter("fileMd5");  
            String chunk = request.getParameter("chunk");  
            String chunkSize = request.getParameter("chunkSize");  
              
            File checkFile = new File(savePath+"/"+fileMd5+"/"+chunk);  
              
            response.setContentType("text/html;charset=utf-8");  
            //检查文件是否存在，且大小是否一致  
            if(checkFile.exists() && checkFile.length()==Integer.parseInt(chunkSize)){  
                //上传过  
                response.getWriter().write("{\"ifExist\":1}");  
            }else{  
                //没有上传过  
                response.getWriter().write("{\"ifExist\":0}");  
            }  
        }    
   }
}
