import sys

file = open(sys.argv[1], 'r')
clean_file = open(sys.argv[2], 'w+')

import random
for line in file:
    splitted = line.split(',')
    try:
        if splitted[-1] != "global\n":
            clean_file.write(','.join(splitted))
    except:
        continue
file.close()
clean_file.close()