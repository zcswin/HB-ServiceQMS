package com.ww.boengongye;

import com.ww.boengongye.entity.Pictures;
import com.ww.boengongye.mapper.PicturesMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

/**
 * @autor 96901
 * @date 2024/12/25
 */


@Slf4j
@SpringBootTest
public class readpicture {



    @Resource
    private PicturesMapper pictureMapper;













    @Test
    public void testUploadImages() {
        String folderPath = "D:\\pictures";  // 替换为实际的图片文件夹路径

        try {
            List<File> imageFiles = (List<File>) FileUtils.listFiles(new File(folderPath), new String[]{"png", "jpg", "jpeg", "gif"}, false);
            for (File imageFile : imageFiles) {
                byte[] fileData = FileUtils.readFileToByteArray(imageFile);
                String base64Encoded = Base64.getEncoder().encodeToString(fileData);

                Pictures picture = new Pictures();
                picture.setName(imageFile.getName());
                picture.setContext(base64Encoded.getBytes(StandardCharsets.UTF_8));

                pictureMapper.insert(picture);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // @Test
    // public void testQueryImages() {
    //     // 简单示例查询所有图片记录，可根据实际需求调整查询条件
    //     QueryWrapper<Pictures> queryWrapper = new QueryWrapper<>();
    //     List<Pictures> pictures = pictureMapper.selectList(queryWrapper);
    //     for (Pictures picture : pictures) {
    //         System.out.println("图片ID: " + pictures.g() + ", 图片名称: " + picture.getName());
    //     }
    // }



}
