%Zineb Senane 

% II. Pre-processing: de-noising

% A. Step 1: load the image and artificially add some noise

img = uint8(imread('ic2.tif'));
noisy_image =img+uint8(50*randn(size(img))) ;

figure;
subplot(1,2,1);
imshow(img);
title('Original Image');
subplot(1,2,2);
imshow(noisy_image,[]);
title('Noisy Image');

% B. Step 2: de-noise image

% Different sizes average filtering
avg_1 = fspecial('average',3);
avg_2 = fspecial('average',5);
avg_3 = fspecial('average',7);
avg_4 = fspecial('average',9);
avg_5 = fspecial('average',15);

avg_1_filtered_image = filter2(avg_1,noisy_image);
avg_2_filtered_image = filter2(avg_2,noisy_image);
avg_3_filtered_image = filter2(avg_3,noisy_image);
avg_4_filtered_image = filter2(avg_4,noisy_image);
avg_5_filtered_image = filter2(avg_5,noisy_image);

figure
subplot(1,5,1);
imshow(avg_1_filtered_image,[]);
title('3x3 Average Filter');
subplot(1,5,2);
imshow(avg_2_filtered_image,[]);
title('5x5 Average Filter');
subplot(1,5,3);
imshow(avg_3_filtered_image,[]);
title('7x7 Average Filter');
subplot(1,5,4);
imshow(avg_4_filtered_image,[]);
title('9x9 Average Filter');
subplot(1,5,5);
imshow(avg_5_filtered_image,[]);
title('15x15 Average Filter');

% Different sizes median filtering
med_1_filtered_image = medfilt2(noisy_image,[3 3]);
med_2_filtered_image = medfilt2(noisy_image,[5 5]);
med_3_filtered_image = medfilt2(noisy_image,[7 7]);
med_4_filtered_image = medfilt2(noisy_image,[9 9]);
med_5_filtered_image = medfilt2(noisy_image,[15 15]);

figure
subplot(1,5,1);
imshow(med_1_filtered_image,[]);
title('3x3 Median Filter');
subplot(1,5,2);
imshow(med_2_filtered_image,[]);
title('5x5 Median Filter');
subplot(1,5,3);
imshow(med_3_filtered_image,[]);
title('7x7 Median Filter');
subplot(1,5,4);
imshow(med_4_filtered_image,[]);
title('9x9 Median Filter');
subplot(1,5,5);
imshow(med_5_filtered_image,[]);
title('15x15 Median Filter');

% Different sizes wiener filtering
wn_1_filtered_image = wiener2(noisy_image,[3 3]);
wn_2_filtered_image = wiener2(noisy_image,[5 5]);
wn_3_filtered_image = wiener2(noisy_image,[7 7]);
wn_4_filtered_image = wiener2(noisy_image,[9 9]);
wn_5_filtered_image = wiener2(noisy_image,[15 15]);

figure
subplot(1,5,1);
imshow(wn_1_filtered_image,[]);
title('3x3 Wiener Filter');
subplot(1,5,2);
imshow(wn_2_filtered_image,[]);
title('5x5 Wiener Filter');
subplot(1,5,3);
imshow(wn_3_filtered_image,[]);
title('7x7 Wiener Filter');
subplot(1,5,4);
imshow(wn_4_filtered_image,[]);
title('9x9 Wiener Filter');
subplot(1,5,5);
imshow(wn_5_filtered_image,[]);
title('15x15 Wiener Filter');

% I choose to test filtering with 5 different sizes (3x3, 5x5, 7x7,
% 9x9 and 15x15) for each filter (average, median and wiener). As we can
% see in the figures, for small sizes the noise is still present in the
% picture and for high sizes we loose some important data for example edges
% So the best filter size to keep at our case is the 5x5.

figure('Name','Expected output');
subplot(1,3,1);
imshow(avg_2_filtered_image,[]);
title('Average Filter');
subplot(1,3,2);
imshow(med_2_filtered_image,[]);
title('Median Filter');
subplot(1,3,3);
imshow(wn_2_filtered_image,[]);
title('Wiener Filter');

 
% By comparing the different filtered images, we can see that Wiener filtering gave the
% best performance by deleting noise and keeping edges.

denoised_image = wn_2_filtered_image;
figure;
imshow(denoised_image,[]);
title('De-noised Image');


% III. Processing: low level feature detection

% A. Step3: highlight edges

% To detect and highlight edges, we are going to use three different
% techniques: gradient, zero crossing and Canny edge detector. We should perform edge detection.

% 1. Gradient

% Vertical and horizontal gradients
hor_filter =  [-1 0 1];
ver_filter = [-1 0 1]';

h_grad = filter2(hor_filter,denoised_image);
v_grad = filter2(ver_filter,denoised_image);
grad_img=sqrt(h_grad.^2+v_grad.^2)/256;
t = graythresh(grad_img);

