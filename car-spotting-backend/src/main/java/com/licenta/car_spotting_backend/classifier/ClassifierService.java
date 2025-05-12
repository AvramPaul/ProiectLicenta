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

            // Read first line: "Audi TTS Coupe 2012"
            String line = reader.readLine();
            if (line != null) {
                // Split into words
                String[] parts = line.split(" ");
                if (parts.length >= 3) {
                    response.setCarMake(parts[0]); // "Audi"

                    // Join the model name (could be multiple words)
                    StringBuilder modelBuilder = new StringBuilder();
                    for (int i = 1; i < parts.length - 1; i++) {
                        modelBuilder.append(parts[i]).append(" ");
                    }
                    response.setCarModel(modelBuilder.toString().trim());

                    // Parse year
                    try {
                        int year = Integer.parseInt(parts[parts.length - 1]);
                        response.setCarYear(year);
                    } catch (NumberFormatException e) {
                        response.setCarYear(0); // fallback or handle as needed
                    }
                }
            }

                line = reader.readLine();
                if (line != null) {
                    try {
                        double confidenceValue = Double.parseDouble(line);
                        response.setConfidence((int) Math.round(confidenceValue * 100));
                    } catch (NumberFormatException e) {
                        response.setConfidence(0); // fallback or handle as needed
                    }
                }
                

            process.waitFor();

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
