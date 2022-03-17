package com.example.Protego.QRCodeGenerator;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class QRCodeGenerator {
    /**
     * @param text patient id from Firestore
     * @return BufferedImage of QR code
     */
    public BufferedImage generateQRCode(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bm = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        return MatrixToImageWriter.toBufferedImage(bm);
    }
}
