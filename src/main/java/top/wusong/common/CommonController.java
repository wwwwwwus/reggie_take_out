package top.wusong.common;

import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${upload.filePath}")
    private String filePath;

    /**
     * 图片上传
     *
     * @param file 图片文件
     * @return Result<String> 上传图片的新名称
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        //获取图片名称
        String originalFilename = file.getOriginalFilename();
        //截取后缀，比如xx.jpg，需要的是.jpg
        String fixName = originalFilename.substring(originalFilename.lastIndexOf("."));
        //为了保证图片的名称不重复，需要使用其他的算法重新命名
        String imgName = UUID.randomUUID().toString() + fixName;
        //读取文件夹
        File fileDir = new File(filePath);
        //判断当前文件夹是否存在
        if (!fileDir.exists()) {
            //如果不存在就创建一个
            fileDir.mkdir();
        }
        //保存图片，路径加上新的名称
        try {
            file.transferTo(new File(filePath + imgName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.success(imgName);
    }

    /**
     * 上传图片后的回显
     * @param name 上传图片后的新名称
     * @param response 需要回显到页面上
     */
    @GetMapping("/download")
    public void load(String name, HttpServletResponse response) {
        //设置响应类型
        response.setContentType("image/jpeg");
        try {
            //读取图片
            FileInputStream fileInputStream = new FileInputStream(new File(filePath + name));
            //获取输出流
            ServletOutputStream outputStream = response.getOutputStream();
            //设置下标
            int len = 0;
            //设置没有读取的字节数组
            byte[] bytes = new byte[1024];
            //开始读取
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            //释放资源
            fileInputStream.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
