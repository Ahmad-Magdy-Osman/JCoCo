from disassembler import *
import sys


def main():
    points = 4
    s = frozenset([1, 2, 3, 4])

    if len(s) == 4:
        print("Passed Test 1")
        points += 1

    if 5 not in s:
        print("Passed Test 2")
        points += 1

    t = frozenset([4, 5, 6])
    u = frozenset([7, 8, 9])

    tmp = s | t | u
    if len(tmp) == 9:
        print("Passed Test 3")
        points += 1

    tmp = s.union(t, u)
    if len(tmp) == 9:
        print("Passed Test 4")
        points += 1

    tmp = s.intersection(t)
    if len(tmp) == 1:
        print("Passed Test 5")
        points += 1

    tmp = s.copy()
    if len(tmp) == 4:
        print("Passed Test 6")
        points += 1

    if len(s.difference(tmp)) == 0:
        print("Passed Test 7")
        points += 1

    if len(s.symmetric_difference(t)) == 5:
        print("Passed Test 8")
        points += 1

    if 4 in s:
        print("Passed Test 9")
        points += 1

    z = frozenset(range(10))

    if z.issuperset(s):
        print("Passed Test 10")
        points += 1

    if not s.isdisjoint(t):
        print("Passed Test 11")
        points += 1

    if s < z:
        print("Passed Test 12")
        points += 1

    if z >= s:
        print("Passed Test 13")
        points += 1

    lst = []
    for i in s:
        lst.append(i)

    if len(lst) == 4:
        print("Passed Test 14")
        points += 1

    b = repr(frozenset([1, 2]))

    if b == "frozenset({1, 2})" or b == "frozenset({2, 1})":
        print("Passed Test 15")
        points += 1

    t = type(s)
    if str(t) == "<class 'frozenset'>":
        print("Passed Test 16")
        points += 1

    print("You got", points, "out of 20.")


if len(sys.argv) == 1:
    main()
else:
    disassemble(main)
