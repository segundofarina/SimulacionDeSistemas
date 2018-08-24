N=300;
L=5;
delta=0.1;

POS= rand(N,2)*L
V= ones(N,1)*0.003;
angle=rand(N,1)*2*pi;
A= [POS,V,angle]
save randDynLatice.txt A