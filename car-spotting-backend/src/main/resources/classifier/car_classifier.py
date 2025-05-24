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
import warnings
warnings.filterwarnings("ignore")

class SelectiveBlurEdge:
    def __call__(self, image):
        image_cv = np.array(image)
        gray = cv2.cvtColor(image_cv, cv2.COLOR_RGB2GRAY)
        clahe = cv2.createCLAHE(clipLimit=2.0, tileGridSize=(8,8))
        gray = clahe.apply(gray)
        edges = cv2.Canny(gray, 100, 200)
        kernel = np.ones((5, 5), np.uint8)
        mask = cv2.dilate(edges, kernel, iterations=2)
        mask = cv2.erode(mask, kernel, iterations=2)
        mask = (mask > 0).astype(np.uint8) * 255
        blurred = cv2.GaussianBlur(image_cv, (25, 25), 10)
        selective_blurred = np.where(mask[..., None] == 255, image_cv, blurred)
        return Image.fromarray(selective_blurred)

class RandomPerspectiveTransform:
    def __call__(self, image):
        width, height = image.size
        start_points = [(0, 0), (width, 0), (width, height), (0, height)]
        offset = width * 0.1
        end_points = [
            (random.uniform(0, offset), random.uniform(0, offset)),
            (random.uniform(width - offset, width), random.uniform(0, offset)),
            (random.uniform(width - offset, width), random.uniform(height - offset, height)),
            (random.uniform(0, offset), random.uniform(height - offset, height))
        ]
        return TF.perspective(image, start_points, end_points, interpolation=Image.BILINEAR)

# Test transforms
test_transforms = transforms.Compose([
    SelectiveBlurEdge(),
    transforms.Resize((224, 224)),
    transforms.ToTensor(),
    transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])
])

def load_model():
    # Paths for model and class names
    base_dir = os.path.dirname(__file__)
    model_path = os.path.join(base_dir, "car_model_resnet18_final.pth")
    names_path = os.path.join(base_dir, "names.csv")

    if not os.path.exists(names_path):
        print(f"Error: names file '{names_path}' not found.")
        sys.exit(1)

    # Read class names (one per line, optional header)
    with open(names_path, 'r') as f:
        lines = f.read().splitlines()
    if lines and lines[0].lower().startswith('class_'):
        class_names = lines[1:]
    else:
        class_names = lines

    num_classes = len(class_names)

    # Initialize model architecture
    model = models.resnet18(pretrained=False)
    model.fc = torch.nn.Linear(model.fc.in_features, num_classes)

    # Load checkpoint
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    checkpoint = torch.load(model_path, map_location=device)
    if isinstance(checkpoint, dict) and 'model_state_dict' in checkpoint:
        model.load_state_dict(checkpoint['model_state_dict'])
    else:
        model.load_state_dict(checkpoint)

    return model, class_names


def classify_car_image(model, image_path, class_names):
    if not os.path.exists(image_path):
        print(f"Error: Image file '{image_path}' does not exist.")
        sys.exit(1)

    try:
        image = Image.open(image_path).convert("RGB")
    except Exception as e:
        print(f"Error opening image: {e}")
        sys.exit(1)

    input_tensor = test_transforms(image).unsqueeze(0)
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    model = model.to(device)
    input_tensor = input_tensor.to(device)

    model.eval()
    with torch.no_grad():
        output = model(input_tensor)
        probabilities = torch.nn.functional.softmax(output, dim=1)
        confidence, predicted_idx = torch.max(probabilities, 1)

    predicted_class = class_names[predicted_idx.item()]
    return predicted_class, confidence.item()


def main():
    parser = argparse.ArgumentParser(description='Classify car make and model from an image')
    parser.add_argument('--image', type=str, required=True, help='Path to the car image')
    args = parser.parse_args()

    model, class_names = load_model()
    predicted_class, confidence = classify_car_image(model, args.image, class_names)

    print(predicted_class)
    print(confidence)


if __name__ == "__main__":
    main()
