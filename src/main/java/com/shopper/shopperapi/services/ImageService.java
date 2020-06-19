package com.shopper.shopperapi.services;

import com.shopper.shopperapi.models.Image;
import com.shopper.shopperapi.repositories.ImageRepository;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    public Image getImageById(String id) throws UnsupportedEncodingException {
        Image image = imageRepository.findById(id).get();
        byte[] decodedImage = Base64.getDecoder().decode(image.getImage().getBytes("UTF-8"));
        String decompressedImage = Base64.getEncoder().encodeToString(decompressBytes(decodedImage));
        image.setImage(decompressedImage);
        return image;
    }

    public Image addImage(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setId(ObjectId.get());
        System.out.println("BEFORE COMPRESSING: " + file.getBytes().length);
//        Binary imageBinData = new Binary(BsonBinarySubType.BINARY, file.getBytes());
        byte[] compressedImage = compressBytes(file.getBytes());
        image.setImage(Base64.getEncoder().encodeToString(compressedImage));
        image = imageRepository.save(image);
        return image;
//        return image.getId();
    }

    // compress the image bytes before storing it in the database
    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }
        System.out.println("COMPRESSED Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    // uncompress the image bytes before returning it to the angular application
    public static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        System.out.println("BEFORE DECOMPRESSING: " + data.length);
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException ioe) {
        } catch (DataFormatException e) {
        }
        return outputStream.toByteArray();
    }
}
