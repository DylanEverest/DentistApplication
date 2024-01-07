import cv2
import numpy as np
import sys

# Fonction pour détecter les contours jaunes et écrire dans un fichier
def detect_yellow_contours_and_write(image_path, output_file):
    # Charger l'image
    image = cv2.imread(image_path)

    # Convertir l'image en format HSV
    hsv_image = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)

    # Définir la plage de couleurs jaunes dans le format HSV
    lower_yellow = np.array([20, 100, 100])
    upper_yellow = np.array([30, 255, 255])

    # Appliquer un seuil pour isoler les pixels jaunes
    yellow_mask = cv2.inRange(hsv_image, lower_yellow, upper_yellow)

    # Trouver les contours dans l'image seuillée
    contours, _ = cv2.findContours(yellow_mask, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

    # Ouvrir le fichier en mode écriture
    with open(output_file, 'w') as file:

        # Écrire les coordonnées des contours et leurs superficies dans le fichier
        for contour in contours:
            # Calculer la superficie du contour
            area = cv2.contourArea(contour)

            # Ignorer les contours de petite superficie
            if area > 100:
                # Trouver les coordonnées du centre du contour
                M = cv2.moments(contour)
                if M["m00"] != 0:
                    cx = int(M["m10"] / M["m00"])
                    cy = int(M["m01"] / M["m00"])

                    # Écrire les coordonnées et la superficie dans le fichier
                    file.write(f"({cx}, {cy}), {area}\n")

                    # Dessiner un cercle autour du centre du contour
                    cv2.circle(image, (cx, cy), 5, (0, 255, 0), -1)

    # Afficher l'image avec les contours détectés
    cv2.imshow('Contours Jaune', image)
    cv2.waitKey(0)
    cv2.destroyAllWindows()

# Récupérer les arguments de la ligne de commande
image_path = sys.argv[1]
output_file = sys.argv[2]

# # Appeler la fonction avec les arguments fournis
detect_yellow_contours_and_write(image_path, output_file)

# test
# detect_yellow_contours_and_write("C:/Users/ratia/Desktop/workspace/S5/pythonTraining/workspace/imgProcessing/Img-Client/2.png", "C:/Users/ratia/Desktop/workspace/S5/pythonTraining/workspace/imgProcessing/dental/Analysis/dataAnalysis/output.txt")