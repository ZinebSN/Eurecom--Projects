# -*- coding: utf-8 -*-
"""
Created on Tue Nov 16 15:19:22 2021

@author: Zineb Senane
"""
import cv2
import numpy as np

#exercise1

first_square=np.uint8(np.ones((1024,1024))*32)
second_square=np.uint8(np.ones((512,512))*64)
third_square=np.uint8(np.ones((256,256))*128)
lena=cv2.imread('lenna.jpg')
width = 128
height = 128
dim=(width,height)
last_square=cv2.resize(lena , dim , interpolation = cv2.INTER_AREA)
first_square[255:767,255:767]=second_square
first_square[383:639,383:639]=third_square
first_square[447:575,447:575]=cv2.cvtColor(last_square, cv2.COLOR_BGR2GRAY)

cv2.imshow('Square',first_square)


#exercice2
my_square=np.uint8(np.zeros((1024,1024,3)))
upper_left=cv2.imread('lenna.jpg')
upper_right=cv2.flip(upper_left[:,:,0],0)
lower_left=cv2.flip(upper_left[:,:,1],1)
lower_right=cv2.flip(cv2.flip(upper_left[:,:,2],0),1)
my_square[:512,:512,:]=upper_left
my_square[512:1024,:512,1]=lower_left
my_square[:512,512:,0]=upper_right
my_square[512:,512:,2]=lower_right

cv2.imshow('Mosaic',my_square)

#Wait until user exit program by pressing a key
cv2.waitKey(0)
cv2.destroyAllWindows()
