%Zineb Senane
my_matrix=ones(256,256)*64;
my_matrix(65:192,65:192)=ones(128,128)*192;
noise_image=rand(256)*120;
my_image=noise_image+my_matrix;

for k=[3,9,51]
dim=[k k];
filtered_image=medfilt2(my_image,dim);

figure();
subplot(1,3,1);
imshow(my_matrix,[]);
subplot(1,3,2);
imshow(my_image,[]);
subplot(1,3,3);
imshow(filtered_image,[]);
end

%I choosed 3 different sizes :[3 3], [9 9], [51 51]
%More the size of the median filter is big More the noise id removed, and
%the ability of supressing this noise increses only at the expense of blurring edges.

%differences for a given size  when the image is filtered by average or median

%When the image is filtered by median the salt and pepper noise is removed.
%It preserves their edges sharp while re;ovimg noise