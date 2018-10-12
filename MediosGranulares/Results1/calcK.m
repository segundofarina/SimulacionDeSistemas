function k=calcK(Q,D)
Np = 1000/(1*0.5)
g =9.8
r = (0.01+0.015)/2

t1 = nthroot( (Q*Q) / (Np*Np*g), 3)

k= (-t1 + D)/r;