% Here, we have applied horizontal and vertical gradients to detect
% vertical and horizontal edges then calculated the gradient magnitude.

figure
subplot(1,3,1);
imshow(h_grad,[]);
title('Horizontal gradient');
subplot(1,3,2);
imshow(v_grad,[]);
title('Vertical gradient');
subplot(1,3,3);
imshow(grad_img,[]);
title('Gradient magnitude');

bin_img = im2bw(grad_img,t);
thin_img = bwmorph(bin_img,'thin');

% Binirizing and applying morphological operation to make edges more
% visible: the edges are becoming more thin.

figure('Name','Expected output for A.1');
subplot(1,2,1);
imshow(bin_img);
title('Edges');
subplot(1,2,2);
imshow(thin_img);
title('Edges thinned')

% 2. Zero crossing

lp = [0 1 0;1 4 1;0 1 0];
lp_filtered_img = edge(denoised_image,'zerocross',0.5,lp);


% 3. Canny edge detector

canny_filtered_img = edge(denoised_image,'canny');

figure;
subplot(1,3,1);
imshow(thin_img,[]);
title('Gradient Method');
subplot(1,3,2);
imshow(lp_filtered_img,[]);
title('Zero Crossing');
subplot(1,3,3);
imshow(canny_filtered_img,[]);
title('Canny');

% The gradient and zero crossing methods are not strong enough to detect
% edges and sometimes fooled  by noise. Canny edge detector seems to be
% more efficient.


% B. Step 4: Compute the Radon transform

R = radon(canny_filtered_img);
figure('Name','Expected output of the radon transform');
subplot(1,2,1);
imshow(canny_filtered_img,[]);
title('canny_edges')
subplot(1,2,2);
imshow(R,[]);
title('radon transform')
xlabel('\theta');
colormap(gca,hot), colorbar

% As we know, In the hough domain, a line in the image is represented by a point (R,theta) 
% and a point in the image is the intersection of many curves(each curve represents a point of the image).
% intersects of the curves for theta=90 which represent horizontal edges
% and for theta around 0 and 180 which represent vertical edges of the
% image.

%Optional questions:
I=zeros(256,256);
I(128,128)=1;
R1 = radon(I);
figure;
subplot(1,2,1);
imshow(I,[])
title('point image')
subplot(1,2,2);
imshow(R1,[]);
title('Radon transform of a point')
colormap(gca,hot), colorbar
% the radon transform of a point in image is a line

I2=zeros(256,256);
I2(128,:)=1;
R2 = radon(I2);
figure;
subplot(1,2,1);
imshow(I2,[])
title('line image')
subplot(1,2,2);
imshow(R2,[])
title('Radon transform of a line')
colormap(gca,hot), colorbar
%the radon transform of a line in image is a point
%For the line, when we show the transform's logarithm, we see that the
%left and right center regions are brighter than others and the center is a very bright point (the maximum of brightness)
%because the logarithm transform shows better the values near to the center, the effect of the logarithm is like a zoom to see better details 
%So, as the radon transform for a line is a point and the radon transform
%for a point is a line, we can say that radon transform relates to the
%hough Transformation.

% Applying the radon transform consists of scaning all point with respect to an angle (between 0 and 180)
% and returning the coefficients of the projection in a column.
% The sum over any column is the same because we have the sum of each pixel
% value on a specific direction.



% IV. Post-processing: high level detection & interpretation

% A. step 5: choose points in Radon transform and observe associated lines

% used interactiveLine(canny_filtered,R,4) to select horizontal and
% vertical lines.

interactiveLine(canny_filtered_img,R,4);

% B. Step 6: find the image orientation and rotate it

V = max(R);
[M, i] = max(V);

V1 = V(1:90) + V(91:180);
[M1, i1] = max(V1);

figure;
subplot(1,2,1);
plot(V);

subplot(1,2,2);
plot(V1);

% To ideentify which two orthogonal directions have more edges
% We calculate V1=V(1:90)+V(91:180) (eadges in direction (theta)
% and edges in direction rotated by 90 (theta+90)are calculated together)
% V plot: as we can see there are 2 considerable peaks for i=88 and for i=178
% that means that most edges are in those two directions.

% V1 plot: we can see a single peak at i1=88, that means the two
% orthogonal directions with most edges are 88 and 88+90.
% So to get those edges appear horizontal and vertical, we rotate
% the picture with an angle theta=-88.

rotated_image = imrotate(denoised_image,-i1);
figure('Name','Expected final output');
subplot(1,2,1);
imshow(noisy_image,[]);
title('Original Image');
subplot(1,2,2);
imshow(rotated_image,[]);
title('Registered Image');

%By using the noise with 150 as power instead of 50, we see that the image
%is not rotated correctly (there is a small deviation), we can rotate it
%correctly by calculating the new angle 9using the same steps), at this
%case it should be i1=-87.
