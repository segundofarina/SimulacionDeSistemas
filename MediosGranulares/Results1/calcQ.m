function Q= calcQ(D)

np=1000/(1.15*0.5)
g=9.8
r = (0.01+0.015)/2
k=0.22942

Q= np*sqrt(g)*(D-k*r)^1.5

