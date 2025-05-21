package com.licenta.car_spotting_backend.services;

import com.licenta.car_spotting_backend.dto.ClassifyingResponse;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
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

            List<String> multiWordMakes = Arrays.asList(
                    "Land Rover", "Rolls Royce", "Aston Martin", "Mini Cooper", "Alfa Romeo"
            );

            String line = reader.readLine();
            if (line != null) {
                String[] words = line.split(" ");
                String carMake = "";
                String carModel = "";
                int carYear = 0;

                // Try to match known multi-word makes first
                for (String knownMake : multiWordMakes) {
                    if (line.startsWith(knownMake)) {
                        carMake = knownMake;
                        String[] makeParts = knownMake.split(" ");
                        int makeWordCount = makeParts.length;

                        // Extract model words between make and year
                        StringBuilder modelBuilder = new StringBuilder();
                        for (int i = makeWordCount; i < words.length - 1; i++) {
                            modelBuilder.append(words[i]).append(" ");
                        }
                        carModel = modelBuilder.toString().trim();

                        // Parse year
                        try {
                            carYear = Integer.parseInt(words[words.length - 1]);
                        } catch (NumberFormatException e) {
                            carYear = 0;
                        }
                        break;
                    }
                }

                // If no multi-word make matched, default to one-word make
                if (carMake.isEmpty() && words.length >= 3) {
                    carMake = words[0];
                    StringBuilder modelBuilder = new StringBuilder();
                    for (int i = 1; i < words.length - 1; i++) {
                        modelBuilder.append(words[i]).append(" ");
                    }
                    carModel = modelBuilder.toString().trim();

                    try {
                        carYear = Integer.parseInt(words[words.length - 1]);
                    } catch (NumberFormatException e) {
                        carYear = 0;
                    }
                }

                response.setCarMake(carMake);
                response.setCarModel(carModel);
                response.setCarYear(carYear);
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
