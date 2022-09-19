%Zineb Senane

my_matrix=ones(256,256)*64;
my_matrix(65:192,65:192)=ones(128,128)*192;
noise_image=rand(256)*120;
my_image=noise_image+my_matrix;

for k=[3,9,111]
dim=[k k];
filtered_image=filter2(fspecial(dim),my_image);

figure();
subplot(1,3,1);
imshow(my_matrix,[]);
subplot(1,3,2);
imshow(my_image,[]);
subplot(1,3,3);
imshow(filtered_image,[]);
end

%creating the averaging filter
function averagefilter=fspecial(dim)
averagefilter=ones(dim(1),dim(2))*(1/(dim(1)*dim(2)));
end

%I choosed 3 different sizes :[3 3], [9 9], [111 111]
%More the size of the averaging filter is big More the filtered image is blur and smooth,
%then this smoothing effect demonstrate that a big size in spatial domain refers to a low pass in frequency domain
%Also for a small size in spatial domain we have a sharpness edges, which refers to a high pass in frequency domain
%As the size of the filter window increases, the ability to remove noise
%increases at ehe expense of blurring 