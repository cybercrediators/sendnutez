for line in open("enums.txt").readlines():
    print(line.strip("\n").upper().replace(" ", "_").replace("-", "_") + "(\"" + line.strip("\n") + "\"),")
