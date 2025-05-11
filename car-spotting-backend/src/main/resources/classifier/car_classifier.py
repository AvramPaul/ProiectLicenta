#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import sys
import torch
import numpy as np
import pandas as pd
import cv2
from PIL import Image
import torchvision.models as models
import torchvision.transforms as transforms
import torchvision.transforms.functional as TF
import argparse
import random

class SelectiveBlurEdge:
    def __call__(self, image):
        image_cv = np.array(image)  # Convert PIL Image to NumPy
        gray = cv2.cvtColor(image_cv, cv2.COLOR_RGB2GRAY)

        # Apply CLAHE for better contrast
        clahe = cv2.createCLAHE(clipLimit=2.0, tileGridSize=(8,8))
        gray = clahe.apply(gray)

        # Apply Canny Edge Detection
        edges = cv2.Canny(gray, 100, 200)

        # Fill in the detected edges using morphological closing
        kernel = np.ones((5, 5), np.uint8)
        mask = cv2.dilate(edges, kernel, iterations=2)
        mask = cv2.erode(mask, kernel, iterations=2)

        # Convert to binary mask
        mask = (mask > 0).astype(np.uint8) * 255

        # Apply Gaussian Blur only to the background
        blurred = cv2.GaussianBlur(image_cv, (25, 25), 10)
        selective_blurred = np.where(mask[..., None] == 255, image_cv, blurred)

        return Image.fromarray(selective_blurred)  # Convert back to PIL Image

class RandomPerspectiveTransform:
    def __call__(self, image):
        width, height = image.size

        # Define slight perspective distortion points
        start_points = [(0, 0), (width, 0), (width, height), (0, height)]
        offset = width * 0.1  # 10% distortion
        end_points = [
            (random.uniform(0, offset), random.uniform(0, offset)),
            (random.uniform(width - offset, width), random.uniform(0, offset)),
            (random.uniform(width - offset, width), random.uniform(height - offset, height)),
            (random.uniform(0, offset), random.uniform(height - offset, height))
        ]

        return TF.perspective(image, start_points, end_points, interpolation=Image.BILINEAR)

# Define the test transforms (no augmentation, just normalization and resize)
test_transforms = transforms.Compose([
    SelectiveBlurEdge(),
    transforms.Resize((224, 224)),
    transforms.ToTensor(),
    transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])
])

def load_model():
    model_path = os.path.join(os.path.dirname(__file__), "regnet_model.pth")

    csv_path = os.path.join(os.path.dirname(__file__), "stanford_cars_with_class_names_train.csv")
    if not os.path.exists(csv_path):
        print(f"Error: CSV file '{csv_path}' not found.")
        sys.exit(1)

    train_df = pd.read_csv(csv_path)
    num_classes = len(train_df["true_class_name"].unique())

    # Load the correct model architecture
    model = models.regnet_y_800mf(weights=models.RegNet_Y_800MF_Weights.DEFAULT)
    model.fc = torch.nn.Linear(model.fc.in_features, num_classes)  # ✅ match checkpoint

    # Load checkpoint
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    checkpoint = torch.load(model_path, map_location=device, weights_only=True)

    if 'model_state_dict' in checkpoint:
        model.load_state_dict(checkpoint['model_state_dict'])
    else:
        model.load_state_dict(checkpoint)

    return model, train_df["true_class_name"].tolist()


def classify_car_image(model, image_path, class_names):
    """Classify the car image and return the predicted class and confidence"""
    # Check if image exists
    if not os.path.exists(image_path):
        print(f"Error: Image file '{image_path}' does not exist.")
        sys.exit(1)
    
    # Load and preprocess the image
    try:
        image = Image.open(image_path).convert("RGB")
    except Exception as e:
        print(f"Error opening image: {str(e)}")
        sys.exit(1)

    # Apply the test transforms
    input_tensor = test_transforms(image).unsqueeze(0)  # Add batch dimension

    # Check if GPU is available
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    model = model.to(device)
    input_tensor = input_tensor.to(device)

    # Set model to evaluation mode and disable gradients for inference
    model.eval()
    with torch.no_grad():
        # Forward pass
        output = model(input_tensor)

        # Get probabilities
        probabilities = torch.nn.functional.softmax(output, dim=1)

        # Get top prediction
        confidence, predicted_idx = torch.max(probabilities, 1)

    # Get the class name
    predicted_class = class_names[predicted_idx.item()]

    return predicted_class, confidence.item()

def main():
    """Main function to run the car classifier"""
    parser = argparse.ArgumentParser(description='Classify car make and model from an image')
    parser.add_argument('--image', type=str, required=True, help='Path to the car image')
    
    args = parser.parse_args()

    model, class_names = load_model()

    predicted_class, confidence = classify_car_image(model, args.image, class_names)
    
    # Print the results
    print(predicted_class)
    print(confidence)

if __name__ == "__main__":
    main()