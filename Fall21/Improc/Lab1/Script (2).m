%Zineb SENANE

%Part2:
%Squares
first_square=uint8(ones(1024,1024)*32);
second_square=uint8(ones(512,512)*64);
third_square=uint8(ones(256,256)*128);
last_square=imresize(imread('lena.tif'),[128 128]);
first_square(256:767,256:767)=second_square;
first_square(384:639,384:639)=third_square;
first_square(448:575,448:575)=rgb2gray(last_square);
f1=figure('Name','squares');% name of the figure 

imshow(first_square);



%Part3:
lena_img=imresize(imread('lena.tif'),[512 512]);
upper_left=lena_img;
upper_right=flipud(lena_img(:,:,3));%blue channel
lower_left=fliplr(lena_img(:,:,1));%red channel
lower_right=fliplr(flipud(lena_img(:,:,2)));%green channel
our_square=uint8(ones(1024,1024,3));%creating the square that will contain all channels and the original image
our_square(1:512,1:512,1:3)=upper_left;
our_square(513:1024,1:512,3)=upper_right;
our_square(1:512,513:1024,1)=lower_left;
our_square(513:1024,513:1024,2)=lower_right;
f2=figure('Name','mosaic with color channel');% name of the figure 
imshow(our_square);

%Part4:
%3D image
lena_size=size(lena_img);
new_lena=imresize(lena_img,[256,256]);
gray_new_lena=rgb2gray(new_lena);
colormap(gray(256));
surf(gray_new_lena);%calculating the intensity
set(surface,'linestyle','none');

%Bitplane slicing
lena=imread('lena.tif');

bitplane1=mod(floor(lena),2);%extracting first bit
bitplane2=mod(floor(lena/2),2);%extracting second bit
bitplane3=mod(floor(lena/4),2);%extracting third bit
bitplane4=mod(floor(lena/8),2);%extracting fourth bit
bitplane5=mod(floor(lena/16),2);%extracting fifth bit
bitplane6=mod(floor(lena/32),2);%extracting sixth bit
bitplane7=mod(floor(lena/64),2);%extracting seventh bit
bitplane8=mod(floor(lena/128),2);%extracting eighth bit
my_huge_square=ones(1024,2048,3);%creating the square that will contain all bitplanes
my_huge_square(1:512,1:512,1:3)=bitplane1;
my_huge_square(1:512,513:1024,1:3)=bitplane2;
my_huge_square(1:512,1025:1536,1:3)=bitplane3;
my_huge_square(1:512,1537:2048,1:3)=bitplane4;
my_huge_square(513:1024,1:512,1:3)=bitplane5;
my_huge_square(513:1024,513:1024,1:3)=bitplane6;
my_huge_square(513:1024,1025:1536,1:3)=bitplane7;
my_huge_square(513:1024,1537:2048,1:3)=bitplane8;
f3=figure('Name','Bitplanes of Lenna image');% name of the figure 
imshow(rgb2gray(my_huge_square));



