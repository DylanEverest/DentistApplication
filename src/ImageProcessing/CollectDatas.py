import cv2
import numpy as np

# Définir le chemin de l'image
image_path = 'C:\\Users\\ratia\\Desktop\\temporaire\\teeth.png'


# Variables pour stocker les coordonnées
coordinates = []

# Fonction de rappel pour la gestion des clics de souris
def click_event(event, x, y, flags, param):
    if event == cv2.EVENT_LBUTTONDOWN:
        coordinates.append((x, y))
        print(f"Coordonnées du clic : ({x}, {y})")

# Charger l'image
image = cv2.imread(image_path)
cv2.imshow('Image', image)

# Définir la fonction de rappel des clics de souris
cv2.setMouseCallback('Image', click_event)

# Attendre que l'utilisateur appuie sur la touche 'ESC' pour quitter
while True:
    key = cv2.waitKey(1) & 0xFF
    if key == 27:  # Touche 'ESC'
        break

# Enregistrer les coordonnées dans un fichier
with open('PartieBas.txt', 'w') as file:
    n = 0
    for coord in coordinates:
        if(n%4==0):
            file.write(f" Gauche: {coord[0]}\n")
        if(n%4==1):
            file.write(f" Droite: {coord[0]}\n")
        if(n%4==2):
            file.write(f" Haut: {coord[1]}\n")
        if(n%4==3):
            file.write(f" Bas: {coord[1]}\n")
        n+=1
# Fermer la fenêtre OpenCV
cv2.destroyAllWindows()
