import sys
import heapq as hq
def read_inputs():
    N, M = sys.stdin.readline().split()
    N, M = int(N), int(M)
    time_list = []
    for i in range(M):
        time_list.append([int(x) for x in sys.stdin.readline().split()])
    return N, time_list

# obs graph index i är person i
def build_graph(N, time_list):
    graph = [set() for _ in range(N+1)]
    # att istället ha graph = [set()]*N gör att varje element i graph är
    # egentligen samma objekt, så jag gör så här ist

    # G = [set((1,2), (2,3), ...), set((1,4), ...)]
    for elem in time_list:
        graph[elem[0]].add((elem[1], elem[2]))
        graph[elem[1]].add((elem[0], elem[2]))

    return graph

def prim(G,r):
    T = [False]*(len(G)+1) # besökta
    Q = [] # prio kön
    hq.heappush(Q,(0,r)) #(i,j), i är vikten för att nå nod j m.a.p
    # vilken granne vi är på
    tot_w = 0
    nbr_nodes = 0 # hur många noder vi har lagt till i mst
    while Q and nbr_nodes != len(G)-1:
        n = hq.heappop(Q)
        w = n[0]
        if not T[n[1]]:
            tot_w += w
            nbr_nodes += 1
            T[n[1]] = True
            for nb in G[n[1]]:
                nb_id = nb[0]
                nb_w = nb[1]
                if not T[nb_id]:
                    hq.heappush(Q, (nb_w, nb_id))
    return tot_w

if __name__ == '__main__':
    N, time_list = read_inputs()
    graph = build_graph(N, time_list)
    print(prim(graph, 2))
