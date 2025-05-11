package com.licenta.car_spotting_backend.classifier;

import com.licenta.car_spotting_backend.model.ClassifyingResponse;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Objects;

@Service
public class ClassifierService {
    public ClassifyingResponse runPythonScript(String imageLocation) {
        try {
            // Convertim în path corect (fără prefixul /C:/)
            String scriptPath = new File(
                    Objects.requireNonNull(getClass().getClassLoader().getResource("classifier/car_classifier.py")).toURI()
            ).getAbsolutePath();

            // Construcția comenzii
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "python", scriptPath, "--image", imageLocation
            );
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            StringBuilder output = new StringBuilder();

            ClassifyingResponse response = new ClassifyingResponse();

            String line = reader.readLine();
            response.setCarName(line);

            line = reader.readLine();
            response.setConfidence(line);

//            while ((line = reader.readLine()) != null) {
//                output.append(line).append("\n");
//            }

            process.waitFor();

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
