 
# Project Eratosthenes: 100
import re
def isPrime(n):
    return re.match(r'^1?$|^(11+?)\1+$', '1' * n) == None
def listPrime(N):
    M = 100              
    l = list()          
    while len(l) < N:
        l += filter(isPrime, range(M - 100, M))
        M += 100                                
    return l[:N]
list=listPrime(10000)
list_m=listPrime(args[0])
list_n=listPrime(args[1])
result=0
for x in list_m:
        result=result+list[x-1]
for x in list_n:
                result=result+list[x-1]
print result