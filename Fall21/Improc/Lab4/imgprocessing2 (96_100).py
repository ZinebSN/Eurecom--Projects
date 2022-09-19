import numpy as np
import cv2

kernel_size = 3

#range in python 3.x and xrange in python 2.x
xrange = range

#Load the image 'Baboon.jpg'
img = cv2.imread('Baboon.jpg')

#Grayscale image
grayBaboon=cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

#Applying histogram equalization to grayscale image
#Get the rows, columns of histogram equalization image
histBaboon= cv2.equalizeHist(grayBaboon)
rows,columns=np.shape(histBaboon)
#Remapping image upside down
upsideBaboon=cv2.flip(histBaboon,0)

#Reflection in the x direction
reflectedBaboon=cv2.flip(histBaboon,1)

#Median filter
medianBaboon=cv2.medianBlur(histBaboon,3)

#Gaussian filter
gaussianBaboon=cv2.GaussianBlur(histBaboon,(3,3),cv2.BORDER_DEFAULT)

#Bilateral filter
bilateralBaboon=cv2.bilateralFilter(histBaboon,3,80,80,cv2.BORDER_DEFAULT)

newBaboon=bilateralBaboon #The best filter is bilqteral filter, because it preserves the sharpness of the image, it's not blur
#Applying the Laplacian function to compute the edge image using the Laplace Operator
laplacienBaboon=cv2.Laplacian(gaussianBaboon,cv2.CV_64F)

#Apply Sobel Edge Detection

##Compute gradient x for Sobel Edge 
sobelBaboon_X=cv2.Sobel(gaussianBaboon,cv2.CV_64F,1,0,ksize=5)

##Compute gradient y for Sobel Edge 
sobelBaboon_Y=cv2.Sobel(gaussianBaboon,cv2.CV_64F,0,1,ksize=5)

##Total gradient for Sobel Edge 
cv2.convertScaleAbs(sobelBaboon_X, sobelBaboon_X);
cv2.convertScaleAbs(sobelBaboon_Y, sobelBaboon_Y);
sobelBaboon=cv2.addWeighted(sobelBaboon_X, 0.25, sobelBaboon_Y, 0.75, 0);

#the best operator for edges is Laplacien because it detects all edges even the small details

#Displaying the images
cv2.imshow('Original image',img)
# Visualizing the grayscale image
cv2.imshow('Gray image', grayBaboon)
# Visualizing the image after equalization
cv2.imshow('Hist image', histBaboon)
# Visualizing the upside down image
cv2.imshow('Upside down image', upsideBaboon)
# Visualizing the upside down image
cv2.imshow('Reflection image', reflectedBaboon)
# Visualizing the Median filtered image
cv2.imshow('Median filtered image', medianBaboon)
# Visualizing the Gaussian filtered image
cv2.imshow('Gaussian filtered image', gaussianBaboon)
# Visualizing the Bilateral filtered image
cv2.imshow('Bilateral filtered image', bilateralBaboon)
# Visualizing the Laplacien filtered image
cv2.imshow('Laplacien filter image', laplacienBaboon)
# Visualizing the Sobel filtered image
cv2.imshow('Sobel filtered image', sobelBaboon)

#Wait until user exit program by pressing a key
cv2.waitKey(0)
cv2.destroyAllWindows()
