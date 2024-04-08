def list_input():
    N = int(sys.stdin.readline())
    numbers_left = 2*N*(N+1)
    numbers = [0]*numbers_left
    while(numbers_left > 0):
        index = 2*N*(N+1) - numbers_left
        line = [int(c) for c in sys.stdin.readline().split()]
        numbers[index:index+len(line)] = line
        numbers_left -= len(line)
    return numbers, N

def list_sorter(num, N):
    M = [None]*N
    W = [None]*N
    for i in range(0, 2*N*(N+1),  N+1):
        if W[num[i]-1] is None:
            W[num[i]-1] = num[i:i+N+1]
        else:
            M[num[i]-1] = num[i:i+N+1]
    return W, M

def sortW(W):
    N = len(W)
    V = [None]*N
    temp = [0]*(N*(N+1))
    for i in range(0,N): # för varje kvinna
        temp[i*(N+1)] = i+1 # kvinnors id
        for j in range(1,N+1): # för varje man i kvinnans preferenslista
            t = W[i][j] # den jte personens id för kvinna i
            temp[i*(N+1)+t] = j
        V[i] = temp[i*(N+1):(i+1)*(N+1)]
    return V

def GS(W, M):
    p = [] # män som vill gifta sig
    t = [None]*len(W) # kvinnor som har gift sig
    W = sortW(W)
    for men in M:
        p.append([men[0],1])
    while len(p) > 0:
        m = p.pop(-1)
        w = M[m[0]-1][m[1]]
        m[1] = m[1]+1
        if t[w-1] is None:
            t[w-1] = m
        elif(W[w-1][t[w-1][0]]-W[w-1][m[0]] > 0):
            p.append(t[w-1])
            t[w-1] = m
        else:
            p.append(m)
    for i in range(0,len(t)):
       print(t[i][0])

if __name__ == '__main__':
    num, N = list_input()
    W, M = list_sorter(num,N)
    GS(W, M)
