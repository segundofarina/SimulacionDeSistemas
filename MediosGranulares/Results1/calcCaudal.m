%Plot 
function calcCaudal(times,N)

size = length(times);

t0=times(N+1:size);
t1=times(1:size-N);

delta = N./( times(N+1:size) - times(1:size-N) );

size = length(delta)
caudal = mean(delta)
desvio = std(delta)

plot(delta);