%Zineb Senane

%EX1
%B
lena=imread('lena.png');%lena image

a=fft2(double(lena));% calculating Fourier Transform of lena image
spectrum=fftshift(a);%shifting zero freauencies to the center

dim=size(spectrum);% dimension of the mask that we will use

for fcoupure=[0.1,0.4,0.7] % cutoff frequency 
mask=freqLPF(dim,fcoupure);% generating our mask using the function freqLPF

filtered_spectrum=mask.*spectrum;%calculating the filtered spectrum using our mask

filtered_lena=ifft2(ifftshift(filtered_spectrum));% generating the filtered image using the filtered spectrum

figure('Name','Mosaic plot');
% mosaic's plot
subplot(2,2,1);
imshow(lena);% visualizing original image
subplot(2,2,2);
imshow(log(abs(spectrum)+1),[]);% visualizing spectrum in 2D
subplot(2,2,3);
imshow(log(abs(filtered_spectrum)+1),[]);% visualizing filtered spectrum in 2D
subplot(2,2,4);
imshow(filtered_lena, []);% visualizing filtered image

end

%Spectrum in 3D
figure('Name','Spectrum in 3D');
surf(log(abs(spectrum)+1));%visualizing spectrum in 3D


%Questions
%Q1: the geometrical shape of the filter is circle( we are using a radius).

%Q2: fcoupure is the cutoff frequency that let pass only the frequencies
%less than it.

%Q3: we should just modify the condition we shoud put index =
%find(R>fcoupure); in the line 10,To keep only high frequencies


%Spectrum changes regarding to the used frequency for the filter
% low frequency : i choose fcoupure=0.1,The filtered spectrum keep only a small 
%circle from the original one, so it keeps only the lowest frequencies. 
%At this case, the filtered image is blur and smooth

% medium frequency : i choose fcoupure=0.4,The filtered spectrum keep only a medium 
%circle from the original one, so it keeps only frequencies that are less than 0.4 . 
%At this case, the filtered image is less blur and less smooth than the first one , a litlle bit sharp.

% high frequency : i choose fcoupure=0.7,The filtered spectrum keep almost frequencies. 
%At this case, the filtered image is more similar to the original one and it's more sharp.

%Conclusion: for low frequency we have a smooth and blur image, for high
%frequency we have a sharp one.

