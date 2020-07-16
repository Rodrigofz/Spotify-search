import re
import sys

data = open(sys.argv[1])
clean = open(sys.argv[2], 'w+')

counter=0
for line in data:
    x = re.sub("\[.*,.*\]", "(feat)", line)
    if x!=None:
        counter = counter if line==x else counter+1
        clean.write(x)
    else:
        clean.write(line)

print("Lines cleared: {}".format(counter))
clean.close()
