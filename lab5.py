import sys
import logging
import time

def read_input():
    symbols = sys.stdin.readline().split()
    nbr_symbols = len(symbols)
    cost_matrix = []
    for i in range(0,nbr_symbols):
        row = [int(x) for x in sys.stdin.readline().split()]
        cost_matrix.append(row)
    nbr_queries = int(sys.stdin.readline())
    queries = []
    for i in range(0, nbr_queries):
        queries.append(tuple(sys.stdin.readline().split()))
    return symbols, cost_matrix, queries

def A(queries, dict, matrix, delta):
    string1 = queries[0]
    string2 = queries[1]
    m = len(string1)
    n = len(string2)
    A = [[0 for y in range(n + 1)] for x in range(m + 1)]
    for i in range(0,m+1):
        A[i][0] = i*delta
    for j in range(0,n+1):
        A[0][j] = j*delta

    for i in range(1, m + 1):
        for j in range(1, n + 1):
#            if A[i][j] is None:
            val = matrix[dict[string1[i-1]]][dict[string2[j-1]]]
            A[i][j] = max(A[i-1][j-1] + val, A[i-1][j] + delta, A[i][j-1] + delta)

    return A

def find_alignment(A, matrix, dict, d, s1, s2):
    alig1 = ''
    alig2 = ''
    i = len(s1)
    j = len(s2)
    while i > 0 or j > 0:
        if i > 0 and j > 0 and A[i][j] == A[i-1][j-1] + matrix[dict[s1[i-1]]][dict[s2[j-1]]]:
            alig1 += s1[i-1]
            alig2 += s2[j-1]
            i -= 1
            j -= 1
        elif i > 0 and A[i][j] == A[i-1][j] + d:
            alig1 += s1[i-1]
            alig2 += '*'
            i -= 1
        else:
            alig1 += '*'
            alig2 += s2[j-1]
            j -= 1
    return alig1, alig2

if __name__ == '__main__':
    symbols, matrix, queries = read_input()
    dict = {}
    for i, symbol in enumerate(symbols):
        dict[symbol] = i
    delta = -4
    for i, query in enumerate(queries):
        A_mat = A(query, dict, matrix, delta)
        a1, a2 = find_alignment(A_mat, matrix, dict, delta, query[0], query[1])
        print(f'{a1[::-1]} {a2[::-1]}')
