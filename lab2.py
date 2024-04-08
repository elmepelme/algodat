import sys
from collections import deque
import time
def read_inputs():
    N, Q = sys.stdin.readline().split()
    N, Q = int(N), int(Q)
    word_list = []
    pair_list = []
    for i in range(N):
        word_list.append(input())
    for i in range(Q):
        pair_list.append(sys.stdin.readline().split())
    return word_list, pair_list

def build_graph(words):
    node_dict = {} # dictionary där key är noden och value är mängd med grannar
    for vert_word in words:
        neighbors = set()
        neighbors.add(vert_word)
        u = vert_word[-4:]
        for word in words:
            if word != vert_word:
                v = word
                edge = True
                for letter in u:
                    if letter not in v:
                        edge = False
                        break
                    else:
                        v = v.replace(letter,'',1) # 1an är att bara ta bort
                        # en instans av letter i v
                if edge:
                    neighbors.add(word)
        node_dict[vert_word] = neighbors
    return node_dict

def build_graph2(words):
    adj_list = {} # dictionary där key är noden och value är mängd med grannar
    for vert_word in words:
        neighbors = set()
        u = vert_word[-4:]
        for word in words:
            v = word
            if u_in_v(u,v):
                neighbors.add(word)
        adj_list[vert_word] = neighbors
    return adj_list

# u har 4 bokstäver, v har 5
def u_in_v(u,v):
    edge = True
    for letter in u:
        if letter not in v:
            edge = False
            break
        else:
            v = v.replace(letter,'',1)
    return edge


def BFS(adj_list, start_node, target_node):
    visited = set()
    # deque istället för lista sparar ca 5 sekunder för hela check_solution
    # tack vare konstant popleft() = pop(0)
    q = deque()
    q.append(start_node)
    visited.add(start_node)
    pred = {}
    pred[start_node] = None
    while q:
        current_node = q.popleft()
        for neighbor in adj_list[current_node]:
            if neighbor not in visited:
                q.append(neighbor)
                pred[neighbor] = current_node
                visited.add(neighbor)
            if neighbor == target_node:
                return calculate_path_length(pred, neighbor)
    return 0

def BFS2(adj_list, start_node, target_node):
    visited = {}
    for node in adj_list:
        visited[node] = 0
    # deque istället för lista sparar ca 5 sekunder för hela check_solution
    # tack vare konstant popleft() = pop(0)
    q = deque()
    q.append(start_node)
    visited[start_node] = 1
    pred = {}
    pred[start_node] = None
    while q:
        current_node = q.popleft()
        for neighbor in adj_list[current_node]:
            if visited[neighbor] == 0:
                visited[neighbor] = 1
                q.append(neighbor)
                pred[neighbor] = current_node
            if neighbor == target_node:
                return calculate_path_length(pred, neighbor)
    return 0

def calculate_path_length(pred, current_node):
    length = 1
    while pred[current_node] is not None:
        length = length + 1
        current_node = pred[current_node]
    return length

if __name__ == '__main__':
    word_list, pair_list = read_inputs()
    start1 = time.time()
    graph = build_graph2(word_list)
    end1 = time.time()
    #print(f'bygga graf tar {end1-start1}')
    start2 = time.time()
    for pairs in pair_list:
        path = BFS(graph, pairs[0],pairs[1])
        # if path == 0:
            # print('Impossible')
        # else:
            # print(path-1)

    end2 = time.time()
    print(f'bfs tar {end2-start2}')
