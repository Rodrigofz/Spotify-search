file = open("data.csv", 'r')
clean_file = open("cleaned_data.csv", 'w+')

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