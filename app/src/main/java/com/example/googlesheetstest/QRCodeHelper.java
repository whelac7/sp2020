package com.example.googlesheetstest;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

//import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCodeHelper {
    public static void test() throws WriterException, IOException, NotFoundException {
        String qrCodeData = "number=1732,initLine=1001,autoLower=1002,autoOuter=1003,autoInner=100,lower=100,outer=100,inner=100,rotation=100,position=100,park=100,hang=100,level=100,disableTime=100,notes=none";
        String filePath = "code.png";
        String charSet = "UTF-8";
        Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        createQRCode(qrCodeData);
        System.out.println("QR code generated.");

        //Dictionary<String, String> properties = readQRCode(filePath, charSet, hintMap);
        //System.out.println(properties.get("initLine"));
        //System.out.println(properties.get("autoLower"));
        //System.out.println(properties.get("autoOuter"));
    }

    public static Bitmap createQRCode(String qrCodeData) throws WriterException, IOException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(qrCodeData, BarcodeFormat.QR_CODE, 512, 512);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }

    /*public static Dictionary<String, String> readQRCode(String filePath, String charset, Map hintMap)
            throws FileNotFoundException, IOException, NotFoundException {
        BinaryBitmap binaryBitmap = new BinaryBitmap(
                new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(filePath)))));
        Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap, hintMap);
        String[] propertyStrings = qrCodeResult.getText().split(",");
        Dictionary<String, String> properties = new Hashtable<String, String>();

        for (String property : propertyStrings) {
            String[] splitProperty = property.split("=");
            properties.put(splitProperty[0], splitProperty[1]);
        }

        return properties;
    }*/
}