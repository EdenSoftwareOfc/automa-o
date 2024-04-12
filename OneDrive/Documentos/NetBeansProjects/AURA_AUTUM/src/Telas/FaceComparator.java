package Telas;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.imgproc.Imgproc;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.CvType;

public class FaceComparator {

    private static double calculateHistogramSimilarity(Mat hist1, Mat hist2) {
        // Usando a correlação para calcular a similaridade
        return Imgproc.compareHist(hist1, hist2, Imgproc.HISTCMP_CORREL);
    }

    private static Mat calculateHistogram(Mat image) {
        Mat grayImage = image.clone();

        if (image.channels() > 1) {
            Imgproc.cvtColor(grayImage, grayImage, Imgproc.COLOR_BGR2GRAY);
        }

        grayImage.convertTo(grayImage, CvType.CV_8U);

        Mat hist = new Mat();

        // Correção na chamada do calcHist
        List<Mat> imagesList = new ArrayList<>();
        imagesList.add(grayImage);

        Imgproc.calcHist(
                imagesList,
                new MatOfInt(0),
                new Mat(),
                hist,
                new MatOfInt(256),
                new MatOfFloat(0, 256)
        );

        Core.normalize(hist, hist, 0, 1, Core.NORM_MINMAX, -1, new Mat());

        return hist;
    }

    public String compareFaces(Mat detectedFace, List<Mat> facesFromDatabase, List<String> namesFromDatabase) {
        Mat detectedFaceHist = calculateHistogram(detectedFace);
        double threshold = 0.7; // Defina o limite de similaridade desejado

        for (int i = 0; i < facesFromDatabase.size(); i++) {
            Mat faceFromDatabase = facesFromDatabase.get(i);
            Mat databaseFaceHist = calculateHistogram(faceFromDatabase);

            double similarity = calculateHistogramSimilarity(detectedFaceHist, databaseFaceHist);

            if (similarity >= threshold) {
                return namesFromDatabase.get(i); // Retorna o nome se a similaridade for superior ao limite
            }
        }

        return null; // Retorna null se nenhuma correspondência satisfatória for encontrada
    }
}
