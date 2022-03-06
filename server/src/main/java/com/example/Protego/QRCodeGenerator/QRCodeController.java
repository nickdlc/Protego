package com.example.Protego.QRCodeGenerator;

import com.google.zxing.WriterException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
public class QRCodeController {
    @GetMapping(path = "/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public BufferedImage getQRCode(@RequestParam("patient") String puid) {
        try {
            QRCodeGenerator qrCodeGenerator = new QRCodeGenerator();

            // will likely set a default width and height based on the QR code screen
            return qrCodeGenerator.generateQRCode(puid, 250, 250);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
