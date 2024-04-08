import sys
import math
import logging
import time
time_check_line = 0
time_points_close = 0

def read_inputs():
    N = int(sys.stdin.readline())
    Points = []
    for i in range(0,N):
        point = tuple([int(x) for x in sys.stdin.readline().split()])
        Points.append(point)
    return N,Points

def distance(p, q):
    return math.sqrt((p[0]-q[0])**2 + (p[1]-q[1])**2)

def naive(P):
    d = distance(P[0], P[1])
    for p1 in P:
        for p2 in P:
            if p1 != p2 and distance(p1,p2) < d:
                d = distance(p1,p2)
    return d

def points_close(P, mid_p, d):
    s1 = time.time()
    P_d = []
    for points in P:
        if abs(points[0]-mid_p[0]) < d:
            P_d.append(points)
    f1 = time.time()
    return P_d

def check_line2(P,d):
    s2 = time.time()
    C = 5
    for i, p1 in enumerate(P):
        if i + C < len(P):
            for p2 in P[i + 1 : i + C]:
                if p1 != p2 and distance(p1,p2) < d:
                    d = distance(p1,p2)
        else:
            for p2 in P[-(len(P) - i + 1):]:
                if p1 != p2 and distance(p1,p2) < d:
                    d = distance(p1,p2)
    f2 = time.time()
    return d

def check_line(P, d):
    s2 = time.time()
    C = 2
    for i, p1 in enumerate(P):
        if i + C < len(P):
            for p2 in P[i : i + C + 1]:
                if p1 != p2 and distance(p1,p2) < d:
                    d = distance(p1,p2)
        else:
            for p2 in P[-(len(P) - i + 1):]:
                if p1 != p2 and distance(p1,p2) < d:
                    d = distance(p1,p2)
    f2 = time.time()
    return d

def closest(Px, Py, n):
    if n <= 7:
        d = naive(Px)
        return d

    mid = n//2
    mid_p = Px[mid]

    Lx = Px[:mid]
    Rx = Px[mid:]

    Ly = []
    Ry = []

    for point in Py:
        if point[0] <= mid_p[0]:
            Ly.append(point)
        else:
            Ry.append(point)

    d1 = closest(Lx, Ly, len(Lx))
    d2 = closest(Rx, Ry, len(Rx))

    d = min(d1,d2)

    P_d = points_close(Py, mid_p, d)
    d = check_line2(P_d, d)
    return d

if __name__ == '__main__':
    lvl = logging.INFO
    fmt = '[%(levelname)s] %(asctime)s - %(message)s'
    logging.basicConfig(level=lvl, format = fmt)
    N,P = read_inputs()
    Px = sorted(P, key = lambda x : x[0])
    Py = sorted(P, key = lambda x : x[1])
    start = time.time()
    # det är closest som tar tid
    d = closest(Px, Py, N)
    finish = time.time()
    logging.debug(f'tid för closest: {finish-start}')
    #print(f'tid för check_line : {time_check_line}')
    #print(f'tid för points_close : {time_points_close}')
    d = round(d, 6)
    print(d)
